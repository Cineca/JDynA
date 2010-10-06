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
package it.cilea.osd.jdyna.value;

import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.Anagrafica4View;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Entity
@NamedQueries( {})
public abstract class MultiValue<P extends Property<TP>, TP extends PropertiesDefinition>
		extends AValue<List<P>> {
	@Transient
	private Log log = LogFactory.getLog(getClass());

	@Transient
	private Anagrafica4View<P, TP> cacheAnagr4View = null;
	
	
	@Override
	public void setReal(List<P> real) {
		for (P p : getObject()) {
			p.setParent(null);
			p.setParentProperty(null);
		}
		getObject().clear();

		if (real != null) {
			Collections.sort(real);
			getObject().addAll(real);
		}

		invalidateAnagraficaCache();
	} 
	
	/**
	 * Utilizzato per la fase di visualizzazione/editing di un combo. La mappa
	 * ha come chiave lo shortname della tipologia di proprieta' e come valore la
	 * lista di proprieta' corrispondenti.
	 * 
	 * @return Mappa key=shortname della tipologia di proprieta' : value=lista di
	 *         elementi dell'anagrafica (se alla stessa key si riferisce una
	 *         lista con piu' elementi vuol dire che la tipologia della proprieta
	 *         e' ripetibile)
	 */
	@Transient
	public Anagrafica4View<P, TP> getAnagrafica4view() {
		// Anagrafica4View<P, TP> cacheAnagr4View = null;
		// verifico se ho gia' disponibile una cache
		if (cacheAnagr4View != null) {
			return cacheAnagr4View;
		}

		// genero una nuova cache di anagrafica organizzata come mappa di tp
		// (utile per la view)
		log.debug("Genero una nuova cache di a4v per il valore multi " + this);

		// FIXME verificare se serve avere un riferimento all'oggetto dotato di
		// anagrafica per le exp OGNL (NullHandler)
		cacheAnagr4View = new Anagrafica4View<P, TP>(null);

		for (P property : getObject()) {
//			if (property.getTipologia().getRendering() instanceof WidgetPointer) {
//				log.debug("inizializzo l'anagrafica dell'oggetto puntato");
//				AnagraficaSupport<?, ?, ?> anagraficaObject = (AnagraficaSupport<?, ?, ?>) property
//						.getValore().getOggetto();
//				if (anagraficaObject != null) {
//					Hibernate.initialize(anagraficaObject.getAnagrafica());
//				}
//			}
			// la lista real contiene solo le proprieta' di primo livello della
			// combo, quindi vanno tutte inserite in a4v
			List<P> appoggio = cacheAnagr4View.get(property.getTypo()
					.getShortName());
			if (appoggio == null) {
				appoggio = new LinkedList<P>();
				cacheAnagr4View.put(property.getTypo().getShortName(),
						appoggio);
			}

			appoggio.add(property);
		}
		if (log.isDebugEnabled()) {
			log.debug("Contenuto di a4v del valore multi" + this);
			for (String key : cacheAnagr4View.keySet()) {
				log.debug("--" + key);
				for (P prop : cacheAnagr4View.get(key)) {
					log.debug(prop.getValue());
				}
			}
		}
		return cacheAnagr4View;
	}

	@Override
	public List<P> getDefaultValue() {
		return new LinkedList<P>();
	}

	public void invalidateAnagraficaCache() {
		if (cacheAnagr4View != null) {
			cacheAnagr4View.clear();
			cacheAnagr4View = null;
		}
	}

	@Override
	public String[] getUntokenizedValue() {
		return null;
	}

	@Override
	public String[] getTokenizedValue() {
		if (getObject() == null) {
			return null;
		} else {
			List<String> tokenizedValues = new LinkedList<String>();
			for (P prop : getObject()) {
				if (prop.getTypo().isSimpleSearch()
						&& prop.getValue() != null) {
					String[] propTokenizedValue = prop.getValue()
							.getTokenizedValue();
					if (propTokenizedValue != null) {
						for (String token : propTokenizedValue) {
							tokenizedValues.add(token);
						}
					}
				}
			}
			String[] tokenizedArray = new String[tokenizedValues.size()];
			tokenizedValues.toArray(tokenizedArray);
			return tokenizedArray;
		}
	}
	
	@Override
	public final boolean isModified() {
		if (!super.isModified()){
			return false;
		}
		else {
			for (P prop : getObject()){
				if (prop.getValue().isModified()) return true;
			}
		}
		return false;
	}

}