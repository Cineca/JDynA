package it.cilea.osd.jdyna.web;

public interface IContainable extends Comparable<IContainable> { 

	public String getShortName();
	public Integer getId();
	public Integer getAccessLevel();
	public String getLabel();
	public boolean getRepeatable();
	public Object getObject();
	public int getPriority();
}
