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
package it.cilea.osd.jdyna.web.controller.json;

import flexjson.JSONSerializer;
import it.cilea.osd.common.controller.BaseAbstractController;
import it.cilea.osd.jdyna.components.IComponent;
import it.cilea.osd.jdyna.web.Box;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.web.json.BoxJSON;
import it.cilea.osd.jdyna.web.json.TabJSON;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

public abstract class AjaxJSONNavigationController<B extends Box, T extends Tab>
        extends BaseAbstractController
{

    @Autowired
    private ITabService applicationService;

    private Class<T> tabClazz;

    private String specificPartPath;

    private Map<String, IComponent> components = new HashMap<String, IComponent>();

    public AjaxJSONNavigationController(Class<T> tabClazz)
    {
        this.tabClazz = tabClazz;
    }

    @Override
    protected ModelAndView handleRequestInternal(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {

        Map<String, Object> model = new HashMap<String, Object>();
        List<TabJSON> tabs = loadNavigation(request, response);

        JSONSerializer serializer = new JSONSerializer();
        serializer.rootName("navigation");
        serializer.exclude("class");
        serializer.deepSerialize(tabs, response.getWriter());
        response.setContentType("application/json");
        return null;
    }

    public List<TabJSON> loadNavigation(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {

        Integer id = Integer.parseInt(request.getParameter("objectId"));

        List<T> tabs = applicationService.getList(tabClazz);
        List<TabJSON> realTabs = new ArrayList<TabJSON>();

        for (T tab : tabs)
        {
            TabJSON tabJSON = new TabJSON();
            tabJSON.setId(tab.getId());
            tabJSON.setShortName(tab.getShortName());
            tabJSON.setTitle(tab.getTitle());
            List<B> boxs = tab.getMask();
            for (B box : boxs)
            {
                BoxJSON boxJson = null;

                IComponent comp = null;
                try {
                    comp = components.get(box.getShortName());
                }
                catch(NullPointerException ex) {
                    log.warn("Components empty for this entity class", ex);
                }
                
                if (comp != null)
                {
                    request.setAttribute("entityID", id);
                    comp.setShortName(box.getShortName());
                    List<String[]> compSubLinks = comp.sublinks(request,
                            response);           
                    for(String[] compSubLink : compSubLinks) {                        
                        boxJson = new BoxJSON();
                        boxJson.setCollapsed(false);
                        boxJson.setShortName(compSubLink[0]);
                        boxJson.setTitle(compSubLink[1]);
                        boxJson.setCountBoxPublicMetadata(Integer.parseInt(compSubLink[2]));
                        tabJSON.getBoxes().add(boxJson);
                    }
                }
                else
                {

                    if (!box.isUnrelevant() && !isBoxHidden(id, box))
                    {
                        if (boxJson == null)
                        {
                            boxJson = new BoxJSON();
                        }
                        boxJson.setCollapsed(box.isCollapsed());
                        boxJson.setId(box.getId());
                        boxJson.setShortName(box.getShortName());
                        boxJson.setTitle(box.getTitle());

                        int countBoxPublicMetadata = countBoxPublicMetadata(id,
                                box, true);
                        boxJson.setCountBoxPublicMetadata(countBoxPublicMetadata);
                        tabJSON.getBoxes().add(boxJson);
                    }
                }
            }
            realTabs.add(tabJSON);
        }

        return realTabs;

    }

    public abstract int countBoxPublicMetadata(Integer objectID, B box,
            boolean b);

    public abstract boolean isBoxHidden(Integer objectID, B box);

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public Map<String, IComponent> getComponents()
    {
        return components;
    }

    public void setComponents(Map<String, IComponent> components)
    {
        this.components = components;
    }

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPath)
    {
        this.specificPartPath = specificPath;
    }

}
