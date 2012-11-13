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
import it.cilea.osd.jdyna.dto.IAnagraficaObjectWithTypeDTO;
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

public abstract class AnagraficaDTOWithTypeCreationAction<P extends Property<TP>, TP extends PropertiesDefinition, AO extends AnagraficaObject<P, TP>, TY extends ATipologia<TP>, IA extends IPersistenceDynaService>
		extends BaseFormAction<IA> {

	protected Log logger = LogFactory
			.getLog(AnagraficaDTOWithTypeCreationAction.class);


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
		IAnagraficaObjectWithTypeDTO anagraficaObjectDTO = (IAnagraficaObjectWithTypeDTO) getFormObject(context);
		Integer tipologiaId = anagraficaObjectDTO.getTipologiaId();
		
		Map breadCumbs=getBreadcrumbs(anagraficaObjectDTO,context);
		context.getFlashScope().put("breadCumbs", breadCumbs);
	
		TY tipologiaObject = applicationService.get(clazzTipologiaObject,
				tipologiaId);
		List<TP> tipProprietaOnCreation = applicationService
				.getTipologiaOnCreation(clazzTipologiaProprieta);

		List<TP> tipProprietaOnCreationInTipologia = new LinkedList<TP>();
		for (TP tip : tipProprietaOnCreation) {
			if (!tipologiaObject.getMaschera().contains(tip)) {
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
	protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry registry) {
		IAnagraficaObjectWithTypeDTO commandDTO = null;
		try {
			commandDTO = (IAnagraficaObjectWithTypeDTO) getFormObject(context);
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
	protected void doValidate(RequestContext context, Object formObject,
			Errors errors) throws Exception {
		super.doValidate(context, formObject, errors);
		IAnagraficaObjectWithTypeDTO dto = (IAnagraficaObjectWithTypeDTO) formObject;
		if (dto.getTipologiaId() != null){
			AO anagraficaObject = clazzAnagraficaObject.newInstance();
			
			TY tipologiaObject = applicationService.get(clazzTipologiaObject, dto.getTipologiaId());
			List<TP> tipProprietaOnCreation = applicationService
						.getTipologiaOnCreation(clazzTipologiaProprieta);
	
			List<TP> tipProprietaOnCreationInTipologia = new LinkedList<TP>();
			for (TP tip : tipProprietaOnCreation) {
				if (!tipologiaObject.getMaschera().contains(tip)) {
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

	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		IAnagraficaObjectWithTypeDTO anagraficaObjectDTO = createObject();
		Integer tipologiaId = context.getFlowScope().getInteger("tipologiaId");
		TY tipologiaObject;
		if (tipologiaId != null
				&& null != (tipologiaObject = applicationService.get(
						clazzTipologiaObject, tipologiaId))) {
			anagraficaObjectDTO.setTipologiaId(tipologiaId);
			
			List<TP> tipProprietaOnCreation = applicationService
					.getTipologiaOnCreation(clazzTipologiaProprieta);

			List<TP> tipProprietaOnCreationInTipologia = new LinkedList<TP>();
			for (TP tip : tipProprietaOnCreation) {
				if (!tipologiaObject.getMaschera().contains(tip)) {
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
	
	public abstract Map getBreadcrumbs(IAnagraficaObjectWithTypeDTO anagraficaObjectDTO ,RequestContext context);
	
	public abstract IAnagraficaObjectWithTypeDTO createObject();
	
	public void setFormulaManager(FormulaManager formulaManager) {
		this.formulaManager = formulaManager;
	}
}
