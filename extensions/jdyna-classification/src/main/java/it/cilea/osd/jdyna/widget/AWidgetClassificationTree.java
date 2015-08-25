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
package it.cilea.osd.jdyna.widget;

import java.beans.PropertyEditor;

import javax.persistence.Column;
import javax.persistence.Entity;

import org.hibernate.annotations.Type;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.editor.ModelPropertyEditor;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.ClassificationValue;


/**
 * 
 * Classification Tree configuration widget
 * 
 * @author l.pascarelli
 *
 */
@Entity
public abstract class AWidgetClassificationTree<AV extends ClassificationValue> extends AWidget {
	
	/**
	 * The object type that owned the tree  
	 */
	@Column(nullable = false)
	private String treeObjectType;
	
	/**
	 * This configuration permits only to choose leaf from the tree (no choice for intermediate node)
	 */
	private boolean chooseOnlyLeaves;
	
	@Type(type="org.hibernate.type.StringClobType")
	protected String display;
	
    //getter e setter

	public String getTriview() {
		return "classificationTree";		
	}

	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService)
	{
		ModelPropertyEditor propertyEditor = new ModelPropertyEditor(getTargetValoreClass());
		propertyEditor.setApplicationService(applicationService);
		return propertyEditor;
	}
	
	public String getTreeObjectType() {
		return treeObjectType;
	}


	public void setTreeObjectType(String treeObjectType) {
		this.treeObjectType = treeObjectType;
	}


	public boolean isChooseOnlyLeaves() {
		return chooseOnlyLeaves;
	}


	public void setChooseOnlyLeaves(boolean chooseOnlyLeaves) {
		this.chooseOnlyLeaves = chooseOnlyLeaves;
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		//TODO validate if is a leaf when chooseonlyleaf is enabled
		return null;
	}
	
	public abstract <AVO extends Identifiable> Class<AVO> getTargetValoreClass();

	public String getDisplay() {
		return display;
	}


	public void setDisplay(String display) {
		this.display = display;
	}
}
