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
package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.jdyna.util.Anagrafica4View;
import it.cilea.osd.jdyna.value.MultiValue;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe astratta che fornisce un'implementazione base per alcuni metodi di gestione dell'anagrafica
 * 
 * @author bollini
 */
public abstract class AnagraficaObject<P extends Property<TP>, TP extends PropertiesDefinition> extends IdentifiableObject implements AnagraficaSupport<P, TP> {

	@Transient
	private Log log = LogFactory.getLog(getClass());
	
	@Transient
	private Anagrafica4View<P, TP> cacheAnagr4View = null;
	
	/**
	 * {@inheritDoc}
	 */
	public P createProprieta(TP tipologiaProprieta) 
	{
		
		if (!tipologiaProprieta.isTopLevel()) {			
			throw new IllegalArgumentException("Non è possibile creare una Property di tipo: "+tipologiaProprieta.getShortName()+"al di fuori del relativo combo");
		}
		List<P> propList = getProprietaDellaTipologia(tipologiaProprieta);
		int numProp = propList.size();
		P proprieta = createProprietaWithoutPosizione(null, tipologiaProprieta);
		proprieta.setPosition(numProp); // la dimensione di una lista è esattamente la prima posizione disponibile
										 // lista vuota - 0; un elemento 1; etc.
		Collections.sort(getAnagrafica());
		// aggiorno il contenuto della cache
		propList.add(proprieta);
        return proprieta;
	}

	/**
	 * {@inheritDoc}
	 */
	public P createProprieta(TP tipologiaProprieta, Integer posizione)
	{
		if (!tipologiaProprieta.isTopLevel()) 
			throw new IllegalArgumentException("Non è possibile creare una Property di tipo: "+tipologiaProprieta.getShortName()+"al di fuori del relativo combo");
		List<P> propList = getProprietaDellaTipologia(tipologiaProprieta);
		int numProp = propList.size();
		for (int idx = posizione; idx < numProp; idx++){
			propList.get(idx).setPosition(idx+1);
		}
		P proprieta = createProprietaWithoutPosizione(null, tipologiaProprieta);
		if (numProp < posizione) proprieta.setPosition(numProp);
		else proprieta.setPosition(posizione);
		Collections.sort(getAnagrafica());
		// aggiorno il contenuto della cache		
		propList.add(posizione, proprieta);
		return proprieta;
	}

	/**
	 * {@inheritDoc}
	 */
	public P createProprieta(P proprieta , TP tipologiaProprieta) {
		if (log.isDebugEnabled())
		{
			log.debug("Creazione della sottoproprietà "+tipologiaProprieta+" in "+proprieta);
			log.debug("P-TP: "+proprieta.getTypo().getShortName()+" P-V: "+proprieta.getValue().getObject());
		}
		//FIXME aggiungere il check delle precondizioni (TP sottotipologia ammessa in P)
		//è nella combo che devo calcolare la posizione delle proprieta di una certa tipologia
		List<P> propList = getProprietaDellaTipologiaInValoreMulti((MultiValue<P,TP>)proprieta.getValue(),tipologiaProprieta);
		int numProp = propList.size();
		P p = createProprietaWithoutPosizione(proprieta, tipologiaProprieta);	
		p.setPosition(numProp);
		Collections.sort(getAnagrafica());
		// aggiorno il contenuto della cache a4v multi		
		propList.add(p);
		return p;	
	}

	/**
	 * {@inheritDoc}
	 */
	public P createProprieta(P proprieta , TP tipologiaProprieta, Integer posizione) {
		//FIXME aggiungere il check delle precondizioni (TP sottotipologia ammessa in P)
		if (log.isDebugEnabled())
		{
			log.debug("Creazione della sottoproprietà "+tipologiaProprieta+" in "+proprieta);
			log.debug("P-TP: "+proprieta.getTypo().getShortName()+" P-V: "+proprieta.getValue().getObject());
			log.debug("TP: "+tipologiaProprieta.getShortName()+" Pos: "+posizione);			
		}
		List<P> propList = getProprietaDellaTipologiaInValoreMulti((MultiValue<P,TP>)proprieta.getValue(),tipologiaProprieta);
		int numProp = propList.size();
		for (int idx = posizione; idx < numProp; idx++){
			propList.get(idx).setPosition(idx+1);
		}
		P p = createProprietaWithoutPosizione(proprieta, tipologiaProprieta);
		if (numProp < posizione) p.setPosition(numProp);
		else p.setPosition(posizione);
		Collections.sort(getAnagrafica());
		// aggiorno il contenuto della cache a4v multi		
		propList.add(posizione, p);
		return p;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void setAnagrafica(List<P> anagrafica)
	{
		for (P p : getAnagrafica()) {
			if (p.getTypo().isTopLevel()) {
				p.setParent(null);
				p.getValue().setOggetto(null);
			}
		}
		getAnagrafica().clear();
		if(anagrafica!=null){
			Collections.sort(anagrafica);
			getAnagrafica().addAll(anagrafica);
		}
		
		for (P p : getAnagrafica()) {
			p.setParent(this);
		}
		invalidateAnagraficaCache();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@SuppressWarnings("unchecked")
	public boolean removeProprieta(P proprieta)
	{
		if (log.isDebugEnabled())
		{
			log.debug("Rimozione della proprietà: " + proprieta + " dall'oggetto: "+proprieta.getParent()==null?proprieta.getPropertyParent().getParent():proprieta.getParent()+" con rendering: "+ proprieta.getTypo().getRendering());
			log.debug("TP: "+proprieta.getTypo().getShortName()+" V: "+proprieta.getValue().getObject());
		}
		P proprietaParent = (P) proprieta.getPropertyParent();

		removeSottoProprieta(proprieta);					
		
		//check se la proprieta viene rimossa dall'anagrafica dell'oggetto o dalla lista di proprieta di una combo  
		boolean rimossa = false;
		// la lista contiene tutte gli elementi di quella tipologia, è ordinata
		// in base alle posizioni
		List<P> propList;
		if (proprietaParent == null) {
			//cancello dall'anagrafica
			proprieta.setParent(null); // 0 1 2
			rimossa = getAnagrafica().remove(proprieta);
					
			if(log.isDebugEnabled()) {
				if(rimossa == true) {
					log.debug("rimozione dall'anagrafica avvenuta con successo");
				}
				else {
					log.debug("errore non è stato possibile rimuovere la proprieta dall'anagrafica");
				}
			}
			
			//aggiorno la cache
			propList = getProprietaDellaTipologia(proprieta.getTypo());			
			rimossa = propList.remove(proprieta);
			
			if(log.isDebugEnabled()) {
				if(rimossa == true) {
					log.debug("rimozione dall'anagrafica4view avvenuta con successo");
				}
				else {
					log.debug("errore non è stato possibile rimuovere la proprieta dall'anagrafica4view");
				}
			}
			
			int pos = proprieta.getPosition();
			int numProp = propList.size();
			
			// occorre partire esattamente da pos perché la proprietà è già
			// stata rimossa dall'anagrafica
			for (int idx = pos; idx < numProp; idx++) {
				propList.get(idx).setPosition(idx);
			}
			// aggiorno il contenuto della cache
			// non è necessario perché il getProprietaDellaTipologia restituisce
			// una referenza
			// cacheAnagr4View.put(proprieta.getTipologia().getShortName(),
			// propList);
		} else {
			//cancello dalla combo
			rimossa = ((MultiValue<P, TP>) proprietaParent.getValue()).getObject()
			.remove(proprieta);
			
			
			if(log.isDebugEnabled()) {
				if(rimossa == true) {
					log.debug("rimozione dalla combo avvenuta con successo");
				}
				else {
					log.debug("errore non è stato possibile rimuovere la proprieta dalla combo");
				}
			}
			
			//aggiorno la cache
			propList = getProprietaDellaTipologiaInValoreMulti(
					(MultiValue<P, TP>) proprietaParent.getValue(), proprieta
							.getTypo());
			int pos = proprieta.getPosition();
			int numProp = propList.size();
			//proprieta.setParent(null); // 0 1 2
			// bisogna partire da pos+1 perchè è vero che dalla anagrafica è già
			// stato rimossa ma non dalla lista di valoreMulti
			for (int idx = pos + 1; idx < numProp; idx++) {
				propList.get(idx).setPosition(idx - 1);
			}
			// aggiorno il contenuto di a4v multi (ho la referenza alla lista
			// contenuta nella mappa...
			rimossa = propList.remove(proprieta);
			
			if(log.isDebugEnabled()) {
				if(rimossa == true) {
					log.debug("rimozione dalla cache della combo avvenuta con successo");
				}
				else {
					log.debug("errore non è stato possibile rimuovere la proprieta dalla cache della combo");
				}
			}
			
			proprieta.setParentProperty(null);
		}

		return rimossa;
	}
	
	/**
	 * Metodo di supporto per la rimozione di tutte le eventuali proprietà
	 * contenute in una data proprietà
	 * 
	 * @param proprieta
	 * @return <code>true</code> se sono state rimosse delle sottoproprietà
	 */
	@SuppressWarnings("unchecked")
	private boolean removeSottoProprieta(P proprieta) {
	
		boolean rimossa = false;		
		try {
			MultiValue<P, TP> valoreMulti = ((MultiValue<P,TP>) proprieta.getValue());
			List<P> sottoProprieta = valoreMulti.getObject();
			for (P proprietaFiglia : sottoProprieta) {				
				rimossa = rimossa || removeSottoProprieta(proprietaFiglia);
				//proprietaFiglia.setParent(null); // necessaria per cancellare la proprietà dal db
				proprietaFiglia.setParentProperty(null);
				//getAnagrafica().remove(proprietaFiglia);
			}
			sottoProprieta.clear();
		} catch (ClassCastException e) {
			// la proprieta non contiene sottoproprietà
		}						
		return rimossa;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Transient
    public Anagrafica4View<P, TP> getAnagrafica4view() {
		// verifico se ho già disponibile una cache
		if (cacheAnagr4View != null) {
			return cacheAnagr4View;
		}

		// genero una nuova cache di anagrafica organizzata come mappa di tp
		// (utile per la view)
		log.debug("Genero una nuova cache di a4v per l'oggetto " + this);

		cacheAnagr4View = new Anagrafica4View<P, TP>(this);

		for (P property : getAnagrafica()) {
//			if (property.getTipologia().getRendering() instanceof WidgetPointer) {
//
//				try {
//					AnagraficaSupport<?, ?, ?> anagraficaObject = (AnagraficaSupport<?, ?, ?>) property
//							.getValore().getOggetto();
//					if (anagraficaObject != null) {
//						Hibernate.initialize(anagraficaObject.getAnagrafica());
//					}
//					log.debug("inizializzo l'anagrafica dell'oggetto puntato");
//				} catch (ClassCastException e) {
//					log
//							.debug("l'oggetto puntato non ha anagrafica dinamica... non la inizializzo");
//				}
//			}

			// in a4v inserisco solo le proprietà di primo livello!
			if (property.getParent() != null && property.getTypo().isTopLevel()==true) {				
				List<P> appoggio = cacheAnagr4View.get(property.getTypo()
						.getShortName());
				if (appoggio == null) {
					appoggio = new LinkedList<P>();
					cacheAnagr4View.put(property.getTypo().getShortName(),
							appoggio);
				}
	
				appoggio.add(property);
				
			}
		}
		if (log.isDebugEnabled()) {
			log.debug("Contenuto di a4v dell'oggetto " + this);
			for (String key : cacheAnagr4View.keySet()) {
				log.debug("--" + key);
				for (P prop : cacheAnagr4View.get(key)) {
					log.debug(prop.getValue());
				}
			}
		}
		return cacheAnagr4View;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public List<P> getProprietaDellaTipologiaInValoreMulti(MultiValue<P,TP> valoreMulti,TP tipologiaProprieta)
	{
		return valoreMulti.getAnagrafica4view().get(tipologiaProprieta.getShortName());
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public List<P> getProprietaDellaTipologia(TP tipologiaProprieta)
	{
		return getAnagrafica4view().get(tipologiaProprieta.getShortName());
	}
	
	@SuppressWarnings("unchecked")
	private P createProprietaWithoutPosizione(P proprietaParent, TP tipologiaProprieta)
	{
		P proprieta = null;
		try {
			proprieta = (P) getClassProperty().newInstance();
		} catch (InstantiationException e) {
			log.error(e);
		} catch (IllegalAccessException e) {
			log.error(e);
		}
				
        proprieta.setTypo(tipologiaProprieta);        
        proprieta.setValue(tipologiaProprieta.getRendering().getInstanceValore());
        // viene effettuato nel setValore()
        //proprieta.getValore().setProprieta(proprieta);
        
        //aggiungo la proprietà all'anagrafica se è top level
        if(tipologiaProprieta.isTopLevel()) {
        	proprieta.setParent(this);
        	getAnagrafica().add(proprieta);
        }
        //altrimenti l'aggiungo solo alla proprieta parent
        else {
        	if (proprietaParent != null)
        	{
        		proprieta.setParentProperty(proprietaParent);
        		((MultiValue<P,TP>)proprietaParent.getValue()).getObject().add(proprieta);
        	}		
        }
        
        //se è una combo creo tutta la struttura 
		if(tipologiaProprieta.getRendering() instanceof WidgetCombo) {
			log.debug("La proprietà in corso di creazione è una combo, creo le sottoproprietà...");
			for(TP tp : ((WidgetCombo<P,TP>)tipologiaProprieta.getRendering()).getSottoTipologie()) {				
				createProprieta(proprieta,tp);
			}
			log.debug("...completata la creazione della combo");
		}
        return proprieta;
	}

	/**
	 * Compila l'anagrafica corrente a partire da una seconda anagrafica.
	 * Se l'anagrafica corrente contiene già delle proprietà queste vengono eliminate.
	 * 
	 * @param sourceAnagrafica l'oggetto dotato di anagrafica da utilizzare come sorgente
	 */
	public void duplicaAnagrafica(AnagraficaSupport<P, TP> sourceAnagrafica) {
		setAnagrafica(new LinkedList<P>());
		if (sourceAnagrafica.getAnagrafica() == null) {
			return;
		}
		for (P prop : sourceAnagrafica.getAnagrafica()) {
			// lavoro solo le top proprietà, le sotto proprietà le recupero
			// implicitamente
			if (prop.getParent() != null && prop.getTypo().isTopLevel()) {
				P cloned = createProprieta(prop.getTypo());
				clonaValore(cloned, prop);
			}
		}
	}

	/**
	 * Metodo di help interno per la duplicazione di una anagrafica.
	 * Attenzione la proprietà target <b>deve essere una proprietà vuota</b> che non 
	 * contenga cioè precedenti valori.
	 * 
	 * @param target Proprietà in cui sarà copiato il valore
	 * @param source Proprietà da cui sarà letto il valore
	 */
	private void clonaValore(P target, P source) {
		if (source.getTypo().getRendering() instanceof WidgetCombo) {
			MultiValue<P, TP> valoreSource = (MultiValue<P, TP>) source
					.getValue();
			int index=0;
			for (P sottoSource : valoreSource.getObject()) {
				// non effettuo nessun check sullo stato corrente del valore della proprietà target perchè per assunzione
				// (vedi javadoc) deve essere una proprietà appena creata. Eventuali check possibili sarebbero inutili nel
				// nostro caso d'uso e avrebbero ripercussioni sulle performance...
				MultiValue<P, TP> valoreSource2 = (MultiValue<P, TP>) target
				.getValue();
				P sottoTarget = valoreSource2.getObject().get(index);
				index++;
				clonaValore(sottoTarget, sottoSource);
				
			}
		} else {
			target.getValue().setOggetto(source.getValue().getObject());
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	public void inizializza() {
		getAnagrafica4view();
	}
	
	/**
	 * {@inheritDoc}
	 */
	public void invalidateAnagraficaCache(){
		if (cacheAnagr4View != null) {
			cacheAnagr4View.clear();
			cacheAnagr4View = null;
		}
	}

	public String getIdentifyingValue() {
		return getId().toString();
	}
	
	public String getDisplayValue() {
		if (getAnagrafica() == null || getAnagrafica().size() == 0) {
			return "new " + this.getClass().getSimpleName() + " " + getId();
		}
		if (getAnagrafica().get(0).getObject() != null) {
			// strip HTML tags... we should consider to use JTidy
			return getAnagrafica().get(0).getObject().toString().replaceAll("\\<.*?>","");
		} else
			return this.getClass().getSimpleName() + " " + getId();
	}
	
	public void pulisciAnagrafica() {
		List<P> propVuote = new LinkedList<P>();
		for (P prop : this.getAnagrafica()) {
			if (prop.getTypo().isTopLevel()
					&& prop.getValue().getObject() == null)
//					&& !prop.getTipologia().getRendering().getClass().isAssignableFrom(WidgetFormula.class))
				propVuote.add(prop);
		}
		
		if(log.isDebugEnabled()) {
			for (P propVuota : propVuote) {
				log.debug("Tipologia della proprieta vuota : "+ propVuota.getTypo().getShortName());
				log.debug("E' obbligatoria? : "+ propVuota.getTypo().isMandatory());
				log.debug("E' contenuta nell'anagrafica? : "+ this.getAnagrafica().contains(propVuota));
			}
		}
		for (P propVuota : propVuote) {
			removeProprieta(propVuota);
		}
		log.debug("eliminate tutte le proprietà nulle di primo livello dall'oggetto "
						+ this);
		
	}
}
