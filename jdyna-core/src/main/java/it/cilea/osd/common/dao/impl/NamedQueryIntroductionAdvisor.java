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

import it.cilea.osd.common.dao.NamedQueryExecutor;

import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.IntroductionInterceptor;
import org.springframework.aop.support.DefaultIntroductionAdvisor;

public class NamedQueryIntroductionAdvisor extends DefaultIntroductionAdvisor {

	public NamedQueryIntroductionAdvisor() {
		super(new IntroductionInterceptor() {
			public Object invoke(MethodInvocation mi) throws Throwable {
				NamedQueryExecutor genericDao = (NamedQueryExecutor) mi
						.getThis();
				String methodName = mi.getMethod().getName();
				if (methodName.startsWith("find")) {
					Object[] args = mi.getArguments();
					return genericDao.executeFinder(mi.getMethod(), args);
				} else if (methodName.startsWith("unique")){
					Object[] args = mi.getArguments();
					return genericDao.executeUnique(mi.getMethod(), args);					
				}else if (methodName.startsWith("count")) {
					Object[] args = mi.getArguments();
					return genericDao.executeCounter(mi.getMethod(), args);
				} else if (methodName.startsWith("delete") && !methodName.equals("delete")) {
					Object[] args = mi.getArguments();
					return genericDao.executeDelete(mi.getMethod(), args);
				}else if (methodName.startsWith("idFind")) {
					Object[] args = mi.getArguments();
					return genericDao.executeIdFinder(mi.getMethod(), args);
				} else if (methodName.startsWith("paginate")) {
					Object[] args = mi.getArguments();
					String sort = (String) args[args.length - 4];
					boolean inverse = (Boolean) args[args.length - 3];
					int firstResult = (Integer) args[args.length - 2];
					int maxResults = (Integer) args[args.length - 1];
					args = Arrays.asList(args).subList(0, args.length - 4)
							.toArray();
					return genericDao.executePaginator(mi.getMethod(), args,
							sort, inverse, firstResult, maxResults);
				} else if (methodName.startsWith("is") || methodName.startsWith("has")
							|| methodName.startsWith("check")){
					Object[] args = mi.getArguments();
					return genericDao.executeBoolean(mi.getMethod(), args);					
				} else if (methodName.startsWith("sum")){
					Object[] args = mi.getArguments();
					return genericDao.executeDouble(mi.getMethod(), args);					
				} else if  (methodName.startsWith("singleResult")){
					Object[] args = mi.getArguments();
					return genericDao.executeSingleResult(mi.getMethod(), args);
				} else {				
					return mi.proceed();
				}
			}

			public boolean implementsInterface(Class intf) {
				return intf.isInterface()
						&& NamedQueryExecutor.class.isAssignableFrom(intf);
			}
		});
	}

}
