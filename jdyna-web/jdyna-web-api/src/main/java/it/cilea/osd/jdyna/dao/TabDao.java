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
package it.cilea.osd.jdyna.dao;

import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.Tab;

import java.util.Collection;
import java.util.List;

public interface TabDao<H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> extends PaginableObjectDao<T,Integer> {
	public List<H> findPropertyHolderInTab(Integer tabId);
	public List<T> findTabsByHolder(H holder);
	public T uniqueTabByShortName(String title);
	public List<T> findByAccessLevel(Integer level);
    public List<T> findByAnonimous();
    public List<T> findByAdmin();
    public List<T> findByOwner();
    public List<PD> findAuthorizedGroupById(Integer id);
    public List<PD> findAuthorizedSingleById(Integer id);
    public List<PD> findAuthorizedGroupByShortName(String shortName);
    public List<PD> findAuthorizedSingleByShortName(String shortName);    
}
