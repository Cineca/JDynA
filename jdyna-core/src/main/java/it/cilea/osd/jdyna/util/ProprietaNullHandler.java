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

import it.cilea.osd.jdyna.model.Property;

import java.util.Map;

import javax.persistence.Transient;

import ognl.NullHandler;
import ognl.ObjectNullHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/** 
 *  Classe di utilita' che dinamicamente sostituisce durante la valutazione delle
 *  espressioni Ognl i null che possono esserci in catena, quindi permette di intercettare
 *  quando i metodi hanno valore di ritorno null e le proprieta sono valutate con un null,
 *  permettendo la sostituzione con il valore di default dell'istanza di AValue che 
 *  c'e' dietro al AWidget.
 *  
 *  @author pascarelli, bollini 
 */
public class ProprietaNullHandler implements NullHandler {

	@Transient
	private Log log = LogFactory.getLog(ProprietaNullHandler.class);
	
	NullHandler baseNullHandler = new ObjectNullHandler();
	
	/** Il metodo per ora scatena l'oggetto NullHandler di default 
	 *  per la gestione di valori null */
	public Object nullMethodResult(Map arg0, Object arg1, String arg2,
			Object[] arg3) {
		log.debug("Chiamato nullMethodValue ProprietaNullHandler");
		return baseNullHandler.nullMethodResult(arg0, arg1, arg2, arg3);
	}


	/** Viene chiamato quando nella valutazione OGNL in formula manager 
	 *  nella espressione vi e' una proprieta' a null; L'interceptor per
	 *  l'handler viene settato sull'oggetto RuntimeException
	 *  
	 *  @see FormulaManager 
	 * */
	public Object nullPropertyValue(Map arg0, Object arg1, Object arg2) {
		log.debug("Chiamato nullPropertyValue ProprietaNullHandler");
		return ((Property) arg1).getTypo().getRendering().getInstanceValore().getDefaultValue();		
	}

}
