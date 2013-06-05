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
package it.cilea.osd.jdyna.model;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author pascarelli
 *
 */
@Entity
@Table(name="jdyna_no_pdef")
@NamedQueries( {
    @NamedQuery(name = "NestedPropertiesDefinition.findAll", query = "from NestedPropertiesDefinition order by id"),    
    @NamedQuery(name = "NestedPropertiesDefinition.findValoriOnCreation", query = "from NestedPropertiesDefinition where onCreation=true"),
    @NamedQuery(name = "NestedPropertiesDefinition.findSimpleSearch", query = "from NestedPropertiesDefinition where simpleSearch=true"),
    @NamedQuery(name = "NestedPropertiesDefinition.findAdvancedSearch", query = "from NestedPropertiesDefinition where advancedSearch=true"),
    @NamedQuery(name = "NestedPropertiesDefinition.uniqueIdByShortName", query = "select id from NestedPropertiesDefinition where shortName = ?"),
    @NamedQuery(name = "NestedPropertiesDefinition.uniqueByShortName", query = "from NestedPropertiesDefinition where shortName = ?"),
    @NamedQuery(name = "NestedPropertiesDefinition.findValoriDaMostrare", query = "from NestedPropertiesDefinition where showInList = true")
})
public class NestedPropertiesDefinition extends ANestedPropertiesDefinition
{

    
    @Override
    public Class getAnagraficaHolderClass()
    {
        return NestedObject.class;
    }

    @Override
    public Class getPropertyHolderClass()
    {
        return NestedProperty.class;
    }

    @Override
    public Class getDecoratorClass()
    {       
        return DecoratorNestedPropertiesDefinition.class;
    }

}
