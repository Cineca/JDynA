package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.Identifiable;

import java.util.List;


public interface IPropertyHolder extends Identifiable {
	
	public String getTitle();
		
	public List<IContainable> getMask();
		
	public void setMaschera(List<IContainable> mask);
}
