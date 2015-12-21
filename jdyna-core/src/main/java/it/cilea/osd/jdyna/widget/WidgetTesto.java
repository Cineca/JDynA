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
import it.cilea.osd.jdyna.value.TextValue;

import java.beans.PropertyEditor;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Type;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;

/** Classe Testo
 * @author biondo,pascarelli
 *
 */
@Entity
@Table(name="jdyna_widget_text")
public class WidgetTesto extends AWidget {
	/** segnalazione di corrispondenze*/
	
    @Column (nullable=true)
	private boolean collisioni;
	
    @Column(length=4000)
	private String regex;
	
	@Transient
	private String triview;
	
	/** se true in rendering viene mostrata come textarea */
	private boolean multilinea;
	
	/** tipo di editor html da utilizzare: nessuno, FCKEditor completa o ridotta */
	private String htmlToolbar;
	
	/** grandezza dell'area di testo, se textbox e' il numero max di lunghezza, se textarea 
	 *  e' il numero di righe.
	 */
	@Embedded 
    @AttributeOverrides( {
        @AttributeOverride(name = "row", column = @Column(name = "widgetrow")),
        @AttributeOverride(name = "col", column = @Column(name = "widgetcol")) })
	private Size dimensione;
	
	@Column(length=4000)
	private String displayFormat;
	
	@Transient
	/** la stringa di configurazione per il widget testo contiene in prima posizione, quindi nella posizione dedicata al
	 *  display (al momento la struttura della stringa di configurazione e' a 3 parametri divisi da un punto e virgola,
	 *  e.g configuration:"<display>;<filtro>;<targetClass>"...e' possibile aggiungere anche altri parametri per poi parserizzarli)
	 */
	private String configuration = "";

	public WidgetTesto() {
		this.dimensione = new Size();
	}
	
	public boolean isCollisioni() {
		return collisioni;
	}

	public void setCollisioni(boolean collisioni) {
		this.collisioni = collisioni;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public String getTriview() {
		if(collisioni==true)
			return triview = "collisioni";
		if (triview != null) return triview;		
		return triview = "testo";
	}
	
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

	public String getHtmlToolbar() {
		return htmlToolbar;
	}

	public void setHtmlToolbar(String htmlToolbar) {
		this.htmlToolbar = htmlToolbar;
	}

	public boolean getMultilinea() {
		return isMultilinea();
	}
	
	public boolean isMultilinea() {
		return multilinea;
	}

	public void setMultilinea(boolean multilinea) {
		this.multilinea = multilinea;
	}

	public List<String> getTipoToolbar() {
		List<String> tipoToolbar = new LinkedList<String>();
		tipoToolbar.add("nessuna");
		tipoToolbar.add("ridotta");
		tipoToolbar.add("completa");
		return tipoToolbar;
	}

	@Deprecated
	public String getConfiguration() {		
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	public Size getDimensione() {
		return dimensione;
	}

	public void setDimensione(Size dimensione) {
		this.dimensione = dimensione;
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		if (valore == null || regex == null) {
			return null;
		}
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher((String) valore);
		if (!matcher.find()) {
			return new ValidationMessage("testo.regexerror",new Object[]{regex}); 
		}
		return null;
	}

	public String getDisplayFormat() {
		return displayFormat;
	}

	public void setDisplayFormat(String showAsLink) {
		this.displayFormat = showAsLink;
	}
	
    
}
