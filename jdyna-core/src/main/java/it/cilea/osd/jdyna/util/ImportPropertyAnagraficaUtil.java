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

import java.io.Serializable;


/**
 * Oggetto che rappresenta un bean sul file di import delle proprieta' dell'anagrafica, 
 * 
 * @author pascarelli
 *
 */
public class ImportPropertyAnagraficaUtil implements Serializable {
	/** Lo shortname della tipologia di proprieta */
	private String shortname;
	
	/** Il valore della proprieta' che si vuole importare 
	 *  NOTA:
	 *  sull'xml puo' essere una 'stringa'-'una lista di stringhe'-'una lista di ImportPropertyAnagraficaUtil'
	 * */
	private Object valore;

	public String getShortname() {
		return shortname;
	}

	public void setShortname(String shortname) {
		this.shortname = shortname;
	}

	public Object getValore() {
		return valore;
	}

	public void setValore(Object valore) {
		this.valore = valore;
	}
	
}
