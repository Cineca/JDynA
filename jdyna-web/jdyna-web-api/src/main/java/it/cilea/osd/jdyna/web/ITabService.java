package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.service.IPersistenceDynaService;

import java.util.List;

public interface ITabService extends IPersistenceDynaService {

	public <H extends IPropertyHolder> List<H> getPropertyHolderOnCreation(
			Class<H> model);

	public <H extends IPropertyHolder> List<H> findPropertyHolderInTab(
			Integer areaId);

	public <H extends IPropertyHolder> void deletePropertyHolderInTabs(
			H propertyHolder);

}
