package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.util.List;

public interface ITabService<H extends IPropertyHolder<Containable>, T extends Tab<H>> extends IPersistenceDynaService {


	public List<H> findPropertyHolderInTab(Class<T> tabClass,
			Integer id);

	public void deletePropertyHolderInTabs(Class<T> tabClass,
			H propertyHolder);

	public List<IContainable> findContainableInPropertyHolder(Class<H> boxClass, Integer id);

	public <TP extends PropertiesDefinition> List<IContainable> findAllContainables(Class<TP> classTipologiaProprieta) throws InstantiationException, IllegalAccessException;

	public void deleteContainableInPropertyHolder(Class<H> clazzH, IContainable containable);

	public IContainable findContainableByDecorable(Class decoratorClass,
			Integer decorable);

}
