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
package it.cilea.osd.jdyna.event;


/**
 *	Interfaccia per i sottoscrittori, ogni oggetto che vuole essere un subscriber deve
 *  implementare questa interfaccia.
 *  
 *   @author pascarelli
 */
public  interface ISubscriber<E extends IEvent> {
	/** 
	 * 	Metodo invocato dal gestore di eventi su tutti i subscriber
	 *  
	 *  @param arg0 la classe dell'evento che e' stato scatenato
	 *  @param arg1 l'instanza contenente i dettagli dell'evento occorso
	 *  
	 *  @author pascarelli
	 *  */
	public void receive(Class<E> classeEvento, E evento);
}
