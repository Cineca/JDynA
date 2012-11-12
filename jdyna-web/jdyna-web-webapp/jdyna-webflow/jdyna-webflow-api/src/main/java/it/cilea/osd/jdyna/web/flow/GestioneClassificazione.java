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
