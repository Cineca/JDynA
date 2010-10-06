package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.dto.ValoreDTOPropertyEditor;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.beans.PropertyEditor;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.Errors;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public abstract class AnagraficaDTOCreationAction
	<P extends Property<TP>, TP extends PropertiesDefinition,
		AO extends AnagraficaObject<P, TP>, IA extends IPersistenceDynaService> extends BaseFormAction<IA> {	
	
	protected Log logger = LogFactory.getLog(AnagraficaDTOCreationAction.class);
		
	protected FormulaManager formulaManager;

	//protected Class<A> clazzArea;
	protected Class<TP> clazzTipologiaProprieta;
	protected Class<AO> clazzAnagraficaObject;
	
//	public void setClazzArea(Class<A> clazzArea) {
//		this.clazzArea = clazzArea;
//	}

	public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta) {
		this.clazzTipologiaProprieta = clazzTipologiaProprieta;
	}

	public void setClazzAnagraficaObject(Class<AO> clazzAnagraficaObject) {
		this.clazzAnagraficaObject = clazzAnagraficaObject;
	}

	public Event referenceData(RequestContext context) throws Exception {
		IAnagraficaObjectDTO anagraficaObjectDTO = (IAnagraficaObjectDTO) getFormObject(context);
		Map breadCumbs=getBreadcrumbs(anagraficaObjectDTO);
		context.getFlashScope().put("breadCumbs", breadCumbs);
		List<TP> tipProprietaOnCreation = applicationService
				.getTipologiaOnCreation(clazzTipologiaProprieta);
		context.getFlashScope().put("tipologieProprietaInArea", tipProprietaOnCreation);
		context.getFlashScope().put("simpleNameAnagraficaObject", clazzAnagraficaObject.getSimpleName());
		return success();
	}
	
	@Override
	protected void doValidate(RequestContext context, Object formObject,
			Errors errors) throws Exception {
		super.doValidate(context, formObject, errors);
		IAnagraficaObjectDTO dto = (IAnagraficaObjectDTO) formObject;
		AO anagraficaObject = clazzAnagraficaObject.newInstance();
		List<TP> tipProprietaInArea = applicationService
				.getTipologiaOnCreation(clazzTipologiaProprieta);
		// bind delle modifiche utente
		AnagraficaUtils.reverseDTO(dto, anagraficaObject, tipProprietaInArea);
		// ricalcolo le formule
		formulaManager.ricalcolaFormule(anagraficaObject);
		// inserisco nel DTO i nuovi valori
		AnagraficaUtils.fillDTO(dto, anagraficaObject, tipProprietaInArea);
	}
	
	@Override
	protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry registry) {
		super.registerPropertyEditors(registry);
		IAnagraficaObjectDTO commandDTO = null;
		try {
			commandDTO = (IAnagraficaObjectDTO) getFormObject(context);
		} catch (Exception e) {
			logger.error("Non e' stato possibile estrarre il Command Object dal flusoo", e);
		}

		Set<String> shortNames = (commandDTO == null || commandDTO.getAnagraficaProperties() == null)?
				null:commandDTO.getAnagraficaProperties().keySet();
		if (shortNames != null) {
			for (String shortName : shortNames) {
				TP tipologiaProprieta = applicationService.findTipologiaProprietaByShortName(clazzTipologiaProprieta, shortName);
				PropertyEditor propertyEditor = tipologiaProprieta.getRendering().getPropertyEditor(applicationService);
				if (tipologiaProprieta.getRendering() instanceof WidgetCombo) {
					WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tipologiaProprieta
							.getRendering();
					for (int idx = 0; idx < 100; idx++){
						for (TP subtp : combo.getSottoTipologie()){
							PropertyEditor subPropertyEditor = subtp.getRendering().getPropertyEditor(applicationService);
							String path = "anagraficaProperties["+shortName+"]["+idx+"].object.anagraficaProperties["+subtp.getShortName()+"]";
							registry.registerCustomEditor(ValoreDTO.class, path, new ValoreDTOPropertyEditor(subPropertyEditor));
							registry.registerCustomEditor(Object.class, path+".object", subPropertyEditor);
							logger.debug("Registrato Wrapper del property editor: "+propertyEditor+" per il path (combo): "+path);
							logger.debug("Registrato property editor: "+propertyEditor+" per il path (combo): "+path+".object");
						}
					}
				}
				else {
					String path = "anagraficaProperties["+shortName+"]";
					registry.registerCustomEditor(ValoreDTO.class, path, new ValoreDTOPropertyEditor(propertyEditor));
					// per le checkbox
					registry.registerCustomEditor(Object.class, path+".object", propertyEditor);
					logger.debug("Registrato Wrapper del property editor: "+propertyEditor+" per il path: "+path);
					logger.debug("Registrato property editor: "+propertyEditor+" per il path: "+path+".object");
				}
			}
		}
	}

	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		List<TP> tipProprietaOnCreation = applicationService
				.getTipologiaOnCreation(clazzTipologiaProprieta);

		IAnagraficaObjectDTO anagraficaObjectDTO = createObject();
		anagraficaObjectDTO.setParentId(context.getFlowScope().getInteger("parentId"));
		AnagraficaUtils.fillDTO(anagraficaObjectDTO, null, tipProprietaOnCreation);
		return anagraficaObjectDTO;
	}

	public abstract Event precompilaForm(RequestContext context) throws Exception;
			
	public abstract Map getBreadcrumbs(IAnagraficaObjectDTO anagraficaObjectDTO);
	
	public abstract IAnagraficaObjectDTO createObject();
	
	public abstract Event persisti (RequestContext context) throws Exception;
		
	public void setFormulaManager(FormulaManager formulaManager) {
		this.formulaManager = formulaManager;
	}
}
