package it.cilea.osd.jdyna.service;

import it.cilea.osd.common.model.Identifiable;
import it.cilea.osd.jdyna.dto.DTOAutocomplete;
import it.cilea.osd.jdyna.utils.CustomAnalyzer;
import it.cilea.osd.jdyna.utils.Utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.Token;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FilteredQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.TermQuery;
import org.hibernate.Session;
import org.hibernate.search.FullTextQuery;
import org.hibernate.search.FullTextSession;
import org.hibernate.search.ProjectionConstants;
import org.hibernate.search.Search;
import org.springframework.orm.hibernate3.HibernateTransactionManager;

public class SearchService implements ISearchDynaService {
 
	
	protected Log log = LogFactory.getLog(getClass());
	
	private CustomAnalyzer customAnalyzer;
	private HibernateTransactionManager hibernateTransactionManager;
	
	public CustomAnalyzer getCustomAnalyzer() {
		return customAnalyzer;
	}

	public HibernateTransactionManager getHibernateTransactionManager() {
		return hibernateTransactionManager;
	}

	public void setCustomAnalyzer(CustomAnalyzer customAnalyzer) {
		this.customAnalyzer = customAnalyzer;
	}

	public void setHibernateTransactionManager(
			HibernateTransactionManager hibernateTransactionManager) {
		this.hibernateTransactionManager = hibernateTransactionManager;
	}
	
	/**
	 *  {@inheritDoc}
	 **/
	public <T> List<T> search(String field, String query, Class<T> model) throws ParseException{
	
		
		Session session = getHibernateTransactionManager().getSessionFactory().getCurrentSession();
		
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		//Transaction tx = fullTextSession.beginTransaction();
		
	//	QueryParser parser = new QueryParser(field, new StandardAnalyzer());
	
		QueryParser parser = new QueryParser(field, getCustomAnalyzer().getAnalyzer(model));
		
		Query luceneQuery = parser.parse(query);
				
		FullTextQuery fullTextQuery = null;
		//Se si deve effettuare la ricerca solo su un indice (ad esempio solo quello "Opere")
		if (model != null) 
		    fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery, model);
		else 
			//oppure su tutti gli indici
			fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
		
	
		
		List result = fullTextQuery.list();
		
		log.debug("La query che si vuole eseguire è: "+fullTextQuery.getQueryString());
		//index are written at commit time..ma essendo ora all'interno della transazionalità sotto spring il commit avviene in automatico
		//tx.commit(); 
		
		//FIXME Chiudere la sessione!!!
		//session.close();
		
		return result;
		
	}
	
	/**
	 *  {@inheritDoc}
	 **/
	public <T> List<T> search(String field, String query, Class<T> model, String sort, String dir, int page, int maxResults) throws ParseException{
	
		
		Session session = getHibernateTransactionManager().getSessionFactory().getCurrentSession();
		
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		//Transaction tx = fullTextSession.beginTransaction();
		
		QueryParser parser = new QueryParser(field, getCustomAnalyzer().getAnalyzer(model));
		//QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		
		Query luceneQuery = parser.parse(query);
		
		QueryWrapperFilter a=new QueryWrapperFilter(luceneQuery);

		FullTextQuery fullTextQuery = null;
		
		//Se si deve effettuare la ricerca solo su un indice (ad esempio solo quello "Opere")
		if (model != null) 
		    fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery, model);
		else 
			//oppure su tutti gli indici
			fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);

		boolean reverse = (dir != null && dir.equals("asc"))?true:false;
		int offset = (page-1)*maxResults;
		fullTextQuery.setFirstResult(offset);
		fullTextQuery.setMaxResults(maxResults);
		if (sort != null && !sort.equals("score"))
		{
			if (sort.equals("id"))
			{
				fullTextQuery.setSort(new Sort(new SortField(sort, SortField.INT, reverse)));
			}
			else
			{
				fullTextQuery.setSort(new Sort(new SortField(sort+"_sort", SortField.STRING, reverse)));	
			}			
		}
		
		//countResult = fullTextQuery.getResultSize();

		
		List result = fullTextQuery.list();
		
		if (log.isDebugEnabled())
		{
			log.debug("La query che si vuole eseguire è: "+fullTextQuery.getQueryString());
		}
		//index are written at commit time..ma essendo ora all'interno della transazionalità sotto spring il commit avviene in automatico
		//tx.commit(); 
		
		//FIXME Chiudere la sessione!!!
		//session.close();
		
		return result;
		
	}
	
	/**
	 *  {@inheritDoc}
	 **/
	public List<DTOAutocomplete> searchWithReturnDtoAutocomplete(String filtro, String query, Class model,String display) throws ParseException, IOException{
			
		Session session = hibernateTransactionManager.getSessionFactory().getCurrentSession();
		
		//List<Identifiable> result = searchWithBuildQuery(null, filtro, query, model);
		List<Identifiable> result = searchWithFiltro(null, filtro, query, model);
		
		// new list
		List<DTOAutocomplete> lista = new LinkedList<DTOAutocomplete>();
		
		//for results
		for(Identifiable res : result) {
			DTOAutocomplete element = new DTOAutocomplete();
			element.setId(res.getId().toString());
			if(display!=null) {
				String htmlString = Utils.calcoloValore(display, res, null,0).toString();
				String noHTMLString = htmlString.replaceAll("\\<.*?>","");
				element.setDisplay(noHTMLString);
			}
			lista.add(element);
		}
		//FIXME Chiudere la sessione!!!
		//session.close();
		
		return lista;
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public <T> List<T> searchWithFiltro(String field, String filtro, String prefix, Class<T> model) throws ParseException, IOException{
	
		
		Session session = hibernateTransactionManager.getSessionFactory().getCurrentSession();
		
		String myField = field == null?"default":field;
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		
		//String prefixLowerCase=prefix.toLowerCase();
		
		Tokenizer tokenizer = new StandardTokenizer(new StringReader(prefix));
		Token token = tokenizer.next();
		BooleanQuery luceneQuery = new BooleanQuery();
		while (token != null){
			Token nextToken = tokenizer.next();
			if (nextToken != null){
				Query tmpQuery = new TermQuery(new Term(myField, new String(token
						.termBuffer(), 0, token.termLength())));
				token = nextToken;
				luceneQuery.add(tmpQuery, BooleanClause.Occur.MUST);
			}
			else {
				break;
			}
		}
		
		luceneQuery.add(new PrefixQuery(new Term(myField, new String(token
				.termBuffer(), 0, token.termLength()))),BooleanClause.Occur.MUST);
		Query finalQuery;
	
		if (filtro == null || filtro.trim().length()==0 || filtro.equals("null"))
		{
			finalQuery = luceneQuery;
		}
		else
		{
			QueryParser parser = new QueryParser(myField, new StandardAnalyzer());
			finalQuery = new FilteredQuery(luceneQuery,new QueryWrapperFilter(parser.parse(filtro)));
		}
				
		FullTextQuery fullTextQuery = null;
		//Se si deve effettuare la ricerca solo su un indice (ad esempio solo quello "Opere")
		if (model != null) 
		    fullTextQuery = fullTextSession.createFullTextQuery( finalQuery, model);
		else 
			//oppure su tutti gli indici
			fullTextQuery = fullTextSession.createFullTextQuery(finalQuery);
			
		fullTextQuery.setMaxResults(10);
		List<T> result = fullTextQuery.list();
		
		log.debug("La query che si vuole eseguire è: "+fullTextQuery.getQueryString());
		//index are written at commit time..ma essendo ora all'interno della transazionalità sotto spring il commit avviene in automatico
		//tx.commit(); 
		
		//FIXME Chiudere la sessione!!!
		//session.close();
		
		return result;
		
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	public <T> int count(String field, String query, Class<T> model) throws ParseException{
	
		
		Session session = getHibernateTransactionManager().getSessionFactory().getCurrentSession();
		
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		//Transaction tx = fullTextSession.beginTransaction();
		
		//QueryParser parser = new QueryParser(field, new StandardAnalyzer());
		QueryParser parser = new QueryParser(field,getCustomAnalyzer().getAnalyzer(model));
		//QueryParser parser = new QueryParser(field, new KeywordAnalyzer());
		Query luceneQuery = parser.parse(query);
				
		FullTextQuery fullTextQuery = null;
		//Se si deve effettuare la ricerca solo su un indice (ad esempio solo quello "Opere")
		if (model != null) 
		    fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery, model);
		else 
			//oppure su tutti gli indici
			fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
		
			
			/* fine filtri*/
		int countResult = fullTextQuery.getResultSize();
		
		return countResult;	
	}
	
	
	public <T> List searchWithProjection(String field, String query, Class<T> model) throws ParseException{
		return searchWrapperWithProjection(field, query, model, -1);
	}

	/**
	 * {@inheritDoc}
	 **/
	public <T> List searchWrapperWithProjection(String field, String query, Class<T> model, int maxResults) throws ParseException{

		
		Session session = getHibernateTransactionManager().getSessionFactory().getCurrentSession();
		
		FullTextSession fullTextSession = Search.createFullTextSession(session);
		//Transaction tx = fullTextSession.beginTransaction();
		
	//	QueryParser parser = new QueryParser(field, new StandardAnalyzer());
	
		QueryParser parser = new QueryParser(field, getCustomAnalyzer().getAnalyzer(model));
		
		Query luceneQuery = parser.parse(query);
				
		FullTextQuery fullTextQuery = null;
		//Se si deve effettuare la ricerca solo su un indice (ad esempio solo quello "Opere")
		if (model != null) 
		    fullTextQuery = fullTextSession.createFullTextQuery( luceneQuery, model);
		else 
			//oppure su tutti gli indici
			fullTextQuery = fullTextSession.createFullTextQuery(luceneQuery);
		
	
		fullTextQuery.setProjection(ProjectionConstants.DOCUMENT);
		if (maxResults != -1){
			fullTextQuery.setMaxResults(maxResults);
		}
		List result = fullTextQuery.list();
		
		log.debug("La query che si vuole eseguire è: "+fullTextQuery.getQueryString());
		//index are written at commit time..ma essendo ora all'interno della transazionalità sotto spring il commit avviene in automatico
		//tx.commit(); 
		
		//FIXME Chiudere la sessione!!!
		//session.close();
		
		return result;
	}

	/** {@inheritDoc} */
	public void purgeAndCreateIndex() {
		//FIXME use key map of customAnalyzer or in configuration inject a list of indexed class for iterate list for purge and create index
//		
//      Example:		
//		Session session = getHibernateTransactionManager().getSessionFactory().openSession();
//		FullTextSession fullTextSession = Search.createFullTextSession(session);
//		Transaction tx = fullTextSession.beginTransaction();
//		
//		// Cancello gli indici esistenti
//		log.info("deleting index...");
//		
//		fullTextSession.purgeAll( YourObject.class );
//		//fullTextSession.getSearchFactory().optimize( YourObject.class );
//		
		//e li ricreo aggiornati
//		log.info("creating new index...");
//		
//		
//		for (YourObject yourObject : (List<YourObject>) session.createCriteria(YourObject.class).list())
//		{
//			fullTextSession.index(yourObject);
//		}
	}

}
