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
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.service.IPersistenceClassificationService;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.value.ClassificationValue;

import java.beans.PropertyEditor;
import java.util.LinkedList;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * WidgetFormula e' una tipologia di rendering che mantiene al suo interno
 * un'espressione e una regola di ricalcolo per decidere se l'espressione
 * deve essere valutata in base a dei parametri.
 * @see FormulaManager, ProprietaDaRicalcolare
 * 
 * @author biondo,pascarelli
 * 
 */
@Entity
@Table(name="dyna_widget_formula_classificazione")
@NamedQueries( {  
	@NamedQuery(name = "WidgetFormulaClassificazione.findAll", query = "from WidgetFormulaClassificazione order by id"),
	@NamedQuery(name = "WidgetFormulaClassificazione.findVariabiliByWidgetFormula", query = "select variabili from WidgetFormulaClassificazione formula join formula.variabili variabili where formula.id = ?")
})
public class WidgetFormulaClassificazione extends WidgetFormula {
		
	public WidgetFormulaClassificazione() {
		variabili = new LinkedList<String>();
	}
	
	@Override
	public String getResultTriview() {
		return "alberoClassificatorio";
	}

	//metodi
	@Override
	public PropertyEditor getPropertyEditor(
			IPersistenceDynaService applicationService) {
		ClassificazionePropertyEditor propertyEditor = new ClassificazionePropertyEditor();
		propertyEditor.setApplicationService((IPersistenceClassificationService)applicationService);
		return propertyEditor;		
	}
	
	public AValue getInstanceValore() {			
		return new ClassificationValue();		
	}

	
	public Class<? extends AValue> getValoreClass() {
		return ClassificationValue.class;
	}
	

		   
}
