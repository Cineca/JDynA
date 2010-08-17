package it.cilea.osd.jdyna.web.flow;

import java.util.Map;


import org.springframework.beans.PropertyEditorRegistry;
import org.springframework.validation.Errors;
import org.springframework.webflow.action.FormAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

public class AnagraficaDTOEditingAction	extends FormAction {	
	
	private Map<String,RealAnagraficaDTOEditingAction> realActions;

	public void setRealActions(
			Map<String, RealAnagraficaDTOEditingAction> realActions) {
		this.realActions = realActions;
	}

	public Event referenceData(RequestContext context) throws Exception {
		return getRealAction(context).referenceData(context);
	}
	
	protected void registerPropertyEditors(RequestContext context, PropertyEditorRegistry registry) {
		getRealAction(context).registerPropertyEditors(context, registry);
	}
	
	protected Object createFormObject(RequestContext context) throws Exception {
		return getRealAction(context).createFormObject(context);		
	}
	
	@Override
	protected void doValidate(RequestContext context, Object formObject,
			Errors errors) throws Exception {
		getRealAction(context).doValidate(context, formObject, errors);
	}
	
	@Override
	public Event bindAndValidate(RequestContext context) throws Exception {
		return getRealAction(context).bindAndValidate(context);
	}
	

	public Event getBaseDetailURL(RequestContext context) {
		context.getFlowScope().put("baseURL",
				getRealAction(context).getBaseDetailURL());
		return success();
	}
	public Event persisti(RequestContext context) throws Exception {
		return getRealAction(context).persisti(context);
	}
	
	private RealAnagraficaDTOEditingAction getRealAction(RequestContext context){
		String modelName = context.getFlowScope().getRequiredString("model");
		RealAnagraficaDTOEditingAction realAction = realActions.get(modelName);
		if (realAction == null){
			throw new IllegalStateException("Nessuna action configurata per il model: "+modelName);
		}
		return realAction;
	}
}
