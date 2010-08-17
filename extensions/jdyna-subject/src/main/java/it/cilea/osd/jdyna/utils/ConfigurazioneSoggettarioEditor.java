package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.model.Soggettario;
import it.cilea.osd.jdyna.service.IPersistenceSubjectService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigurazioneSoggettarioEditor extends java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceSubjectService applicationService;
	
	/** The logger */
	private final static Log log = LogFactory.getLog(ConfigurazioneSoggettarioEditor.class);

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("chiamato ConfigurazioneSoggettarioEditor - setAsText text: "+text);
		if (text == null || text.trim().equals(""))
		{
			setValue(null);
		}
		else
		{
			setValue(applicationService.uniqueSoggettarioByName(text));
		}
	}

	@Override
	public String getAsText() {
		log.debug("chiamato ConfigurazioneSoggettarioEditor - getAsText");
		Soggettario valore = (Soggettario) getValue();		
		return valore==null?"":valore.getNome();
	}

	public void setApplicationService(IPersistenceSubjectService applicationService) {
		this.applicationService = applicationService;
	}

}
