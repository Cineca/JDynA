/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.AnagraficaObjectAreaDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.dto.ValoreDTOPropertyEditor;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.beans.PropertyEditor;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.context.MessageSourceAware;
import org.springframework.validation.Errors;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class RealAnagraficaDTOEditingAction
	<P extends Property<TP>, TP extends PropertiesDefinition, I extends IContainable, H extends IPropertyHolder<I>, A extends Tab<I,H>,
		AO extends AnagraficaObject<P, TP>> extends BaseFormAction implements MessageSourceAware {	
	
	protected Log logger = LogFactory.getLog(RealAnagraficaDTOEditingAction.class);
	
	private IPersistenceDynaService applicationService;
	
	private FormulaManager formulaManager;
			
    private Class<A> clazzArea;
	private Class<TP> clazzTipologiaProprieta;
	private Class<AO> clazzAnagraficaObject;
//	private AreaCollectionFilter<P, TP, A, Identifiable> areaCollectionFilter;

	private String baseDetailURL;

	
	public String getBaseDetailURL() {
		return baseDetailURL;
	}

	public void setBaseDetailURL(String baseDetailURL) {
		this.baseDetailURL = baseDetailURL;
	}

	public void setClazzArea(Class<A> clazzArea) {
		this.clazzArea = clazzArea;
	}

	public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta) {
		this.clazzTipologiaProprieta = clazzTipologiaProprieta;
	}

	public void setClazzAnagraficaObject(Class<AO> clazzAnagraficaObject) {
		this.clazzAnagraficaObject = clazzAnagraficaObject;
	}

//	public void setAreaCollectionFilter(
//			AreaCollectionFilter<P, TP, A, Identifiable> areaCollectionFilter) {
//		this.areaCollectionFilter = areaCollectionFilter;
//	}

	public Event referenceData(RequestContext context) throws Exception {
		AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) getFormObject(context);
		AO epiObject = applicationService.get(clazzAnagraficaObject,
				anagraficaObjectDTO.getObjectId());
		Map breadCumbs=getBreadcrumbs(anagraficaObjectDTO);
		context.getFlashScope().put("breadCumbs", breadCumbs);
		List<I> tipProprietaInArea = ((ITabService)applicationService)
				.findTipologieProprietaAssegnabiliAndArea(epiObject, anagraficaObjectDTO
						.getAreaId());
		context.getFlashScope().put("tipologieProprietaInArea", tipProprietaInArea);
		List<A> aree = applicationService.getList(clazzArea);
		List<A> aree4Edit=new LinkedList<A>();
		
//		AreaCollectionFilter<P, TP, A, Identifiable> a=new AreaCollectionFilter<P, TP, A, Identifiable>();		
//		a.setApplicationService(applicationService);
		
//		aree4Edit=(List<A>)a.filter((List<A>)aree,(Identifiable)epiObject, 2);
		aree4Edit = aree;
		
		List<I> tipProprietaInAreaWithRenderingFormula = ((ITabService)applicationService)
				.findTipologieProprietaInAreaWithRenderingFormula(clazzArea,
						anagraficaObjectDTO.getAreaId());
		if (tipProprietaInAreaWithRenderingFormula != null
				&& !tipProprietaInAreaWithRenderingFormula.isEmpty()) {
			context.getFlashScope().put("mostraPulsanteFormule", Boolean.TRUE);
		} else {
			context.getFlashScope().put("mostraPulsanteFormule", Boolean.FALSE);
		}
		
		context.getFlashScope().put("areaList", aree4Edit);
		//FIXME: nel caso della rivista il simple name e' collana
		context.getFlashScope().put("simpleNameAnagraficaObject", clazzAnagraficaObject.getSimpleName());
		return success();
	}
	
	public Map getBreadcrumbs(AnagraficaObjectAreaDTO anagraficaObjectDTO) {
		return null;
	}

	@Override
	protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry registry) {
		AnagraficaObjectAreaDTO commandDTO = null;
		try {
			commandDTO = (AnagraficaObjectAreaDTO) getFormObject(context);
		} catch (Exception e) {
			logger.error("Non e' stato possibile estrarre il Command Object dal flusoo", e);
		}

		Set<String> shortNames = (commandDTO == null || commandDTO.getAnagraficaProperties() == null)?
				null:commandDTO.getAnagraficaProperties().keySet();
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

	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		Integer areaId = context.getFlowScope().getRequiredInteger("areaId");
		Integer anagraficaId = context.getFlowScope().getRequiredInteger("epiobject_id");

		List<A> aree = applicationService.getList(clazzArea);
		
		AO epiObject = applicationService.get(clazzAnagraficaObject,
				anagraficaId);

		List<I> tipProprietaInArea = ((ITabService)applicationService)
				.findTipologieProprietaAssegnabiliAndArea(epiObject, areaId);

		// richiamo il super per generare il DTO giusto (semplice, type, multitype) con la reflection
		AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) super.createFormObject(context);
		anagraficaObjectDTO.setAreaId(areaId);
		anagraficaObjectDTO.setObjectId(anagraficaId);
		
		AnagraficaUtils.fillDTO(anagraficaObjectDTO, epiObject, tipProprietaInArea);
		return anagraficaObjectDTO;
	}
	
	@Override
	protected void doValidate(RequestContext context, Object formObject,
			Errors errors) throws Exception {
		super.doValidate(context, formObject, errors);
		AnagraficaObjectAreaDTO dto = (AnagraficaObjectAreaDTO) formObject;
		AO anagraficaObject = applicationService.get(clazzAnagraficaObject,
				dto.getObjectId());
		List<TP> tipProprietaInArea = ((ITabService)applicationService)
				.findTipologieProprietaInArea(clazzArea, dto
						.getAreaId());
		
		// bind delle modifiche utente
		AnagraficaUtils.reverseDTO(dto, anagraficaObject, tipProprietaInArea);
		// ricalcolo le formule
		formulaManager.ricalcolaFormule(anagraficaObject);
		// inserisco nel DTO i nuovi valori
		AnagraficaUtils.fillDTO(dto, anagraficaObject, tipProprietaInArea);
	}
	
	public Event persisti(RequestContext context) throws Exception {
		AnagraficaObjectAreaDTO anagraficaObjectDTO = (AnagraficaObjectAreaDTO) getFormObject(context);

		AO anagraficaObject = applicationService.get(clazzAnagraficaObject,
				anagraficaObjectDTO.getObjectId());

		List<TP> tipProprietaInArea = ((ITabService)applicationService)
				.findTipologieProprietaAssegnabiliAndArea(anagraficaObject, anagraficaObjectDTO
						.getAreaId());
		
		AnagraficaUtils.reverseDTO(anagraficaObjectDTO, anagraficaObject, tipProprietaInArea);
		
		anagraficaObject.pulisciAnagrafica();
		applicationService.saveOrUpdate(clazzAnagraficaObject, anagraficaObject);
		A area = applicationService.get(clazzArea, anagraficaObjectDTO
				.getAreaId());
		final String areaTitle = area.getTitle();
		saveMessage(context, getText("action.anagrafica.edited",
				new Object[] { areaTitle }));

		return success();
	}


	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}
		
	public void setFormulaManager(FormulaManager formulaManager) {
		this.formulaManager = formulaManager;
	}
}
