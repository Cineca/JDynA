/*
 * JDynA, Dynamic Metadata Management for Java Domain Object
 *
 * Copyright (c) 2008, CILEA and third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by CILEA.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License v3 or any later version, as published 
 * by the Free Software Foundation, Inc. <http://fsf.org/>.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 *
 */
package it.cilea.osd.common.service;

import it.cilea.osd.common.core.HasTimeStampInfo;
import it.cilea.osd.common.core.ITimeStampInfo;
import it.cilea.osd.common.core.SingleTimeStampInfo;
import it.cilea.osd.common.core.TimeStampInfo;
import it.cilea.osd.common.dao.GenericDao;
import it.cilea.osd.common.event.GestoreEventi;
import it.cilea.osd.common.event.IEvent;
import it.cilea.osd.common.event.ISubscriber;
import it.cilea.osd.common.event.JPAEvent;
import it.cilea.osd.common.model.Identifiable;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class PersistenceService implements IPersistenceService, IEventService 
{
	
    private Map<String, GenericDao> modelDaos;
    
    private GestoreEventi gestoreEventi;
      
    private boolean flagGestoreEventi;
    
    protected final Log log = LogFactory.getLog(getClass());
    
	{
		log.debug("inizializzazione classe BASEApplicationService");
	}
	
    public GestoreEventi getGestoreEventi() {
		return gestoreEventi;
	}


	public void setGestoreEventi(GestoreEventi gestoreEventi) {
		this.gestoreEventi = gestoreEventi;		
	}

	
	/**
     * 
     * @param <T>
     * @param modelClass
     * @param transientObject
     * @param operatore
     * @return true se si tratta di un'operazione di creazione, false se si tratta di un aggiornamento
     */
	protected <T extends Identifiable> Boolean recordTimeStampInfo(Class<T> modelClass,
			T transientObject) {
		if (transientObject instanceof HasTimeStampInfo)
        {
			log.debug("Added record TimeStampInfo: modelClass: "+modelClass.getName()+" id: "+ transientObject.getId());
        	HasTimeStampInfo hrt = (HasTimeStampInfo) transientObject;
        	ITimeStampInfo rt = hrt.getTimeStampInfo();
        	if (rt == null) {
				rt = new TimeStampInfo();
			}
        	if (rt.getTimestampCreated() == null) {        		
        		rt.setInfoCreated(getCurrentTimeStampInfo());
        		return true;
        	}
        	else {
        		rt.setInfoLastModified(getCurrentTimeStampInfo());
        		return false;
        	}        	
        }
		return null;
	}
	
	protected SingleTimeStampInfo getCurrentTimeStampInfo() {
		return new SingleTimeStampInfo(new Date());		
	}

	/* (non-Javadoc)
	 * @see it.cilea.osd.common.service.IPersistenceService#get(java.lang.Class, PK)
	 */
	public <T, PK extends Serializable> T get(Class<T> modelClass, PK pkey)
    {
    	GenericDao<T, PK> modelDao = modelDaos.get(modelClass.getName());   
    	log.debug("modelClass:"+modelClass);
    	log.debug("dao:"+modelDao);
        return modelDao.read(pkey);
    }

    
    /* (non-Javadoc)
	 * @see it.cilea.osd.common.service.IPersistenceService#saveOrUpdate(java.lang.Class, T)
	 */
    public <T extends Identifiable> void saveOrUpdate(Class<T> modelClass, T transientObject)
    {
    	final String modelClassName = modelClass.getName();
    	
    	log.debug("saveOrUpdate: "+modelClassName+ " id: "+transientObject.getId());
        GenericDao<T, ?> modelDao = modelDaos.get(modelClassName);    
        Boolean creation = recordTimeStampInfo(modelClass, transientObject);
        modelDao.saveOrUpdate(transientObject);
        log.debug("after saveOrUpdate id: "+transientObject.getId());
        if (flagGestoreEventi && creation!=null) {        	
				gestoreEventi.notifica(JPAEvent.class, new JPAEvent(modelClass
						.getCanonicalName(), transientObject.getId(),
						creation?JPAEvent.CREATE:JPAEvent.UPDATE));
		}
     }

    
    /* (non-Javadoc)
	 * @see it.cilea.osd.common.service.IPersistenceService#merge(T, java.lang.Class)
	 */
    public <T extends Identifiable> T merge(T oggetto,Class<T> classe) {    	
    	log.debug("merge: "+classe.getName()+" -id: "+oggetto.getId());
    	recordTimeStampInfo(classe, oggetto);
    	T result = getDaoByModel(classe).merge(oggetto);
    	log.debug("after merger id: "+oggetto.getId());
    	return result;
    }
    
    /* (non-Javadoc)
	 * @see it.cilea.osd.common.service.IPersistenceService#refresh(T, java.lang.Class)
	 */
    public <T extends Identifiable> T refresh(T oggetto,Class<T> classe) {
    	return getDaoByModel(classe).merge(oggetto);
    }    
    
     /* (non-Javadoc)
	 * @see it.cilea.osd.common.service.IPersistenceService#delete(java.lang.Class, PK)
	 */
    public <P, PK extends Serializable> void delete(Class<P> model, PK pkey)
    {
        GenericDao<P, PK> modelDao = modelDaos.get(model.getName());
        modelDao.delete(modelDao.read(pkey));
        gestoreEventi.notifica(JPAEvent.class,new JPAEvent(model.getCanonicalName(),(Integer) pkey,JPAEvent.DELETE));
    }


    public void setModelDaos(Map<String, GenericDao> modelDaos)
    {
    	log.debug("injection on PersistenceService");
        this.modelDaos = modelDaos;
    }
    
    protected <T> GenericDao<T, ?> getDaoByModel(Class<T> model)
    {    	
       	String nome = model.getName();
       	log.debug("CHIAMATO getDaoByModel "+nome);       
       	return modelDaos.get(nome);
    }

    
    /**
	 * {@inheritDoc}
	 */
	public <E extends IEvent> void addSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass) {
		getGestoreEventi().addSubscriber(subscriber, eventClass);
	}

	/**
	 * {@inheritDoc}
	 */
	public <E extends IEvent> void removeSubscriber(ISubscriber<E> subscriber,
			Class<E> eventClass) {
		getGestoreEventi().removeSubscriber(subscriber, eventClass);
	}
	
	public boolean isFlagGestoreEventi() {
		return flagGestoreEventi;
	}

	public void setFlagGestoreEventi(boolean flagGestoreEventi) {
		this.flagGestoreEventi = flagGestoreEventi;
	}
}