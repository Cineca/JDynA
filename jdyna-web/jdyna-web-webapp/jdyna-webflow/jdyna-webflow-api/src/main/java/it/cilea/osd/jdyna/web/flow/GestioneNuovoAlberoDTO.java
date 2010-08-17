package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.DTOAlberoClassificatore;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;



public class GestioneNuovoAlberoDTO extends BaseFormAction {
	
	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		
		
		Integer alberoPKEY = context.getFlowScope().getInteger("albero_id");
		DTOAlberoClassificatore alberoDTO = new DTOAlberoClassificatore();
		
		if(alberoPKEY!=null) {
			//devo recuperare l'albero da db per riempire i campi del dto
			AlberoClassificatorio albero = applicationService.get(AlberoClassificatorio.class, alberoPKEY);
			alberoDTO.setFlat(false);		
			alberoDTO.setAttiva(albero.isAttiva());
			alberoDTO.setDescrizione(albero.getDescrizione());
			alberoDTO.setNome(albero.getNome());
		}
		
		return alberoDTO;
		
	}
	
	public Event putBreadCrumbs(RequestContext context) throws Exception {
		Map<String,String> links=new LinkedHashMap<String,String>();
	
			
			links.put("albero/list.htm", "lista alberi classificatori");
			links.put("","nuova albero");
			context.getFlashScope().put("breadCumbs", links);
		return success();
	}
	
	public Event persisti(RequestContext context) throws Exception {		

		DTOAlberoClassificatore alberoDTO = (DTOAlberoClassificatore)getFormObject(context);
		Integer alberoPKEY = context.getFlowScope().getInteger("albero_id");
		AlberoClassificatorio albero = null;
		if(alberoPKEY!=null) {
			//devo recuperare l'albero da db per riempire i campi del dto
			 albero = applicationService.get(AlberoClassificatorio.class, alberoPKEY);			
		}
		else {
			albero = new AlberoClassificatorio();
		}
		albero.setAttiva(alberoDTO.isAttiva());
		albero.setDescrizione(alberoDTO.getDescrizione());
		albero.setNome(alberoDTO.getNome());
		albero.setFlat(false);
		albero.setCodiceSignificativo(alberoDTO.isCodiceSignificativo());
		applicationService.saveOrUpdate(AlberoClassificatorio.class, albero);
		context.getFlowScope().put("albero_id", albero.getId());
		context.getFlowScope().put("albero", albero);
		return success();
		
	}
	
}	
