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
package it.cilea.osd.jdyna.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AnagraficaObjectDTO implements Serializable, IAnagraficaObjectDTO {
	
	private Integer parentId;
	
	private Integer objectId;
	
	private Date timeStampCreated;
	private Date timeStampModified;
	
	/**
	 * La chiave della mappa e lo shortName della tipologia di proprieta'. 
	 * I valori rappresentano la lista ordinata dei valori associati alle
	 * proprieta' della tipologia individuata dalla chiave
	 */
	private Map<String, List<ValoreDTO>> anagraficaProperties = new HashMap<String, List<ValoreDTO>>();
	
	public Map<String, List<ValoreDTO>> getAnagraficaProperties() {
		return anagraficaProperties;
	}

	public void setAnagraficaProperties(
			Map<String, List<ValoreDTO>> anagraficaProperties) {
		this.anagraficaProperties = anagraficaProperties;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}
	
	public Integer getObjectId() {
		return objectId;
	}

	public void setObjectId(Integer objectId) {
		this.objectId = objectId;
	}

    
    public Date getTimeStampCreated()
    {
       return timeStampCreated;
    }

    
    public Date getTimeStampModified()
    {
        return timeStampModified;
    }

    public void setTimeStampCreated(Date timeStampCreated)
    {
        this.timeStampCreated = timeStampCreated;
    }

    public void setTimeStampModified(Date timeStampModified)
    {
        this.timeStampModified = timeStampModified;
    }

}
