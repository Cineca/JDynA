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
package it.cilea.osd.jdyna.value;

import it.cilea.osd.jdyna.model.AValue;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value="double")
public class NumberValue extends AValue<Double>{	
	@Basic
	@Column(name="doubleValue")
	private Double real; 
	
	@Override
	public Double getObject() {
		return real;
	}
	
	@Override
	public void setReal(Double real) {
		this.real = real;
		if (real != null){
			Double tmp = real * 10000;
			sortValue = String.format("%1$020d", real.longValue());
		}
	}
	
	@Override
	public String[] getUntokenizedValue() {
		return real!=null?new String[]{String.format("%1$020d", real.longValue())}:null;
	}
	
	@Override
	public String[] getTokenizedValue() {
		return null;
	}
	
	@Override
	public Double getDefaultValue() {
		return new Double(0);
	}
}
