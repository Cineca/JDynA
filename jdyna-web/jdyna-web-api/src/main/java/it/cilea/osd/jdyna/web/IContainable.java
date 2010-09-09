package it.cilea.osd.jdyna.web;




public interface IContainable { 

	public String getShortName();
	public Integer getId();
	public Integer getAccessLevel();
	public String getLabel();
	public boolean getRepeatable();
	public Object getObject();
	
}
