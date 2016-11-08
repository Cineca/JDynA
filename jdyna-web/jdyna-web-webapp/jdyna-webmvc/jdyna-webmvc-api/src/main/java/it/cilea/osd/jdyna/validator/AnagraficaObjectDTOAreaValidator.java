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
package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.dto.AnagraficaObjectAreaDTO;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.IContainable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;

public class AnagraficaObjectDTOAreaValidator<P extends Property<TP>, TP extends PropertiesDefinition, I extends IPropertyHolder<Containable>, T extends Tab<I>, EO extends AnagraficaObject<P, TP>>
		extends AnagraficaObjectDTOValidator<P, TP, I, EO> {

	private static Log log = LogFactory.getLog(AnagraficaObjectDTOAreaValidator.class);
	
	protected ITabService applicationService;

	protected Class<TP> clazzTipologiaProprieta;

	protected Class<EO> clazzAnagraficaObject;

	protected Class<I> clazzPropertyHolder;
	
	protected Class<T> clazzTab;

	public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}

	public boolean supports(Class arg0) {
		return AnagraficaObjectAreaDTO.class.isAssignableFrom(arg0);
	}

	public void validate(Object commandObject, Errors errors) {
		AnagraficaObjectAreaDTO dto = (AnagraficaObjectAreaDTO) commandObject;
		List<IContainable> tipologieDaValidare = new LinkedList<IContainable>();
		if (dto.getObjectId() != null) { // edit
			List<I> propertyHolders = applicationService.findPropertyHolderInTab(clazzTab, dto
					.getTabId());
			for(I iph : propertyHolders) {
				tipologieDaValidare.addAll(applicationService.<I, T, TP>findContainableInPropertyHolder(clazzPropertyHolder, iph.getId()));
			}
						
		} else { // creation
			
			tipologieDaValidare = ((ITabService)applicationService)
					.getContainableOnCreation(clazzTipologiaProprieta);
		}
		
		List<TP> realTPS = new LinkedList<TP>();
		
		for (IContainable c : tipologieDaValidare) {
			TP rpPd = applicationService
					.findPropertiesDefinitionByShortName(
							clazzTipologiaProprieta, c.getShortName());
			if (rpPd != null) {
				realTPS.add(rpPd);
			}
		}
		validate(dto, errors, realTPS, "");
	}


	public void setClazzTipologiaProprieta(Class<TP> clazzTipologiaProprieta) {
		this.clazzTipologiaProprieta = clazzTipologiaProprieta;
	}
	

	public void setClazzAnagraficaObject(Class<EO> clazzAnagraficaObject) {
		this.clazzAnagraficaObject = clazzAnagraficaObject;
	}

	public void setClazzPropertyHolder(Class<I> clazzPropertyHolder) {
		this.clazzPropertyHolder = clazzPropertyHolder;
	}
	
	public void setClazzTab(Class<T> clazzTab) {
		this.clazzTab = clazzTab;
	}
}
