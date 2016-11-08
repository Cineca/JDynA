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
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class FormTabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>, PD extends PropertiesDefinition>
        extends
        AFormTabController<H, T, ET, PD>
{
    

    public FormTabController(Class<T> clazzT,
            Class<H> clazzB, Class<ET> clazzET, Class<PD> clazzPD)
    {
        super(clazzT, clazzB, clazzET, clazzPD);
    }
        
    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception
    {
        String paramId = request.getParameter("id");
        Map<String, Object> map = super.referenceData(request);
        if (paramId != null)
        {
            ET editTab = applicationService
                    .getEditTabByDisplayTab(Integer.parseInt(paramId), tabEditClass);
            if (editTab != null)
            {
                map.put("edittabid", editTab.getId());
            }
        }
        return map;
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception
    {
        T object = (T) command;

        String deleteImage_s = request.getParameter("deleteIcon");

        if (deleteImage_s != null)
        {
            Boolean deleteImage = Boolean.parseBoolean(deleteImage_s);
            if (deleteImage)
            {
                removeTabIcon(object);
            }
        }

        boolean createEditTab = false;
        if (object.getId() == null)
        {
            createEditTab = true;
        }
        applicationService.saveOrUpdate(tabClass,
                object);

        MultipartFile itemIcon = object.getIconFile();

        // if there is a remote url we don't upload the file
        if (itemIcon != null && !itemIcon.getOriginalFilename().isEmpty())
        {
            loadTabIcon(object, object.getId().toString(),
                    object.getIconFile());
            applicationService.saveOrUpdate(tabClass,
                    object);
        }
        if (createEditTab)
        {
            createEditTab(object);
        }
         
        String shortName = Utils.getAdminSpecificPath(request, null);                
        return new ModelAndView(getSuccessView() + "?path=" + shortName);
    }

    protected void createEditTab(T object) throws InstantiationException,
            IllegalAccessException
    {
        String name = ITabService.PREFIX_SHORTNAME_EDIT_TAB
                + object.getShortName();
        String title = ITabService.PREFIX_TITLE_EDIT_TAB
                + object.getTitle();
        ET e = applicationService
                .getTabByShortName(tabEditClass,
                        name);
        if (e == null)
        {
            e = tabEditClass.newInstance();
            e.setDisplayTab(object);
            e.setTitle(title);
            e.setShortName(name);
            applicationService.saveOrUpdate(
                    tabEditClass, e);
        }
        else
        {
            if (e.getDisplayTab() == null)
            {
                e.setDisplayTab(object);
                applicationService.saveOrUpdate(
                        tabEditClass, e);
            }
        }
    }
}
