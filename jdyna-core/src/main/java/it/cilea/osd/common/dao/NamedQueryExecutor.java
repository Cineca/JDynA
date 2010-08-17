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

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.List;

public interface NamedQueryExecutor<T> {

	List<T> executeFinder(Method method, Object[] queryArgs);

	List<T> executePaginator(Method method, Object[] queryArgs, String sort,
			boolean inverse, int firstResult, int maxResults);

	long executeCounter(Method method, Object[] queryArgs);
	
	BigDecimal executeIdFinder(Method method, Object[] queryArgs);
	
	T executeUnique(Method method, Object[] queryArgs);
	
	Integer  executeDelete(Method method, Object[] queryArgs);

	Boolean executeBoolean(Method method, Object[] queryArgs);
	
	Double executeDouble(Method method, Object[] queryArgs);

	T executeSingleResult(Method method, Object[] args);
}
