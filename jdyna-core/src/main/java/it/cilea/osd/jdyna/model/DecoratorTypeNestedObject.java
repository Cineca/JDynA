package it.cilea.osd.jdyna.model;

import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

public class DecoratorTypeNestedObject<PD extends ANestedPropertiesDefinition> extends ADecoratorTypeDefinition<ATypeNestedObject<PD>, PD>
{

    @OneToOne(optional=true)
    @JoinColumn(name="typenestedobject_fk")
    @Cascade(value = {CascadeType.ALL,CascadeType.DELETE_ORPHAN})
    private TypeNestedObject real;
    
    @Override
    public String getShortName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Integer getAccessLevel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getLabel()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getRepeatable()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public int getPriority()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int compareTo(IContainable o)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public void setReal(ATypeNestedObject<PD> object)
    {
        // TODO Auto-generated method stub
        
    }

    @Override
    public ATypeNestedObject<PD> getObject()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
