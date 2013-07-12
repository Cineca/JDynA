package it.cilea.osd.jdyna.controller;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

public class RedirectToAdminDynamicController extends
        ParameterizableViewController
{
 
    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception {
        
        String idString = request.getPathInfo();
        String[] pathInfo = idString.split("/", 5);
        Enumeration<String> enums = request.getParameterNames();
        String path = null;
        String otherParams = "";
        boolean setOtherParams = false;
        while(enums.hasMoreElements()) {
            String ee = enums.nextElement();            
            if(ee.equals("path")) {
                path = request.getParameter(ee);
            }
            else {
                if(setOtherParams) {
                    otherParams += "&";    
                }
                else {
                    otherParams += "?";
                }
                otherParams += ee +"="+ request.getParameter(ee);
                setOtherParams = true;                
            }
        }        
        String page = pathInfo[4] + (setOtherParams?otherParams:"");
        response.sendRedirect(request.getContextPath() + "/cris/"+pathInfo[1]+"/" + path + "/" + page); 
        return null;        
    }
}