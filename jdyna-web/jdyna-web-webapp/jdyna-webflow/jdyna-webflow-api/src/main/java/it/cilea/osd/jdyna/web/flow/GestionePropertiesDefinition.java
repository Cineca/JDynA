package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.editor.ModelPropertyEditor;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.widget.WidgetCheckRadio;
import it.cilea.osd.jdyna.widget.WidgetClassificazione;
import it.cilea.osd.jdyna.widget.WidgetCombo;
import it.cilea.osd.jdyna.widget.WidgetFormula;
import it.cilea.osd.jdyna.widget.WidgetFormulaClassificazione;
import it.cilea.osd.jdyna.widget.WidgetFormulaNumero;
import it.cilea.osd.jdyna.widget.WidgetFormulaTesto;
import it.cilea.osd.jdyna.widget.WidgetSoggettario;
import it.cilea.osd.jdyna.widget.WidgetTesto;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Hibernate;
import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;



public class GestionePropertiesDefinition<T extends PropertiesDefinition> extends BaseFormAction {
	

	
	private Log log = LogFactory.getLog(GestionePropertiesDefinition.class);
	
	private Class<T> targetModel;
		
	public Class<T> getTargetModel() {
		return targetModel;
	}

	public void setTargetModel(Class<T> targetModel) {
		this.targetModel = targetModel;
	}

	@Override
	public Event createFormObject(RequestContext request)
			throws Exception {	
		log.debug("formBackingObject "+targetModel);
		PropertiesDefinition propertiesDefinition;
        String tipologiaIdString = request.getRequestParameters().get("id");
		if (tipologiaIdString != null && !("".equals(tipologiaIdString))) {
			log.debug("restituita tipologia esistente");
			propertiesDefinition = applicationService.get(targetModel, Integer.valueOf(tipologiaIdString));
			AWidget widget = propertiesDefinition.getRendering();
			if (widget instanceof WidgetCombo)
			{
				Hibernate.initialize(((WidgetCombo) widget).getSottoTipologie());
			}
			else if (widget instanceof WidgetFormulaNumero)
			{
				Hibernate.initialize(((WidgetFormulaNumero) widget).getVariabili());
			}
			else if (widget instanceof WidgetFormulaClassificazione)
			{
				Hibernate.initialize(((WidgetFormulaClassificazione) widget).getVariabili());
			}
			else if (widget instanceof WidgetFormulaTesto)
			{
				Hibernate.initialize(((WidgetFormulaTesto) widget).getVariabili());
			}
			else if (widget instanceof WidgetSoggettario)
			{
				Hibernate.initialize(((WidgetSoggettario) widget).getSoggettari());
			}
			else if (widget instanceof WidgetClassificazione)
			{
				Hibernate.initialize(((WidgetClassificazione) widget).getAlberoClassificatorio());
			}
			if (widget instanceof WidgetCheckRadio)
			{
				Hibernate.initialize(((WidgetCheckRadio) widget).getAlberoClassificatorio());
				Hibernate.initialize(((WidgetCheckRadio) widget).getSoggettario());
			}
			//inserisco nel flusso un flag per bloccare la modifica dei widget sulla tipologia gia' esistente
			request.getFlowScope().put("flag",true);
		}
	    else {
	    	log.debug("restituita nuova tipologia");			
			propertiesDefinition = targetModel.newInstance();
			String isSottoflusso = (String)request.getFlowScope().get("isSottoFlusso");
			if(isSottoflusso!=null && isSottoflusso.equals("true")) {
				propertiesDefinition.setTopLevel(false);
			}
		}
		request.getFlowScope().put("tipologiaProprieta", propertiesDefinition);
		return success();
	}

	public <TP extends PropertiesDefinition> Event bindAndValidateTP(RequestContext request) throws Exception
	{
		log.debug("bindAndValidateTP "+targetModel);
		Event bvEvent = super.bindAndValidate(request);
		String widgetClass = request.getRequestParameters().get("widget");
		TP tipologiaProprieta = (TP)getFormObject(request);
		// se non e' gia' stata associata un widget del tipo selezionato ne creo una nuova istanza
		if (tipologiaProprieta.getRendering() == null ||(!tipologiaProprieta.getRendering().getClass().getName().equals(widgetClass)
		&&widgetClass!=null)		
		)
			//||!tipologiaProprieta.getRendering().getClass().getName().equals(widgetClass))
		{
			log.debug("creato un nuovo widget della classe: "+widgetClass);
			// se il widget non e' passato neanche nella request
			if (widgetClass.equals("")) {
				Errors errors = new BindException(tipologiaProprieta,
						"tipologiaProprieta");
				errors.rejectValue("rendering", "error.message.widget.null");
				getFormErrors(request).addAllErrors(errors);
				return error();
			}

			AWidget widget = (AWidget) Class.forName(widgetClass).newInstance();
			tipologiaProprieta.setRendering(widget);
		}
		else {
			//cancello da formulaEvent le formule registrate in precedenza perche' qualche parametro potrebbe variare in edit del widget
			if(tipologiaProprieta.getRendering() instanceof WidgetFormula) {
				//FIXME KNOW ISSSUE se le cancello le devo pure ricreare altrimenti fino a che non riapro l'oggetto in modifica le formule 				   
				// non si ricalcolano...
			}
		}
		return bvEvent;
	}	

	public Event addSottoTipologie(RequestContext request) throws Exception
	{
		//FIXME il parent in questo caso e' una stringa ERRORE
		WidgetCombo wc = (WidgetCombo)request.getFlowScope().get("parent");
		PropertiesDefinition tip = (PropertiesDefinition)request.getFlowScope().get("tipologiaProprieta");		
		List<PropertiesDefinition> sottoTipologie = wc.getSottoTipologie();
		sottoTipologie.add(tip);
		return success();
	}
	
	public Event cancellaSottoTipologiaCombo(RequestContext request) throws Exception
	{
		PropertiesDefinition p = (PropertiesDefinition)getFormObject(request);
		
		WidgetCombo wc = (WidgetCombo)p.getRendering();
		
		String tip = (String)request.getRequestParameters().get("daRimuovere");
		
		PropertiesDefinition tp = applicationService.get(PropertiesDefinition.class, Integer.parseInt(tip));
				
		applicationService.deleteAllProprietaByTipologiaProprieta(tp.getPropertyHolderClass(),tp);
		wc.getSottoTipologie().remove(tp);		
		return success();
	}	
	
	
	@Override
	protected void registerPropertyEditors(PropertyEditorRegistry registry) {
		super.registerPropertyEditors(registry);
		ModelPropertyEditor propertyEditor = new ModelPropertyEditor(PropertiesDefinition.class);
		propertyEditor.setApplicationService(applicationService);
		registry.registerCustomEditor(PropertiesDefinition.class, "rendering.sottoTipologie", propertyEditor);
		log.debug("Registrato customEditor per le liste di sotto tipologie proprieta della combo");	
	}
	
	@Override
	public Event bindAndValidate(RequestContext context) throws Exception {
		Event event = super.bindAndValidate(context);
		T tipologiaProprieta = (T)getFormObject(context);
		//se il rendering e' un testo devo verificare se l'utente ha inserito la funzionalita' delle collisioni			
		if(tipologiaProprieta.getRendering() instanceof WidgetTesto) {
			WidgetTesto widgetTesto = (WidgetTesto)tipologiaProprieta.getRendering();
			if(widgetTesto.isCollisioni()) {
				//bisogna indicizzare il campo su cui si vuole la funzionalita' (HibernateSearch fara' la query su quel field)
				tipologiaProprieta.setAdvancedSearch(true);
			}
		}
		return event;
	}
	
}
