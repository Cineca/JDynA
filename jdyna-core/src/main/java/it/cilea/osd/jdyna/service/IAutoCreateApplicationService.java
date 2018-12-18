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
package it.cilea.osd.jdyna.service;

/***
 * Service used to auto create a IAutoCreableObject.
 *
 * Implementation object is responsible of caching object data (just it's name
 * with a generated id). It is expected than the new object will be persisted if
 * accessed using the standard get method
 */
public interface IAutoCreateApplicationService {

	/***
	 * Generate a temporary pointer candidate for a new Object
	 * 
	 * @param type the type of Object to cache, it is not necessary the java class
	 *             name but instead an unique application specific alias of the
	 *             business entity
	 * @param name The cached value
	 * @param tag  Used to highlight new objects
	 * @return true if auto created is enabled
	 */
	public Integer generateTemporaryPointerCandidate(String type, String name, String tag);
	
	/**
	 * Convert a temporary point candidate in a persistent object. If the id of the
	 * temporary candidate is unknown it must return null without failing
	 * 
	 * @param id the id of the temporary candidate
	 * @return the id of the new persisted object
	 */
	public Integer persistTemporaryPointerCandidate(Integer id);
}
