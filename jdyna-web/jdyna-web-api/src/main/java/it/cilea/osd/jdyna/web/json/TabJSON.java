package it.cilea.osd.jdyna.web.json;

import java.util.List;

public class TabJSON {
	private int id;
	private String shortName;
	private String title;
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
		return boxes;
	}
	public void setBoxes(List<BoxJSON> boxes) {
		this.boxes = boxes;
	}
	private List<BoxJSON> boxes;
	
	
}
