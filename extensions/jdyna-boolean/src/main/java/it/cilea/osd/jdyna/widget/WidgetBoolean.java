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

import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.BooleanValue;

import java.beans.PropertyEditor;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.propertyeditors.CustomBooleanEditor;

/**
 * 
 * WidgetBoolean
 * 
 * @author l.pascarelli
 *
 */
@Entity
@Table(name="jdyna_widget_boolean")
@NamedQueries( {  
	@NamedQuery(name = "WidgetBoolean.findAll", query = "from WidgetBoolean order by id")
 } )
public class WidgetBoolean extends AWidget {
	
	/**
	 * NOT USED. In the future this field could be use for custom rendering
	 */
	private String showAsType;
	
	/**
	 * If true render the checkbox "checked"
	 */
	private boolean checked;
	
	/**
	 * If true the element is hide
	 */
	private boolean hideWhenFalse;
	
	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {
		CustomBooleanEditor propertyEditor = new CustomBooleanEditor(true);
		return propertyEditor;
	}
	
	@Override
	public AValue getInstanceValore() {
		return new BooleanValue();
	}

	@Override
	public String getTriview() {
		return "boolean";
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		return BooleanValue.class;
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		return null;
	}

	public String getShowAsType() {
		return showAsType;
	}

	public void setShowAsType(String showAsType) {
		this.showAsType = showAsType;
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(boolean checked) {
		this.checked = checked;
	}

	public boolean isHideWhenFalse() {
		return hideWhenFalse;
	}

	public void setHideWhenFalse(boolean hideWhenFalse) {
		this.hideWhenFalse = hideWhenFalse;
	}

}
