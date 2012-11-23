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
import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;

import java.util.List;

public interface NestedObjectDAO<P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition, TTP extends ATypeNestedObject<NTP>> extends TypeDaoSupport<TTP, NTP>, PaginableObjectDao<ANO, Integer>
{

    List<ANO> findNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID);

    List<ANO> findNestedObjectsByParentIDAndTypoShortname(Integer dynamicFieldID,
            String typoShortname);
    
    public List<ANO> paginateNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID, String sort, boolean inverse, int firstResult,
            int maxResults);

    long countNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID);

    
    List<ANO> findActiveNestedObjectsByParentIDAndTypoShortname(Integer dynamicFieldID,
            String typoShortname);
    
    public List<ANO> paginateActiveNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID, String sort, boolean inverse, int firstResult,
            int maxResults);

    long countActiveNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID);
    
    
    List<ANO> findNestedObjectsByTypoID(Integer typeId);

    void deleteNestedObjectsByTypoID(Integer typeId);    
}
