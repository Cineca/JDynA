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

import it.cilea.osd.common.model.IdentifiableObject;
import it.cilea.osd.jdyna.util.Anagrafica4View;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Classe astratta che fornisce un'implementazione base per alcuni metodi di
 * gestione dell'anagrafica
 * 
 * @author bollini
 */
public abstract class AnagraficaObject<P extends Property<TP>, TP extends PropertiesDefinition>
        extends IdentifiableObject implements AnagraficaSupport<P, TP>
{

    @Transient
    private Log log = LogFactory.getLog(getClass());

    /**
     * {@inheritDoc}
     */
    public P createProprieta(TP tipologiaProprieta)
    {

        List<P> propList = getProprietaDellaTipologia(tipologiaProprieta);
        int numProp = propList.size();
        P proprieta = createProprietaWithoutPosizione(tipologiaProprieta);
        proprieta.setPositionDef(numProp); // la dimensione di una lista e'
                                        // esattamente la prima posizione
                                        // disponibile
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

        List<P> propList = getProprietaDellaTipologia(tipologiaProprieta);
        int numProp = propList.size();
        for (int idx = posizione; idx < numProp; idx++)
        {
            propList.get(idx).setPositionDef(idx + 1);
        }
        P proprieta = createProprietaWithoutPosizione(tipologiaProprieta);
        if (numProp < posizione)
            proprieta.setPositionDef(numProp);
        else
            proprieta.setPositionDef(posizione);
        Collections.sort(getAnagrafica());
        // aggiorno il contenuto della cache
        propList.add(posizione, proprieta);
        return proprieta;
    }

    /**
     * {@inheritDoc}
     */
    public void setAnagrafica(List<P> anagrafica)
    {
        for (P p : getAnagrafica())
        {

            p.setParent(null);
            p.getValue().setOggetto(null);

        }
        getAnagrafica().clear();
        if (anagrafica != null)
        {
            Collections.sort(anagrafica);
            getAnagrafica().addAll(anagrafica);
        }

        for (P p : getAnagrafica())
        {
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
            log.debug("Rimozione della proprieta': " + proprieta
                    + " dall'oggetto: " + proprieta.getParent()
                    + " con rendering: " + proprieta.getTypo().getRendering());
            log.debug("TP: " + proprieta.getTypo().getShortName() + " V: "
                    + proprieta.getValue().getObject());
        }

        // check se la proprieta viene rimossa dall'anagrafica dell'oggetto o
        // dalla lista di proprieta di una combo
        boolean rimossa = false;
        // la lista contiene tutte gli elementi di quella tipologia, e' ordinata
        // in base alle posizioni
        List<P> propList;

        // cancello dall'anagrafica
        proprieta.setParent(null); // 0 1 2
        rimossa = getAnagrafica().remove(proprieta);

        if (log.isDebugEnabled())
        {
            if (rimossa == true)
            {
                log.debug("rimozione dall'anagrafica avvenuta con successo");
            }
            else
            {
                log.debug("errore non e' stato possibile rimuovere la proprieta dall'anagrafica");
            }
        }

        // aggiorno la cache
        propList = getProprietaDellaTipologia(proprieta.getTypo());
        rimossa = propList.remove(proprieta);

        if (log.isDebugEnabled())
        {
            if (rimossa == true)
            {
                log.debug("rimozione dall'anagrafica4view avvenuta con successo");
            }
            else
            {
                log.debug("errore non e' stato possibile rimuovere la proprieta dall'anagrafica4view");
            }
        }

        int pos = proprieta.getPositionDef();
        int numProp = propList.size();

        // occorre partire esattamente da pos perche' la proprieta' e' gia'
        // stata rimossa dall'anagrafica
        for (int idx = pos; idx < numProp; idx++)
        {
            propList.get(idx).setPositionDef(idx);
        }
        // aggiorno il contenuto della cache
        // non e' necessario perche' il getProprietaDellaTipologia restituisce
        // una referenza
        // cacheAnagr4View.put(proprieta.getTipologia().getShortName(),
        // propList);

        return rimossa;
    }

    /**
     * {@inheritDoc}
     */
    @Transient
    public Anagrafica4View<P, TP> getAnagrafica4view()
    {
        // genero una nuova cache di anagrafica organizzata come mappa di tp
        // (utile per la view)
        log.debug("Genero una nuova cache di a4v per l'oggetto " + this);

        Anagrafica4View<P, TP> cacheAnagr4View = new Anagrafica4View<P, TP>(this);

        for (P property : getAnagrafica())
        {
            // if (property.getTipologia().getRendering() instanceof
            // WidgetPointer) {
            //
            // try {
            // AnagraficaSupport<?, ?, ?> anagraficaObject =
            // (AnagraficaSupport<?, ?, ?>) property
            // .getValore().getOggetto();
            // if (anagraficaObject != null) {
            // Hibernate.initialize(anagraficaObject.getAnagrafica());
            // }
            // log.debug("inizializzo l'anagrafica dell'oggetto puntato");
            // } catch (ClassCastException e) {
            // log
            // .debug("l'oggetto puntato non ha anagrafica dinamica... non la inizializzo");
            // }
            // }

            // in a4v inserisco solo le proprieta' di primo livello!
            if (property.getParent() != null)
            {
                List<P> appoggio = cacheAnagr4View.get(property.getTypo()
                        .getShortName());
                if (appoggio == null)
                {
                    appoggio = new LinkedList<P>();
                    cacheAnagr4View.put(property.getTypo().getShortName(),
                            appoggio);
                }

                appoggio.add(property);

            }
        }
        if (log.isDebugEnabled())
        {
            log.debug("Contenuto di a4v dell'oggetto " + this);
            for (String key : cacheAnagr4View.keySet())
            {
                log.debug("--" + key);
                for (P prop : cacheAnagr4View.get(key))
                {
                    log.debug(prop.getValue());
                }
            }
        }
        return cacheAnagr4View;
    }

    /**
     * {@inheritDoc}
     */
    public List<P> getProprietaDellaTipologia(TP tipologiaProprieta)
    {
        return getAnagrafica4view().get(tipologiaProprieta.getShortName());
    }

    @SuppressWarnings("unchecked")
    private P createProprietaWithoutPosizione(TP tipologiaProprieta)
    {
        P proprieta = null;
        try
        {
            proprieta = (P) getClassProperty().newInstance();
        }
        catch (InstantiationException e)
        {
            log.error(e);
        }
        catch (IllegalAccessException e)
        {
            log.error(e);
        }

        proprieta.setTypo(tipologiaProprieta);
        proprieta.setValue(tipologiaProprieta.getRendering()
                .getInstanceValore());
        // viene effettuato nel setValore()
        // proprieta.getValore().setProprieta(proprieta);

        // aggiungo la proprieta' all'anagrafica se e' top level

        proprieta.setParent(this);
        getAnagrafica().add(proprieta);

        return proprieta;
    }

    /**
     * Compila l'anagrafica corrente a partire da una seconda anagrafica. Se
     * l'anagrafica corrente contiene gia' delle proprieta' queste vengono
     * eliminate.
     * 
     * @param sourceAnagrafica
     *            l'oggetto dotato di anagrafica da utilizzare come sorgente
     */
    public void duplicaAnagrafica(AnagraficaSupport<P, TP> sourceAnagrafica)
    {
        setAnagrafica(new LinkedList<P>());
        if (sourceAnagrafica.getAnagrafica() == null)
        {
            return;
        }
        for (P prop : sourceAnagrafica.getAnagrafica())
        {
            // lavoro solo le top proprieta', le sotto proprieta' le recupero
            // implicitamente
            if (prop.getParent() != null)
            {
                P cloned = createProprieta(prop.getTypo());
                clonaValore(cloned, prop);
            }
        }
    }

    /**
     * Metodo di help interno per la duplicazione di una anagrafica. Attenzione
     * la proprieta' target <b>deve essere una proprieta' vuota</b> che non
     * contenga cioe' precedenti valori.
     * 
     * @param target
     *            Proprieta' in cui sara' copiato il valore
     * @param source
     *            Proprieta' da cui sara' letto il valore
     */
    private void clonaValore(P target, P source)
    {

        target.getValue().setOggetto(source.getValue().getObject());
        target.setVisibility(source.getVisibility());

    }

    /**
     * {@inheritDoc}
     */
    public void inizializza()
    {
        getAnagrafica4view();
    }

    /**
     * {@inheritDoc}
     */
    public void invalidateAnagraficaCache()
    {
        // cache is always regenerated - do nothing
    }

    public String getIdentifyingValue()
    {
        return getId().toString();
    }

    public String getDisplayValue()
    {
        if (getAnagrafica() == null || getAnagrafica().size() == 0)
        {
            return "new " + this.getClass().getSimpleName() + " " + getId();
        }
        if (getAnagrafica().get(0).getObject() != null)
        {
            // strip HTML tags... we should consider to use JTidy
            return getAnagrafica().get(0).getObject().toString()
                    .replaceAll("\\<.*?>", "");
        }
        else
            return this.getClass().getSimpleName() + " " + getId();
    }

    public void pulisciAnagrafica()
    {
        List<P> propVuote = new LinkedList<P>();
        for (P prop : this.getAnagrafica())
        {
            if (prop.getValue().getObject() == null)
                // &&
                // !prop.getTipologia().getRendering().getClass().isAssignableFrom(WidgetFormula.class))
                propVuote.add(prop);
        }

        if (log.isDebugEnabled())
        {
            for (P propVuota : propVuote)
            {
                log.debug("Tipologia della proprieta vuota : "
                        + propVuota.getTypo().getShortName());
                log.debug("E' obbligatoria? : "
                        + propVuota.getTypo().isMandatory());
                log.debug("E' contenuta nell'anagrafica? : "
                        + this.getAnagrafica().contains(propVuota));
            }
        }
        for (P propVuota : propVuote)
        {
            removeProprieta(propVuota);
        }
        log.debug("eliminate tutte le proprieta' nulle di primo livello dall'oggetto "
                + this);

    }
}
