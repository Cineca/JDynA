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

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JPAEvent implements IEvent {

	public static final String UPDATE = "UPDATE";
	public static final String CREATE = "CREATE";
	public static final String DELETE = "DELETE";

	@Column(name="jpaevent_clazz")
	private String clazz;
	
	@Column(name="jpaevent_optype")
	private String operationType;
	
	@Column(name="jpaevent_id")
	private Integer id;

	public JPAEvent() {
	}
	
	public String getClazz() {
		return clazz;
	}

	public String getOperationType() {
		return operationType;
	}

	public Integer getId() {
		return id;
	}

	public JPAEvent(String clazz, Integer id, String operationType) {
		if (clazz != null && clazz.indexOf("$$") != -1)
		{
			clazz = clazz.substring(0,clazz.indexOf("$$"));
		}
		this.clazz = clazz;
		this.id = id;
		this.operationType = operationType;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getDetail() {
		return operationType + " " + clazz + " " + id;
	}

}
