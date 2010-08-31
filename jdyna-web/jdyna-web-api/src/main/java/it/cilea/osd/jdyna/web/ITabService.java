package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.util.List;

public interface ITabService extends IPersistenceDynaService {


	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<H> findPropertyHolderInTab(Class<T> tabClass,
			Integer id);

	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deletePropertyHolderInTabs(Class<T> tabClass,
			H propertyHolder);

	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> List<IContainable> findContainableInPropertyHolder(Class<H> boxClass, Integer id);

	public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException;

	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> void deleteContainableInPropertyHolder(Class<H> clazzH, IContainable containable);

	public IContainable findContainableByDecorable(Class decoratorClass,
			Integer decorable);

	public <TP extends PropertiesDefinition> WidgetCombo findComboByChild(Class<TP> clazz, TP tip);

	public <H extends IPropertyHolder<Containable>, T extends Tab<H>> T getTabByShortName(Class<T> clazzTab,String title);

	public <H extends IPropertyHolder<Containable>> H getBoxByShortName(Class<H> clazzBox, String title);

	public List<IContainable> getContainableOnCreation();

}
