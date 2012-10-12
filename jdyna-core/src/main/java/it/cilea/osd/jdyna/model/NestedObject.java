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

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OrderBy;

/**
 * @author pascarelli
 *
 */
@Entity
@Table(name = "model_jdyna_nestedobject")
@NamedQueries( {
        @NamedQuery(name = "NestedObject.findAll", query = "from NestedObject order by id"),
        @NamedQuery(name = "NestedObject.paginate.id.asc", query = "from NestedObject order by id asc"),
        @NamedQuery(name = "NestedObject.paginate.id.desc", query = "from NestedObject order by id desc")
//        @NamedQuery(name = "NestedObject.findNestedObjectsByParentIDAndTypoID", query = "from NestedObject where parent.id = ? and typo.id = ?"),
//        @NamedQuery(name = "NestedObject.paginateNestedObjectsByParentIDAndTypoID.asc.asc", query = "from NestedObject where parent.id = ? and typo.id = ?"),
//        @NamedQuery(name = "NestedObject.countNestedObjectsByParentIDAndTypoID", query = "select count(*) from NestedObject where parent.id = ? and typo.id = ?"),
//        @NamedQuery(name = "NestedObject.findNestedObjectsByTypoID", query = "from NestedObject where typo.id = ?"),
//        @NamedQuery(name = "NestedObject.deleteNestedObjectsByTypoID", query = "delete from NestedObject where typo.id = ?")
        })
public class NestedObject extends ANestedObjectWithTypeSupport<NestedProperty, NestedPropertiesDefinition>
{

    
    @OneToMany(mappedBy = "parent")
    @Cascade(value = { CascadeType.ALL, CascadeType.DELETE_ORPHAN })    
    @OrderBy(clause="position asc")
    private List<NestedProperty> anagrafica;

    @ManyToOne
    private TypeNestedObject typo;
    
    @Transient
    private DynamicObject parent; 
    
    @Override
    public List<NestedProperty> getAnagrafica() {
        if(this.anagrafica == null) {
            this.anagrafica = new LinkedList<NestedProperty>();
        }
        return anagrafica;
    }

    @Override
    public Class getClassProperty()
    {
        return NestedProperty.class;
    }

    @Override
    public Class getClassPropertiesDefinition()
    {
        return NestedPropertiesDefinition.class;
    }

    @Override
    public TypeNestedObject getTipologia()
    {
        return typo;
    }

    @Override
    public DynamicObject getParent()
    {
        return this.parent;
    }

}
