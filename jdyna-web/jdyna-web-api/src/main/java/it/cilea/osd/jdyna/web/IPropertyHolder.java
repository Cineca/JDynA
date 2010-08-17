package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.Identifiable;

import java.util.List;


public interface IPropertyHolder extends Identifiable, Comparable<IPropertyHolder> {
	
	public String getTitle();
	
	public int getPriority();
	
	public List<IContainable> getMaschera();
		
	public void setMaschera(List<IContainable> mascherate);
}
