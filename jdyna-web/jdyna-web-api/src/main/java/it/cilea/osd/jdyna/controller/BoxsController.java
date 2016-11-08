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
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

/**
 * Concrete SpringMVC controller is used to list, delete and view detail of box
 * 
 * @author pascarelli
 * 
 */
public class BoxsController<H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition>
        extends BaseAbstractController
{

    private Class<H> boxClass;
    
    private Class<PD> pdClass;

    private ITabService applicationService;

    private String specificPartPath;

    public BoxsController(Class<H> boxClass, Class<PD> pdClass)
    {
        this.boxClass = boxClass;
        this.pdClass = pdClass;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        ModelAndView retValue = null;
        if ("details".equals(method))
            retValue = handleDetails(request);
        else if ("delete".equals(method))
            retValue = handleDelete(request);
        else if ("list".equals(method))
            retValue = handleList(request);
        return retValue;
    }

    protected ModelAndView handleDetails(HttpServletRequest request)
    {

        Map<String, Object> model = new HashMap<String, Object>();
        String tabId = request.getParameter("id");
        Integer paramTypeBoxId = Integer.valueOf(tabId);
        H box = applicationService.get(boxClass, paramTypeBoxId);

        List<IContainable> containableList = applicationService
                .<H, T, PD> findContainableInPropertyHolder(boxClass, box.getId());

        model.put("box", box);
        model.put("containableList", containableList);
        String specificPartPath = Utils.getAdminSpecificPath(request, null);
        model.put("specificPartPath", specificPartPath);
        return new ModelAndView(getDetailsView()+"?path=" + specificPartPath, model);

    }

    protected ModelAndView handleDelete(HttpServletRequest request)
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String boxId = request.getParameter("id");
        Integer paramTypeBoxId = Integer.valueOf(boxId);
        try
        {
            applicationService.delete(boxClass, paramTypeBoxId);
            saveMessage(request, getText("action.box.deleted"));
        }
        catch (Exception e)
        {
            saveMessage(request, getText("action.box.deleted.noSuccess"));
        }
        return new ModelAndView(getListView()+"?path=" + Utils.getAdminSpecificPath(request, null), model);
    }

    protected ModelAndView handleList(HttpServletRequest arg0) throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
        List<H> boxs = new LinkedList<H>();
        boxs = applicationService.getList(boxClass);
        model.put("listBox", boxs);
        model.put("specificPartPath", getSpecificPartPath());
        return new ModelAndView(getListView(), model);
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }
    
    public String getSpecificPartPath() {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPartPath) {
        this.specificPartPath = specificPartPath;
    }
   
    public Class<H> getBoxClass()
    {
        return boxClass;
    }
}
