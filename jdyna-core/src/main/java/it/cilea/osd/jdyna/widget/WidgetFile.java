package it.cilea.osd.jdyna.widget;

import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.value.EmbeddedFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.WildcardFilter;
import org.hibernate.annotations.Type;

@Entity
public abstract class WidgetFile extends AWidget {

	@Transient
	private String triview = "file";
		
	private boolean showPreview = false;
	
	private String labelAnchor;
	
	@Type(type="text")
	private String fileDescription;
	
	/**
	 * Percent size (this value is default for input size either link description and value)
	 */
	private int size = 40;	
	

	/**
	 * 
	 * Remove file on server by researcher get from parameter method.
	 * 
	 * @param fileRP
	 * @param researcher
	 */
	public void remove(EmbeddedFile fileRP) {		
		File directory = new File(getBasePath() + File.separatorChar
				+ fileRP.getFolderFile());

		if (directory.exists()) {
			String fileName = fileRP.getValueFile();
			if(fileRP.getExtFile()!=null && !fileRP.getExtFile().isEmpty()) {
				fileName += ("."	+ fileRP.getExtFile());
			}
			Collection<File> files = FileUtils.listFiles(directory,
					new WildcardFilter(fileName), null);

			for (File file : files) {
				file.delete();
			}
		}
	}

	/**
	 * Load file and copy to default directory.
	 * 
	 * @param file
	 */	
	public EmbeddedFile load(InputStream stream, String originalFilename, String contentType, String extAuthority, String intAuthority) throws IOException, FileNotFoundException {
		EmbeddedFile result = new EmbeddedFile();
		String folder = intAuthority;
		if(extAuthority!=null && extAuthority.length()>0) {
			folder = getCustomFolderByAuthority(extAuthority, intAuthority);
		}
		String fileName = "";
		String pathCV = getBasePath();
		String ext = null;
		File dir = new File(pathCV + File.separatorChar + folder);
		dir.mkdirs();
		File file = null;
		if(originalFilename.lastIndexOf(".")==-1) {			
			fileName = originalFilename;
			file = new File(dir, fileName);
		}
		else {
			fileName = originalFilename.substring(0,
		        originalFilename.lastIndexOf("."));
			ext = originalFilename.substring(
			        originalFilename.lastIndexOf(".") + 1);
			file = new File(dir, fileName +"."+ ext);
		}					
		
		int i = 1;
		while(!file.createNewFile()) {
			fileName = fileName +"_" + i;
			file = new File(dir, fileName + ((ext!=null)?("."+ ext):""));
			i++;
		}
		
		FileOutputStream out = new FileOutputStream(file);
		it.cilea.osd.common.util.Utils.bufferedCopy(stream, out);
		out.close();
		result.setValueFile(fileName);
		result.setExtFile(ext);
		result.setMimeFile(contentType);
		result.setFolderFile(folder);
		return result;
	}


	public abstract String getBasePath();	
	public abstract String getServletPath();
	public abstract String getCustomFolderByAuthority(String extAuthority, String intAuthority);

	@Override
	public String getTriview() {
		return triview;
	}

	
	public boolean isShowPreview() {
		return showPreview;
	}

	public void setShowPreview(boolean showPreview) {
		this.showPreview = showPreview;
	}

	public void setLabelAnchor(String labelAnchor) {
		this.labelAnchor = labelAnchor;
	}

	public String getLabelAnchor() {
		return labelAnchor;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setFileDescription(String fileDescription) {
		this.fileDescription = fileDescription;
	}

	public String getFileDescription() {
		return fileDescription;
	}
}
