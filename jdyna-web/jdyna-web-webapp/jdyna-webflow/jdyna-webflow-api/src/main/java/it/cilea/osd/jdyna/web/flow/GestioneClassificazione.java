package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;



public class GestioneClassificazione extends BaseFormAction {
	
	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		
		AlberoClassificatorio albero = ((AlberoClassificatorio)context.getFlowScope().get("parent"));
		albero = applicationService.refresh(albero, AlberoClassificatorio.class);
		return albero.createClassificazione();
		
	}
	
	public Event save(RequestContext request) throws Exception {		
		applicationService.saveOrUpdate(Classificazione.class, (Classificazione) getFormObject(request));
		return success();
		
	}
	
	public Event verificaSeEsistonoClassificazioniTopLevel(RequestContext request) throws Exception {
		AlberoClassificatorio albero = ((AlberoClassificatorio)request.getFlowScope().get("parent"));
		albero = applicationService.refresh(albero, AlberoClassificatorio.class);
		int i = 0;
		for (Classificazione cla : albero.getTopClassificazioni()) {
			i++;
		}
		
		if(i>0) {
			request.getFlowScope().put("top", true);			
		}
		
		return success();
	}

}
