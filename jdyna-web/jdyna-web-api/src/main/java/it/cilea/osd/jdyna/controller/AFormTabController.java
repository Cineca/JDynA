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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Utils;

public abstract class AFormTabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>, PD extends PropertiesDefinition>
        extends BaseFormController
{

    public static final String DIRECTORY_TAB_ICON = "icon";

    public static final String PREFIX_TAB_ICON = "tab_";

    
    /**
     * the applicationService for query the Tab db, injected by Spring IoC
     */
    protected ITabService applicationService;

    protected Class<T> tabClass;

    protected Class<H> boxClass;

    protected Class<ET> tabEditClass;
    
    protected Class<PD> pdClass;
    
    private String specificPartPath;

    public AFormTabController(Class<T> clazzT, Class<H> clazzB, Class<ET> clazzET, Class<PD> clazzPD)
    {
        this.tabClass = clazzT;
        this.boxClass = clazzB;
        this.tabEditClass = clazzET;
        this.pdClass = clazzPD;
    }

    public String getSpecificPartPath()
    {
        return specificPartPath;
    }

    public void setSpecificPartPath(String specificPartPath)
    {
        this.specificPartPath = specificPartPath;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception {

        Map<String, Object> map = new HashMap<String, Object>();
        String paramId = request.getParameter("id");
        List<H> owneredContainers = new LinkedList<H>();
        List<H> containers = applicationService.getList(boxClass);
        if (paramId != null) {
            Integer id = Integer.parseInt(paramId);
            owneredContainers = applicationService.findPropertyHolderInTab(
                    tabClass, id);
        }
        map.put("boxsList", containers);
        map.put("owneredBoxs", owneredContainers);
        String adminSpecificPath = Utils.getAdminSpecificPath(request, null);
        map.put("specificPartPath", adminSpecificPath);
        map.put("metadataWithPolicySingle", applicationService.getAllPropertiesDefinitionWithPolicySingle(pdClass));
        map.put("metadataWithPolicyGroup", applicationService.getAllPropertiesDefinitionWithPolicyGroup(pdClass));
        return map;
    }

    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception {
        String paramId = request.getParameter("id");
        T tab = null;
        if (paramId == null) {
            tab = tabClass.newInstance();
        
        } else {
            tab = applicationService.get(tabClass, Integer.parseInt(paramId));
        }
        return tab;
    }
    
    
    @Override
    protected ModelAndView onSubmit(HttpServletRequest request,
            HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        T object = (T)command;      
        applicationService.saveOrUpdate(tabClass, object);        
        return new ModelAndView(getSuccessView());
    }
    
    public void setApplicationService(ITabService applicationService) {
        this.applicationService = applicationService;
    }

    public void setTabClass(Class<T> tabClass) {
        this.tabClass = tabClass;
    }

    public void setBoxClass(Class<H> boxClass) {
        this.boxClass = boxClass;
    }

    /**
     * 
     * Load tab icon and copy to default directory.
     * 
     * @param researcher
     * @param rp
     * @param itemImage
     *            MultipartFile to use in webform
     * @throws IOException
     * @throws FileNotFoundException
     */
    public void loadTabIcon(T tab, String iconName, MultipartFile itemImage)
            throws IOException, FileNotFoundException
    {
        String pathImage = tab.getFileSystemPath();
        String ext = itemImage.getOriginalFilename().substring(
                itemImage.getOriginalFilename().lastIndexOf(".") + 1);
        File dir = new File(pathImage + File.separatorChar + DIRECTORY_TAB_ICON);
        dir.mkdir();
        File file = new File(dir, PREFIX_TAB_ICON + iconName + "." + ext);
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);
        it.cilea.osd.common.util.Utils.bufferedCopy(itemImage.getInputStream(), out);
        out.close();
        tab.setExt(ext);
        tab.setMime(itemImage.getContentType());
    }

    /**
     * Remove tab icon from the server.
     * 
     * @param researcher
     */
    public void removeTabIcon(T tab)
    {

        File image = new File(
                tab.getFileSystemPath()
                        + File.separatorChar + DIRECTORY_TAB_ICON
                        + File.separatorChar + PREFIX_TAB_ICON + tab.getId()
                        + "." + tab.getExt());
        image.delete();
        tab.setExt(null);
        tab.setMime(null);
    }
 
 
}