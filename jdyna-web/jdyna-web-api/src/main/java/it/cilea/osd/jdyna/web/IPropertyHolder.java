package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.Identifiable;

import java.util.List;


public interface IPropertyHolder<C extends Containable> extends Identifiable {
	
	public String getTitle();
		
	public String getShortName();
	
	public int getPriority();
	
	public List<C> getMask();
		
	public void setMask(List<C> mask);
}
