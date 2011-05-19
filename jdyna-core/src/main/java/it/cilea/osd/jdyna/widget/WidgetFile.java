package it.cilea.osd.jdyna.widget;

import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.value.EmbeddedFile;
import it.cilea.osd.jdyna.value.FileValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.persistence.Transient;

public abstract class WidgetFile extends AWidget {

	@Transient
	private String triview = "file";
	
	@Override
	public String getTriview() {
		return triview;
	}

	public abstract void remove(EmbeddedFile fileRP);
	public abstract EmbeddedFile load(String rp, File itemCV) throws IOException, FileNotFoundException;
}
