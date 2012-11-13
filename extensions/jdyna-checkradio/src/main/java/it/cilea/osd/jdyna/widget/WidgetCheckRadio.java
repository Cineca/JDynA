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

import it.cilea.osd.jdyna.editor.ClassificazionePropertyEditor;
import it.cilea.osd.jdyna.editor.SoggettoPropertyEditor;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.model.Classificazione;
import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.service.IPersistenceClassificationService;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.service.IPersistenceSubjectService;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.value.ClassificationValue;
import it.cilea.osd.jdyna.value.SubjectValue;

import java.beans.PropertyEditor;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/** Classe Classificazione
 * @author pascarelli
 *
 */
@Entity
@Table(name="dyna_widget_checkradio")
public class WidgetCheckRadio extends AWidget {

	@ManyToOne (targetEntity = AlberoClassificatorio.class)
	private AlberoClassificatorio alberoClassificatorio;
	
	@ManyToOne
	private Soggettario soggettario;

	/** Number of option to show in a line */
	private Integer option4row;
	
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

	
	public Soggettario getSoggettario() {
		return soggettario;
	}


	public void setSoggettario(Soggettario soggettario) {
		this.soggettario = soggettario;
		this.alberoClassificatorio = null;
	}


	public String getConfiguration() {
		return "";
	}
	
    //getter e setter
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
	public PropertyEditor getPropertyEditor(
			IPersistenceDynaService applicationService) {
		if (alberoClassificatorio != null) {
			ClassificazionePropertyEditor propertyEditor = new ClassificazionePropertyEditor();
			propertyEditor.setApplicationService((IPersistenceClassificationService)applicationService);
			return propertyEditor;
		}
		else {
			SoggettoPropertyEditor propertyEditor = new SoggettoPropertyEditor();
			propertyEditor.setApplicationService((IPersistenceSubjectService)applicationService);
			return propertyEditor;
		}
	}

	@Override
	public ValidationMessage valida(Object valore) {
		if (valore == null || alberoClassificatorio == null) {
			return null;
		}
		else {
			Classificazione classificazione = (Classificazione) valore;
			return classificazione.isSelezionabile()?null:new ValidationMessage(Classificazione.NOTSELECTABLE,null);
		}
	}
	
	@Override
	public AValue getInstanceValore() {
		if (alberoClassificatorio != null) {	
			return new ClassificationValue();
		}
		else {
			return new SubjectValue();
		}
	}

	@Override
	public Class<? extends AValue> getValoreClass() {
		if (alberoClassificatorio != null) {
			return ClassificationValue.class;
		}
		else {
			return SubjectValue.class;
		}
	}

	public AlberoClassificatorio getAlberoClassificatorio() {
		return alberoClassificatorio;
	}


	public void setAlberoClassificatorio(AlberoClassificatorio alberoClassificatorio) {		
		this.alberoClassificatorio = alberoClassificatorio;
		this.soggettario = null;
	}
    
}
