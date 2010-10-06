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

import it.cilea.osd.jdyna.editor.ClassificazionePropertyEditor;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.service.IPersistenceClassificationService;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.ClassificationValue;

import java.beans.PropertyEditor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/** Classe Classificazione
 * @author pascarelli
 *
 */
@Entity
@Table(name="dyna_widget_classificazione")
public class WidgetClassificazione extends AWidget {

	/** inserito per notificare alla tag library di triview ( con l'attributo 'force') che deve renderizzarla come una classificazione*/
	@Transient
	private String triview;

	/** la configurazione del widget e' l'id dell'albero classificatorio*/
	@Transient
	private String configuration;
	
	@ManyToOne (targetEntity = AlberoClassificatorio.class)
	private AlberoClassificatorio alberoClassificatorio;
	
	public String getConfiguration() {
		if (configuration != null) return configuration;
		configuration = "";

		if(alberoClassificatorio!=null) {
			configuration = alberoClassificatorio.getNome();
			log.debug("configuration:"+configuration);
			return configuration;
		}
		return "";
	}
	
    //getter e setter

	

	//FIXME possibile che il toHtml e il toString fanno la stessa cosaa????
	@Override
	public String toHTML(Object valore) {

		if(valore!=null && valore instanceof Classificazione) {
			AlberoClassificatorio alberoClassificatorio=((Classificazione)valore).getAlberoClassificatorio();
				if(alberoClassificatorio.isCodiceSignificativo()){
					return ((Classificazione)valore).getCodice();
				}
				else {
					return ((Classificazione)valore).getNome();
				}
		}
		return super.toString(valore);
	}

	@Override
	public String toString(Object valore) {


		if(valore!=null && valore instanceof Classificazione) {
			return ((Classificazione)valore).getCodice();
		}
		return super.toString(valore);
	}


	public String getTriview() {
		if (triview != null) return triview;		
		return triview = "alberoClassificatorio";		
	}

	@Override
	public PropertyEditor getPropertyEditor(
			IPersistenceDynaService applicationService) {
		ClassificazionePropertyEditor propertyEditor = new ClassificazionePropertyEditor();
		propertyEditor.setApplicationService((IPersistenceClassificationService)applicationService);
		return propertyEditor;
	}

	
	@Override
	public AValue getInstanceValore() {
			
		return new ClassificationValue();	
		
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		return ClassificationValue.class;
	}

	public AlberoClassificatorio getAlberoClassificatorio() {
		return alberoClassificatorio;
	}


	public void setAlberoClassificatorio(AlberoClassificatorio alberoClassificatorio) {
		if(alberoClassificatorio == null) {
			throw new IllegalArgumentException("Non c'e' nessun albero classificatorio associato al widget");
		}
		this.alberoClassificatorio = alberoClassificatorio;
	}
	
	@Override
	public ValidationMessage valida(Object valore) {
		Classificazione classificazione = (Classificazione) valore;
		return classificazione == null || classificazione.isSelezionabile() ? null
				: new ValidationMessage(Classificazione.NOTSELECTABLE,null);
	}
    
}