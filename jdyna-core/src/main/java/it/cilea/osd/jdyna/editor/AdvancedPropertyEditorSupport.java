package it.cilea.osd.jdyna.editor;

import java.beans.PropertyEditorSupport;

public class AdvancedPropertyEditorSupport extends PropertyEditorSupport
{
    public final static String MODE_VIEW = "view";
    public final static String MODE_CSV = "csv";
    public final static String MODE_XML = "xml";
    
    private String mode = MODE_VIEW;

    public String getMode()
    {
        return mode;
    }

    public void setMode(String mode)
    {
        this.mode = mode;
    }
    
    public String getCustomText() {
        return getAsText();
    }
    
}
