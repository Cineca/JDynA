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

import org.springframework.beans.propertyeditors.StringTrimmerEditor;

import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.TextValue;

/**
 * Perform a html radio or checkbox. Retrieve element from persistence layer or use staticValues for fast use.
 * 
 * @author l.pascarelli
 *
 */
@Entity
@Table(name="jdyna_widget_checkradio")
public class WidgetCheckRadio extends AWidget {


	/** Number of option to show in a line */
	private Integer option4row;
	
	/**
	 * String separated by ||| (use ### to separate identify value from display value in single option)  
	 */
	private String staticValues; // e.g OPTION_ID###OPTION_LABEL|||OPTION_ID###OPTION_LABEL|||OPTION_ID###OPTION_LABEL
		
	/**
	 * Query to perform (depend on application persistence layer)
	 */
	private String query;
	
	public Integer getOption4row() {
		return option4row;
	}

	public void setOption4row(Integer option4row) {
		this.option4row = option4row;
	}

	@Override
	public String getTriview() {		
		return "checkradio";
	}

	public String getConfiguration() {
		return "";
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		return TextValue.class;
	}
	
	@Override
	public AValue getInstanceValore() {		
		return new TextValue();
	}

	@Override
	/**
	 * Restituisce lo StringTrimmer editor configurato per la conversione delle stringhe vuote in null
	 */
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {	
		return new StringTrimmerEditor(true);
	}

	@Override
	public ValidationMessage valida(Object valore) {
		return null;
	}

	public String getStaticValues() {
		return staticValues;
	}

	public void setStaticValues(String staticValues) {
		this.staticValues = staticValues;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

}
