package it.cilea.osd.jdyna.controller;

import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.web.AbstractEditTab;
import it.cilea.osd.jdyna.web.AbstractTab;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

public class FormTabController<H extends IPropertyHolder<Containable>, T extends AbstractTab<H>, ET extends AbstractEditTab<H, T>>
        extends
        AFormTabController<H, T, ET>
{
    

    public FormTabController(Class<T> clazzT,
            Class<H> clazzB, Class<ET> clazzET)
    {
        super(clazzT, clazzB, clazzET);
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
                tabUtils.removeTabIcon(object);
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
            tabUtils.loadTabIcon(object, object.getId().toString(),
                    object.getIconFile());
            applicationService.saveOrUpdate(tabClass,
                    object);
        }
        if (createEditTab)
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
        return new ModelAndView(getSuccessView());
    }
}
