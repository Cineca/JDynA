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
package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.service.ISearchDynaService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class RigeneraIndiciSearch {
	public static void main(String[] args) {
		ApplicationContext context = null;
		try {
			String conf0 = args[0];
			String conf1 = args[1];
			String conf2 = args[2];
			String conf3 = args[3];
			String conf4 = args[4];			
			
			context = new FileSystemXmlApplicationContext(
					new String[] {conf0,conf1,conf2,conf3,conf4});
			ISearchDynaService searchService = (ISearchDynaService) context.getBean(
					"searchService");
			searchService.purgeAndCreateIndex();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (context != null){
				context.publishEvent(new ContextClosedEvent(context));
			}
		}
	}
}
