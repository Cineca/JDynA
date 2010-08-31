package it.cilea.osd.jdyna.validator;

import it.cilea.osd.jdyna.dto.AnagraficaObjectAreaDTO;
import it.cilea.osd.jdyna.dto.AnagraficaObjectDTO;
import it.cilea.osd.jdyna.dto.ValoreDTO;
import it.cilea.osd.jdyna.model.AWidget;
import it.cilea.osd.jdyna.model.AnagraficaObject;
import it.cilea.osd.jdyna.model.IPropertiesDefinition;
import it.cilea.osd.jdyna.model.PropertiesDefinition;
import it.cilea.osd.jdyna.model.Property;
import it.cilea.osd.jdyna.util.AnagraficaUtils;
import it.cilea.osd.jdyna.util.ValidationMessage;
import it.cilea.osd.jdyna.web.ADecoratorPropertiesDefinition;
import it.cilea.osd.jdyna.web.IContainable;
import it.cilea.osd.jdyna.web.IPropertyHolder;
import it.cilea.osd.jdyna.web.ITabService;
import it.cilea.osd.jdyna.widget.WidgetCombo;

import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class AnagraficaObjectDTOAreaValidator<P extends Property<TP>, TP extends PropertiesDefinition, I extends IPropertyHolder, EO extends AnagraficaObject<P, TP>>
		implements Validator {

	protected ITabService applicationService;

	protected Class<TP> clazzTipologiaProprieta;

	protected Class<EO> clazzAnagraficaObject;

	protected Class<I> clazzPropertyHolder;

	public void setApplicationService(ITabService applicationService) {
		this.applicationService = applicationService;
	}

	public boolean supports(Class arg0) {
		return AnagraficaObjectAreaDTO.class.isAssignableFrom(arg0);
	}

	public void validate(Object commandObject, Errors errors) {
		AnagraficaObjectAreaDTO dto = (AnagraficaObjectAreaDTO) commandObject;
		List<IContainable> tipologieDaValidare;
		if (dto.getObjectId() != null) { // edit
			AnagraficaObject<P, TP> epiObject = applicationService.get(
					clazzAnagraficaObject, dto.getObjectId());
			tipologieDaValidare = applicationService.findContainableInPropertyHolder(clazzPropertyHolder, dto.getAreaId());
		} else { // creation
			tipologieDaValidare = ((ITabService)applicationService)
					.getContainableOnCreation();
		}
		validate(dto, errors, tipologieDaValidare, "");
	}

	public void validate(AnagraficaObjectDTO dto, Errors errors,
			List<IContainable> tipologieDaValidare, String propertyPathPrefix) {
		for (IContainable t : tipologieDaValidare) {
			IPropertiesDefinition tipologia = (ADecoratorPropertiesDefinition)t;
			AWidget widget = tipologia.getRendering();

			int sizeDTO = 0;
			int idx = 0;
			if (dto.getAnagraficaProperties().get(
					tipologia.getShortName()) != null) {
				for (ValoreDTO valoreDTO : dto.getAnagraficaProperties().get(
						tipologia.getShortName())) {

					if (widget instanceof WidgetCombo) {
						AnagraficaObjectDTO valore = valoreDTO != null ? ((AnagraficaObjectDTO) valoreDTO
								.getObject())
								: null;

						final WidgetCombo combo = (WidgetCombo) widget;
						final List sottoTipologie = combo.getSottoTipologie();
						if (valore != null
								&& !AnagraficaUtils.checkIsAllNull(valore,
										sottoTipologie)) {
							sizeDTO++;

							validate(valore, errors, sottoTipologie,
									propertyPathPrefix
											+ "anagraficaProperties["
											+ tipologia.getShortName() + "]["
											+ idx + "].object.");
						}
					} else {
						Object valore = valoreDTO == null ? null : valoreDTO
								.getObject();
						if (valore != null) {

							sizeDTO++;

							ValidationMessage errorMsg = widget.valida(valore);
							if (errorMsg != null) {
								errors.rejectValue(propertyPathPrefix
										+ "anagraficaProperties["
										+ tipologia.getShortName() + "][" + idx
										+ "]", errorMsg.getI18nKey(), errorMsg
										.getParameters(), tipologia.getLabel());
							}
						}
					}
					idx++;
				}

				if (sizeDTO == 0 && tipologia.isMandatory()) {
					errors.rejectValue(propertyPathPrefix
							+ "anagraficaProperties["
							+ tipologia.getShortName() + "][0]",
							"field.required", tipologia.getLabel());
				}
			}
		}
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
}
