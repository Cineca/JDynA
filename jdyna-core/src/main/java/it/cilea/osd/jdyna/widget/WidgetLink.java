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

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import it.cilea.osd.jdyna.editor.AdvancedPropertyEditorSupport;
import it.cilea.osd.jdyna.editor.LinkPropertyEditor;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.LinkValue;

/** 
 * 
 * Link widget
 * 
 * 
 * @author pascarelli
 *
 */
@Entity
@Table(name="jdyna_widget_link")
public class WidgetLink extends AWidget {

	@Transient
	private String triview;
	
	/**
	 * Percent widgetSize (this value is default for input widgetSize either link description and value)
	 */
	private int widgetSize = 40;		

	/**
	 * Value to show label for value label as column header
	 */
	private String labelHeaderLabel;
	
	/**
	 * Value to show label for URL as column header
	 */
	private String labelHeaderURL;
	
	public String getTriview() {		
		return "link";
	}
	
	@Override
	/**
	 * Return LinkPropertyEditor to manage view
	 */
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {	
		return new LinkPropertyEditor(AdvancedPropertyEditorSupport.MODE_VIEW);
	}
	
    @Override
    public PropertyEditor getImportPropertyEditor(
            IPersistenceDynaService applicationService, String service) {
        return new LinkPropertyEditor(service);
    }

	@Override
	public AValue getInstanceValore() {		
		return new LinkValue();
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		return LinkValue.class;
	}

	@Override
	public ValidationMessage valida(Object valore) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getSize() {
        return getWidgetSize();
    }

    public int getWidgetSize() {
		return widgetSize;
	}

	public void setSize(int size) {
        setWidgetSize(size);
    }

    public void setWidgetSize(int size) {
		this.widgetSize = size;
	}

	public void setLabelHeaderLabel(String labelHeaderLabel) {
		this.labelHeaderLabel = labelHeaderLabel;
	}

	public String getLabelHeaderLabel() {
		return labelHeaderLabel;
	}

	public void setLabelHeaderURL(String labelHeaderURL) {
		this.labelHeaderURL = labelHeaderURL;
	}

	public String getLabelHeaderURL() {
		return labelHeaderURL;
	}


    
}
