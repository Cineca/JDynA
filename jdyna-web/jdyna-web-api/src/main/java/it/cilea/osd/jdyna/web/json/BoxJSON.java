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
package it.cilea.osd.jdyna.web.json;

import java.util.List;

public class BoxJSON {
	private int id;
	private String shortName;
	private String title;
	private boolean collapsed;
	private List<String[]> componentSubLinks;
	private Integer countBoxPublicMetadata;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isCollapsed() {
		return collapsed;
	}
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
    
    public List<String[]> getComponentSubLinks()
    {
        return componentSubLinks;
    }
    public void setComponentSubLinks(List<String[]> componentSubLinks)
    {
        this.componentSubLinks = componentSubLinks;
    }
    
    public Integer getCountBoxPublicMetadata()
    {
        return countBoxPublicMetadata;
    }
    public void setCountBoxPublicMetadata(Integer countBoxPublicMetadata)
    {
        this.countBoxPublicMetadata = countBoxPublicMetadata;
    }
}
