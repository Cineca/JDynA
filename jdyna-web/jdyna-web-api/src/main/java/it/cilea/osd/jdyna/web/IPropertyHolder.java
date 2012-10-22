package it.cilea.osd.jdyna.web;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.model.IContainable;

import java.util.List;


public interface IPropertyHolder<C extends IContainable> extends Identifiable, Comparable<IPropertyHolder<C>> {
	
	public String getTitle();
		
	public String getShortName();
	
	public int getPriority();
	
	public List<C> getMask();
		
	public void setMask(List<C> mask);
	
	public boolean isCollapsed();
	public void setCollapsed(boolean collapsed);
}
