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
package it.cilea.osd.jdyna.util;

import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Anagrafica4View <P extends Property<TP>, TP extends PropertiesDefinition> extends HashMap<String,List<P>> {
	private AnagraficaObject<P, TP> anagrObject;
	
	public Anagrafica4View(AnagraficaObject<P, TP> anagrObject) {
	 super();
	 this.anagrObject = anagrObject;
	}
	
	@Override
	public List<P> get(Object key) {
		List<P> result = super.get(key);
		if (result == null)
		{
			List<P> list = new LinkedList<P>();
			put(key.toString(), list);
			return list;
		}
		else return result;
	}
	
	public Class<P> getClassProprieta()
	{
		return anagrObject.getClassProperty();
	}
	
	public Class<TP> getClassTipologiaProprieta()
	{
		return anagrObject.getClassPropertiesDefinition();
	}
}
