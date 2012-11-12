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
package it.cilea.osd.jdyna.web.controller;

import it.cilea.osd.jdyna.dto.DTOAutocomplete;
import it.cilea.osd.jdyna.model.Soggetto;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.service.IPersistenceSubjectService;
import it.cilea.osd.jdyna.service.ISearchDynaService;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.queryParser.ParseException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

public class AjaxFrontController extends MultiActionController {
	private transient final Log log = LogFactory
			.getLog(AjaxFrontController.class);

	private ISearchDynaService searchService;

	private IPersistenceDynaService applicationService;

	private IPersistenceSubjectService subjectApplicationService;
	
	public AjaxFrontController(IPersistenceDynaService applicationService, ISearchDynaService searchService) {
		this.searchService = searchService;
		this.applicationService = applicationService;
	}
	
	public ModelAndView ajaxFrontPuntatore(HttpServletRequest request,
			HttpServletResponse response) throws ParseException,
			ClassNotFoundException, IOException {
		String filtro = request.getParameter("filtro");
		String query = request.getParameter("query");
		String model = request.getParameter("model");
		String display = request.getParameter("display");
		

		
		if (log.isDebugEnabled()) {
			log.debug("Call ajax controller pointer - filter:"
					+ filtro + " query:" + query + " model:" + model
					+ " display:" + display);
		}
		String queryLowerCase = query.toLowerCase();
		List result = searchService.searchWithReturnDtoAutocomplete(filtro,
				queryLowerCase, Class.forName(model), display);
		return new ModelAndView("autocompleteUsersXML", "result", result);
		
	}

	public ModelAndView ajaxFrontSubject(HttpServletRequest request,
			HttpServletResponse response) throws ParseException,
			ClassNotFoundException {
		String subject = request.getParameter("subject");
		String query = request.getParameter("query");

		List<Soggetto> results = getSubjectApplicationService().likeBySoggettario(Integer
				.parseInt(subject), query);
		// for results

		List<DTOAutocomplete> lista = new LinkedList<DTOAutocomplete>();
		for (Soggetto res : results) {
			DTOAutocomplete element = new DTOAutocomplete();
			element.setId(subject + ":" + res.getVoce());
			element.setDisplay(res.getVoce());
			lista.add(element);
		}
		return new ModelAndView("autocompleteUsersXML", "result", lista);

	}

	public void setSubjectApplicationService(IPersistenceSubjectService subjectApplicationService) {
		this.subjectApplicationService = subjectApplicationService;
	}

	public IPersistenceSubjectService getSubjectApplicationService() {
		return subjectApplicationService;
	}
	
	
}
