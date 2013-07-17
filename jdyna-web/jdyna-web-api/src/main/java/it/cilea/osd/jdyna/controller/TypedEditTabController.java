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

import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.TypedAbstractEditTab;
import it.cilea.osd.jdyna.web.TypedAbstractTab;
import it.cilea.osd.jdyna.web.Utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;

public class TypedEditTabController<H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, T extends TypedAbstractTab<H, A, PD>, ET extends TypedAbstractEditTab<H, A, PD, T>> extends EditTabController<H, T, ET>
{
    
    private Class<A> typoClass; 

    public TypedEditTabController(Class<ET> tabsClass, Class<A> typoClass)
    {
        super(tabsClass);
        this.typoClass = typoClass;
    }

       
    
    @Override
    protected ModelAndView handleList(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        List<ET> tabs = new LinkedList<ET>();
        
        String shortName = Utils.getAdminSpecificPath(request, null);     
        
        A dot = getApplicationService().findTypoByShortName(typoClass, shortName);
        tabs = getApplicationService().findEditTabByType(getTabsClass(), dot);
        model.put("listTab", tabs);
        model.put("specificPartPath", shortName);
        return new ModelAndView(getListView(),model);
    }
}
