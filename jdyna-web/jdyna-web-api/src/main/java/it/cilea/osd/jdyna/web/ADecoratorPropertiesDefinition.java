package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance (strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class ADecoratorPropertiesDefinition<TP extends PropertiesDefinition> implements IPropertiesDefinition, IContainable {

	@Id
	private TP id;

	@Override
	public Class getAnagraficaHolderClass() {
		return id.getAnagraficaHolderClass();
	}

	@Override
	public Class getPropertyHolderClass() {
		return id.getPropertyHolderClass();
	}

	@Override
	public Integer getId() {
		return id.getId();
	}


	public int compareTo(IContainable o) {		
		if (o == null) return -1;
		if (getId() < o.getId()) return 1;
		return -1;
	}

	@Override
	public AWidget getRendering() {
		return this.id.getRendering();
	}

	@Override
	public String getShortName() {
		return this.id.getShortName();
	}

	@Override
	public boolean isMandatory() {
		return this.id.isMandatory();
	}

	@Override
	public String getLabel() {
		return this.id.getLabel();
	}

	@Override
	public int getPriority() {
		return this.id.getPriority();
	}


}
