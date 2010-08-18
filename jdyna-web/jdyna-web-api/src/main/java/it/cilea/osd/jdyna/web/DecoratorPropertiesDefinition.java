package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

public class DecoratorPropertiesDefinition extends IContainable implements IPropertiesDefinition {

	private PropertiesDefinition propertiesDefinition;

	@Override
	public Class getAnagraficaHolderClass() {
		return propertiesDefinition.getAnagraficaHolderClass();
	}

	@Override
	public Class getPropertyHolderClass() {
		return propertiesDefinition.getPropertyHolderClass();
	}

	@Override
	public Integer getId() {
		return propertiesDefinition.getId();
	}


	public int compareTo(IContainable o) {		
		if (o == null) return -1;
		if (getId() < o.getId()) return 1;
		return -1;
	}

	@Override
	public AWidget getRendering() {
		return this.propertiesDefinition.getRendering();
	}

	@Override
	public String getShortName() {
		return this.propertiesDefinition.getShortName();
	}

	@Override
	public boolean isMandatory() {
		return this.propertiesDefinition.isMandatory();
	}

	@Override
	public String getLabel() {
		return this.propertiesDefinition.getLabel();
	}

	@Override
	public int getPriority() {
		return this.propertiesDefinition.getPriority();
	}


}
