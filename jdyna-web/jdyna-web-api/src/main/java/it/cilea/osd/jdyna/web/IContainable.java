package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.Identifiable;



public interface IContainable extends Identifiable, Comparable<IContainable> { 
	public String getShortName();
}
