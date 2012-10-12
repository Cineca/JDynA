package it.cilea.osd.jdyna.editor;

import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.value.EmbeddedFile;
import it.cilea.osd.jdyna.widget.WidgetFile;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.multipart.MultipartFile;

public class FilePropertyEditor<WF extends WidgetFile> extends
		java.beans.PropertyEditorSupport {

	/** The logger */
	private final static Log log = LogFactory.getLog(FilePropertyEditor.class);

	private WF widgetFile;

	private String externalAuthority;
	private String internalAuthority;

	public FilePropertyEditor(WF widgetFile) {
		this.widgetFile = widgetFile;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("FilePropertyEditor - setAsText: " + text);
		// text: 'description|||link'
		if (StringUtils.isEmpty(text)) {
			setValue(null);
		} else {
			String[] splitted = text.split("\\|\\|\\|");
			if (splitted.length < 7) {
			    setValue(null);			    
				log.error(
						"Invalid text string for file: " + text);
				return;
			}
			EmbeddedFile file = new EmbeddedFile();

			file.setValueFile(splitted.length > 3 ? splitted[3] : null);
			file.setExtFile(splitted.length > 4 ? splitted[4] : null);
			file.setMimeFile(splitted.length > 5 ? splitted[5] : null);			
			file.setFolderFile(widgetFile
						.getCustomFolderByAuthority(getExternalAuthority(),getInternalAuthority()));			
			if (splitted.length == 8 && Boolean.parseBoolean(splitted[7])) {
				widgetFile.remove(file);
				setValue(null);
			} else {
				setValue(file);
			}
		}
	}

	@Override
	public String getAsText() {
		log.debug("send call to FileConverter - getAsText");

		EmbeddedFile valore = (EmbeddedFile) getValue();

		if (valore == null)
			return "";
		log.debug("isDeletable|||basePath|||servletPath|||valueFile|||ext|||mime|||folderFile");
		log.debug("isDeletable = " + valore.getValueFile() != null ? true
				: false);
		log.debug("basePath = " + widgetFile.getBasePath());
		log.debug("servletPath = " + widgetFile.getServletPath());
		log.debug("valueFile = " + valore.getValueFile());
		log.debug("ext = " + valore.getExtFile());
		log.debug("mime = " + valore.getMimeFile());
		log.debug("folderFile = " + valore.getFolderFile());

		return valore == null ? "false|||"
				: (valore.getValueFile() != null ? true : false)
						+ "|||"
						+ widgetFile.getBasePath()
						+ "|||"
						+ widgetFile.getServletPath()
						+ "|||"
						+ (valore.getValueFile() != null ? valore
								.getValueFile() : "")
						+ "|||"
						+ (valore.getExtFile() != null ? valore.getExtFile()
								: "")
						+ "|||"
						+ (valore.getMimeFile() != null ? valore.getMimeFile()
								: "")
						+ "|||"
						+ (valore.getFolderFile() != null ? valore
								.getFolderFile() : "");
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof MultipartFile) {
			MultipartFile multipart = (MultipartFile) value;
			if (multipart.getSize() > 0) {
				try {
					EmbeddedFile file = widgetFile.load(
							multipart.getInputStream(),
							multipart.getOriginalFilename(),
							multipart.getContentType(), getExternalAuthority(), getInternalAuthority());
					super.setValue(new ValoreDTO(file));
				} catch (IOException ex) {
					log.warn("Cannot read contents of multipart file", ex);
					throw new IllegalArgumentException(
							"Cannot read contents of multipart file: "
									+ ex.getMessage());

				}
			} else {
				setValue(null);
			}
		} else {
			super.setValue(value);
		}
	}

	public void setExternalAuthority(String externalAuthority) {
		this.externalAuthority = externalAuthority;
	}

	public String getExternalAuthority() {
		return externalAuthority;
	}

	public void setInternalAuthority(String internalAuthority) {
		this.internalAuthority = internalAuthority;
	}

	public String getInternalAuthority() {
		return internalAuthority;
	}

}
