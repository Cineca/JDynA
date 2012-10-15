package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class TypeController<P extends Property<TP>,TP extends PropertiesDefinition, A extends ATipologia<TP>> extends BaseAbstractController {

	protected IPersistenceDynaService applicationService;

	private Class<A> tipologiaModel;
	private Class<TP> tpModel;

	public TypeController(Class<A> tipologiaModel, Class<TP> tpModel) {
		this.tipologiaModel = tipologiaModel;
		this.tpModel = tpModel;
	}
	
	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

	@Override
	protected ModelAndView handleRequestInternal(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView retValue = null;
		if ("details".equals(method))
			retValue = handleDetails(request);
		else if ("delete".equals(method))
			retValue = handleDelete(request);
		else if ("list".equals(method))
			retValue = handleList(request);
		return retValue;
	}

	protected ModelAndView handleDetails(HttpServletRequest request) {
		//devo mettere nel modello le tipologiediproprieta progetto che non matchano
		//con quelle mascherate
		Map<String, Object> model = new HashMap<String, Object>();
		String typeObjectId = request.getParameter("id");
		Integer paramTypeObjectId = Integer.valueOf(typeObjectId);
		A tipologia = applicationService.get(tipologiaModel, paramTypeObjectId);	
		
		
		
		//daMostrare contiene le tipologie dei valori da mostrare
		model.put("mostraValoriList", tipologia.getMask());
		model.put("sizeMostraValori", tipologia.getMask().size());
		model.put("id", paramTypeObjectId);
		model.put("tipologiaObject", tipologia);
		model.put("addModeType", "display");
		return new ModelAndView(detailsView, model);
	
	}


	protected ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<A> listaTipologie = applicationService.getList(tipologiaModel);		 
		model.put("tipologiaList", listaTipologie);
		return new ModelAndView(listView, model);
	}
	

	protected ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String typeId = request.getParameter("id");
		Integer paramTypeId = Integer.valueOf(typeId);
		try {
			applicationService.delete(tpModel, paramTypeId);		
			saveMessage(request, getText("action.tipologia.deleted", request.getLocale()));
		}
		catch (Exception e) {
			saveMessage(request, getText("action.tipologia.deleted.noSuccess", request.getLocale()));			
		}
		return new ModelAndView(listView, model);
	}
}
