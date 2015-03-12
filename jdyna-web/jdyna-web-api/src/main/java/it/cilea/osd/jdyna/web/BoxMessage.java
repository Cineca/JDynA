package it.cilea.osd.jdyna.web;

import it.cilea.osd.jdyna.model.Containable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "jdyna_box_message")
public class BoxMessage
{
    /** DB Primary key */
    @Id
    @GeneratedValue(generator = "JDYNA_MESSAGEBOX_SEQ")
    @SequenceGenerator(name = "JDYNA_MESSAGEBOX_SEQ", sequenceName = "JDYNA_MESSAGEBOX_SEQ", allocationSize = 1)
    private Integer id;    
    
    @Column(length=4000)
    private String body;
    
    /** The message will be position after this containable element, if null place at the top */
    @ManyToOne
    private Containable elementAfter;

    private boolean showInPublicView;

    private boolean showInEdit;

    private boolean useBodyAsKeyMessageBundle;

    @OneToOne    
    private Box parent;
    
    public Integer getId()
    {
        return id;
    }
    public void setId(Integer id)
    {
        this.id = id;
    }
    
    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public Containable getElementAfter()
    {
        return elementAfter;
    }

    public void setElementAfter(Containable elementAfter)
    {
        this.elementAfter = elementAfter;
    }

    public boolean isShowInPublicView()
    {
        return showInPublicView;
    }

    public void setShowInPublicView(boolean showInPublicView)
    {
        this.showInPublicView = showInPublicView;
    }

    public boolean isShowInEdit()
    {
        return showInEdit;
    }

    public void setShowInEdit(boolean showInEdite)
    {
        this.showInEdit = showInEdit;
    }

    public boolean isUseBodyAsKeyMessageBundle()
    {
        return useBodyAsKeyMessageBundle;
    }

    public void setUseBodyAsKeyMessageBundle(boolean useBodyAsKeyMessageBundle)
    {
        this.useBodyAsKeyMessageBundle = useBodyAsKeyMessageBundle;
    }
    public void setParent(Box parent)
    {
        this.parent = parent;
    }
    public Box getParent()
    {
        return parent;
    }

}
