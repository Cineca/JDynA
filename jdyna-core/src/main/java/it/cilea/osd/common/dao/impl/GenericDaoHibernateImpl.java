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
package it.cilea.osd.common.dao.impl;

import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.dao.NamedQueryExecutor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

public class GenericDaoHibernateImpl<T, PK extends Serializable> extends
		HibernateDaoSupport implements GenericDao<T, PK>, NamedQueryExecutor<T> {
	/** Logger for this class and subclasses */
    protected final Log log = LogFactory.getLog(getClass());
    
	private Class<T> type;

	public GenericDaoHibernateImpl(Class<T> type) {
		this.type = type;
	}

	public PK create(T newInstance) {
		return (PK) getSession().save(newInstance);
	}

	public T read(PK id) {
		return (T) getSession().get(type, id);
	}

	public void update(T transientObject) {		
		getSession().update(transientObject);
	}

	public T merge(T transientObject) {
		return (T) getSession().merge(transientObject);
	}	
	
	public void saveOrUpdate(T transientObject) {
		getSession().saveOrUpdate(transientObject);
	}	
	
	public void delete(T persistentObject) {
		getSession().delete(persistentObject);
	}

	public List<T> executeFinder(Method method, Object[] queryArgs) {
		return (List<T>) buildQuery(method, queryArgs).list();
	}

	public T executeUnique(Method method, Object[] queryArgs) {
		return (T) buildQuery(method, queryArgs).uniqueResult();
	}
	
	public Boolean executeBoolean(Method method, Object[] queryArgs) {
		return (Boolean) buildQuery(method, queryArgs).uniqueResult();
	}
	
	public Double executeDouble(Method method, Object[] queryArgs) {
		return (Double) buildQuery(method, queryArgs).uniqueResult();
	}
	
	public long executeCounter(Method method, Object[] queryArgs) {
		return (Long) buildQuery(method, queryArgs).uniqueResult();
	}
	public BigDecimal executeIdFinder(Method method, Object[] queryArgs) {
		return (BigDecimal) buildQuery(method, queryArgs).uniqueResult();
	}

	public List<T> executePaginator(Method method, Object[] queryArgs,
			String sort, boolean inverse, int firstResult, int maxResults) {
		log.debug("==>GenericDaoHibernateImpl: executePaginator("
				+ firstResult + ", " + maxResults + ")");
		Query query = buildQuery(method, new String[] { sort,
				inverse ? "desc" : "asc" }, queryArgs);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		return (List<T>) query.list();
	}
	
	public Integer executeDelete(Method method, Object[] queryArgs) {
		return buildQuery(method, queryArgs).executeUpdate();
	}

	private Query buildQuery(Method method, Object[] queryArgs) {
		return buildQuery(method, null, queryArgs);
	}
	
	/**
	 * We support only named query without named parameter OR with only named parameter named like par&lt;idx&gt;
	 * @param method
	 * @param querySuffixes
	 * @param queryArgs
	 * @return
	 */
	private Query buildQuery(Method method, String[] querySuffixes,
			Object[] queryArgs) {
		StringBuffer sb = new StringBuffer();
		sb.append(type.getSimpleName()).append('.').append(method.getName());
		if (querySuffixes != null) {
			for (int i = 0; i < querySuffixes.length; i++) {
				sb.append('.').append(querySuffixes[i]);
			}
		}
		String queryName = new String(sb);
		Query namedQuery = getSession().getNamedQuery(queryName);
		String[] namedParameters = namedQuery.getNamedParameters();
	
		
		if (namedParameters.length > 0)
		{
			for (int i = 0; i < namedParameters.length; i++)
			{
				Object arg = queryArgs[i];
				if (arg instanceof Collection)
				{
				
					namedQuery.setParameterList("par"+i, (Collection) arg);
				}
				else
				{
					
					namedQuery.setParameter("par"+i, arg);
				}
			}
		}
		else if (queryArgs != null) {
			 for (int i = 0; i < queryArgs.length; i++) {
				Object arg = queryArgs[i];
				namedQuery.setParameter(i, arg);				
			}
		} 
		return namedQuery;
	}

	public T executeSingleResult(Method method, Object[] queryArgs) {
		Query query = buildQuery(method, queryArgs);
		query.setMaxResults(1);
		return (T) query.uniqueResult();
	}

}
