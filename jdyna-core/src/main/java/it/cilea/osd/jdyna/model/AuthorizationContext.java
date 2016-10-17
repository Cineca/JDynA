package it.cilea.osd.jdyna.model;

import java.util.List;

import it.cilea.osd.common.service.IPersistenceService;

public interface AuthorizationContext
{
    public List<String> getAuthorizedSingle();

    public void setAuthorizedSingle(List<String> authorizedSingle);

    public List<String> getAuthorizedGroup();

    public void setAuthorizedGroup(List<String> authorizedGroup);
    
    public <AS extends IPersistenceService> List<String> getMetadataWithPolicySingle(AS tabService);
    
    public <AS extends IPersistenceService> List<String> getMetadataWithPolicyGroup(AS tabService);
    
    public Integer getVisibility();
}
