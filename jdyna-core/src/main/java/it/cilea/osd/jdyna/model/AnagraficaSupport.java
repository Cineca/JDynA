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
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.Selectable;

import java.util.List;
import java.util.Map;
/**
 * Questa interfaccia deve essere implementata da tutti gli oggetti che vogliono gestire una anagrafica
 * dinamica di proprieta'.
 * 
 * @author bollini
 *
 * @param <P> la classe delle proprieta' presenti nell'anagrafica dell'oggetto
 * @param <TP> la classe delle tipologie di proprieta' presenti nell'anagrafica dell'oggetto
 * 
 */
public interface AnagraficaSupport <P extends Property<TP>, TP extends PropertiesDefinition> extends Selectable {
	/**
	 * Restituisce l'anagrafica completa dell'oggetto correttamente ordinata.
	 * 
	 * @return l'anagrafica completa dell'oggetto 
	 */		
	public List<P> getAnagrafica();
  
	/**
     * Utilizzato per la fase di visualizzazione dell'anagrafica nel resoconto
     * degli oggetti, la lista come valore della mappa che viene tornata dal metodo
     * indica che ad una tipologia di proprieta possono corrispondere piu' proprieta.
     * 
     * @return Mappa key=shortname della tipologia di proprieta' : value=lista di elementi
     *         dell'anagrafica (se alla stessa key si riferisce una lista con piu' elementi vuol dire 
     *         che la tipologia della proprieta e' ripetibile) 
     */
    public Map<String, List<P>> getAnagrafica4view();
    
    /**
	 * Il metodo setta la nuova anagrafica cancellando la vecchia
	 * in modo tale da settare anche il parent delle singole proprieta
	 * a null (altrimenti si verrebbe a creare una doppia anagrafica).
	 * E' responsabilita' del setter garantire il corretto ordinamento dell'anagrafica
	 * 
	 * @param anagrafica la nuova anagrafica da settare
	 */
	public void setAnagrafica(List<P> anagrafica);
	
	/**
     * Crea una nuova proprieta, della tipologia specificata, e la inserisce nell'anagrafica dell'oggetto.
     * La nuova proprieta' sara' posizionata ({@link Property#setPositionDef(int)}) in coda alle altre eventuali 
     * proprieta' della medesima tipologia. 
     * 
     * @throws IllegalArgumentException se la tipologia di proprieta' specificata non e' di primo livello ({@link PropertiesDefinition#isTopLevel()} 
     * @return la nuova proprieta creata 
     */
	public P createProprieta(TP tipologiaProprieta) throws IllegalArgumentException;
	
	/**
     * Crea una nuova proprieta, della tipologia specificata, e la inserisce nell'anagrafica dell'oggetto.
     * La nuova proprieta' sara' posizionata ({@link Property#setPositionDef(int)}) nella posizione specificata
     * da <code>posizione</code> modificando coerentemente la posizione delle altre proprieta'. Nel caso in cui
     * la posizione specificata fosse superiore al numero di proprieta' della tipologia specificata 
     * ({@link #getProprietaDellaTipologia(PropertiesDefinition)}), la nuova proprieta' sara' inserita in ultima 
     * posizione. 
     * 
     * @throws IllegalArgumentException se la tipologia di proprieta' specificata non e' di primo livello ({@link PropertiesDefinition#isTopLevel()}
     * @return la nuova proprieta creata 
     */
	public P createProprieta(TP tipologiaProprieta, Integer posizione) throws IllegalArgumentException;
	
	/**
	 * Rimuove, se presente, la proprieta indicata dall'anagrafica dell'oggetto. 
	 * Se la proprieta' e' una combo rimuove anche le sottoproprieta' collegate
	 *
	 * @see Property#equals(Object)
	 * @param la proprieta da rimuovere 
	 * @return <code>true</code> se la proprieta e stata trovata e rimossa dall'anagrafica
	 */
	public boolean removeProprieta(P proprieta);
	
	/**
     * Data una tipologia di proprieta', restituisce la lista di proprieta' di quella tipologia
     * presenti nell'anagrafica del oggetto. La determinazione della corrispondenza tra la tipologia
     * delle proprieta' in anagrafica e la tipologia di proprieta' passata come argomento e' effettuata
     * utilizzando il metodo <code>equals()</code>.
     * 
     * @see PropertiesDefinition#equals(Object)
     * @param tipologiaProprieta La tipologia di proprieta' da considerare
     * @return la lista di proprieta dell'anagrafica della tipologia tipologiaProprieta
     */
	public List<P> getProprietaDellaTipologia(TP tipologiaProprieta);	
		

	/** 
	 * Restituisce la classe di proprieta' utilizzata per l'anagrafica dell'oggetto.
	 * Deve estendere <code>Property&gt;TP&lt;</code>, dove TP estende <code>PropertiesDefinition</code>
	 * 
	 * @see Property
	 * @return Restituisce la classe di proprieta' utilizzata per l'anagrafica dell'oggetto
	 */
	public Class<P> getClassProperty();
	
	/**
	 * Restituisce la classe di tipologia di proprieta utilizzata per l'anagrafica dell'oggetto  

	 * @see PropertiesDefinition
	 * @return la classe di tipologia di proprieta utilizzata per l'anagrafica dell'oggetto  
	 */
	public Class<TP> getClassPropertiesDefinition();
	

	/**
	 * Inizializza l'anagrafica nel caso in cui non sia stata ancora recuperata dal DB (lazy)
	 */
	public void inizializza();
	
	/**
	 * Invalida la cache dell'anagrafica generata forzando una rigenerazione alla prima richiesta
	 */
	public void invalidateAnagraficaCache();
	
	/**
	 * Pulisce l'anagrafica dalle proprieta' non valorizzate
	 */
	public void pulisciAnagrafica();
	
}
