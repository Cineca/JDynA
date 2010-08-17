package it.cilea.osd.jdyna.dto;

import java.util.List;

public interface IAnagraficaObjectWithMultiTypeDTO extends IAnagraficaObjectDTO {
	public List<Integer> getTipologieId();
	public void setTipologieId(List<Integer> tipologieId);
}
