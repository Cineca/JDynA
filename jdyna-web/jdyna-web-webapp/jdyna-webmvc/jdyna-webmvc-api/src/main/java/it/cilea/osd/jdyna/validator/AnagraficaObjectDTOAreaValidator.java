package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.dto.AnagraficaObjectAreaDTO;
import it.cilea.osd.jdyna.dto.AnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.web.Containable;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.web.Tab;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

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
				tipologieDaValidare.addAll(applicationService.<I, T>findContainableInPropertyHolder(clazzPropertyHolder, iph.getId()));
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
