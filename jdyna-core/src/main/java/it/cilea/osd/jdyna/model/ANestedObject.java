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

import it.cilea.osd.common.core.TimeStampInfo;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
/**
*
* @author pascarelli
*
*/
@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ANestedObject<P extends ANestedProperty<TP>, TP extends ANestedPropertiesDefinition> extends AnagraficaObject<P, TP>
{
    /** DB Primary key */
    @Id
    @GeneratedValue(generator = "JDYNA_NESTEDOBJECT_SEQ")
    @SequenceGenerator(name = "JDYNA_NESTEDOBJECT_SEQ", sequenceName = "JDYNA_NESTEDOBJECT_SEQ")    
    private Integer id;
    
    @Column(nullable = false, unique = true)
    private String uuid;
   
    /** timestamp info for creation and last modify */
    @Embedded
    private TimeStampInfo timeStampInfo;
    
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getUuid()
    {
        return uuid;
    }

    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }

    public TimeStampInfo getTimeStampInfo()
    {
        return timeStampInfo;
    }

    public void setTimeStampInfo(TimeStampInfo timeStampInfo)
    {
        this.timeStampInfo = timeStampInfo;
    }
    
    public abstract <PP extends Property<PTP>, PTP extends PropertiesDefinition> AnagraficaSupport<PP, PTP> getParent();
    
    public abstract <PP extends Property<PTP>, PTP extends PropertiesDefinition> void setParent(AnagraficaSupport<PP, PTP> parent);
    
    public abstract Class getClassParent();
}
