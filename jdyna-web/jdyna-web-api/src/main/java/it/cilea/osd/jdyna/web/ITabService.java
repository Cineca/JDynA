package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.util.List;

/**
 * 
 * Interface to manage Tab, Box and Containable objects
 * 
 * @author Pascarelli Andrea
 *
 */
public interface ITabService extends IPersistenceDynaService {


	/**
	 * Find all properties holder (box) in tab
	 * 
	 * @param <H>
	 * @param <T>
	 * @param tabClass
	 * @param id
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<H> findPropertyHolderInTab(Class<T> tabClass,
			Integer id);

	/**
	 * Delete this properties holder
	 * 
	 * @param <H>
	 * @param <T>
	 * @param tabClass
	 * @param propertyHolder
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deletePropertyHolderInTabs(Class<T> tabClass,
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
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<IContainable> findContainableInPropertyHolder(Class<H> boxClass, Integer id);

	/**
	 * Find all containables, this method internally send a call to customization method
	 * 
	 * @param <TP>
	 * @param classTipologiaProprieta
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException;

	/**
	 * 
	 * Delete containable 
	 * 
	 * @param <H>
	 * @param <T>
	 * @param clazzH
	 * @param containable
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deleteContainableInPropertyHolder(Class<H> clazzH, IContainable containable);

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
	 * Find widget combo by child
	 * 
	 * @param <TP>
	 * @param clazz
	 * @param tip
	 * @return
	 */
	public <TP extends PropertiesDefinition> WidgetCombo findComboByChild(Class<TP> clazz, TP tip);

	/**
	 * Find a tab by shortname
	 * 
	 * @param <H>
	 * @param <T>
	 * @param clazzTab
	 * @param title
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> T getTabByShortName(Class<T> clazzTab,String title);

	/**
	 * Find a box by shortname
	 * 
	 * @param <H>
	 * @param clazzBox
	 * @param title
	 * @return
	 */
	public <H extends IPropertyHolder<Containable>> H getBoxByShortName(Class<H> clazzBox, String title);

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
	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<T> getTabsByVisibility(Class<T> model, Boolean isAdmin);


}
