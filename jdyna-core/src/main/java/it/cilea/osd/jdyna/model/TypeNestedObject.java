/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.jdyna.model;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
*
* @author pascarelli
*
*/
@Entity
@Table(name = "jdyna_nestedobject_typo")
@NamedQueries ({
    @NamedQuery(name="TypeNestedObject.findAll", query = "from TypeNestedObject order by id" ),
    @NamedQuery(name="TypeNestedObject.uniqueByNome", query = "from TypeNestedObject where shortName = ?" )
})
public class TypeNestedObject extends ATypeNestedObject<NestedPropertiesDefinition>
{
    
    @ManyToMany
    @JoinTable(name = "jdyna_nestedobject_typo2mask")
    @Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private List<NestedPropertiesDefinition> mask;

    @Override
    public List<NestedPropertiesDefinition> getMask()
    {
        return mask;
    }

    public void setMaschera(List<NestedPropertiesDefinition> mask) {
        this.mask = mask;
    }

    @Override
    public Class getDecoratorClass()
    {
        return DecoratorTypeNestedObject.class;
    }
}
