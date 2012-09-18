package it.cilea.osd.jdyna.dao;

import java.util.List;

import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;

public interface NestedObjectDAO<ANO extends ANestedObject<NP, NTP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition> extends PaginableObjectDao<ANO, Integer>
{

    List<NP> findNestedPropertiesByParentIDAndShortnameTypo(
            Integer dynamicFieldID, String shortNameTypo);

    List<ANO> findNestedObjectsByParentID(Integer id);

    ANO findNestedObjectByParentIDAndShortnameTypo(Integer id,
            String typoShortname);

    ANO findNestedObjectWithTypeSupportByParentIDAndTypoShortname(Integer id,
            String typoShortname);

}
