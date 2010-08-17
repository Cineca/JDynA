package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.jdyna.dto.DTOAutocomplete;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.ajaxtags.xml.AjaxXmlBuilder;
import org.springframework.web.servlet.view.AbstractView;


public class AutocompleteUsersView extends AbstractView {

	protected void renderMergedOutputModel(Map model,
			HttpServletRequest httpServletRequest, HttpServletResponse response)
			throws Exception {
		
		response.setContentType("text/xml");
		response.setHeader("Cache-Control", "no-cache");
		
		List<DTOAutocomplete> users = (List<DTOAutocomplete>) model.get("result");

		String xml = new AjaxXmlBuilder().addItems(users, "display",
				"id",true).toString();

		ServletOutputStream out = response.getOutputStream();
		out.print(xml);
		out.close();
		
	    
	}

}
