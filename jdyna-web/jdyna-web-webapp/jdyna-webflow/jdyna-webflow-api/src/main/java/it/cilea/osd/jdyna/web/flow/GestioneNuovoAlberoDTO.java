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
