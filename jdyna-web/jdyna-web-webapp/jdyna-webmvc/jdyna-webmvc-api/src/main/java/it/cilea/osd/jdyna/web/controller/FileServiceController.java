package it.cilea.osd.jdyna.web.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class FileServiceController implements Controller
{

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        String idString = request.getPathInfo();
        String[] pathInfo = idString.split("/", 4);

        String folder = pathInfo[3];
        String fileName = request.getParameter("filename");

        File image = new File(
                getPath()
                        + File.separatorChar + folder 
                        + fileName);

        InputStream is = null;
        try
        {
            is = new FileInputStream(image);

            response.setContentType(request.getSession().getServletContext().getMimeType(
                    image.getName()));
            Long len = image.length();
            response.setContentLength(len.intValue());
            response.setHeader("Content-Disposition", "attachment; filename="
                    + image.getName());
            FileCopyUtils.copy(is, response.getOutputStream());
            response.getOutputStream().flush();
        }
        finally
        {
            if (is != null)
            {
                is.close();
            }
            
        }
        return null;
    }

    protected String getPath()
    {       
        return "/var/jdyna/";
    }

}
