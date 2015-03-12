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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
/**
*
* @author pascarelli
*
*/
@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
@Cache(usage=CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public abstract class ATypeNestedObject<TP extends ANestedPropertiesDefinition> extends AType<TP> implements Comparable<ATypeNestedObject<TP>>, IPropertiesDefinition
{

    /** DB Primary key */
    @Id
    @GeneratedValue(generator = "JDYNA_TYPONESTEDOBJECT_SEQ")
    @SequenceGenerator(name = "JDYNA_TYPONESTEDOBJECT_SEQ", sequenceName = "JDYNA_TYPONESTEDOBJECT_SEQ", allocationSize = 1)    
    private Integer id;
    
    /** The fact of being field obligatory */
    private boolean mandatory;
    
    /** Field repeatability*/
    private boolean repeatable;
    
    /** 
     * Indica la priorita' con cui deve essere visualizzata nei riepiloghi delle 
     * anagrafiche. Maggiore e' la priorita' prima sara' visualizzata la tipologia (default 0)
     **/
    private int priority;
    
    @Column(length = 4000)
    /**
     * Testo di help da mostrare durante l'editing di proprieta' di questa tipologia
     */
    private String help;
    
       
    /**
     * Level access of metadata value {@see AccessLevelConstants}
     */
    private Integer accessLevel;
    
    /** Require new line after it */
    private boolean newline;
    
    
    /** Render mode table or easy view */
    private boolean inline;
    
    @Override
    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory(boolean mandatory)
    {
        this.mandatory = mandatory;
    }

    public boolean isRepeatable()
    {
        return repeatable;
    }

    public void setRepeatable(boolean repeatable)
    {
        this.repeatable = repeatable;
    }

    public int getPriority()
    {
        return priority;
    }

    public void setPriority(int priority)
    {
        this.priority = priority;
    }

    public String getHelp()
    {
        return help;
    }

    public void setHelp(String help)
    {
        this.help = help;
    }

    public Integer getAccessLevel()
    {
        if(accessLevel == null) {
            this.accessLevel = AccessLevelConstants.ADMIN_ACCESS;
        }
        return accessLevel;
    }

    public void setAccessLevel(Integer accessLevel)
    {
        this.accessLevel = accessLevel;
    }
    
    @Override
    public int compareTo(ATypeNestedObject<TP> o)
    {
        if (o == null) return -1;
        if (getPriority() < o.getPriority()) return -1;
        else if (getPriority() > o.getPriority()) return 1;
             else return getShortName().compareTo(o.getShortName());
    }
    
    /** Il metodo restituisce il decorator di tipo contenibile per funzionalita' web
     * */   
    @Transient
    public abstract Class getDecoratorClass();

    public boolean isNewline()
    {
        return newline;
    }

    public void setNewline(boolean newline)
    {
        this.newline = newline;
    }

    public boolean isInline()
    {
        return inline;
    }

    public void setInline(boolean inline)
    {
        this.inline = inline;
    }

    @Override
    public int hashCode() {
        return getShortName() != null ? getShortName().hashCode() : 0;
    } 
}
