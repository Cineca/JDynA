package it.cilea.osd.jdyna.web;

import javax.servlet.http.HttpServletRequest;

public class Utils
{
    
    public static String getAdminSpecificPath(HttpServletRequest request, String candidate)
    {
        if(candidate==null) {
            String idString = request.getPathInfo();
            String[] pathInfo = idString.split("\\/");
            return pathInfo[2];
        }
        return candidate;
    }
    
    public static String getSpecificPath(HttpServletRequest request, String candidate)
    {
        if(candidate==null) {
            String idString = request.getPathInfo();
            String[] pathInfo = idString.split("\\/");            
            return pathInfo[1];
        }
        return candidate;
    }

    public static String getOriginalURL(HttpServletRequest request)
    {
        String fullURL = request.getRequestURL().toString();

        if (request.getQueryString() != null)
        {
            fullURL = fullURL + "?" + request.getQueryString();
        }
        return fullURL;
    }
    
    
}
