package it.cilea.osd.common.core;


public interface ITimeStampInfo {
	public void setInfoCreated(SingleTimeStampInfo timestamp);
	public void setInfoLastModified(SingleTimeStampInfo timestamp);
	public SingleTimeStampInfo getTimestampCreated();
	public SingleTimeStampInfo getTimestampLastModified();
}
