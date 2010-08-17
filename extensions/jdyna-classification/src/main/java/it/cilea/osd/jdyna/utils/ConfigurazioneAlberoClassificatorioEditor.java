package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.model.AlberoClassificatorio;
import it.cilea.osd.jdyna.service.IPersistenceClassificationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigurazioneAlberoClassificatorioEditor extends java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceClassificationService applicationService;
	
	/** The logger */
	private final static Log log = LogFactory.getLog(ConfigurazioneAlberoClassificatorioEditor.class);

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("chiamato ConfigurazioneAlberoClassificatorioEditor - setAsText text: "+text);
		if (text == null || text.trim().equals(""))
		{
			setValue(null);
		}
		else
		{
			setValue(applicationService.getAlberoByName(text));
		}
	}

	@Override
	public String getAsText() {
		log.debug("chiamato ConfigurazioneAlberoClassificatorioEditor - getAsText");
		AlberoClassificatorio valore = (AlberoClassificatorio) getValue();		
		return valore==null?"":valore.getNome();
	}

	public void setApplicationService(IPersistenceClassificationService applicationService) {
		this.applicationService = applicationService;
	}

}
