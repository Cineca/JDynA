package it.cilea.osd.jdyna.utils;

import it.cilea.osd.common.model.Selectable;

public class SelectableDTO implements Selectable
{

    private Integer id;

    private String display;

    public SelectableDTO(Integer id, String display)
    {
        this.id = id;
        this.display = display;
    }

    public SelectableDTO(String id, String display)
    {
        this.id = Integer.valueOf(id);
        this.display = display;
    }

    @Override
    public String getIdentifyingValue()
    {
        return String.valueOf(id);
    }

    @Override
    public String getDisplayValue()
    {
        return display;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public Integer getId()
    {
        return id;
    }

    public void setDisplay(String display)
    {
        this.display = display;
    }

    public String getDisplay()
    {
        return display;
    }

}
