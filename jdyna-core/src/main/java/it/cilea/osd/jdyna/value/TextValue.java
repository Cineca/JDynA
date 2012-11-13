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

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

@Entity
@DiscriminatorValue(value="testo")
public class TextValue extends AValue<String> {	
	@Type(type = "text")
	@Column(name="testoValue")
	private String real;
	
	//FIXME gestire le stopwords per l'ordinamento
	//@Transient
	//private static String[] stopwords = new String[]{"il","la","lo","un","uno","una","di","the","an","in","l","a"};
	
	@Transient
	private boolean modified;
	
	@Override
	public String getObject() {
		return real;
	}

	@Override
	public void setReal(String real) {
		this.real = real;
		//FIXME gestire le stopwords per l'ordinamento
		if (real != null)
		{
			sortValue = real.substring(0,(real.length()<200?real.length():200)).toLowerCase();
		}
	}

	@Override
	public String getDefaultValue() {
		return "";
	}
	
	@Override
	public String[] getTokenizedValue() {
		return real!=null?new String[]{real}:null;
	}
	
	@Override
	public String[] getUntokenizedValue() {
		return null;
	}
}
