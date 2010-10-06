package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectWithMultiTypeDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.dto.ValoreDTOPropertyEditor;
import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.beans.PropertyEditor;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.Errors;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public abstract class AnagraficaDTOWithMultiTypeCreationAction<P extends Property<TP>, TP extends PropertiesDefinition, AO extends AnagraficaObject<P, TP>, TY extends ATipologia<TP>>
		extends BaseFormAction {

	protected Log logger = LogFactory
			.getLog(AnagraficaDTOWithMultiTypeCreationAction.class);

	
	protected IPersistenceDynaService applicationService;
	
	protected FormulaManager formulaManager;

//	private Class<A> clazzArea;
	private Class<TP> clazzTipologiaProprieta;
	private Class<AO> clazzAnagraficaObject;
	private Class<TY> clazzTipologiaObject;
	
//	public void setClazzArea(Class<A> clazzArea) {
//		this.clazzArea = clazzArea;
//	}

	public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta) {
		this.clazzTipologiaProprieta = clazzTipologiaProprieta;
	}

	public void setClazzAnagraficaObject(Class<AO> clazzAnagraficaObject) {
		this.clazzAnagraficaObject = clazzAnagraficaObject;
	}

	public void setClazzTipologiaObject(Class<TY> clazzTipologiaObject) {
		this.clazzTipologiaObject = clazzTipologiaObject;
	}

	public Event referenceData(RequestContext context) throws Exception {
		IAnagraficaObjectWithMultiTypeDTO anagraficaObjectDTO = (IAnagraficaObjectWithMultiTypeDTO) getFormObject(context);
		Map breadCumbs=getBreadcrumbs();
		context.getFlashScope().put("breadCumbs", breadCumbs);
	
		List<Integer> tipologieId = anagraficaObjectDTO.getTipologieId();
		List<TY> tipologieObject = new LinkedList<TY>();
		for (Integer tipologiaId : tipologieId){
			TY tipologiaObject = applicationService.get(clazzTipologiaObject,
					tipologiaId);
			tipologieObject.add(tipologiaObject);
		}
		List<TP> tipProprietaOnCreation = applicationService
				.getTipologiaOnCreation(clazzTipologiaProprieta);

		List<TP> tipProprietaOnCreationInTipologia = new LinkedList<TP>();

		for (TP tip : tipProprietaOnCreation) {
			boolean daMostrare = false;
			for (TY tipObject : tipologieObject){
				if (!tipObject.getMaschera().contains(tip)){
					daMostrare = true;
				}
			}
			if (daMostrare) {
				tipProprietaOnCreationInTipologia.add(tip);
			}
		}

		context.getFlashScope().put("tipologieProprietaInArea",
				tipProprietaOnCreationInTipologia);
		context.getFlashScope().put("simpleNameAnagraficaObject",
				clazzAnagraficaObject.getSimpleName());
		return success();
	}
	
	@Override
	protected void doValidate(RequestContext context, Object formObject,
			Errors errors) throws Exception {
		super.doValidate(context, formObject, errors);
		IAnagraficaObjectWithMultiTypeDTO dto = (IAnagraficaObjectWithMultiTypeDTO) formObject;
		
		AnagraficaObject<P, TP> anagraficaObject = clazzAnagraficaObject.newInstance();
		List<Integer> tipologieId = dto.getTipologieId();
		List<TY> tipologieObject = new LinkedList<TY>();
		if (tipologieId != null	&& tipologieId.size() != 0) {
			for (Integer tipologiaId : tipologieId){
				TY tipologiaObject = applicationService.get(clazzTipologiaObject,
						tipologiaId);
				tipologieObject.add(tipologiaObject);
			}
			
			List<TP> tipProprietaOnCreation = applicationService
						.getTipologiaOnCreation(clazzTipologiaProprieta);

			List<TP> tipProprietaOnCreationInTipologia = new LinkedList<TP>();

			for (TP tip : tipProprietaOnCreation) {
				boolean daMostrare = false;
				for (TY tipObject : tipologieObject){
					if (!tipObject.getMaschera().contains(tip)){
						daMostrare = true;
					}
				}
				if (daMostrare) {
					tipProprietaOnCreationInTipologia.add(tip);
				}
			}

			// bind delle modifiche utente
			AnagraficaUtils.reverseDTO(dto, anagraficaObject, tipProprietaOnCreationInTipologia);
			// ricalcolo le formule
			formulaManager.ricalcolaFormule(anagraficaObject);
			// inserisco nel DTO i nuovi valori
			AnagraficaUtils.fillDTO(dto, anagraficaObject, tipProprietaOnCreationInTipologia);
		}
	}
	
	public Event selezionaUnicaTipologia(RequestContext context) throws Exception {
		List<TY> tipologie = applicationService.getList(clazzTipologiaObject);
		List<Integer> tipologieId = new LinkedList<Integer>();
		tipologieId.add(tipologie.get(0).getId());
		context.getFlowScope().put("tipologieId", tipologieId);
		return success();
	}

	@Override
	protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry registry) {
		IAnagraficaObjectWithMultiTypeDTO commandDTO = null;
		try {
			commandDTO = (IAnagraficaObjectWithMultiTypeDTO) getFormObject(context);
		} catch (Exception e) {
			logger.error("Non e' stato possibile estrarre il Command Object dal flusoo", e);
		}
		
		Set<String> shortNames = (commandDTO == null || commandDTO.getAnagraficaProperties() == null)?
				null:commandDTO.getAnagraficaProperties().keySet();
		if (shortNames != null) {
			for (String shortName : shortNames) {
				TP tipologiaProprieta = applicationService
						.findTipologiaProprietaByShortName(
								clazzTipologiaProprieta, shortName);
				PropertyEditor propertyEditor = tipologiaProprieta
						.getRendering().getPropertyEditor(applicationService);
				if (tipologiaProprieta.getRendering() instanceof WidgetCombo) {
					WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tipologiaProprieta
							.getRendering();
					for (int idx = 0; idx < 100; idx++){
						for (TP subtp : combo.getSottoTipologie()) {
							PropertyEditor subPropertyEditor = subtp
									.getRendering().getPropertyEditor(
											applicationService);
							String path = "anagraficaProperties[" + shortName
									+ "]["+idx+"].object.anagraficaProperties["
									+ subtp.getShortName() + "]";
							registry.registerCustomEditor(
									ValoreDTO.class, path,
									new ValoreDTOPropertyEditor(
											subPropertyEditor));
							registry.registerCustomEditor(
									Object.class, path + ".object",
									subPropertyEditor);
							logger
									.debug("Registrato Wrapper del property editor: "
											+ propertyEditor
											+ " per il path (combo): " + path);
							logger.debug("Registrato property editor: "
									+ propertyEditor + " per il path (combo): "
									+ path + ".object");
						}
					}
				} else {
					String path = "anagraficaProperties[" + shortName + "]";
					registry.registerCustomEditor(
							ValoreDTO.class, path, new ValoreDTOPropertyEditor(
									propertyEditor));
					// per le checkbox
					registry.registerCustomEditor(Object.class,
							path + ".object", propertyEditor);
					logger.debug("Registrato Wrapper del property editor: "
							+ propertyEditor + " per il path: " + path);
					logger.debug("Registrato property editor: "
							+ propertyEditor + " per il path: " + path
							+ ".object");
				}
			}
		}
	}

	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		IAnagraficaObjectWithMultiTypeDTO anagraficaObjectDTO = createObject();
		List<Integer> tipologieId = (List<Integer>) context.getFlowScope().get("tipologieId");
		List<TY> tipologieObject = new LinkedList<TY>();
		if (tipologieId != null	&& tipologieId.size() != 0) {
			for (Integer tipologiaId : tipologieId){
				TY tipologiaObject = applicationService.get(clazzTipologiaObject,
						tipologiaId);
				tipologieObject.add(tipologiaObject);
			}
			
			anagraficaObjectDTO.setTipologieId(tipologieId);
			
			List<TP> tipProprietaOnCreation = applicationService
					.getTipologiaOnCreation(clazzTipologiaProprieta);

			List<TP> tipProprietaOnCreationInTipologia = new LinkedList<TP>();

			for (TP tip : tipProprietaOnCreation) {
				boolean daMostrare = false;
				for (TY tipObject : tipologieObject){
					if (!tipObject.getMaschera().contains(tip)){
						daMostrare = true;
					}
				}
				if (daMostrare) {
					tipProprietaOnCreationInTipologia.add(tip);
				}
			}

			AnagraficaUtils.fillDTO(anagraficaObjectDTO, null,
					tipProprietaOnCreationInTipologia);
		}
		anagraficaObjectDTO.setParentId(context.getFlowScope().getInteger(
				"parentId"));
		return anagraficaObjectDTO;
	}

	public abstract Event precompilaForm(RequestContext context)
			throws Exception;

	public abstract Map getBreadcrumbs();
	
	public abstract IAnagraficaObjectWithMultiTypeDTO createObject();
	
	
	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	public void setFormulaManager(FormulaManager formulaManager) {
		this.formulaManager = formulaManager;
	}
}
