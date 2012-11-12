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
