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
