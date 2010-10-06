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
import it.cilea.osd.jdyna.util.ValidationMessage;

import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.hibernate.annotations.CollectionOfElements;
import org.hibernate.annotations.Type;

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
@NamedQueries( {  
	@NamedQuery(name = "WidgetFormula.findAll", query = "from WidgetFormula order by id"),
	@NamedQuery(name = "WidgetFormula.findVariabiliByWidgetFormula", query = "select variabili from WidgetFormula formula join formula.variabili variabili where formula.id = ?")
})
public abstract class WidgetFormula extends AWidget {
	
	@Type(type = "text")
	private String expression;
	
	private String regolaDiRicalcolo;
	
	//serve nella valutazione delle espressioni e delle regole di ricalcolo
	//in modo tale da castare il risultato che ne esce al tipo di risultato
	//private String resultType;
	
	/** Serve per gestire la ripetibilita' dei metadati */
	@Type(type = "text")
	private String resultNumber;
	
	@CollectionOfElements	
	@JoinTable(name="dyna_widget_formula2variabili")
	protected List<String> variabili;

	@Transient
	private String configuration;
	
	public WidgetFormula() {
		variabili = new LinkedList<String>();
	}
	
	// get and set
	
	
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	
	public String getRegolaDiRicalcolo() {
		return regolaDiRicalcolo;
	}

	public void setRegolaDiRicalcolo(String regolaDiRicalcolo) {
		this.regolaDiRicalcolo = regolaDiRicalcolo;
	}

	public List<String> getVariabili() {
		return variabili;
	}

	public void setVariabili(List<String> variabili) {
		this.variabili = variabili;
	}

	
	//metodi

	@Override
	public ValidationMessage valida(Object valore) {
		return null;
	}
	
	@Override
	public String getTriview() {
		return "formula";
	}
	
	public abstract String getResultTriview();
	
	public String getConfiguration() {
		if (configuration != null) return configuration;
		configuration = "";		
		configuration = this.getId().toString(); 
		return configuration;
		
	}

//	@Override
//	public AValue getInstanceValore() {
//		try {
//			return (AValue) Class.forName(resultType).newInstance();
//		} catch (Exception e) {
//			log.error(e);
//			throw new IllegalStateException("Il tipo di risultato previsto dalla formula non e' supportato",e);
//		}
//	}
	
//	@Override
//	public Class<? extends AValue> getValoreClass() {
//		try {
//			return (Class<? extends AValue>) Class.forName(resultType);
//		} catch (ClassNotFoundException e) {
//			log.error(e);
//			throw new IllegalStateException("Il tipo di risultato previsto dalla formula non e' supportato",e);
//		}
//	}
	
	
	public abstract AValue getInstanceValore();
	
	
	public abstract Class<? extends AValue> getValoreClass();
	
//	@Override
//	public PropertyEditor getPropertyEditor(
//			ApplicationService applicationService) {		
//		return null; //FIXME le formule non necessitano di convertire input utente, cosa succede però nell'elaborazione del DTO???
//	}

//	public String getResultType() {
//		return resultType;
//	}
//
//	public void setResultType(String resultType) {
//		this.resultType = resultType;
//	}

	public String getResultNumber() {
		return resultNumber;
	}

	public void setResultNumber(String resultNumber) {
		this.resultNumber = resultNumber;
	}
	
	//public abstract Class<? extends WidgetFormula> getWidgetFormulaClass();

		   
}