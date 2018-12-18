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
package it.cilea.osd.jdyna.editor;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.common.service.IPersistenceService;
import it.cilea.osd.jdyna.service.IAutoCreateApplicationService;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ModelPropertyEditor extends AdvancedPropertyEditorSupport {
	/** My gateway to business logic */
	private IPersistenceService applicationService;
	
	/** Model Class */
	private Class clazz;
	
	/** The logger */
	private final static Log log = LogFactory.getLog(ModelPropertyEditor.class);

	public ModelPropertyEditor(Class model) {
		clazz = model;
	}

	@Override
	public void setAsText(String text) throws IllegalArgumentException {
		log.debug("chiamato ModelConverter - setAsText text: "+text);
		if (text == null || text.trim().equals(""))
		{
			setValue(null);
		}
		else
		{
			int id = Integer.parseInt(text);
			Object object = applicationService.get(clazz, id);
			if (object == null && applicationService instanceof IAutoCreateApplicationService) {
				Integer newId = ((IAutoCreateApplicationService) applicationService)
						.persistTemporaryPointerCandidate(id);
				if (newId != null) {
					object = applicationService.get(clazz, newId);
				}
			}
			setValue(object);
		}
	}

	@Override
	public String getAsText() {
		log.debug("chiamato ModelConverer - getAsText");
		Identifiable valore = (Identifiable) getValue();		
		return (valore==null || valore.getId() == null)?"":valore.getId().toString();
	}

	public IPersistenceService getApplicationService() {
		return applicationService;
	}

	public void setApplicationService(IPersistenceService applicationService) {
		this.applicationService = applicationService;
	}
}
