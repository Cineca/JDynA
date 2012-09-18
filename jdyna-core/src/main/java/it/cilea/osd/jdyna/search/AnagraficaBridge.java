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
package it.cilea.osd.jdyna.search;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.model.AValue;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.hibernate.search.bridge.FieldBridge;
import org.hibernate.search.bridge.LuceneOptions;

/**
 * @author biondo, bollini, pascarelli
 * 
 * Questo bridge permette di trattare le proprieta' dinamiche dell'anagrafica
 * come se fossero attributi espliciti dell'oggetto. Se la tipologia di
 * proprieta' prevede l'indicizzazione avanzata del valore allora lo
 * {@link PropertiesDefinition#getShortName() shortname} della tipologia sara'
 * utilizzata come nome dell'indice. Se la tipologia di proprieta' prevede
 * l'inserimento nella ricerca semplice allora il valore sara' inserito
 * all'interno dell'indice <b>default</b>.
 * 
 * @see PropertiesDefinition#isSimpleSearch()
 * @see PropertiesDefinition#isAdvancedSearch()
 */
public class AnagraficaBridge<P extends Property<TP>, TP extends PropertiesDefinition>
		implements FieldBridge {

	private Log log = LogFactory.getLog(AnagraficaBridge.class);

	/**
	 * Costante da utilizzarsi come name nel caso di oggetti wrapper di
	 * anagrafiche dinamiche (ad es. ordine di stampa, allegato, etc.)
	 */
	public static final String WRAPPED = "wrapped";

	@Override
	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		
		set(name, value, document, luceneOptions.getStore(), luceneOptions.getIndex(), luceneOptions.getBoost());
		
	}

	
	private final void set(String name, Object obj, Document doc, Store store,
			Index idx, Float boost) {
		log.debug("Chiamato l'AnagraficaBridge");
		name = indicizzaDyna(name, obj, doc, store, idx, boost);
		
		extraStuff(name,obj,doc,store,idx,boost);
	}

	/**
	 * @param name
	 * @param obj
	 * @param doc
	 * @param store
	 * @param idx
	 * @param boost
	 * @return
	 */
	private String indicizzaDyna(String name, Object obj, Document doc,
			Store store, Index idx, Float boost) {
		// la costante wrapped indica che il campo e' wrappato nel contenitore e
		// le sue proprieta' dinamiche devono essere indicizzate come se si
		// trovassero ad un livello superiore
		
		if (WRAPPED.equals(name)){
			name = "";
		}
		name = name.replaceAll("embedded\\.", "");
		// AValue avalore = (AValue) obj;
		AnagraficaSupport<P, TP> anagraficaSupport = (AnagraficaSupport<P, TP>) obj;
		List<P> anagrafica = anagraficaSupport.getAnagrafica();
							
		Field fieldDisplayValue=new Field("displayValue",anagraficaSupport.getDisplayValue(),Store.YES,Index.UN_TOKENIZED);
		doc.add(fieldDisplayValue);
		Field fieldIdentifyingValue=new Field("identifyingValue",anagraficaSupport.getIdentifyingValue(),Store.YES,Index.UN_TOKENIZED);
		doc.add(fieldIdentifyingValue);
				
		if (anagrafica != null) {
			for (P prop : anagrafica) {
				if (prop != null) {
					
					indicizzaProprieta(prop, name, obj, doc, store, idx, boost);
					
				}
			}
		}
		return name;
	}
	
	/** 
	 * Template method da estendere per effettuare operazioni aggiuntive di indicizzazione 
	 **/
	public <O extends Object> void extraStuff(String name, O obj, Document doc, Store store,
			Index idx, Float boost) {
		
		log.debug("chiamato templateMethod di "+ getClass());
	}
	
		
	private void indicizzaProprieta(P prop, String name, Object obj, Document doc, Store store,
			Index idx, Float boost) {
		Object contenuto = prop.getObject();
		AValue avalore = prop.getValue();
		if (contenuto != null) {

			PropertiesDefinition tipProp = prop.getTypo();
			String fieldName = name + tipProp.getShortName();
						
			
			// gestisco un indice di ordinamento in cui inserisco
			// solo la prima proprieta' di ogni tipologia
			if (prop.getPosition() == 0
					&& fieldName.equals(tipProp.getShortName())
					&& avalore.getSortValue() != null) {
				Field sortField = new Field(fieldName + "_sort",
						avalore.getSortValue(), Store.NO,
						Index.UN_TOKENIZED);
				doc.add(sortField);
				if (log.isDebugEnabled()) {
					log
							.debug("Aggiunto un field di sorting al documento: "
									+ doc);
					log.debug("-- Field name: " + fieldName);
					log.debug("-- Field valore: "
							+ avalore.getSortValue());
					log.debug("-- Idx: UN_TOKENIZED store: NO");
				}
			}

			// se stiamo indicizzando l'oggetto principale l'indice
			// di default sara': default
			// se invece stiamo indicizzando un sotto-oggetto es
			// opera.collana.shortnametipologia sara' utilizzato
			// l'indice collana (ovvero il nome del sotto-oggetto)
			String fieldDefault = name.equals("") ? "default"
					: name.substring(0, name.length() - 1);

			if (contenuto instanceof AnagraficaSupport) {
				log
						.debug("Il valore che indicizzo e' un puntatore ad un oggetto con Anagrafica - fieldName: "
								+ fieldName);
				// se e' prevista la ricerca avanzata devo creare indici del tipo
				// oggettoprinc.oggettosec; oggettoprinc.oggettosec.default e
				// oggettoprinc.oggettosec.shortname per ogni tp dell'oggetto sec di 
				// cui e' prevista la ricerca avanzata
			
				if (tipProp.isAdvancedSearch()) {
					indicizzaDyna(fieldName + ".", contenuto, doc, store, idx, boost);
					if (contenuto instanceof Identifiable){
						Field field = new Field(fieldName + ".id",
							((Identifiable) contenuto).getId()
									.toString(), Store.NO,
							Index.TOKENIZED);
						doc.add(field);
					}
				}
				
				// se e' prevista la ricerca semplice allora devo inserire le proprieta'
				// semplici dell'oggetto secondario nell'indice di default dell'oggetto
				// principale
				if (tipProp.isSimpleSearch())
				{
					indicizzaDyna(fieldDefault + ".", contenuto, doc, store, idx, boost);
				}
			}// else if (contenuto instanceof List) {
				// non faccio nulla, saranno indicizzate solo le
				// sotto proprieta' contenute
			//}
			else {
				String[] fieldValues = avalore.getTokenizedValue();
				if (fieldValues != null){
					for (String fieldValue : fieldValues){
						//String fieldValue = contenuto.toString();
						if (fieldValue.length()!=0) {
							// se il name finisce per default. vuol dire che siamo stati chiamati
							// per ricorsione durante l'indicizzazione semplice di una proprieta'
							// che puntava ad un'oggetto dotato di anagrafica dinamica
							if (tipProp.isAdvancedSearch() && !name.endsWith("default.")) {
								Field field = new Field(fieldName,
										fieldValue, store, Index.TOKENIZED);
								doc.add(field);
								if (log.isDebugEnabled()) {
									log
											.debug("Aggiunto un field TOKENIZED al documento: "
													+ doc);
									log
											.debug("-- Field name: "
													+ fieldName);
									log.debug("-- Field valore: "
											+ fieldValue);
									log
											.debug("-- Idx: "
													+ idx.toString()
													+ " store: "
													+ store.toString());
								}
							}

							if (tipProp.isSimpleSearch()) {
								Field field = new Field(fieldDefault,
										fieldValue, store, Index.TOKENIZED);
								doc.add(field);
								if (log.isDebugEnabled()) {
									log
											.debug("Aggiunto un field al documento: "
													+ doc);
									log.debug("-- Field name: "
											+ fieldDefault);
									log.debug("-- Field valore: "
											+ fieldValue);
									log
											.debug("-- Idx: "
													+ idx.toString()
													+ " store: "
													+ store.toString());
								}
							}
						} else {
							log
									.debug("Valore vuoto non indicizzato! proprieta' tp: "
											+ prop.getTypo().getShortName()
											+ " proprieta' pos: "
											+ prop.getPosition());
						}
					}
				}
				fieldValues = avalore.getUntokenizedValue();
				if (fieldValues != null){
					for (String fieldValue : fieldValues){
						//String fieldValue = contenuto.toString();
						if (fieldValue.length()!=0) {
							// se il name finisce per default. vuol dire che siamo stati chiamati
							// per ricorsione durante l'indicizzazione semplice di una proprieta'
							// che puntava ad un'oggetto dotato di anagrafica dinamica
							if (tipProp.isAdvancedSearch() && !name.endsWith("default.")) {
								Field field = new Field(fieldName,
										fieldValue, store, Index.UN_TOKENIZED);
								doc.add(field);
								if (log.isDebugEnabled()) {
									log
											.debug("Aggiunto un field TOKENIZED al documento: "
													+ doc);
									log
											.debug("-- Field name: "
													+ fieldName);
									log.debug("-- Field valore: "
											+ fieldValue);
									log
											.debug("-- Idx: "
													+ idx.toString()
													+ " store: "
													+ store.toString());
								}
							}

							if (tipProp.isSimpleSearch()) {
								Field field = new Field(fieldDefault,
										fieldValue, store, Index.UN_TOKENIZED);
								doc.add(field);
								if (log.isDebugEnabled()) {
									log
											.debug("Aggiunto un field al documento: "
													+ doc);
									log.debug("-- Field name: "
											+ fieldDefault);
									log.debug("-- Field valore: "
											+ fieldValue);
									log
											.debug("-- Idx: "
													+ idx.toString()
													+ " store: "
													+ store.toString());
								}
							}
						} else {
							log
									.debug("Valore vuoto non indicizzato! proprieta' tp: "
											+ prop.getTypo().getShortName()
											+ " proprieta' pos: "
											+ prop.getPosition());
						}
					}
				}
			}
		} else {
			log.debug("Valore non indicizzato! contenuto: "
					+ contenuto + " proprieta' tp: "
					+ prop.getTypo().getShortName() + " proprieta' pos: "
					+ prop.getPosition());
		}
	}

}
