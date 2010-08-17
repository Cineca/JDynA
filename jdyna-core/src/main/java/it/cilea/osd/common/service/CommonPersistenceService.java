package it.cilea.osd.common.service;

import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.dao.IApplicationDao;
import it.cilea.osd.common.dao.PaginableObjectDao;
import it.cilea.osd.common.model.Identifiable;

import java.io.Serializable;
import java.util.List;

public class CommonPersistenceService extends PersistenceService {
	
	protected IApplicationDao applicationDao;


	public void setApplicationDao(IApplicationDao applicationDao) {
		this.applicationDao = applicationDao;
	}

	public IApplicationDao getApplicationDao() {
		return applicationDao;
	}

	
	/**
	 * {@inheritDoc}
	 */
	public <T> long count(Class<T> classe) {
		return ((PaginableObjectDao<T, ?>) getDaoByModel(classe)).count();
	}

	/**
	 * {@inheritDoc}
	 */
	public <PK extends Serializable> boolean exist(Class model, PK id) {
		GenericDao<?, PK> modelDao = getDaoByModel(model);
		if (modelDao.read(id) != null) {
			return true;
		}
		return false;
	}


	/**
	 * {@inheritDoc}
	 */
	public <T> List<T> getList(Class<T> model) {
		PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
		List<T> modelList = modelDao.findAll();
		return modelList;
	}

	/**
	 * {@inheritDoc}
	 */
	public <T> List<T> getPaginateList(Class<T> model, String sort,
			boolean inverse, int page, int maxResults) {
		PaginableObjectDao<T, ? extends Serializable> modelDao = (PaginableObjectDao<T, ? extends Serializable>) getDaoByModel(model);
		List<T> modelList = modelDao.paginate(sort, inverse, (page - 1)
				* maxResults, maxResults);
		return modelList;
	}
	

	/**
	 * {@inheritDoc}
	 */
	public void evict(Identifiable identifiable) {
		applicationDao.evict(identifiable);
	}


}
