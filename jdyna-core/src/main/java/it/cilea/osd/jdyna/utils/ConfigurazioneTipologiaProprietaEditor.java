package it.cilea.osd.jdyna.utils;

import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ConfigurazioneTipologiaProprietaEditor<TP extends PropertiesDefinition> extends java.beans.PropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceDynaService applicationService;
	
	/** Model Class */
	private Class<TP> clazz;
	
	/** The logger */
	private final static Log log = LogFactory.getLog(ConfigurazioneTipologiaProprietaEditor.class);

	public ConfigurazioneTipologiaProprietaEditor(Class<TP> model) {
		clazz = model;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("chiamato ConfigurazioneTipologiaProprietaEditor - setAsText text: "+text);
		if (text == null || text.trim().equals(""))
		{
			setValue(null);
		}
		else
		{
			setValue(applicationService.findTipologiaProprietaByShortName(clazz,text));
		}
	}

	@Override
	public String getAsText() {
		log.debug("chiamato ConfigurazioneTipologiaProprietaEditor - getAsText");
		PropertiesDefinition valore = (PropertiesDefinition) getValue();		
		return valore==null?"":valore.getShortName();
	}

	public void setApplicationService(IPersistenceDynaService applicationService) {
		this.applicationService = applicationService;
	}

}
