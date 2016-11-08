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
import it.cilea.osd.jdyna.web.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class FormEditTabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>, PD extends PropertiesDefinition>
        extends AFormEditTabController<H, T, ET, PD>
{

    public final static String SUBMIT_DECOUPLE = "dehookupit";
    public final static String SUBMIT_HOOKUP = "hookupit";
    
    
    public FormEditTabController(Class<T> tabClass, Class<H> clazzB, Class<ET> editTabClass, Class<PD> pdClass)
    {
        super(tabClass, clazzB, editTabClass, pdClass);        

    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        String paramId = request.getParameter("id");
        if (paramId != null) {
            ET object = applicationService.get(
                    tabEditClass,
                    Integer.parseInt(paramId));
            if (object != null && object.getDisplayTab() != null) {
                List<H> owneredContainers = new LinkedList<H>();
                for (H box : object.getDisplayTab()
                        .getMask()) {               
                    owneredContainers.add(box);
                }
                map.put("boxsList", owneredContainers);
            } else {
                map = super.referenceData(request);
            }
        } else {
            map = super.referenceData(request);
        }
        map.put("specificPartPath", Utils.getAdminSpecificPath(request, null));
        return map;
    }

    @Override
    protected ModelAndView processFormSubmission(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ET object = (ET) command;
        Map<String, String> maprequest = request.getParameterMap();

        if (maprequest.containsKey(SUBMIT_DECOUPLE)) {
            applicationService.decoupleEditTabByDisplayTab(object.getDisplayTab().getId(), tabEditClass);
        }
        if (maprequest.containsKey(SUBMIT_HOOKUP)) {
            String hookit = request.getParameter("hookedtab");
            T tab = applicationService.getTabByShortName(
                    tabClass, hookit);
            if (tab == null) {
                return new ModelAndView(getFormView(), "tab", object);
            }
            applicationService.saveOrUpdate(
                    tabEditClass, object);
            applicationService
                    .hookUpEditTabToDisplayTab(object.getId(), tab.getId(), tabEditClass);
        }
        return super.processFormSubmission(request, response, command, errors);
    }

    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        ET object = (ET) command;
      
        String deleteImage_s = request.getParameter("deleteIcon");      

        if (deleteImage_s != null)
        {
            Boolean deleteImage = Boolean.parseBoolean(deleteImage_s);
            if (deleteImage)
            {
                removeTabIcon(object);
            }
        }

                
        applicationService.saveOrUpdate(tabEditClass,
                object);
        
        MultipartFile itemIcon = object.getIconFile();
        
        // if there is a remote url we don't upload the file 
        if (itemIcon != null && !itemIcon.getOriginalFilename().isEmpty())
        {
           loadTabIcon(object, object.getId().toString(), object.getIconFile());
           applicationService.saveOrUpdate(tabEditClass,
                object);
        }
        String shortName = Utils.getAdminSpecificPath(request, null);                
        return new ModelAndView(getSuccessView() + "?path=" + shortName);
    }

}
