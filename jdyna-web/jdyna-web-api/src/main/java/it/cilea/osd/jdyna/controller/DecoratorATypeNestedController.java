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
package it.cilea.osd.jdyna.controller;

import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public class DecoratorATypeNestedController<P extends Property<TP>, TP extends PropertiesDefinition, A extends ATypeNestedObject<NTP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, ANO extends ANestedObject<NP, NTP, P, TP>> extends ADecoratorController {

    private Class<A> targetModel;
    private Class<ANO> objectModel;
    private Class<H> holderModel;
    
	protected ITabService applicationService;


	public DecoratorATypeNestedController(Class<A> targetModel, Class<ANO> objectModel, Class<H> holderModel)
    {
        this.targetModel = targetModel;
        this.objectModel = objectModel;
        this.holderModel = holderModel;
    }

    public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}

	protected ModelAndView handleDetails(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();        
        String typeObjectId = request.getParameter("id");
        Integer paramTypeObjectId = Integer.valueOf(typeObjectId);
        A tipologia = applicationService.get(targetModel, paramTypeObjectId);    
              
        
        //daMostrare contiene le tipologie dei valori da mostrare
        model.put("mostraValoriList", tipologia.getMask());
        model.put("sizeMostraValori", tipologia.getMask().size());
        model.put("id", paramTypeObjectId);
        model.put("tipologiaObject", tipologia);
        model.put("addModeType", "display");
        return new ModelAndView(getDetailsView() +"?path="+ Utils.getAdminSpecificPath(request, null), model);
    
    }

	protected ModelAndView handleList(HttpServletRequest request) {
	    Map<String, Object> model = new HashMap<String, Object>();
        List<A> listaTipologie = applicationService.getList(targetModel);         
        model.put("tipologiaList", listaTipologie);
        return new ModelAndView(getListView(), model);
	}
	

	protected ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramOTipologiaProprietaId = request.getParameter("pDId");
		String boxId = request.getParameter("boxId");
		String tabId = request.getParameter("tabId");
		Integer tipologiaProprietaId = Integer.decode(paramOTipologiaProprietaId);
		
		try {
		    //find the nestedobject type
			A tip = applicationService.get(targetModel, tipologiaProprietaId);
			
			List<NTP> nestedPropertiesDefinitionMask =  tip.getMask();
			List<NTP> todelete = new ArrayList<NTP>();
			//try to delete all metadata definition 
			for(NTP nestedPropertiesDefinition : nestedPropertiesDefinitionMask) {
			    applicationService.deleteAllProprietaByTipologiaProprieta(nestedPropertiesDefinition.getPropertyHolderClass(), nestedPropertiesDefinition);
			    todelete.add(nestedPropertiesDefinition);
			}
			tip.getMask().clear();
			for(NTP nestedPropertiesDefinition : todelete) {
			    IContainable containable = applicationService.findContainableByDecorable(nestedPropertiesDefinition.getDecoratorClass(),nestedPropertiesDefinition.getId());
			    applicationService.delete(nestedPropertiesDefinition.getDecoratorClass(), containable.getId());
			}

	         //remove all instance of nestedobject
            applicationService.deleteNestedObjectByTypeID(objectModel, tipologiaProprietaId);
            
			//try to remove the nestedobject type from the box
			IContainable containable = applicationService.findContainableByDecorable(tip.getDecoratorClass(),tipologiaProprietaId);
            applicationService.<H, T, TP>deleteContainableInPropertyHolder(holderModel,containable);
			applicationService.delete(tip.getDecoratorClass(), containable.getId());
						
			saveMessage(request, getText("action.propertiesdefinition.deleted", request
					.getLocale()));
		} catch (Exception ecc) {			
			saveMessage(request, getText("action.propertiesdefinition.deleted.noSuccess", request
					.getLocale()));			
		}
		
		return new ModelAndView(getListView()+"?id="+boxId+"&tabId="+tabId+"&path="+ Utils.getAdminSpecificPath(request, null), model);
	}
}

