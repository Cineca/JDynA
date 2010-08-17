package it.cilea.osd.jdyna.web.flow;

import it.cilea.osd.common.webflow.BaseFormAction;
import it.cilea.osd.jdyna.dto.DTOGestioneAlberoClassificatore;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.utils.ClassificationExportUtils;

import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;



public class GestioneAlberoDTO extends BaseFormAction {
	
	private ClassificationExportUtils exportUtils;
	
	
	public Event putBreadCrumbs(RequestContext context) throws Exception {
		Map<String,String> links=new LinkedHashMap<String,String>();
	
		AlberoClassificatorio albero=(AlberoClassificatorio) context.getFlowScope().get("albero");
			
			links.put("albero/list.htm", "lista alberi classificatori");
			links.put("flusso.flow?id="+albero.getId()+"&_flowId=gestioneAlbero-flow",albero.getNome());
			links.put("","modifica albero");
			context.getFlashScope().put("breadCumbs", links);
		return success();
	}
	
	@Override
	protected Object createFormObject(RequestContext context) throws Exception {
		
		
		Integer alberoPKEY = context.getFlowScope().getInteger("albero_id");
		
		DTOGestioneAlberoClassificatore alberoDTO = new DTOGestioneAlberoClassificatore();
		
		if(alberoPKEY!=null) {
			//devo recuperare l'albero da db per riempire i campi del dto
			AlberoClassificatorio albero = applicationService.get(AlberoClassificatorio.class, alberoPKEY);
			context.getFlowScope().put("albero", albero);
			//alberoDTO.setAttiva(albero.isAttiva());
			alberoDTO.setDescrizione(albero.getDescrizione());
			alberoDTO.setNome(albero.getNome());
			alberoDTO.setId(albero.getId());
			alberoDTO.setCodiceSignificativo(albero.isCodiceSignificativo());
			if(albero.isFlat()) {
				alberoDTO.setFlat(true);
			}
						
		}
		
		return alberoDTO;
		
	}
	
	public Event persistiAlbero(RequestContext context) throws Exception {		

		DTOGestioneAlberoClassificatore alberoDTO = (DTOGestioneAlberoClassificatore)getFormObject(context);
		Integer alberoPKEY = context.getFlowScope().getInteger("albero_id");
		AlberoClassificatorio albero = null;
		if(alberoPKEY!=null) {
			//devo recuperare l'albero da db per riempire i campi del dto
			 albero = applicationService.get(AlberoClassificatorio.class, alberoPKEY);			
		}
		
		//albero.setAttiva(alberoDTO.isAttiva());
		albero.setDescrizione(alberoDTO.getDescrizione());
		albero.setNome(alberoDTO.getNome());
		albero.setFlat(alberoDTO.isFlat());
		albero.setCodiceSignificativo(alberoDTO.isCodiceSignificativo());
		applicationService.saveOrUpdate(AlberoClassificatorio.class, albero);
		context.getFlowScope().put("albero_id", albero.getId());
		context.getFlowScope().put("albero", albero);
		return success();
		
	}
	
	public Event persistiTopClassificazione(RequestContext context) throws Exception {		

		DTOGestioneAlberoClassificatore alberoDTO = (DTOGestioneAlberoClassificatore)getFormObject(context);
		Integer alberoPKEY = context.getFlowScope().getInteger("albero_id");
		AlberoClassificatorio albero = null;
		if(alberoPKEY!=null) {
			//devo recuperare l'albero da db per riempire i campi del dto
			 albero = applicationService.get(AlberoClassificatorio.class, alberoPKEY);			
		}		
		Classificazione real = albero.createClassificazione();
		real.setAttivo(alberoDTO.isTopAttivo());
		real.setSelezionabile(alberoDTO.isTopSelezionabile());
		real.setCodice(alberoDTO.getTopCodice());
		real.setNome(alberoDTO.getTopNome());			
		
		//svuoto form			
		alberoDTO.setCreateSubClassificazione(false);
		alberoDTO.setTopAttivo(false);
		alberoDTO.setTopSelezionabile(false);
		alberoDTO.setTopCodice("");
		alberoDTO.setTopNome("");
		
		applicationService.saveOrUpdate(AlberoClassificatorio.class, albero);		
		context.getFlowScope().put("albero_id", albero.getId());
		context.getFlowScope().put("albero", albero);
		return success();
		
	}
	
	public Event persistiModificheClassificazione(RequestContext context) throws Exception {		

		DTOGestioneAlberoClassificatore alberoDTO = (DTOGestioneAlberoClassificatore)getFormObject(context);
		Integer alberoPKEY = context.getFlowScope().getInteger("albero_id");
		Integer classificazionePKEY = alberoDTO.getEditID(); //context.getFlowScope().getInteger("classificazione_id");
		AlberoClassificatorio albero = null;
		Classificazione real = null;
		if(alberoPKEY!=null && classificazionePKEY!=null ) {
			//devo recuperare l'albero da db per riempire i campi del dto
			 albero = applicationService.get(AlberoClassificatorio.class, alberoPKEY);			
			 real = applicationService.get(Classificazione.class, classificazionePKEY);
		}		
		else {
			throw new Exception("Nessun id dell'albero o della classificazione trovato");
		}
		
		real.setAttivo(alberoDTO.isEditAttivo());
		real.setSelezionabile(alberoDTO.isEditSelezionabile());
		real.setCodice(alberoDTO.getEditCodice());
		real.setNome(alberoDTO.getEditNome());		
		
		if(alberoDTO.isCreateSubClassificazione()) {
			//devo creare la sottoclassificazione solo se l'albero non è flat
			if(alberoDTO.isFlat()) {
				log.debug("Non è stata possibile creare la sottoclassificazione poichè l'albero è flat");
				Errors errors = new BindException(alberoDTO, "gestioneAlbero");
				errors.rejectValue("createSubClassificazione","error.create.subclassificazione.albero.is.flat");
				getFormErrors(context).addAllErrors(errors);	
			}
			else {
				Classificazione sub = real.createClassificazione();
				sub.setAttivo(alberoDTO.isSubAttivo());
				sub.setSelezionabile(alberoDTO.isSubSelezionabile());
				sub.setCodice(alberoDTO.getSubCodice());
				sub.setNome(alberoDTO.getSubNome());	
			
			
				//svuoto form						
				alberoDTO.setSubAttivo(false);
				alberoDTO.setSubSelezionabile(false);
				alberoDTO.setSubCodice("");
				alberoDTO.setSubNome("");
			}
		}
		
		//svuoto form
		alberoDTO.setCreateSubClassificazione(false);
		alberoDTO.setDescrizione("");
		alberoDTO.setEditAttivo(false);
		alberoDTO.setEditSelezionabile(false);
		alberoDTO.setEditCodice("");
		alberoDTO.setEditNome("");
		
		
		applicationService.saveOrUpdate(AlberoClassificatorio.class, albero);		
		context.getFlowScope().put("albero_id", albero.getId());
		context.getFlowScope().put("albero", albero);
		return success();
		
	}
	
	public Event export(RequestContext context) throws Exception {
		
		DTOGestioneAlberoClassificatore alberoDTO = (DTOGestioneAlberoClassificatore)getFormObject(context);
		ServletExternalContext externalContext = (ServletExternalContext) context.getExternalContext();
		HttpServletResponse response = externalContext.getResponse();
				
		Integer alberoID = alberoDTO.getId();		
		AlberoClassificatorio albero = null;
		if (alberoID != null) {
			albero = applicationService.get(AlberoClassificatorio.class,
					alberoID);
		}

		String filename;
		
		filename = "configurazione-albero_classificatorio_"
					+ albero.getNome().replaceAll(" ", "_") + ".xml";
		
		response.setContentType("application/xml");
		response.addHeader("Content-Disposition", "attachment; filename="
				+ filename);

		PrintWriter writer = response.getWriter();
		exportUtils.writeDocTypeANDCustomEditorAlberoClassificatore(writer);

		if (albero != null) {
			exportUtils.alberoClassificatorioToXML(writer, albero);
		}
				
		response.getWriter().print("</beans>");		
		response.flushBuffer();
		return success();
	}
	
	public Event eliminaClassificazione(RequestContext context) throws Exception {
		DTOGestioneAlberoClassificatore alberoDTO = (DTOGestioneAlberoClassificatore)getFormObject(context);
		
		Integer classificazionePKEY = alberoDTO.getEditID()==null?context.getRequestParameters().getInteger("editID"):alberoDTO.getEditID();
		
		try {			
			applicationService.delete(Classificazione.class, classificazionePKEY);			
		}
		catch (Exception e) {			
			log.debug("Non è stata possibile cancellare la classificazione");
			Errors errors = new BindException(alberoDTO, "gestioneAlbero");
			errors.rejectValue("editID","error.delete.classificazione");
			getFormErrors(context).addAllErrors(errors);
			return error();
		}
		return success();
	}

	public ClassificationExportUtils getExportUtils() {
		return exportUtils;
	}

	public void setExportUtils(ClassificationExportUtils exportUtils) {
		this.exportUtils = exportUtils;
	}
}	
