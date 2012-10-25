package it.cilea.osd.jdyna.controller;

import it.cilea.osd.common.controller.BaseFormController;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;

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

public abstract class AFormTabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>>
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
    
    private String specificPartPath;

    public AFormTabController(Class<T> clazzT, Class<H> clazzB, Class<ET> clazzET)
    {
        this.tabClass = clazzT;
        this.boxClass = clazzB;
        this.tabEditClass = clazzET;
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
        map.put("specificPartPath", getSpecificPartPath());
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