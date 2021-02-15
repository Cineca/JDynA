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
package it.cilea.osd.jdyna.service;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.common.service.IPersistenceService;
import it.cilea.osd.jdyna.dao.MultiTypeDaoSupport;
import it.cilea.osd.jdyna.dao.PropertiesDefinitionDao;
import it.cilea.osd.jdyna.dao.TypeDaoSupport;
import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.MultiTypeSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.model.TypeSupport;

import java.util.List;

public interface IPersistenceDynaService extends IPersistenceService
{

    // /**
    // * Trova gli oggetti da ricalcolare utilizzando le informazioni clazz e id
    // * del JPAEvent. NB: al momento la tipologia di evento non viene
    // utilizzata,
    // * il dato e' stato memorizzato solo per la comodita' di sfruttare la
    // * defininizione del JPAEvent all'interno della definizione di
    // * OggettoDaRicalcolare
    // */
    // public <TP extends PropertiesDefinition, P extends Property<TP>>
    // Set<AnagraficaSupport<P, TP, A>> findOggettoDaRicalcolareByEvento(
    // JPAEvent evento);

    /**
     * Resituisce tutte le tipologie di proprieta che hanno rendering un widget
     * formula
     */
    public <TP extends PropertiesDefinition> List<TP> getAllTipologieProprietaWithWidgetFormula(
            Class<TP> classTipologiaProprieta);
    
    // /** Cancella gli oggetti da ricalcolare
    // *
    // * @param id - l'id dell'oggetto
    // * @param canonicalName - il nome della classe
    // * */
    // void deleteOggettoDaRicalcolareByOggetto(Integer id, String
    // canonicalName);

    /**
     * Trova tutte le proprieta' con il parent e la tipologia passata come
     * parametro
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> List<P> getProprietaByParentAndTipologia(
            Identifiable oggetto, TP tip);

    /**
     * Restituisce la tipologia di proprieta' (unica) data dallo shortName
     * passato come parametro
     * 
     * @see PropertiesDefinitionDao#uniqueByShortName(String)
     * @param clazz
     *            - la classe di tipologia proprieta su cui cercare
     * @param shortName
     *            - il codice univoco della tipologia di proprieta
     * @return la tipologia di proprieta per quello shortname
     */
    public <T extends PropertiesDefinition> T findPropertiesDefinitionByShortName(
            Class<T> clazz, String shortName);

       
        
    /**
     * @see MultiTypeDaoSupport
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param objectWithMultiTypeSupport
     * @return
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> List<TP> findTipologieProprietaAssegnabiliPerMultiTipologie(
            MultiTypeSupport<P, TP> objectWithMultiTypeSupport);

    /**
     * Cancella tutte le proprieta per la tipologia di metadato passata come
     * parametro
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param model
     * @param tip
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void deleteAllProprietaByTipologiaProprieta(
            Class model, TP tip);

    /**
     * 
     * @param <T>
     * @param model
     * @return Lista delle tipologie di proprieta che devono essere mostrate
     *         come colonne delle tabelle di visualizzazione della lista di
     *         oggetti (showInList = TRUE).
     */
    public <T extends PropertiesDefinition> List<T> getValoriDaMostrare(
            Class<T> model);

    /**
     * 
     * @see TypeDaoSupport#uniqueByShortName(String)
     * @param <TY>
     * @param <TP>
     * @param clazz
     * @param nome
     * @return
     */
    public <TY extends AType<TP>, TP extends PropertiesDefinition> TY findTypoByShortName(
            Class<TY> clazz, String shortName);

    /**
     * Restituisce la lista ordinata di tipologia di proprieta richieste in
     * creazione (quelle che hanno oncreation == true)
     * 
     * @see PropertiesDefinitionDao#findValoriOnCreation()
     * @see PropertiesDefinition#compareTo(PropertiesDefinition)
     * @param model
     *            - la classe di tipologia proprieta da cui riprendere il dao
     * @return la lista di tipologie di proprieta richieste in creazione
     */
    public <T extends PropertiesDefinition> List<T> getTipologiaOnCreation(
            Class<T> model);

    /**
     * Conta il numero di dati(le proprieta') associati alla tipologia di
     * proprieta
     * 
     * @param tipologiaProprietaID
     *            : l'id della tipologia
     * @return il numero di proprieta trovate con quella tipologia
     * */
    public long countValoriByTipologiaProprieta(Integer tipologiaProprietaID);

    /**
     * Metodo di supporto per la realizzazione del fitObject, inizializza
     * sull'oggetto passato come parametro le proprieta che risultano nulle.
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param oggetto
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            AnagraficaSupport<P, TP> oggetto);

    /**
     * Metodo di supporto per la realizzazione del fitObject, inizializza
     * sull'oggetto passato come parametro le proprieta che risultano nulle.
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param oggetto
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagraficaOnCreation(
            AnagraficaSupport<P, TP> oggetto);

    /**
     * Metodo di supporto per la realizzazione del fitObject, inizializza
     * sull'oggetto passato come parametro le proprieta che risultano nulle.
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param oggetto
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            TypeSupport<P, TP> oggetto);

    /**
     * Metodo di supporto per la realizzazione del fitObject, inizializza
     * sull'oggetto passato come parametro le proprieta che risultano nulle.
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param oggetto
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagraficaOnCreation(
            TypeSupport<P, TP> oggetto);

    /**
     * Metodo di supporto per la realizzazione del fitObject, inizializza
     * sull'oggetto passato come parametro le proprieta che risultano nulle.
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param oggetto
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            MultiTypeSupport<P, TP> oggetto);

    /**
     * Metodo di supporto per la realizzazione del fitObject, inizializza
     * sull'oggetto passato come parametro le proprieta che risultano nulle.
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param oggetto
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagraficaOnCreation(
            MultiTypeSupport<P, TP> oggetto);

    /**
     * Metodo per la paginazione degli oggetti usando come criterio la tipologia
     * di proprieta
     * 
     * @param <T>
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param model
     * @param tipologiaId
     * @param inverse
     * @param page
     * @param maxResults
     * @return
     */
    public <T extends AnagraficaSupport<P, TP>, P extends Property<TP>, TP extends PropertiesDefinition> List<T> getPaginateListByTipologiaProprieta(
            Class<T> model, Integer tipologiaId, boolean inverse, int page,
            int maxResults);

    /**
     * 
     * Return properties definition by widget
     * 
     * @param <TP>
     * @param widget
     * @return
     */
    public <TP extends PropertiesDefinition> TP getPropertiesDefinitionByWidget(
            AWidget widget);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndTypoID(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndShortname(
            Integer dynamicFieldID, String typoShortname, Class<ANO> model);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset, String sort, String order);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> long countNestedObjectsByParentIDAndTypoID(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getActiveNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getActiveNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset, String sort, String order);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> long countActiveNestedObjectsByParentIDAndTypoID(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> findNestedObjectByTypeID(
            Class<ANO> model, Integer tipologiaID);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> void deleteNestedObjectByTypeID(
            Class<ANO> model, Integer tipologiaProprietaId);
    
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> Integer getNestedMaxPosition(ANO model);

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> ANO findNestedObjectByUUID(Class<ANO> model, String uuid);
    
    public <TP extends PropertiesDefinition> List<TP> likePropertiesDefinitionsByShortName(Class<TP> modelClass, String shortName);
    
    
    /**
     * Return all properties definition that got type of single policy (single vs group is a concept defined into higher layer)
     */
    public <TP extends PropertiesDefinition> List<TP> likeAllPropertiesDefinitionWithPolicySingle(
            Class<TP> classTipologiaProprieta, String specificPart);

    /**
     * Return all properties definition that got type of group policy (single vs group is a concept defined into higher layer)
     */
    public <TP extends PropertiesDefinition> List<TP> likeAllPropertiesDefinitionWithPolicyGroup(
            Class<TP> classTipologiaProprieta, String specificPart);
    
    /**
     * Return all properties definition that got type of single policy (single vs group is a concept defined into higher layer)
     */
    public <TP extends PropertiesDefinition> List<TP> getAllPropertiesDefinitionWithPolicySingle(
            Class<TP> classTipologiaProprieta);

    /**
     * Return all properties definition that got type of group policy (single vs group is a concept defined into higher layer)
     */
    public <TP extends PropertiesDefinition> List<TP> getAllPropertiesDefinitionWithPolicyGroup(
            Class<TP> classTipologiaProprieta);

    /**
     * Return all properties definition that got type of {@link WidgetCheckRadio}
     */
    public <TP extends PropertiesDefinition> List<TP> getAllPropertiesDefinitionWithRadioCheckDropdown(
            Class<TP> classTipologiaProprieta);
}
