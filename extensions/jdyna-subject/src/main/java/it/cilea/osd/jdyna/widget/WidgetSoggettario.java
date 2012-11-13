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

import it.cilea.osd.jdyna.editor.SoggettoPropertyEditor;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.model.Soggetto;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.service.IPersistenceSubjectService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.SubjectValue;

import java.beans.PropertyEditor;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/** Classe Soggettario
 * @author pascarelli
 *
 */
@Entity
@Table(name="dyna_widget_soggettario")
public class WidgetSoggettario extends AWidget {
	
	
	/** inserito per notificare alla tag library di triview ( con l'attributo 'force') che deve renderizzarla come un soggettario*/
	@Transient	
	private String triview;
	
	/** contiene la configurazione del soggettario nella forma (IdentifyingValue,DisplayValue,Chiuso))*/
	@Transient	
	private String configuration;
	
	/** size of input box */
	private int size = 20;
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int newSize) {
		size = newSize;
	}
	
	/** lista dei soggettari utilizzabili dal widget */
	@ManyToMany (targetEntity = Soggettario.class)
	@JoinTable(name="dyna_widget_soggettario2soggettari")
	private List<Soggettario> soggettari;
	
	
	/** Costruttore di default, crea la lista per accogliere gli eventuali soggettari. */
	public WidgetSoggettario() {
		soggettari = new LinkedList<Soggettario>();
	}
	
	@Deprecated
	public String getConfiguration() {
		if (configuration != null) return configuration;
		configuration = "";
		if(soggettari!=null && soggettari.size()>0) {
			for (Soggettario soggettario : soggettari) {
				configuration += "("+soggettario.getIdentifyingValue()+","+soggettario.getDisplayValue()+","+soggettario.isChiuso()+");"; 
			}
			return configuration;
		}
		return "";
	}
	
    //getter e setter

	public List<Soggettario> getSoggettari() {
		return soggettari;
	}


	public void setSoggettari(List<Soggettario> soggettari) {
		if(soggettari==null) {
			throw new IllegalArgumentException("Nessun soggettario associato al widget");
		}
		this.soggettari = soggettari;
	}

	//FIXME possibile che il toHtml e il toString fanno la stessa cosaa????
	@Override
	public String toHTML(Object valore) {
		if(valore!=null && valore instanceof Soggetto) {
			log.debug(valore + "valore nel else tostring DI wIDGETtesto");
			return ((Soggetto)valore).getVoce();
		}
	
		return super.toString(valore);
	}

	@Override
	public String toString(Object valore) {	
		if(valore!=null && valore instanceof Soggetto) {
			return ((Soggetto)valore).getVoce();
		}
		return super.toString(valore);
	}


	public String getTriview() {			
		return triview = "soggettari";	
	}

	@Override
	public PropertyEditor getPropertyEditor(
			IPersistenceDynaService applicationService) {
		SoggettoPropertyEditor propertyEditor = new SoggettoPropertyEditor();
		propertyEditor.setApplicationService((IPersistenceSubjectService)applicationService);
		return propertyEditor;
	}
	
	@Override
	public AValue getInstanceValore() {
			return new SubjectValue();
	}
   
	@Override
	public Class<? extends AValue> getValoreClass() {
		return SubjectValue.class;
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		Soggetto soggetto = (Soggetto) valore;
		if (soggetto != null) {
			if (soggetto.getSoggettario() != null && soggetto.getSoggettario().isChiuso()
					&& soggetto.getId() == null){
				return new ValidationMessage("error.message.anagrafica.soggettario.chiuso",null);
		 	}
		}
		return null;
	}
}
