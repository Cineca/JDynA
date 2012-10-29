package it.cilea.osd.jdyna.web.json;

import java.util.List;

public class BoxJSON {
	private int id;
	private String shortName;
	private String title;
	private boolean collapsed;
	private List<String[]> componentSubLinks;
	private Integer countBoxPublicMetadata;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public boolean isCollapsed() {
		return collapsed;
	}
	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
    
    public List<String[]> getComponentSubLinks()
    {
        return componentSubLinks;
    }
    public void setComponentSubLinks(List<String[]> componentSubLinks)
    {
        this.componentSubLinks = componentSubLinks;
    }
    
    public Integer getCountBoxPublicMetadata()
    {
        return countBoxPublicMetadata;
    }
    public void setCountBoxPublicMetadata(Integer countBoxPublicMetadata)
    {
        this.countBoxPublicMetadata = countBoxPublicMetadata;
    }
}
