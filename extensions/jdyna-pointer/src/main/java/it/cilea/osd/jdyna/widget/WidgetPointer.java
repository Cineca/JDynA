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
import javax.persistence.Transient;

import org.hibernate.annotations.Type;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.editor.ModelPropertyEditor;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.PointerValue;

/**
 * Classe puntatore
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
public abstract class WidgetPointer<AV extends PointerValue> extends AWidget {
	
	protected String target;
		
	/** E'il valore che si vuole mostrare sulla view.
	 *  display conterra' stringhe immesse dall'amministratore del
	 *  tipo '${cognome}+", "+${nome}.substring(0,1)'.
	 *  Il parse di questa stringa utilizza le stesse regole di 
	 *  parsering di {@link FormulaManager.getOgnlExpression()};
	 *  E' indispensabile per visualizzare qualcosa nella tendina,
	 *  quindi non puo' essere nullo.
	 * 
	 */
	@Type(type="org.hibernate.type.StringClobType")
	protected String display;

	/**
	 * filtro da utilizzare nell'autocompletamento per la ricerca. Ad es
	 * (pointer -> ContattoFisico) +type.name:"cliente"
	 */
	@Type(type="org.hibernate.type.StringClobType")
	protected String filtro;
	
	protected String indexName;
	
	/** size of input box */
	@Column(name="widgetSize")
	protected int size = 20;
	
	protected String urlPath;
		
	public int getSize() {
		return size;
	}
	
	public void setSize(int newSize) {
		size = newSize;
	}

	@Override
	public PropertyEditor getPropertyEditor(IPersistenceDynaService applicationService)
	{
		ModelPropertyEditor propertyEditor = new ModelPropertyEditor(getTargetValoreClass());
		propertyEditor.setApplicationService(applicationService);
		return propertyEditor;
	}
	
    @Override
    public abstract PropertyEditor getImportPropertyEditor(
            IPersistenceDynaService applicationService, String service);
	   
	@Override
	public PointerValue<?> getInstanceValore() {
		try {
		    PointerValue pointer = getValoreClass().newInstance();
			return pointer;
		} catch (Exception e) {
			log.error(e);
			throw new IllegalStateException("Il tipo di valore associato al widget pointer non e' consentito",e);
		}
	}

	@Override
	public Class<AV> getValoreClass() {
		try {
			return (Class<AV>) Class.forName(target);
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
	public <AVO extends Identifiable> Class<AVO> getTargetValoreClass() {
		try {
			return ((Class<PointerValue<AVO>>) Class.forName(target)).newInstance().getTargetClass();
		} catch (Exception e) {
			log.error(e);
			throw new IllegalStateException(
					"Il widget pointer id:"
							+ getId()
							+ " non e' configurato propriamente - valore target inesistente o non valido: "
							+ target, e);
		}
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
	public String toString(Object valore)
	{
	    return valore != null?valore.toString():null;
	}

	@Override
	public String getTriview() {
		return "pointer";
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

    public String getIndexName()
    {
        return indexName;
    }

    public void setIndexName(String indexName)
    {
        this.indexName = indexName;
    }

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
}
