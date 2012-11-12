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

import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class DecoratorNestedPropertiesDefinitionController<NTP extends ANestedPropertiesDefinition, TTP extends ATypeNestedObject<NTP>, H extends IPropertyHolder<Containable>, T extends Tab<H>> extends BaseAbstractController {

    private Class<TTP> holderModel;
    private Class<NTP> targetModel;
        
	protected ITabService applicationService;


	public DecoratorNestedPropertiesDefinitionController(Class<NTP> targetModel, Class<TTP> holderModel)
    {
        this.targetModel = targetModel;    
        this.holderModel = holderModel;
    }

    public void setApplicationService(ITabService applicationService) {
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

	private ModelAndView handleDetails(HttpServletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        String paramTipologiaProprietaId = request.getParameter("id");
        Integer tipologiaProprietaId = Integer.valueOf(paramTipologiaProprietaId);
        ANestedPropertiesDefinition propertiesDefinition = applicationService.get(targetModel, tipologiaProprietaId);
        
        model.put("tipologiaProprieta", propertiesDefinition);
        model.put("addModeType", "display");
        return new ModelAndView(detailsView, model);
    }

	private ModelAndView handleList(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		List<? extends PropertiesDefinition> listTipologiaProprieta = applicationService.getList(targetModel);	 
		model.put("tipologiaProprietaList", listTipologiaProprieta);		
		return new ModelAndView(listView, model);
	}
	

	private ModelAndView handleDelete(HttpServletRequest request) {
		Map<String, Object> model = new HashMap<String, Object>();
		String paramOTipologiaProprietaId = request.getParameter("pDId");
		String parentOTipologiaProprietaId = request.getParameter("parentId");
		String boxId = request.getParameter("boxId");
		String tabId = request.getParameter("tabId");
		Integer tipologiaProprietaId = Integer.decode(paramOTipologiaProprietaId);
		Integer parentId = Integer.decode(parentOTipologiaProprietaId);
		
		try {

			NTP tip = applicationService.get(targetModel, tipologiaProprietaId);
			
			//cancello tutte le proprieta' salvate in passato
			applicationService.deleteAllProprietaByTipologiaProprieta(tip.getPropertyHolderClass(), tip);
			//cancello se fanno parte di qualche property holder		
			IContainable containable = applicationService.findContainableByDecorable(tip.getDecoratorClass(),tipologiaProprietaId);
			TTP ttp = applicationService.get(holderModel, parentId);
			ttp.getMask().remove(tip);
			applicationService.delete(tip.getDecoratorClass(), containable.getId());
			
			saveMessage(request, getText("action.propertiesdefinition.deleted", request
					.getLocale()));
		} catch (Exception ecc) {			
			saveMessage(request, getText("action.propertiesdefinition.deleted.noSuccess", request
					.getLocale()));			
		}
		
		return new ModelAndView(listView+"?id="+boxId+"&tabId="+tabId, model);
	}
}

