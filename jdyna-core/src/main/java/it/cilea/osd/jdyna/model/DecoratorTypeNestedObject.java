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

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

public class DecoratorTypeNestedObject<PD extends ANestedPropertiesDefinition> extends ADecoratorTypeDefinition<ATypeNestedObject<PD>, PD>
{

    @OneToOne(optional=true)
    @JoinColumn(name="jdyna_no_tp_fk")
    @Cascade(value = {CascadeType.ALL,CascadeType.DELETE_ORPHAN})
    private TypeNestedObject real;
    
    @Override
    public String getShortName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getAccessLevel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLabel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getRepeatable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getPriority()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int compareTo(IContainable o)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setReal(ATypeNestedObject<PD> object)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ATypeNestedObject<PD> getObject()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
