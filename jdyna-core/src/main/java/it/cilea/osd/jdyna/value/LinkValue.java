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
package it.cilea.osd.jdyna.value;

import it.cilea.osd.jdyna.model.AValue;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Embedded;
import javax.persistence.Entity;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity
@DiscriminatorValue(value="link")
public class LinkValue extends AValue<EmbeddedLinkValue> {	
	
	@Embedded
    @AttributeOverrides( {
        @AttributeOverride(name = "valueLink", column = @Column(name = "linkvalue")),
        @AttributeOverride(name = "descriptionLink", column = @Column(name = "linkdescription")) })
    @Cascade(value = { CascadeType.ALL })
	private EmbeddedLinkValue real;

	public LinkValue() {
		this.real = new EmbeddedLinkValue();
	}
	
	@Override
	public EmbeddedLinkValue getObject() {
		return real;
	}

	@Override
	public void setReal(EmbeddedLinkValue real) {
		this.real = real;	
	}

	@Override
	public EmbeddedLinkValue getDefaultValue() {
		return new EmbeddedLinkValue();
	}

	@Override
	public String[] getUntokenizedValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getTokenizedValue() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getValueLink() {
		return real.getValueLink();
	}
	
	public String getDescriptionLink() {
		return real.getDescriptionLink();
	}
	
	@Override
	public String toString()
	{
	    return getDescriptionLink() + " ["+getValueLink()+"]";
	}

}
