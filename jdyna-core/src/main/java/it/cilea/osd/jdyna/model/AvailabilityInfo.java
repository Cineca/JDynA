package it.cilea.osd.jdyna.model;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import it.cilea.osd.common.core.SingleTimeStampInfo;

@Embeddable
public class AvailabilityInfo implements Serializable
{
    @Embedded
    @AttributeOverride(name = "timestamp", column = @Column(name = "startDate"))
    private SingleTimeStampInfo startDate;
    
    @Embedded
    @AttributeOverride(name = "timestamp", column = @Column(name = "endDate"))
    private SingleTimeStampInfo endDate;

    public void setEndDate(SingleTimeStampInfo endDate)
    {
        this.endDate = endDate;
    }

    public SingleTimeStampInfo getEndDate()
    {
        return endDate;
    }

    public SingleTimeStampInfo getStartDate()
    {
        return startDate;
    }

    public void setStartDate(SingleTimeStampInfo startDate)
    {
        this.startDate = startDate;
    }
}
