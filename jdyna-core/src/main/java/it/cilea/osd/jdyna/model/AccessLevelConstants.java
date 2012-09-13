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
package it.cilea.osd.jdyna.model;

import java.util.LinkedList;
import java.util.List;
/**
*
* @author pascarelli
*
*/
public class AccessLevelConstants {
	/**
	 * All grants on metadata value and visibility
	 */
	public final static Integer HIGH_ACCESS = 3;
	/**
	 * Only visibility edit
	 */
	public final static Integer STANDARD_ACCESS = 2;
	
	/**
	 * Hiding to RP
	 */
	public final static Integer ADMIN_ACCESS = 1;
	
	/**
	 * Nothing operation on metadata
	 */
	public final static Integer LOW_ACCESS = 0;
	
	public static List<Integer> getValues() {
		List<Integer> values = new LinkedList<Integer>();
		values.add(HIGH_ACCESS);
		values.add(STANDARD_ACCESS);
		values.add(ADMIN_ACCESS);
		values.add(LOW_ACCESS);
		return values;
	}
}
