package it.cilea.osd.jdyna.validator;

import it.cilea.osd.common.validation.BaseValidator;
import it.cilea.osd.jdyna.service.IValidatorDynaService;

public abstract class JDynaBaseValidator extends BaseValidator {
	private IValidatorDynaService validatorService;

	public JDynaBaseValidator(IValidatorDynaService validatorService) {
		this.setValidatorService(validatorService);
	}

	public void setValidatorService(IValidatorDynaService validatorService) {
		this.validatorService = validatorService;
	}

	public IValidatorDynaService getValidatorService() {
		return validatorService;
	}

}
