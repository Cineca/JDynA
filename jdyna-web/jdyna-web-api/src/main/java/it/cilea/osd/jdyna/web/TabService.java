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
package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.dao.ContainableDao;
import it.cilea.osd.jdyna.dao.EditTabDao;
import it.cilea.osd.jdyna.dao.EditTypedTabDao;
import it.cilea.osd.jdyna.dao.PropertiesDefinitionDao;
import it.cilea.osd.jdyna.dao.PropertyHolderDao;
import it.cilea.osd.jdyna.dao.TabDao;
import it.cilea.osd.jdyna.dao.TypedBoxDao;
import it.cilea.osd.jdyna.dao.TypedTabDao;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.PersistenceDynaService;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Real service to manage Tab, Box and Containable objects
 * 
 * @author Pascarelli Andrea
 * 
 */
public abstract class TabService extends PersistenceDynaService implements
        ITabService
{

    /**
     * {@inheritDoc}
     */
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<H> findPropertyHolderInTab(
            Class<T> classTab, Integer tabId)
    {
        log.debug("Call findPropertyHolderInTab with id " + tabId);

        if (tabId == null)
        {
            tabId = getList(classTab).get(0).getId();
        }
        T area = get(classTab, tabId);
        TabDao<H, T> tabDao = (TabDao<H, T>) getDaoByModel(classTab);
        if (area == null)
        {
            tabId = getList(classTab).get(0).getId();
        }
        List<H> results = tabDao.findPropertyHolderInTab(tabId);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deletePropertyHolderInTabs(
            Class<T> classTab, H holder)
    {
        TabDao<H, T> tabDao = (TabDao<H, T>) getDaoByModel(classTab);
        // trovo le aree dove e' mascherata la tipologia di proprieta' e rompo
        // l'associazione
        List<T> tabs = tabDao.findTabsByHolder(holder);
        for (T tab : tabs)
        {
            tab.getMask().remove(holder);
        }
    }

    /**
     * {@inheritDoc}
     */
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<IContainable> findContainableInPropertyHolder(
            Class<H> boxClass, Integer boxID)
    {
        log.debug("Chiamato findContainable in box con arg: " + boxClass + "  "
                + boxID);

        if (boxID == null)
        {
            boxID = getList(boxClass).get(0).getId();
        }
        H area = get(boxClass, boxID);
        PropertyHolderDao<H> boxDao = (PropertyHolderDao<H>) getDaoByModel(boxClass);
        if (area == null)
        {
            boxID = getList(boxClass).get(0).getId();
        }
        List<IContainable> results = boxDao.findContainableByHolder(boxID);
        // Don't use this, hard-use on jsp
        // findOtherContainablesInBoxByConfiguration(area.getShortName(),
        // results);
        Collections.sort(results);
        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(
            Class<TP> classTipologiaProprieta) throws InstantiationException,
            IllegalAccessException
    {
        List<IContainable> containables = new LinkedList<IContainable>();
        List<TP> listTP = new LinkedList<TP>();
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        listTP.addAll(modelTipologiaProprietaDao.findAll());

        for (TP tp : listTP)
        {
            ContainableDao<Containable> dao = (ContainableDao<Containable>) getDaoByModel(tp
                    .getDecoratorClass());
            containables.add(dao.uniqueContainableByDecorable(tp.getId()));
        }
        findOtherContainables(containables);
        Collections.sort(containables);
        return containables;
    }

    public <TP extends PropertiesDefinition> List<IContainable> newFindAllContainables(
            Class<TP> classTipologiaProprieta) throws InstantiationException,
            IllegalAccessException
    {
        List<IContainable> containables = new LinkedList<IContainable>();
        List<TP> listTP = new LinkedList<TP>();
        PropertiesDefinitionDao<TP> modelTipologiaProprietaDao = (PropertiesDefinitionDao<TP>) getDaoByModel(classTipologiaProprieta);
        listTP.addAll(modelTipologiaProprietaDao.findAll());

        for (TP tp : listTP)
        {
            ContainableDao<Containable> dao = (ContainableDao<Containable>) getDaoByModel(tp
                    .getDecoratorClass());
            Containable uniqueContainableByDecorable = dao
                    .uniqueContainableByDecorable(tp.getId());
            if (uniqueContainableByDecorable != null)
            {
                containables.add(uniqueContainableByDecorable);
            }
        }
        findOtherContainables(containables, classTipologiaProprieta.getName());
        if (containables.size() > 0)
        {
            Collections.sort(containables);
        }
        return containables;
    }

    /** Extends this method to add other containables object type */
    @Deprecated
    protected abstract void findOtherContainables(
            List<IContainable> containables);

    protected abstract void findOtherContainables(
            List<IContainable> containables, String extraPrefixConfiguration);

    /** Extends this method to add other containables object type */
    @Deprecated
    public abstract void findOtherContainablesInBoxByConfiguration(
            String holderName, List<IContainable> containables);

    /** Extends this method to add other containables object type */
    public abstract void findOtherContainablesInBoxByConfiguration(
            String holderName, List<IContainable> containables,
            String extraPrefixConfiguration);

    /**
     * {@inheritDoc}
     */
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deleteContainableInPropertyHolder(
            Class<H> clazzH, IContainable containable)
    {
        PropertyHolderDao<H> modelDao = (PropertyHolderDao<H>) getDaoByModel(clazzH);
        List<H> boxs = modelDao.findHolderByContainable(containable);
        for (H box : boxs)
        {
            box.getMask().remove(containable);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IContainable findContainableByDecorable(Class decoratorClass,
            Integer decorable)
    {
        ContainableDao modelDao = (ContainableDao) getDaoByModel(decoratorClass);
        IContainable result = modelDao.uniqueContainableByDecorable(decorable);
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>> T getTabByShortName(
            Class<T> clazzTab, String title)
    {
        TabDao<H, T> tabDao = (TabDao<H, T>) getDaoByModel(clazzTab);
        return tabDao.uniqueTabByShortName(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <H extends IPropertyHolder<Containable>> H getBoxByShortName(
            Class<H> clazzBox, String title)
    {
        PropertyHolderDao<H> boxDao = (PropertyHolderDao<H>) getDaoByModel(clazzBox);
        return boxDao.uniqueBoxByShortName(title);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <TP extends PropertiesDefinition> List<IContainable> getContainableOnCreation(
            Class<TP> model)
    {
        log.debug("find all containable on object creation");
        List<TP> results = getTipologiaOnCreation(model);
        List<IContainable> iResult = new LinkedList<IContainable>();
        for (TP rpPd : results)
        {
            iResult.add(findContainableByDecorable(rpPd.getDecoratorClass(),
                    rpPd.getId()));
        }
        getOtherContainableOnCreation(iResult);
        return iResult;
    }

    /** Extends this method to add other containables on creation */
    protected abstract void getOtherContainableOnCreation(
            List<IContainable> containables);

    /**
     * Decouple edit tab wrapper from display tab id, check persistence object
     * is alive and after unhook it.
     * 
     * @param tabId
     */
    public <H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H, D>> void decoupleEditTabByDisplayTab(
            int tabId, Class<T> clazz)
    {

        T editTab = getEditTabByDisplayTab(tabId, clazz);
        if (editTab != null)
        {
            for (H box : editTab.getDisplayTab().getMask())
            {
                editTab.getMask().add(box);
            }
            editTab.setDisplayTab(null);
            saveOrUpdate(clazz, editTab);
        }
    }

    /**
     * Hook edit tab with display tab
     * 
     * @param id
     */
    public <H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H, D>> void hookUpEditTabToDisplayTab(
            Integer editTabId, Integer displayTabId, Class<T> clazz)
    {
        T editTab = get(clazz, editTabId);
        if (editTab != null)
        {
            D displayTab = get(editTab.getDisplayTabClass(), displayTabId);
            editTab.setDisplayTab(displayTab);
            saveOrUpdate(clazz, editTab);
        }

    }

    /**
     * Get edit tab by display tab id
     * 
     * @param tabId
     * @return
     */
    public <H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H, D>> T getEditTabByDisplayTab(
            Integer tabId, Class<T> clazz)
    {
        EditTabDao<H, D, T> dao = (EditTabDao<H, D, T>) getDaoByModel(clazz);
        T editTab = dao.uniqueByDisplayTab(tabId);
        return editTab;
    }

    /**
     * 
     * Find by access level, @see {@link VisibilityTabConstant}
     * 
     * 
     * @param isAdmin
     *            three mode, null get all visibility (ADMIN and OWNER
     *            visibility), true get all admin access level, false get all
     *            owner rp access level
     * @return
     */
    @Override
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<T> getTabsByVisibility(
            Class<T> model, Boolean isAdmin)
    {

        TabDao<H, T> dao = (TabDao<H, T>) getDaoByModel(model);
        List<T> tabs = new LinkedList<T>();
        if (isAdmin == null)
        {
            tabs.addAll(dao.findByAnonimous());
            // tabs.addAll(dao.findByAccessLevel(VisibilityTabConstant.HIGH));
        }
        else
        {
            if (isAdmin)
            {
                tabs.addAll(dao.findByAdmin());
                // tabs.addAll(dao.findByAccessLevel(VisibilityTabConstant.HIGH));
                // tabs.addAll(dao.findByAccessLevel(VisibilityTabConstant.ADMIN));
                // tabs.addAll(dao
                // .findByAccessLevel(VisibilityTabConstant.STANDARD));
            }
            else
            {
                tabs.addAll(dao.findByOwner());
                // tabs.addAll(dao.findByAccessLevel(VisibilityTabConstant.HIGH));
                // tabs.addAll(dao
                // .findByAccessLevel(VisibilityTabConstant.STANDARD));
                // tabs.addAll(dao.findByAccessLevel(VisibilityTabConstant.LOW));
            }
        }

        Collections.sort(tabs);
        return tabs;

    }

    @Override
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>> List<D> findTabByType(
            Class<D> tabClass, A typo)
    {
        TypedTabDao<H, A, PD, D> dao = (TypedTabDao<H, A, PD, D>) getDaoByModel(tabClass);
        return dao.findTabByType(typo);
    }

    @Override
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>, DA extends TypedAbstractEditTab<H, A, PD, D>> List<DA> findEditTabByType(
            Class<DA> tabClass, A typo)
    {
        EditTypedTabDao<H, A, PD, D, DA> dao = (EditTypedTabDao<H, A, PD, D, DA>) getDaoByModel(tabClass);
        return dao.findTabByType(typo);
    }

    @Override
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition> List<H> findBoxByType(
            Class<H> boxClass, A typo)
    {
        TypedBoxDao<H, A, PD> dao = (TypedBoxDao<H, A, PD>) getDaoByModel(boxClass);
        return dao.findBoxByType(typo);
    }

    @Override
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>, DA extends TypedAbstractEditTab<H, A, PD, D>> List<DA> getEditTabsByVisibilityAndType(
            Class<DA> model, Boolean isAdmin, A typo)
    {

        EditTypedTabDao<H, A, PD, D, DA> dao = (EditTypedTabDao<H, A, PD, D, DA>) getDaoByModel(model);
        List<DA> tabs = new LinkedList<DA>();
        if (isAdmin == null)
        {
            tabs.addAll(dao.findByAnonimousAndTypoDef(typo));
        }
        else
        {
            if (isAdmin)
            {
                tabs.addAll(dao.findByAdminAndTypoDef(typo));
            }
            else
            {
                tabs.addAll(dao.findByOwnerAndTypoDef(typo));
            }
        }

        Collections.sort(tabs);
        return tabs;

    }
    
    @Override
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>> List<D> getTabsByVisibilityAndTypo(
            Class<D> model, Boolean isAdmin, A typo)
    {

        TypedTabDao<H, A, PD, D> dao = (TypedTabDao<H, A, PD, D>) getDaoByModel(model);
        List<D> tabs = new LinkedList<D>();
        if (isAdmin == null)
        {
            tabs.addAll(dao.findByAnonimousAndTypoDef(typo));
        }
        else
        {
            if (isAdmin)
            {
                tabs.addAll(dao.findByAdminAndTypoDef(typo));
            }
            else
            {
                tabs.addAll(dao.findByOwnerAndTypoDef(typo));
            }
        }

        Collections.sort(tabs);
        return tabs;

    }
        
}
