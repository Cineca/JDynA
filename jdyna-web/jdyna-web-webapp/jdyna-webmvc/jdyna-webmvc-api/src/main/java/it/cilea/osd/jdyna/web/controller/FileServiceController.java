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

        File dir = new File(getPath() + File.separatorChar + folder);
        File image = new File(dir, fileName);
                    
        if (image.getParentFile().compareTo(dir) == 0)
        {
            if (image.exists())
            {
                InputStream is = null;
                try
                {
                    is = new FileInputStream(image);

                    response.setContentType(request.getSession()
                            .getServletContext().getMimeType(image.getName()));
                    Long len = image.length();
                    response.setContentLength(len.intValue());
                    response.setHeader("Content-Disposition",
                            "attachment; filename=" + image.getName());
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
            }
            else {
                throw new RuntimeException("File doesn't exists!");
            }
        }
        else {
            throw new RuntimeException("No permission to download this file");
        }
        return null;
    }

    protected String getPath()
    {
        return "/var/jdyna/";
    }

}
