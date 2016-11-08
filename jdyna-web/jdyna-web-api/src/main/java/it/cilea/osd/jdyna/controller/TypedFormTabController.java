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

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.TypedAbstractEditTab;
import it.cilea.osd.jdyna.web.TypedAbstractTab;
import it.cilea.osd.jdyna.web.Utils;

public class TypedFormTabController<H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, T extends TypedAbstractTab<H, A, PD>, ET extends TypedAbstractEditTab<H, A, PD, T>>
        extends FormTabController<H, T, ET, PD>
{

    private Class<A> typoClass;

    public TypedFormTabController(Class<T> clazzT, Class<H> clazzB,
            Class<ET> clazzET, Class<A> typoClass, Class<PD> pdClass)
    {
        super(clazzT, clazzB, clazzET, pdClass);
        this.typoClass = typoClass;
    }

    @Override
    protected Map referenceData(HttpServletRequest request) throws Exception
    {
        String shortName = Utils
        .getAdminSpecificPath(request, null);
        setSpecificPartPath(shortName);
        Map map = super.referenceData(request);
        A dot = applicationService.findTypoByShortName(typoClass, getSpecificPartPath());
        List<H> box = applicationService.findBoxByType(boxClass, dot);
        map.put("boxsList", box);
        return map;
    }
    
    @Override
    protected Object formBackingObject(HttpServletRequest request)
            throws Exception
    {
        String shortName = Utils
                .getAdminSpecificPath(request, null);        
        A dot = applicationService.findTypoByShortName(typoClass, shortName);
        T tab = (T) super.formBackingObject(request);
        tab.setTypeDef(dot);
        return tab;
    }

    @Override
    protected void createEditTab(T object) throws InstantiationException,
            IllegalAccessException
    {

        String name = ITabService.PREFIX_SHORTNAME_EDIT_TAB
                + object.getShortName();
        String title = ITabService.PREFIX_TITLE_EDIT_TAB + object.getTitle();
        ET e = applicationService.getTabByShortName(tabEditClass, name);
        if (e == null)
        {
            e = tabEditClass.newInstance();
            e.setDisplayTab(object);
            e.setTitle(title);
            e.setShortName(name);
            e.setTypeDef(object.getTypeDef());
            applicationService.saveOrUpdate(tabEditClass, e);
        }
        else
        {
            if (e.getDisplayTab() == null)
            {
                e.setDisplayTab(object);
                applicationService.saveOrUpdate(tabEditClass, e);
            }
        }
    }

}
