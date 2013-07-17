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
package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class TypedAbstractEditTab<H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>> extends AbstractEditTab<H, D> implements ITypedTab<H, A, PD> {

    public abstract A getTypeDef();
    public abstract void setTypeDef(A typo);
    
	public abstract Tab<H> getDisplayTab();

	public abstract void setDisplayTab(D tab);

	public abstract Class<D> getDisplayTabClass();
		
}
