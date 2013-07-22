package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.AType;
import it.cilea.osd.jdyna.model.Containable;
import it.cilea.osd.jdyna.model.PropertiesDefinition;

public interface ITypedTab<H extends IPropertyHolder<Containable>, A extends AType<PD>, PD extends PropertiesDefinition>
{
    public abstract A getTypeDef();
    public abstract void setTypeDef(A typo);
    
}
