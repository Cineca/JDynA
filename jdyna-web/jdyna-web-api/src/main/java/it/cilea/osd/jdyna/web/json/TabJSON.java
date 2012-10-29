package it.cilea.osd.jdyna.web.json;

import java.util.LinkedList;
import java.util.List;

public class TabJSON {
	
    private int id;
	private String shortName;
	private String title;
	private List<BoxJSON> boxes;
	
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
	public List<BoxJSON> getBoxes() {
	    if(boxes==null) {
	        this.boxes = new LinkedList<BoxJSON>();
	    }
		return boxes;
	}
	public void setBoxes(List<BoxJSON> boxes) {
		this.boxes = boxes;
	}
	
}
