package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.DTOSoggetto;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;



public class GestioneSoggetto extends BaseFormAction {
	
	public Event save(RequestContext request) throws Exception {

		DTOSoggetto dto = (DTOSoggetto) getFormObject(request);		 
		
		Soggettario soggettario = (Soggettario)request.getFlowScope().get("parent");
		Soggetto soggetto = soggettario.createSoggetto();
		soggetto.setVoce(dto.getVoce());	
		
		return success();
	}	
	
}
