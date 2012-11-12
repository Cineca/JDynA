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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@DiscriminatorValue(value="date")
public class DateValue extends AValue<Date> {	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name="dateValue")
	private Date real;
	
	public static final DateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy");
	public static final DateFormat dateFormatterFull = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	@Override
	public Date getObject() {
		return real;
	}

	@Override
	public void setReal(Date real) {
		this.real = real;
		if (real != null)
		{
			sortValue = String.valueOf(real.getTime());
		}
	}
	
	@Override
	public String[] getUntokenizedValue() {
		return real!=null?new String[]{String.valueOf(real.getTime())}:null;
	}
	
	@Override
	public String[] getTokenizedValue() {
		return real!=null?new String[]{dateFormatter.format(real)}:null;
	}

	@Override
	public Date getDefaultValue() {
		return new Date();
	}
	
	@Override
	public String toString()	{	 
	    return real!=null?dateFormatterFull.format(real):null;
	}
}
