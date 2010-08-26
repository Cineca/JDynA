/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.jdyna.widget;

import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.TextValue;

import java.beans.PropertyEditor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.beans.propertyeditors.StringTrimmerEditor;





/**
 * Oggetto WidgetEmail che rappresenta l'email di un soggetto
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name="jdyna_widget_email")
@NamedQueries( {  
	@NamedQuery(name = "WidgetEmail.findAll", query = "from WidgetEmail order by id")
 } )
public class WidgetEmail extends AWidget {

	/** Regular Expression per la validazione sintattica dell'email */
	@Transient
	private final static String regex = "^[aA-zZ0-9._%-]+@[aA-zZ0-9.-]+\\.[aA-zZ]{2,4}$";

	@Transient
	/** oggetto pattern */
	private final static Pattern pattern = Pattern.compile(regex);


	public boolean isNull() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String getTriview() {
		return "email";
	}

	/** Valida l'indirizzo email passato come parametro */
	@Deprecated
	public boolean valida(String email) {
		Matcher matcher = pattern.matcher(email);
		if (matcher.find()) {
			log.debug(email + " è un indirizzo email ben scritto");
			return true;
		}
			
		else {
			log.debug(email + " errata scrittura dell'indirizzo email");
			return false;
		}

	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		if (valore == null) {
			return null;
		}
		Matcher matcher = pattern.matcher((String) valore);
		if (!matcher.find()) {
			return new ValidationMessage("email.syntaxerror",null); 
		}
		return null;
	}

	// getter e setter
	
	@Override
	/**
	 * Restituisce lo StringTrimmer editor configurato per la conversione delle stringhe vuote in null
	 */
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService) {	
		return new StringTrimmerEditor(true);
	}
	
	@Override
	public AValue getInstanceValore() {	
		return new TextValue();
	}
	
	@Override
	public Class<? extends AValue> getValoreClass() {
		return TextValue.class;
	}

}