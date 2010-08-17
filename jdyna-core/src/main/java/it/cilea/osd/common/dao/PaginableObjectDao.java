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
import java.util.List;

public interface PaginableObjectDao<T, PK extends Serializable> extends GenericDao<T, PK> {
	public List<T> findAll();
	public long count();
	
	/**
	 * Esegue la named Query denominata paginate.[sort].asc o desc in caso di inverse posto a <code>true</code>
	 * @param sort "id"
	 * @param inverse true per desc
	 * @param firstResult
	 * @param maxResults
	 * @return
	 */
	public List<T> paginate(String sort, boolean inverse, int firstResult,
			int maxResults);	
}
