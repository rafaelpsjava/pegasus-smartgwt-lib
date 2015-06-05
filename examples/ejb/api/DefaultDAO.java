package br.com.pegasus.solutions.zws.ejb.lib.api;

import java.io.Serializable;
import java.util.List;

import br.com.pegasus.solutions.zws.ejb.lib.exception.DAOException;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public abstract interface DefaultDAO<Entity extends Serializable, Id extends Serializable> {

	/**
	 * save or update an entity
	 * 
	 * @throws DAOException
	 */
	void saveOrUpdate(Entity entity) throws DAOException;

	/**
	 * save or update entities
	 */
	void saveOrUpdate(List<Entity> entities) throws DAOException;

	/**
	 * remove an entity
	 */
	void remove(Entity entity) throws DAOException;

	/**
	 * remove an entity by id
	 */
	void removeById(Id id) throws DAOException;

	/**
	 * remove entities by id
	 */
	void removeByIds(List<Id> ids) throws DAOException;

	/**
	 * get row count
	 */
	Long getRowCount() throws DAOException;

	/**
	 * find an entity
	 */
	Entity find(Entity entity) throws DAOException;

	/**
	 * find entity by id
	 */
	Entity findById(Id id) throws DAOException;

	/**
	 * find entity by id
	 */
	Entity findById(Id id, boolean isLock) throws DAOException;

	/**
	 * find by example
	 */
	List<Entity> findByExample(Entity entity, String[] excludeProperty) throws DAOException;

	/**
	 * find by criteria
	 */
	FindResponse<Entity> findByCriteria(Entity entity, Integer startRow, Integer endRow) throws DAOException;

	/**
	 * find all entities
	 */
	List<Entity> findAll() throws DAOException;

}