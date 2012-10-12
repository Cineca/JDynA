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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Index;

/**
 * @author pascarelli
 *
 */
@Entity
@Table(name="model_jdyna_nestedobject_prop", 
        uniqueConstraints = {@UniqueConstraint(columnNames={"position","typo_id","parent_id"})})
@NamedQueries( {
    @NamedQuery(name = "NestedProperty.findPropertyByPropertiesDefinition", query = "from NestedProperty where typo = ? order by position"),
    @NamedQuery(name = "NestedProperty.findAll", query = "from NestedProperty order by id"),    
    @NamedQuery(name = "NestedProperty.findPropertyByParentAndTypo", query = "from NestedProperty  where (parent.id = ? and typo.id = ?) order by position"),   
    @NamedQuery(name = "NestedProperty.deleteAllPropertyByPropertiesDefinition", query = "delete from NestedProperty property where typo = ?)")
})
public class NestedProperty extends ANestedProperty<NestedPropertiesDefinition>
{
    

    @ManyToOne(fetch=FetchType.EAGER)
    @Fetch(FetchMode.SELECT)    
    private NestedPropertiesDefinition typo;
    
    
    @ManyToOne  
    @Index(name = "model_jdyna_nestedobject_prop_parent_id")
    private NestedObject parent;

    @Override
    public NestedPropertiesDefinition getTypo()
    {
        return this.typo;
    }

    @Override
    public void setTypo(NestedPropertiesDefinition propertyDefinition)
    {
        this.typo = propertyDefinition;
    }

    @Override
    public void setParent(
            AnagraficaSupport<? extends Property<NestedPropertiesDefinition>, NestedPropertiesDefinition> parent)
    {
        this.parent = (NestedObject)parent;
    }

    @Override
    public AnagraficaSupport<? extends Property<NestedPropertiesDefinition>, NestedPropertiesDefinition> getParent()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
