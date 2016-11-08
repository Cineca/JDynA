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

import java.util.List;

import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.TypedAbstractEditTab;
import it.cilea.osd.jdyna.web.TypedAbstractTab;

public interface EditTypedTabDao<H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>, DA extends TypedAbstractEditTab<H, A, PD, D>> extends EditTabDao<H, D, DA, PD> {

    List<DA> findTabByType(A typo);
    
    List<DA> findByAnonimousAndTypoDef(A typo);
    List<DA> findByAdminAndTypoDef(A typo);
    List<DA> findByOwnerAndTypoDef(A typo);
}
