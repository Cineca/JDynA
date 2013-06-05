package it.cilea.osd.jdyna.model;

import it.cilea.osd.common.model.IdentifiableObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "jdyna_scopedefinition")
public class ScopeDefinition extends IdentifiableObject
{
    @Id
    @GeneratedValue(generator = "JDYNA_SCOPEDEF_SEQ")
    @SequenceGenerator(name = "JDYNA_SCOPEDEF_SEQ", sequenceName = "JDYNA_SCOPEDEF_SEQ")
    private Integer id;
    
    private String label;

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }
    
    
}
