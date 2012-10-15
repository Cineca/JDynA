package it.cilea.osd.jdyna.dao;

import java.util.List;

import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.jdyna.model.ANestedObject;
import it.cilea.osd.jdyna.model.ANestedObjectWithTypeSupport;
import it.cilea.osd.jdyna.model.ANestedPropertiesDefinition;
import it.cilea.osd.jdyna.model.ANestedProperty;
import it.cilea.osd.jdyna.model.ATipologia;
import it.cilea.osd.jdyna.model.ATypeNestedObject;

public interface NestedObjectDAO<ANO extends ANestedObject<NP, NTP>, NP extends ANestedProperty<NTP>, NTP extends ANestedPropertiesDefinition, TTP extends ATypeNestedObject<NTP>> extends TypeDaoSupport<TTP, NTP>, PaginableObjectDao<ANO, Integer>
{

    List<ANO> findNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID);

    List<ANO> findNestedObjectsByParentIDAndTypoShortname(Integer dynamicFieldID,
            String typoShortname);
    
    public List<ANO> paginateNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID, String sort, boolean inverse, int firstResult,
            int maxResults);

    long countNestedObjectsByParentIDAndTypoID(Integer dynamicFieldID,
            Integer typoID);

    List<ANO> findNestedObjectsByTypoID(Integer typeId);

    void deleteNestedObjectsByTypoID(Integer typeId);    
}
