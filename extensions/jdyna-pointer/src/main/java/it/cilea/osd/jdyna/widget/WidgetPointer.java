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

import it.cilea.osd.jdyna.editor.ModelPropertyEditor;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.FormulaManager;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.PointerValue;

import java.beans.PropertyEditor;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import ognl.OgnlException;

import org.hibernate.annotations.Type;

/**
 * Classe puntatore
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name="dyna_widget_pointer")
@NamedQueries( {
		@NamedQuery(name = "WidgetPointer.findAll", query = "from WidgetPointer order by id"),
		@NamedQuery(name = "WidgetPointer.findWidgetByTarget", query = "from WidgetPointer where target = ?") 
})
public class WidgetPointer extends AWidget {
	
	protected String target;
	
	/** E'il valore che si vuole mostrare sulla view.
	 *  display conterra' stringhe immesse dall'amministratore del
	 *  tipo '${cognome}+", "+${nome}.substring(0,1)'.
	 *  Il parse di questa stringa utilizza le stesse regole di 
	 *  parsering di {@link FormulaManager.getOgnlExpression()};
	 *  E' indispensabile per visualizzare qualcosa nella tendina,
	 *  quindi non può essere nullo.
	 * 
	 */
	@Type(type = "text")
	protected String display;

	/**
	 * filtro da utilizzare nell'autocompletamento per la ricerca. Ad es
	 * (pointer -> ContattoFisico) +type.name:"cliente"
	 */
	protected String filtro;
	
	/** size of input box */
	protected int size = 20;
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int newSize) {
		size = newSize;
	}

	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService)
	{
		ModelPropertyEditor propertyEditor = new ModelPropertyEditor(getInstanceValore().getTargetClass());
		propertyEditor.setApplicationService(applicationService);
		return propertyEditor;
	}
	
	
	@Override
	public PointerValue getInstanceValore() {
		try {
			return (PointerValue) Class.forName(target).newInstance();
		} catch (Exception e) {
			log.error(e);
			throw new IllegalStateException("Il tipo di valore associato al widget pointer non e' consentito",e);
		}
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		try {
			return (Class<? extends AValue>) Class.forName(target);
		} catch (ClassNotFoundException e) {
			log.error(e);
			throw new IllegalStateException("Il tipo di valore associato al widget pointer non e' consentito",e);
		}
	}
	
	/**
	 * 
	 * @return il canonicalname della classe valore collegata
	 */
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	@Transient
	/**
	 * @return l'oggetto class utilizzato come valore da questo widget
	 * 
	 */
	public Class<? extends PointerValue> getTargetValoreClass() {
		try {
			return (Class<? extends PointerValue>) Class.forName(target);
		} catch (ClassNotFoundException e) {
			log.error(e);
			throw new IllegalStateException(
					"Il widget pointer id:"
							+ getId()
							+ " non e' configurato propriamente - valore target inesistente: "
							+ target);
		}
	}

	/**
	 * Calcola il valore da stampare valutando l'espressione contenuta
	 * nell'attributo display sull'oggetto <em>value</em> passato come
	 * parametro. Il calcolo del valore e' fatto da FormulaManager usato come
	 * classe di utilita' per i widget. Torna la stringa vuota se l'oggetto e'
	 * null. FIXME attualmente non cattura l'eccezione ognl
	 * 
	 * @see FormulaManager
	 * @param value :
	 *            l'oggetto da valutare per stampare la sua descrizione testuale
	 * @exception OgnlException
	 * @return la stringa da visualizzare
	 */
	@Override
	public String toString(Object value) {
		if (value == null) {
			log.debug("widgetPointer - il value e' null");
			return "";
		}
		Object result = FormulaManager.calcoloValore(display, value, null, 0);
		if (result == null) {
			return "";
		}
		return (String) result;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}

	@Override
	public String toHTML(Object valore)
	{
		//FIXME try to return a link to detail page of object value
		if (valore == null) {
			return "";
		}
		return toString(valore);
	}

	@Override
	public String getTriview() {
		return "puntatore";
	}

	@Deprecated
	@SuppressWarnings("unchecked")
	public String getConfiguration() {
		PointerValue vp;
		try {
			vp = ((Class<PointerValue>) Class.forName(target)).newInstance();
			return display + ";" + filtro + ";" + vp.getTargetClass().getName();
		} catch (Exception e) {
			new RuntimeException(e);
		}
		return null;
	}

	public String getFiltro() {
		return filtro;
	}

	public void setFiltro(String filtro) {
		this.filtro = filtro;
	}

	@Override
	public ValidationMessage valida(Object valore) {
		return null;
	}
}