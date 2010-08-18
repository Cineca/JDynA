package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.Identifiable;



public abstract class IContainable implements Identifiable, Comparable<IContainable> { 

	public abstract String getShortName();
	
}
