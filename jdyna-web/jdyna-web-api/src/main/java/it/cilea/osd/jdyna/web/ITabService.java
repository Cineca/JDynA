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

import java.util.List;

import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.ATypeNestedObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

/**
 * 
 * Interface to manage Tab, Box and Containable objects
 * 
 * @author Pascarelli Andrea
 *
 */
public interface ITabService extends IPersistenceDynaService {


	public final static String PREFIX_TITLE_EDIT_BOX = "Edit ";
    public final static String PREFIX_SHORTNAME_EDIT_BOX = "edit";
    public final static String PREFIX_TITLE_EDIT_TAB = "Edit ";
    public final static String PREFIX_SHORTNAME_EDIT_TAB = "edit";

    /**
	 * Find all properties holder (box) in tab
	 * 
	 * @param <H>
	 * @param <T>
	 * @param tabClass
	 * @param id
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> List<H> findPropertyHolderInTab(Class<T> tabClass,
			Integer id);

	/**
	 * Delete this properties holder
	 * 
	 * @param <H>
	 * @param <T>
	 * @param tabClass
	 * @param propertyHolder
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> void deletePropertyHolderInTabs(Class<T> tabClass,
			H propertyHolder);

	/**
	 * Find containables on property holder
	 * 
	 * @param <H>
	 * @param <T>
	 * @param boxClass
	 * @param id
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> List<IContainable> findContainableInPropertyHolder(Class<H> boxClass, Integer id);

	/**
	 * Find all containables, this method internally send a call to customization method
	 * @deprecated {@link ITabService#newFindAllContainables(Class)}
	 * @param <TP>
	 * @param classTipologiaProprieta
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	@Deprecated
	public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException;
	/**
     * Find all containables, this method internally send a call to customization method
     * 
     * @param <TP>
     * @param classTipologiaProprieta
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */    
	public <TP extends PropertiesDefinition> List<IContainable> newFindAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException;
	/**
	 * 
	 * Delete containable 
	 * 
	 * @param <H>
	 * @param <T>
	 * @param clazzH
	 * @param containable
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> void deleteContainableInPropertyHolder(Class<H> clazzH, IContainable containable);

	/**
	 * Find containable by real object that is a decorable for it
	 * 
	 * @param decoratorClass
	 * @param decorable
	 * @return
	 */
	public <IC> IC findContainableByDecorable(Class decoratorClass,
			Integer decorable);

	/**
	 * Find a tab by shortname
	 * 
	 * @param <H>
	 * @param <T>
	 * @param clazzTab
	 * @param title
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> T getTabByShortName(Class<T> clazzTab,String title);

	/**
	 * Find a box by shortname
	 * 
	 * @param <H>
	 * @param clazzBox
	 * @param title
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, PD extends PropertiesDefinition> H getBoxByShortName(Class<H> clazzBox, String title);

	/**
	 * Find containables with decorable on creation state, internally send a call to customization method
	 * 
	 * @param <TP>
	 * @param model
	 * @return
	 */
	public <TP extends PropertiesDefinition> List<IContainable> getContainableOnCreation(Class<TP> model);

	/**
	 * Each customization have a custom method to get tab visibility access level, see concrete service to see implementation details
	 * 
	 * @param <H>
	 * @param <T>
	 * @param model
	 * @param isAdmin
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> List<T> getTabsByVisibility(Class<T> model, Boolean isAdmin);

    public <H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H,D>> void decoupleEditTabByDisplayTab(int tabId, Class<T> clazz);
    
    public <H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H,D>> void hookUpEditTabToDisplayTab(Integer editTabId,
            Integer displayTabId, Class<T> clazz);
    
    public <H extends IPropertyHolder<Containable>, D extends AbstractTab<H>, T extends AbstractEditTab<H,D>, PD extends PropertiesDefinition> T getEditTabByDisplayTab(Integer tabId, Class<T> clazz);
    
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition> List<H> findBoxByType(Class<H> boxClass, A typo);
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>> List<D> findTabByType(Class<D> tabClass, A typo);
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>, DA extends TypedAbstractEditTab<H, A, PD, D>> List<DA> findEditTabByType(Class<DA> tabClass, A typo);

    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>, DA extends TypedAbstractEditTab<H, A, PD, D>> List<DA> getEditTabsByVisibilityAndType(Class<DA> model, Boolean isAdmin, A typo);
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>> List<D> getTabsByVisibilityAndTypo(Class<D> model, Boolean isAdmin, A typo);

	public <IC> IC findContainableByDecorable(Class decoratorClass, String shortname);
	
	public <TP extends PropertiesDefinition, H extends IPropertyHolder<Containable>, T extends Tab<H>, ATTP extends ANestedPropertiesDefinition, TTP extends ATypeNestedObject<ATTP>> List<H> findBoxesByTTP(Class<H> clazzH, Class<TTP> clazzTTP, String decorable);
	
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> List<T> getTabsByAccessLevel(Class<T> modelClass, Integer level);
    public <H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition, D extends TypedAbstractTab<H, A, PD>> List<D> getTabsByTypoAndAccessLevel(
            Class<D> model, Integer level, A typo);

    public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> List<PD> findPDAuthorizationGroupInTab(Class<T> tabClass, Integer id);
    public <H extends IPropertyHolder<Containable>, T extends Tab<H>, PD extends PropertiesDefinition> List<PD> findPDAuthorizationSingleInTab(Class<T> tabClass, Integer id);
    public <H extends IPropertyHolder<Containable>, PD extends PropertiesDefinition> List<PD> findPDAuthorizationGroupInBox(Class<H> boxClass, Integer id);
    public <H extends IPropertyHolder<Containable>, PD extends PropertiesDefinition> List<PD> findPDAuthorizationSingleInBox(Class<H> boxClass, Integer id);
}
