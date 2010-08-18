package it.cilea.osd.jdyna.model;


public interface IPropertiesDefinition {
	
	public Class getAnagraficaHolderClass();
	
	public Class getPropertyHolderClass();
	
	public AWidget getRendering();
	
	public String getShortName();
	
	public boolean isMandatory();
	
	public String getLabel();
	
	public int getPriority();
}
