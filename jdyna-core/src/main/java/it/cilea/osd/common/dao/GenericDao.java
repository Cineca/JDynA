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
package it.cilea.osd.common.dao;

import java.io.Serializable;

public interface GenericDao<T, PK extends Serializable> {

	// Persist the newInstance object into database
	PK create(T newInstance);

	// Retrieve an object that was previously persisted to the database using
	// the indicated id as primary key
	T read(PK id);

	// Save changes made to a persistent object
	void update(T transientObject);

	T merge(T transientObject);

	void saveOrUpdate(T transientObject);	
	
	// Remove an object from persistent storage in the database
	void delete(T persistentObject);

}
