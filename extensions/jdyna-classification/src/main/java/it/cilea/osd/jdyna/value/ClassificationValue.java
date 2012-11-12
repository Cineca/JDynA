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
import it.cilea.osd.jdyna.model.Classificazione;

import java.util.LinkedList;
import java.util.List;

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
@DiscriminatorValue(value="classificazione")
public class ClassificationValue extends AValue<Classificazione> {	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="classificazioneValue")
	@Fetch(FetchMode.SELECT)
	@Cascade(value={CascadeType.SAVE_UPDATE,CascadeType.MERGE})
	private Classificazione real;
	
	@Override
	public Classificazione getObject() {
		return real;
	}

	@Override
	public void setReal(Classificazione real) {
		this.real = real;
		Classificazione tmp = real;
		sortValue = "";
		while (tmp != null)
		{
			sortValue = (tmp.getCodice()!=null?tmp.getCodice().toLowerCase():"") + sortValue;
			tmp = tmp.getPadre(); 	
		}
	}
	
	@Override
	public Classificazione getDefaultValue() {
		return null;
	}
	
	@Override
	public String[] getTokenizedValue() {
		if (real == null) {
			return null;
		} else {
			StringBuffer tokenizedValue = new StringBuffer();
			Classificazione classificazione = real;
			while (classificazione != null){
				tokenizedValue.append(classificazione.getNome() + " " + classificazione.getCodice());
				classificazione = classificazione.getPadre();
			}
			return new String[]{tokenizedValue.toString()};
		}
	}
	
	@Override
	public String[] getUntokenizedValue() {
		if (real == null) {
			return null;
		} else {
			List<String> untokenizedValue = new LinkedList<String>();
			Classificazione classificazione = real;
			while (classificazione != null){
				untokenizedValue.add(classificazione.getCodice());
				untokenizedValue.add(String.valueOf(classificazione.getId()));
				classificazione = classificazione.getPadre();
			}
			String[] untokenizedArray = new String[untokenizedValue.size()];
			untokenizedValue.toArray(untokenizedArray); 
			return untokenizedArray;
		}
	}
}
