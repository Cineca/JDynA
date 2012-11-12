/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 * 
 *  Copyright (c) 2008, CILEA and third-party contributors as
 *  indicated by the @author tags or express copyright attribution
 *  statements applied by the authors.  All third-party contributions are
 *  distributed under license by CILEA.
 * 
 *  This copyrighted material is made available to anyone wishing to use, modify,
 *  copy, or redistribute it subject to the terms and conditions of the GNU
 *  Lesser General Public License v3 or any later version, as published 
 *  by the Free Software Foundation, Inc. <http://fsf.org/>.
 * 
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 *  for more details.
 * 
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this distribution; if not, write to:
 *   Free Software Foundation, Inc.
 *   51 Franklin Street, Fifth Floor
 *   Boston, MA  02110-1301  USA
 */
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
			setValue(applicationService.findPropertiesDefinitionByShortName(clazz,text));
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
