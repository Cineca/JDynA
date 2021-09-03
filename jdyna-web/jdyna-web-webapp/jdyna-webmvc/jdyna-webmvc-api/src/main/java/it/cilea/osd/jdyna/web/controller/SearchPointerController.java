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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.ParameterizableViewController;

import flexjson.JSONSerializer;
import it.cilea.osd.common.model.Selectable;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.utils.SelectableDTO;
import it.cilea.osd.jdyna.web.ITabService;

public abstract class SearchPointerController<PD extends PropertiesDefinition, T extends Selectable>
        extends ParameterizableViewController
{
    protected final Log logger = LogFactory.getLog(getClass());

    private ITabService applicationService;

    private Class<PD> clazzPD;

    @Override
    public ModelAndView handleRequest(HttpServletRequest request,
            HttpServletResponse response) throws Exception
    {
        Map<String, Object> model = new HashMap<String, Object>();
        String elementID = request.getParameter("elementID");
        String query = request.getParameter("query");

        PD pd = applicationService.get(clazzPD, Integer.parseInt(elementID));
        String[] filter = getFilter(pd.getRendering());
        String display = getDisplay(pd.getRendering());
        List results = getResult(pd.getRendering(), query,
                display, filter);

        JSONSerializer serializer = new JSONSerializer();
        serializer.rootName("pointers");
        serializer.exclude("class");
        serializer.deepSerialize(results, response.getWriter());
        response.setContentType("application/json");

        return null;

    }

    protected abstract String[] getFilter(AWidget widget);
    protected abstract String getDisplay(AWidget widget);
    

    protected abstract List<SelectableDTO> getResult(AWidget widget, String query, String expression, String... filtro);
    

    public void setApplicationService(ITabService applicationService)
    {
        this.applicationService = applicationService;
    }

    public ITabService getApplicationService()
    {
        return applicationService;
    }

    public Class<PD> getClazzPD()
    {
        return clazzPD;
    }

    public void setClazzPD(Class<PD> clazzPD)
    {
        this.clazzPD = clazzPD;
    }

}
