package br.com.pegasus.solutions.zws.ejb.lib.api;

import java.io.Serializable;
import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;

import br.com.pegasus.solutions.zws.ejb.lib.exception.BOException;
import br.com.pegasus.solutions.zws.util.exception.ValidationException;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public abstract interface DefaultBO<Entity extends Serializable, Id extends Serializable> {

	/**
	 * save or update an entity
	 */
	@WebMethod(operationName = "saveOrUpdate")
	void saveOrUpdate(@WebParam(name = "entity") Entity entity, @WebParam(name = "locale") String locale) throws BOException, ValidationException;

	/**
	 * save or update entities
	 */
	@WebMethod(operationName = "saveOrUpdateList")
	void saveOrUpdate(@WebParam(name = "entities") List<Entity> entities, @WebParam(name = "locale") String locale) throws BOException, ValidationException;

	/**
	 * remove an entity
	 */
	@WebMethod(operationName = "remove")
	void remove(@WebParam(name = "entity") Entity entity) throws BOException;

	/**
	 * remove an entity by id
	 */
	@WebMethod(operationName = "removeById")
	void removeById(@WebParam(name = "id") Id id) throws BOException;

	/**
	 * remove entities by id
	 */
	@WebMethod(operationName = "removeByIds")
	void removeByIds(@WebParam(name = "ids") List<Id> ids) throws BOException;

	/**
	 * get row count
	 */
	@WebMethod(operationName = "getRowCount")
	Long getRowCount() throws BOException;

	/**
	 * find an entity
	 */
	@WebMethod(operationName = "find")
	Entity find(@WebParam(name = "entity") Entity entity) throws BOException;

	/**
	 * find entity by id
	 */
	Entity findById(@WebParam(name = "id") Id id, @WebParam(name = "isLock") boolean isLock) throws BOException;

	/**
	 * find by criteria an entity
	 */
	@WebMethod(operationName = "findByCriteria")
	FindResponse<Entity> findByCriteria(@WebParam(name = "entity") Entity entity) throws BOException;

	/**
	 * find by criteria an entity paged
	 */
	@WebMethod(operationName = "findByCriteria")
	FindResponse<Entity> findByCriteria(@WebParam(name = "entity") Entity entity, @WebParam(name = "startRow") Integer startRow,
			@WebParam(name = "lastRow") Integer lastRow) throws BOException;

	/**
	 * find alll entities
	 */
	@WebMethod(operationName = "findAll")
	List<Entity> findAll() throws BOException;
}