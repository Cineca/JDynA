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

import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.TypedBox;
import it.cilea.osd.jdyna.web.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.ModelAndView;

public class DecoratorPropertiesDefinitionController<TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>> extends ADecoratorController {

    private Class<TP> targetModel;
    private Class<H> holderModel;
    
	protected ITabService applicationService;


	public DecoratorPropertiesDefinitionController(Class<TP> targetModel, Class<H> holderModel)
    {
        this.targetModel = targetModel;
        this.holderModel = holderModel;
    }

    public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}


    protected ModelAndView handleDetails(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        String paramTipologiaProprietaId = request.getParameter("id");
        Integer tipologiaProprietaId = Integer.valueOf(paramTipologiaProprietaId);
        PropertiesDefinition propertiesDefinition = applicationService.get(targetModel, tipologiaProprietaId);
        
        model.put("tipologiaProprieta", propertiesDefinition);
        model.put("addModeType", "display");
        return new ModelAndView(getDetailsView()+"?path="+ Utils.getAdminSpecificPath(request, null), model);
    }

	protected ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<? extends PropertiesDefinition> listTipologiaProprieta = applicationService.getList(targetModel);	 
		model.put("tipologiaProprietaList", listTipologiaProprieta);		
		return new ModelAndView(getListView(), model);
	}
	

	protected ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramOTipologiaProprietaId = request.getParameter("pDId");
		String boxId = request.getParameter("boxId");
		String tabId = request.getParameter("tabId");
		Integer tipologiaProprietaId = Integer.decode(paramOTipologiaProprietaId);
		
		try {

			TP tip = applicationService.get(targetModel, tipologiaProprietaId);
			
			//cancello tutte le proprieta' salvate in passato
			applicationService.<it.cilea.osd.jdyna.model.Property<TP>, TP>deleteAllProprietaByTipologiaProprieta(tip.getPropertyHolderClass(), tip);
			//cancello se fanno parte di qualche property holder		
			IContainable containable = applicationService.findContainableByDecorable(tip.getDecoratorClass(),tipologiaProprietaId);
			applicationService.<H, T, TP>deleteContainableInPropertyHolder(holderModel,containable);
			if(TypedBox.class.isAssignableFrom(holderModel)) {
			    TypedBox box = (TypedBox)applicationService.get(holderModel, Integer.parseInt(boxId));
			    box.getTypeDef().getMask().remove(tip);
			}
			
			applicationService.delete(tip.getDecoratorClass(), containable.getId());
			
			saveMessage(request, getText("action.propertiesdefinition.deleted", request
					.getLocale()));
		} catch (Exception ecc) {			
			saveMessage(request, getText("action.propertiesdefinition.deleted.noSuccess", request
					.getLocale()));			
		}
		
		return new ModelAndView(getListView()+"?id="+boxId+"&tabId="+tabId+"&path="+Utils.getAdminSpecificPath(request, null), model);
	}
}

