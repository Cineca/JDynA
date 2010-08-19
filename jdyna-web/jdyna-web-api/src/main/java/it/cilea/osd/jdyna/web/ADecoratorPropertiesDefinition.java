package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class ADecoratorPropertiesDefinition<TP extends PropertiesDefinition> implements IPropertiesDefinition, IContainable {

	public abstract void setRpdynamicfield(TP rpdynamicfield);	

	public abstract TP getRpdynamicfield();

}
