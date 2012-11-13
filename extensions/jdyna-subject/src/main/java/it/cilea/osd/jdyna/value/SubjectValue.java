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
import it.cilea.osd.jdyna.model.Soggetto;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

@Entity
@DiscriminatorValue(value="soggetto")
public class SubjectValue extends AValue<Soggetto> {	
	@ManyToOne(fetch=FetchType.EAGER)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.MERGE})
	@Fetch(FetchMode.SELECT)
	@JoinColumn(name="soggettoValue")
	private Soggetto real;
		
	@Override
	public Soggetto getObject() {
		return real;
	}

	@Override
	public void setReal(Soggetto real) {
		this.real = real;
		if (real != null && real.getVoce() != null){
			sortValue = real.getVoce().toLowerCase();
		}
	}

	@Override
	public Soggetto getDefaultValue() {
		return null;
	}
	
	@Override
	public String[] getTokenizedValue() {
		return real!=null?new String[]{real.getVoce()}:null;
	}
	
	@Override
	public String[] getUntokenizedValue() {
		return real!=null?new String[]{String.valueOf(real.getId())}:null;
	}
}
