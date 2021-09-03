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
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import java.util.List;

public interface PropertiesDefinitionDao<T extends PropertiesDefinition> extends PaginableObjectDao<T, Integer> {
	public T uniqueByShortName(String shortName);
	public Integer uniqueIdByShortName(String shortName);
	public List<T> findValoriOnCreation();
	public List<T> findValoriDaMostrare();	
	public List<T> findSimpleSearch();
	public List<T> findAdvancedSearch();
	public List<T> findAllWithWidgetFormula();
	public List<T> findAllWithPolicySingle();
	public List<T> findAllWithPolicyGroup();
	public List<T> likeAllWithPolicySingle(String specificPart);
	public List<T> likeAllWithPolicyGroup(String specificPart);	
	public List<T> findAdvancedSearchAndClassificazione();	
	public List<T> findAdvancedSearchAndBoolean();
	public T findPropertiesDefinitionByWidget(AWidget widget);
	public List<T> likeByShortName(String shortName);
	public List<T> findAllWithCheckRadioDropdown();
}
