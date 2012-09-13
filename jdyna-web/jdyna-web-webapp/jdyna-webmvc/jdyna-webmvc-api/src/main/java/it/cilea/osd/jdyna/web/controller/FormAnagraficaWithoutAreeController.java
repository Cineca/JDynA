package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.dto.AnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.IAnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.dto.ValoreDTOPropertyEditor;
import it.cilea.osd.jdyna.editor.FilePropertyEditor;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class FormAnagraficaWithoutAreeController<P extends Property<TP>, TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, A extends Tab<H>, EO extends AnagraficaObject<P, TP>>
		extends BaseFormController {

	
	//KNOW ISSUE add method for breadcrumbs
	//KNOW ISSUE add procedure for recalculate formula (jsp too)
	private IPersistenceDynaService applicationService;
	
	private FormulaManager formulaManager;

	private Class<TP> clazzTipologiaProprieta;
	private Class<EO> clazzAnagraficaObject;

	public void setFormulaManager(FormulaManager formulaManager) {
		this.formulaManager = formulaManager;
	}
	
	public void setClazzAnagraficaObject(Class<EO> clazzAnagraficaObject) {
		this.clazzAnagraficaObject = clazzAnagraficaObject;
	}

	
	public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta)
	 {
	 this.clazzTipologiaProprieta = clazzTipologiaProprieta;
	 }

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected ServletRequestDataBinder createBinder(HttpServletRequest request,
			Object command) throws Exception {
		ServletRequestDataBinder servletRequestDataBinder = super.createBinder(request, command);
		AnagraficaObjectDTO commandDTO = (AnagraficaObjectDTO) command;
		for (String shortName : commandDTO.getAnagraficaProperties().keySet()) {
			TP tipologiaProprieta = applicationService.findPropertiesDefinitionByShortName(clazzTipologiaProprieta, shortName);
			PropertyEditor propertyEditor = tipologiaProprieta.getRendering().getPropertyEditor(applicationService);
			if(propertyEditor instanceof FilePropertyEditor) {
				((FilePropertyEditor)propertyEditor).setExternalAuthority(String.valueOf(commandDTO.getParentId()));
				((FilePropertyEditor)propertyEditor).setInternalAuthority(String.valueOf(tipologiaProprieta.getId()));
			}
			if (tipologiaProprieta.getRendering() instanceof WidgetCombo) {
				WidgetCombo<P, TP> combo = (WidgetCombo<P, TP>) tipologiaProprieta
						.getRendering();
				for (int i = 0; i < 100; i++) {
					for (TP subtp : combo.getSottoTipologie()){
						PropertyEditor subPropertyEditor = subtp.getRendering().getPropertyEditor(applicationService);
						if(subPropertyEditor instanceof FilePropertyEditor) {
							((FilePropertyEditor)subPropertyEditor).setExternalAuthority(String.valueOf(commandDTO.getParentId()));
							((FilePropertyEditor)subPropertyEditor).setInternalAuthority(String.valueOf(subtp.getId()));
						}
						String path = "anagraficaProperties["+shortName+"]["+i+"].object.anagraficaProperties["+subtp.getShortName()+"]";
						servletRequestDataBinder.registerCustomEditor(ValoreDTO.class, path, new ValoreDTOPropertyEditor(subPropertyEditor));						
						servletRequestDataBinder.registerCustomEditor(Object.class, path+".object", subPropertyEditor);
						logger.debug("Registrato Wrapper del property editor: "+propertyEditor+" per il path (combo): "+path);
						logger.debug("Registrato property editor: "+propertyEditor+" per il path (combo): "+path+".object");
					}
				}
			}
			else {
				String path = "anagraficaProperties["+shortName+"]";
				servletRequestDataBinder.registerCustomEditor(ValoreDTO.class, path, new ValoreDTOPropertyEditor(propertyEditor));				
				// per le checkbox
				servletRequestDataBinder.registerCustomEditor(Object.class, path+".object", propertyEditor);
				logger.debug("Registrato Wrapper del property editor: "+propertyEditor+" per il path: "+path);
				logger.debug("Registrato property editor: "+propertyEditor+" per il path: "+path+".object");
			}
		}
		return servletRequestDataBinder;
	}

	@Override
	protected Map referenceData(HttpServletRequest request, Object command,
			Errors errors) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		AnagraficaObjectDTO anagraficaObjectDTO = (AnagraficaObjectDTO) command;
				
		List<TP> tipProprieta;
		if(anagraficaObjectDTO.getObjectId()!=null) {
		 tipProprieta = applicationService
				.getListTipologieProprietaFirstLevel(clazzTipologiaProprieta);
		}
		else {
			tipProprieta = applicationService
			.getTipologiaOnCreation(clazzTipologiaProprieta);
		}
		
		map.put("tipologieProprieta", tipProprieta);				
		map.put("simpleNameAnagraficaObject", clazzAnagraficaObject.getSimpleName());
		return map;
	}

	@Override
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {

		AnagraficaObjectDTO anagraficaObjectDTO = new AnagraficaObjectDTO();
		
		String paramAnagraficaObjectId = request.getParameter("id");
		Integer anagraficaId;
		EO object;
		List<TP> tipProprieta;
		
		if(paramAnagraficaObjectId!=null) {
			 anagraficaId = Integer.parseInt(paramAnagraficaObjectId);
			 object = applicationService.get(clazzAnagraficaObject,
						anagraficaId);
			 tipProprieta = applicationService
				.getListTipologieProprietaFirstLevel(clazzTipologiaProprieta);
			 anagraficaObjectDTO.setObjectId(anagraficaId);
		}
		else {
			tipProprieta = applicationService
			.getTipologiaOnCreation(clazzTipologiaProprieta);
			object = clazzAnagraficaObject.newInstance();
		}
						
		AnagraficaUtils.fillDTO(anagraficaObjectDTO, object, tipProprieta);
		return anagraficaObjectDTO;
	}

	
	@Override
	protected ModelAndView onSubmit(HttpServletRequest request,
			HttpServletResponse response, Object object, BindException errors)
			throws Exception {
		AnagraficaObjectDTO anagraficaObjectDTO = (AnagraficaObjectDTO) object;

		if (request.getParameter("cancel") != null) {
			return new ModelAndView(listView);
		}
		
		EO anagraficaObject;
		List<TP> tipProprieta;
		if(anagraficaObjectDTO.getObjectId()!=null) {
			anagraficaObject = applicationService.get(clazzAnagraficaObject,
					anagraficaObjectDTO.getObjectId());
			 tipProprieta = applicationService
				.getListTipologieProprietaFirstLevel(clazzTipologiaProprieta);
			}
		else {
			anagraficaObject = clazzAnagraficaObject.newInstance();
			tipProprieta = applicationService
			.getTipologiaOnCreation(clazzTipologiaProprieta);
		}
						
		AnagraficaUtils.reverseDTO(anagraficaObjectDTO, anagraficaObject, tipProprieta);
		
		anagraficaObject.pulisciAnagrafica();
		applicationService.saveOrUpdate(clazzAnagraficaObject, anagraficaObject);
		saveMessage(request, getText("action.anagrafica.edited", request.getLocale()));

		return new ModelAndView(detailsView, "id", anagraficaObject.getId());	
	}
		
	@Override
	protected void onBindAndValidate(HttpServletRequest request,
			Object command, BindException errors) throws Exception {
		
		super.onBindAndValidate(request, command, errors);
		IAnagraficaObjectDTO dto = (IAnagraficaObjectDTO) command;
		EO anagraficaObject = clazzAnagraficaObject.newInstance();
		
		List<TP> tipProprieta;
		if(dto.getObjectId()!=null) {
		 tipProprieta = applicationService
				.getListTipologieProprietaFirstLevel(clazzTipologiaProprieta);
		}
		else {
			tipProprieta = applicationService
			.getTipologiaOnCreation(clazzTipologiaProprieta);
		}
				
		// bind delle modifiche utente
		AnagraficaUtils.reverseDTO(dto, anagraficaObject, tipProprieta);
		// ricalcolo le formule
		formulaManager.ricalcolaFormule(anagraficaObject);
		// inserisco nel DTO i nuovi valori
		AnagraficaUtils.fillDTO(dto, anagraficaObject, tipProprieta);
	}
}
