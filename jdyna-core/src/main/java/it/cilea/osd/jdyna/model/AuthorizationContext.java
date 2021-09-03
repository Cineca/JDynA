package it.cilea.osd.jdyna.model;

import java.util.List;

import it.cilea.osd.common.service.IPersistenceService;

public interface AuthorizationContext
{
    public <PD extends PropertiesDefinition> List<PD> getAuthorizedSingle();

    public <PD extends PropertiesDefinition> List<PD> getAuthorizedGroup();
    
    public Integer getVisibility();
}
