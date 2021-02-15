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

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.dao.IApplicationDao;
import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.common.service.PersistenceService;
import it.cilea.osd.jdyna.dao.AnagraficaSupportDao;
import it.cilea.osd.jdyna.dao.MultiTypeDaoSupport;
import it.cilea.osd.jdyna.dao.NestedObjectDAO;
import it.cilea.osd.jdyna.dao.PropertiesDefinitionDao;
import it.cilea.osd.jdyna.dao.PropertyDao;
import it.cilea.osd.jdyna.dao.TypeDaoSupport;
import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AnagraficaSupport;
import it.cilea.osd.jdyna.model.MultiTypeSupport;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.model.TypeSupport;

/**
 * 
 * Concrete Persistence Service for JDynA
 * 
 * @author pascarelli
 * 
 */
public class PersistenceDynaService extends PersistenceService implements
        IPersistenceDynaService
{

    protected IApplicationDao applicationDao;

    protected PropertyDao propertyDao;

    protected PropertiesDefinitionDao propertiesDefinitionDao;

    /**
     * Metodo di inizializzazione dei generics dao utilizzati direttamente in
     * modo tale da non doverli sempre prendere dalla mappa dei dao
     */
    public void init()
    {
        propertyDao = (PropertyDao) getDaoByModel(Property.class);
        propertiesDefinitionDao = (PropertiesDefinitionDao) getDaoByModel(PropertiesDefinition.class);
    }

    public void setApplicationDao(IApplicationDao applicationDao)
    {
        this.applicationDao = applicationDao;
    }

    public IApplicationDao getApplicationDao()
    {
        return applicationDao;
    }

    /**
     * {@inheritDoc}
     */
    public void evict(Identifiable identifiable)
    {
        applicationDao.evict(identifiable);
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void deleteAllProprietaByTipologiaProprieta(
            Class model, TP tip)
    {
        PropertyDao modelDao = (PropertyDao) getDaoByModel(model);
        // modelDao.deleteAllProprietaByTipologiaProprieta(tip);
        List<P> props = modelDao.findPropertyByPropertiesDefinition(tip);
        for (P prop : props)
        {
            if (prop.getParent() != null)
            {
                ((AnagraficaSupport<P, TP>) prop.getParent())
                        .removeProprieta(prop);
            }
        }
    }

    /**
     * Metodo di supporto per la cancellazione di proprieta
     * 
     * @param <P>
     * @param <TP>
     * @param <A>
     * @param propDaRimuovere
     * @param proprietaPadre
     */
    private <P extends Property<TP>, TP extends PropertiesDefinition> void deleteProprieta(
            P propDaRimuovere, P proprietaPadre)
    {

        if (proprietaPadre.getParent() != null)
        {
            ((AnagraficaSupport<P, TP>) proprietaPadre.getParent())
                    .removeProprieta(propDaRimuovere);
        }

    }

    /**
     * {@inheritDoc}
     */
    public <T extends PropertiesDefinition> T findPropertiesDefinitionByShortName(
            Class<T> clazz, String shortName)
    {
        return ((PropertiesDefinitionDao<T>) getDaoByModel(clazz))
                .uniqueByShortName(shortName);
    }

 
        
    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> List<TP> findTipologieProprietaAssegnabiliPerMultiTipologie(
            MultiTypeSupport<P, TP> objectWithMultiTypeSupport)
    {
        log.debug("Sono dentro MULTiTYPE");

        MultiTypeDaoSupport<? extends AType<TP>, TP> tipologiaProprietaDao = (MultiTypeDaoSupport<? extends AType<TP>, TP>) getDaoByModel(objectWithMultiTypeSupport
                .getClassPropertiesDefinition());

        List<? extends AType<TP>> listaTipologie = objectWithMultiTypeSupport
                .getTipologie();

        // si potrebbe sparare una query per ogni tipologia e poi mettere
        // insieme i risultati
        List<TP> results = tipologiaProprietaDao
                .findTipologieProprietaAssegnabiliPerMultiType(listaTipologie,
                        listaTipologie.size());
        Collections.sort(results);
        return results;

    }

    /**
     * {@inheritDoc}
     */
    public <TP extends PropertiesDefinition> List<TP> getAllTipologieProprietaWithWidgetFormula(
            Class<TP> classTipologiaProprieta)
    {
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        List<TP> modelList = modelTipologiaProprietaDao
                .findAllWithWidgetFormula();
        return modelList;
    }    
    
    /**
     * {@inheritDoc}
     */
    public <T> List<T> getList(Class<T> model)
    {
        PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
        List<T> modelList = modelDao.findAll();
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> List<P> getProprietaByParentAndTipologia(
            Identifiable oggetto, TP tip)
    {
        List<P> results = ((PropertyDao) getDaoByModel(tip
                .getPropertyHolderClass())).findPropertyByParentAndTypo(
                oggetto.getId(), tip.getId());
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public <T extends PropertiesDefinition> List<T> getTipologiaOnCreation(
            Class<T> model)
    {
        log.debug("ricerca delle tipologie di proprieta da richiedere durante la creazione");
        List<T> results = ((PropertiesDefinitionDao<T>) getDaoByModel(model))
                .findValoriOnCreation();
        Collections.sort(results);
        return results;
    }

    public <T extends PropertiesDefinition> List<T> getValoriDaMostrare(
            Class<T> model)
    {
        List<T> results = ((PropertiesDefinitionDao<T>) getDaoByModel(model))
                .findValoriDaMostrare();
        Collections.sort(results);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public <T> long count(Class<T> classe)
    {
        return ((PaginableObjectDao<T, ?>) getDaoByModel(classe)).count();
    }

    /**
     * {@inheritDoc}
     */
    public <TY extends AType<TP>, TP extends PropertiesDefinition> TY findTypoByShortName(
            Class<TY> clazz, String nome)
    {
        return ((TypeDaoSupport<TY, TP>) getDaoByModel(clazz))
                .uniqueByShortName(nome);
    }

    public <TY extends AType<TP>, TP extends PropertiesDefinition> List<TP> findMaskByShortName(
            Class<TY> clazz, String nome)
    {
        return ((TypeDaoSupport<TY, TP>) getDaoByModel(clazz))
                .findMaskByShortName(nome);
    }
    
    public <TY extends AType<TP>, TP extends PropertiesDefinition> List<TP> findMaskById(
            Class<TY> clazz, Integer id)
    {
        return ((TypeDaoSupport<TY, TP>) getDaoByModel(clazz))
                .findMaskById(id);
    }
    
    /**
     * {@inheritDoc}
     */
    public <T extends AnagraficaSupport<P, TP>, P extends Property<TP>, TP extends PropertiesDefinition> List<T> getPaginateListByTipologiaProprieta(
            Class<T> model, Integer tipologiaId, boolean inverse, int page,
            int maxResults)
    {

        AnagraficaSupportDao<T, ? extends Serializable> modelDao = (AnagraficaSupportDao<T, ? extends Serializable>) getDaoByModel(model);

        List<T> emptyPropObjectList = new LinkedList<T>();
        List<T> modelList = new LinkedList<T>();
        List<T> fullObjectList = new LinkedList<T>();

        if (!inverse)
        {
            // ordinamento naturale, vanno prima i "vuoti" poi quelli
            // valorizzati _ A B C...
            emptyPropObjectList = modelDao.paginateEmptyById(tipologiaId,
                    "valore", inverse, (page - 1) * maxResults, maxResults);
            int emptyListSize = emptyPropObjectList.size();
            long totEmpty = modelDao
                    .countEmptyByTipologiaProprieta(tipologiaId);
            if (emptyListSize < maxResults)
            {
                // ho riempito solo parzialmente la pagina quindi se sono
                // presenti oggetti
                // con proprieta' di tipo TP vanno aggiunti in coda
                modelList = modelDao.paginateByTipologiaProprieta(tipologiaId,
                        "valore", inverse,
                        Long.valueOf(((page - 1) * maxResults) - totEmpty)
                                .intValue(),
                        Long.valueOf(maxResults - emptyListSize).intValue());
            }
            fullObjectList.addAll(emptyPropObjectList);
            fullObjectList.addAll(modelList);
        }
        else
        {
            // ordinamento inverso, vanno prima quelli "pieni" e poi quelli non
            // valorizzati Z V .. B A _
            modelList = modelDao.paginateByTipologiaProprieta(tipologiaId,
                    "valore", inverse, (page - 1) * maxResults, maxResults);
            int modelListSize = modelList.size();
            long totNotEmpty = modelDao
                    .countNotEmptyByTipologiaProprieta(tipologiaId);
            if (modelListSize < maxResults)
            {
                // ho riempito solo parzialmente la pagina quindi se sono
                // presenti oggetti
                // con proprieta' di tipo TP vanno aggiunti in coda
                emptyPropObjectList = modelDao.paginateEmptyById(tipologiaId,
                        "valore", inverse,
                        Long.valueOf(((page - 1) * maxResults) - totNotEmpty)
                                .intValue(),
                        Long.valueOf(maxResults - modelListSize).intValue());
            }
            fullObjectList.addAll(modelList);
            fullObjectList.addAll(emptyPropObjectList);
        }
        // FIXME al momento questa query esclude gli oggetti che non hanno alcun
        // valore per la TP indicata...
        // List<T> modelList =
        // modelDao.paginateByTipologiaProprieta(tipologiaId,"valore", inverse,
        // (page - 1)
        // * maxResults, maxResults);
        return fullObjectList;
    }

    /**
     * {@inheritDoc}
     */
    public <T> List<T> getPaginateList(Class<T> model, String sort,
            boolean inverse, int page, int maxResults)
    {
        PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
        List<T> modelList = modelDao.paginate(sort, inverse, (page - 1)
                * maxResults, maxResults);
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            AnagraficaSupport<P, TP> oggetto)
    {
        List<TP> tpoList = getList(oggetto.getClassPropertiesDefinition());
        fitAnagrafica(oggetto, tpoList, false);
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagraficaOnCreation(
            AnagraficaSupport<P, TP> oggetto)
    {
        List<TP> tpoList = getList(oggetto.getClassPropertiesDefinition());
        fitAnagrafica(oggetto, tpoList, true);
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            TypeSupport<P, TP> oggetto)
    {
        log.debug("Predispongo le proprieta' per la tipologia: "
                + oggetto.getTypo());
        List<TP> tpoList = oggetto.getTypo().getMask();
        fitAnagrafica(oggetto, tpoList, false);
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagraficaOnCreation(
            TypeSupport<P, TP> oggetto)
    {
        log.debug("Predispongo le proprieta' per la tipologia: "
                + oggetto.getTypo());
        List<TP> tpoList = oggetto.getTypo().getMask();
        fitAnagrafica(oggetto, tpoList, true);
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            MultiTypeSupport<P, TP> oggetto)
    {
        List<TP> tpoList = findTipologieProprietaAssegnabiliPerMultiTipologie(oggetto);
        fitAnagrafica(oggetto, tpoList, false);
    }

    /**
     * {@inheritDoc}
     */
    public <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagraficaOnCreation(
            MultiTypeSupport<P, TP> oggetto)
    {
        List<TP> tpoList = findTipologieProprietaAssegnabiliPerMultiTipologie(oggetto);
        fitAnagrafica(oggetto, tpoList, true);
    }

    /**
     * Metodo di supporto per la realizzazione del fitObject
     * 
     * @param <P>
     *            proprieta
     * @param <TP>
     *            tipologia di proprieta
     * @param <A>
     *            area
     * @param oggetto
     *            l'oggetto sul quale si vuole avere la certazza della presenza
     *            di proprieta' di una particolare lista di tipologie
     * @param tpoList
     *            la lista delle tipologie di proprieta' da rendere disponibili
     *            nell'oggetto (saranno filtrate sulle sole topLevel)
     * @param onCreation
     *            mettere a true se si vuole limitare la lista delle tipologie
     *            di proprieta' passate alle sole "onCreation"
     */
    private <P extends Property<TP>, TP extends PropertiesDefinition> void fitAnagrafica(
            AnagraficaSupport<P, TP> oggetto, List<TP> tpoList,
            boolean onCreation)
    {
        for (TP tpo : tpoList)
        {
            if ((!onCreation || (onCreation && tpo.isOnCreation())))
            // || tpo.getRendering().getClass().isAssignableFrom(
            // WidgetFormula.class))) // le formule devono
            // // sempre essere
            // // inizializzate
            // // anche se non
            // // fossero
            // // onCreation
            {
                if (oggetto.getProprietaDellaTipologia(tpo).size() == 0)
                    oggetto.createProprieta(tpo);

            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public <PK extends Serializable> boolean exist(Class model, PK id)
    {
        GenericDao<?, PK> modelDao = getDaoByModel(model);
        if (modelDao.read(id) != null)
        {
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public long countValoriByTipologiaProprieta(Integer propertiesDefinitionID)
    {
        return propertyDao
                .countValueByPropertiesDefinition(propertiesDefinitionID);
    }

    /**
     * {@inheritDoc}
     */
    public <TP extends PropertiesDefinition> TP getPropertiesDefinitionByWidget(
            AWidget widget)
    {
        return (TP) propertiesDefinitionDao
                .findPropertiesDefinitionByWidget(widget);
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndTypoID(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        List<ANO> modelList = modelDao.findNestedObjectsByParentIDAndTypoID(
                dynamicFieldID, typoID);
        return modelList;
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset)
    {
        return getNestedObjectsByParentIDAndTypoIDLimitAt(dynamicFieldID, typoID, model, limit, offset, "positiondef", "asc");
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset, String sort, String order)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        List<ANO> modelList = modelDao
                .paginateNestedObjectsByParentIDAndTypoID(dynamicFieldID,
                        typoID, sort, "desc".equals(order), offset, limit);
        return modelList;
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getActiveNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset)
    {
        return getActiveNestedObjectsByParentIDAndTypoIDLimitAt(dynamicFieldID, typoID, model, limit, offset, "positiondef", "asc");
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getActiveNestedObjectsByParentIDAndTypoIDLimitAt(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model,
            int limit, int offset, String sort, String order)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        List<ANO> modelList = modelDao
                .paginateActiveNestedObjectsByParentIDAndTypoID(dynamicFieldID,
                        typoID, sort, "desc".equals(order), offset, limit);
        return modelList;
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> getNestedObjectsByParentIDAndShortname(
            Integer dynamicFieldID, String shortNameTypo, Class<ANO> model)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        List<ANO> modelList = modelDao
                .findNestedObjectsByParentIDAndTypoShortname(dynamicFieldID,
                        shortNameTypo);
        return modelList;
    }

    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> ANO getNestedObjectsByParentIdAndTypoIDAndNestedSourceReference(
            Integer parentId, Integer typeNestedId, String nestedSourceRef,
            String nestedSourceID, Class<ANO> model)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        return modelDao.uniqueNestedObjectsByParentIdAndTypoIDAndSourceReference(parentId, typeNestedId, nestedSourceRef, nestedSourceID);
    }
    
    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> long countNestedObjectsByParentIDAndTypoID(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        return modelDao.countNestedObjectsByParentIDAndTypoID(dynamicFieldID,
                typoID);
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> long countActiveNestedObjectsByParentIDAndTypoID(
            Integer dynamicFieldID, Integer typoID, Class<ANO> model)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        return modelDao.countActiveNestedObjectsByParentIDAndTypoID(
                dynamicFieldID, typoID);
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> List<ANO> findNestedObjectByTypeID(
            Class<ANO> model, Integer tipologiaID)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        List<ANO> modelList = modelDao.findNestedObjectsByTypoID(tipologiaID);
        return modelList;
    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> void deleteNestedObjectByTypeID(
            Class<ANO> model, Integer typeId)
    {

        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        modelDao.deleteNestedObjectsByTypoID(typeId);

    }

    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> ANO findNestedObjectByUUID(
            Class<ANO> model, String uuid)
    {

        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(model);
        return modelDao.uniqueByUUID(uuid);

    }
    
    @Override
    public <P extends Property<TP>, TP extends PropertiesDefinition, ANO extends ANestedObject<NP, NTP, P, TP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> Integer getNestedMaxPosition(
            ANO object)
    {
        NestedObjectDAO<P, TP, ANO, NP, NTP, ?> modelDao = (NestedObjectDAO<P, TP, ANO, NP, NTP, ?>) getDaoByModel(object
                .getClass());
        Integer result = 0;

        if (ANestedObjectWithTypeSupport.class.isAssignableFrom(object
                .getClass()))
        {

            result = modelDao
                    .maxPositionNestedObjectsByTypoID(((ANestedObjectWithTypeSupport) object)
                            .getTypo().getId());

        }
        else
        {
            result = modelDao.maxPositionNestedObjects();
        }
        
        return result==null?0:result;
    }

    @Override
	public <TP extends PropertiesDefinition> List<TP> likePropertiesDefinitionsByShortName(Class<TP> modelClass, String shortName) {
    	PropertiesDefinitionDao<TP> modelDao = (PropertiesDefinitionDao<TP>) getDaoByModel(modelClass);
    	return modelDao.likeByShortName(shortName);
	}
    
    /**
     * {@inheritDoc}
     */
    @Override
    public <TP extends PropertiesDefinition> List<TP> likeAllPropertiesDefinitionWithPolicySingle(
            Class<TP> classTipologiaProprieta, String specificPart)
    {
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        List<TP> modelList = modelTipologiaProprietaDao
                .likeAllWithPolicySingle(specificPart);
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TP extends PropertiesDefinition> List<TP> likeAllPropertiesDefinitionWithPolicyGroup(
            Class<TP> classTipologiaProprieta, String specificPart)
    {
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        List<TP> modelList = modelTipologiaProprietaDao
                .likeAllWithPolicyGroup(specificPart);
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TP extends PropertiesDefinition> List<TP> getAllPropertiesDefinitionWithPolicySingle(
            Class<TP> classTipologiaProprieta)
    {
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        List<TP> modelList = modelTipologiaProprietaDao
                .findAllWithPolicySingle();
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TP extends PropertiesDefinition> List<TP> getAllPropertiesDefinitionWithPolicyGroup(
            Class<TP> classTipologiaProprieta)
    {
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        List<TP> modelList = modelTipologiaProprietaDao
                .findAllWithPolicyGroup();
        return modelList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TP extends PropertiesDefinition> List<TP> getAllPropertiesDefinitionWithRadioCheckDropdown(
            Class<TP> classTipologiaProprieta)
    {
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        List<TP> modelList = modelTipologiaProprietaDao
                .findAllWithCheckRadioDropdown();
        return modelList;
    }
}
