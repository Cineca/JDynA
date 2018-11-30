package it.cilea.osd.jdyna.service;

/***
 * Service used to auto create a IAutoCreableObject.
 *
 * Implementation object is responsible of caching object data 
 * (just it's name with a generated id) and then creating a 
 * new object using cached data.
 */
public interface IAutoCreateApplicationService {
	
	/***
	 * Cache a candidate of a new Object
	 * 
	 * @param name The cached value	
	 * @return true if auto created is enabled
	 */
	public Integer cache(String name);
	
	/***
	 * Check if auto create is enabled for class c
	 * 
	 * @param clazz The class
	 * @return true if auto created is enabled
	 */
	@SuppressWarnings("rawtypes")
	public boolean canSave(Class clazz);
	
	/***
	 * Try to create a new persistent object of class c.
	 * 
	 * @param clazz The class
	 * @param id The id of the object
	 * @return The id of the object created.
	 */
	@SuppressWarnings("rawtypes")
	public Integer saveObject(Class clazz, int id);
}
