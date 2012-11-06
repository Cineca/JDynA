package it.cilea.osd.jdyna.components;

import java.util.List;

public interface IBeanComponent
{
    String getComponentIdentifier();
    
    int getEtal();
    
    int getRpp();
    
    String getOrder();
    
    int getSortby();
    
    List<String> getFilters();
    
    String getQuery();
}
