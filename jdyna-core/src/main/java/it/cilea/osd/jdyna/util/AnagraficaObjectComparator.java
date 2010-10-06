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
package it.cilea.osd.jdyna.util;

import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;

import java.util.Comparator;
import java.util.List;

public class AnagraficaObjectComparator <P extends Property<TP>, TP extends PropertiesDefinition> implements Comparator<AnagraficaObject<P, TP>> {

	/** shortName della tipologia di proprieta' da utilizzare per la comparazione */
	private String shortName;
	
	/** moltiplicatore indicante la direzione di ordinamento: inverso=-1 normale=1*/
	private int inverse = 1;
	
	/** comparator di proprieta', utilizzato internamente per confrontare gli oggetti dotati di anagrafica */
	private ProprietaComparator<P, TP> proprietaComparator = new ProprietaComparator<P, TP>();
	
	public AnagraficaObjectComparator(String shortName, boolean inverse) {
		super();
		this.shortName = shortName;
		this.inverse = inverse?-1:1;
	}

	public int compare(AnagraficaObject<P, TP> anagraficaObject1, AnagraficaObject<P, TP> anagraficaObject2) {
		if (shortName == null)
		{
			if (anagraficaObject1 == null && anagraficaObject2 == null) return 0;
			if (anagraficaObject1 == null) return -1*inverse;
			if (anagraficaObject2 == null) return 1*inverse;
			return anagraficaObject1.getDisplayValue().compareTo(anagraficaObject2.getDisplayValue())*inverse;
		}
		List<P> prop1 = anagraficaObject1.getAnagrafica4view().get(shortName);
		List<P> prop2 = anagraficaObject2.getAnagrafica4view().get(shortName);
		
		if ((prop1 == null || prop1.size() == 0) && (prop2 == null || prop2.size() == 0)) return 0;
		if (prop1 == null || prop1.size() == 0) return -1*inverse;
		if (prop2 == null || prop2.size() == 0) return 1*inverse;
		return proprietaComparator.compare(prop1.get(0), prop2.get(0))*inverse;
	}

}
