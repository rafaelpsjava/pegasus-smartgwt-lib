package br.com.pegasus.solutions.zws.ejb.lib.api;

import java.io.Serializable;
import java.util.List;

import javax.persistence.EntityManager;

import br.com.pegasus.solutions.zws.ejb.lib.exception.DAOException;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public interface GenericCMTHBDAO {

	/**
	 * saveOrUpdate an entity
	 * 
	 * @throws DAOException
	 */
	<Entity> void saveOrUpdate(Entity entity) throws DAOException;

	/**
	 * saveOrUpdate all entities
	 */
	<Entity> void saveOrUpdate(List<Entity> entities) throws DAOException;

	/**
	 * if you need cascade you must write other method that don't use hql to
	 * perform the exclusion
	 */
	<Id extends Serializable> void remove(Class<?> entityClass, List<Id> ids) throws DAOException;

	/**
	 * remove an entity
	 */
	<Entity> void remove(Class<?> entityClass, Entity entity) throws DAOException;

	/**
	 * remove with hql
	 */
	<Id extends Serializable> void removeWithHql(Class<?> entityClass, String pk, List<Id> ids) throws DAOException;

	/**
	 * remove by id with hql
	 */
	<Id extends Serializable> void removeByIdWithHql(Class<?> entityClass, String pk, Id id) throws DAOException;

	/**
	 * remove an entity by id
	 */
	<Id extends Serializable> void removeById(Class<?> entity, Id id) throws DAOException;

	/**
	 * get row count use a entityClass to find the row count of an entity
	 */
	Long getRowCount(Class<?> entityClass) throws DAOException;

	/**
	 * find an enty by id using hibernate
	 */
	<Entity, Id extends Serializable> Entity findById(Class<?> entityClass, Id id) throws DAOException;

	/**
	 * find entity by id using hibernate, entityClass is an entity that you want
	 * find, id is a id of an entity that you want to find, canLock if you want
	 * to lock a table or not
	 */
	<Entity, Id extends Serializable> Entity findById(Class<?> entityClass, Id id, boolean canLock) throws DAOException;

	/**
	 * find entity by id using jpa, entityClass is an entity that you want find,
	 * id is a id of an entity that you want to find, canLock if you want to
	 * lock a table or not
	 */
	<Entity, Id extends Serializable> Entity findByIdJpa(Class<?> entityClass, Id id, boolean lock) throws DAOException;

	/**
	 * find by example, entity is an example of entity that you pass to find by
	 * example, excludeProperty is a properties that you want to ignore
	 */
	<Entity> List<Entity> findByExample(Entity entity, String[] excludeProperty) throws DAOException;

	/**
	 * find all entity, this use entityClass that represents an entity that you
	 * want to find all records
	 */
	<Entity> List<Entity> findAll(Class<?> entityClass) throws DAOException;

	/**
	 * logic exclusion by id, the table must have a field flag_enabled and the
	 * entity must have a field that represents this field
	 */
	<Id extends Serializable> void logicExclusionWithHql(Class<?> entityClass, String pk, String flagFieldName, Id id) throws DAOException;

	/**
	 * logic exclusions by ids, the table must have a field flag_enabled and the
	 * entity must have a field that represents this field
	 */
	<Id extends Serializable> void logicExclusionWithHql(Class<?> entityClass, String pk, String flagFieldName, List<Id> ids) throws DAOException;

	/**
	 * get entity manager, use only when you are creation the criteria. this
	 * method was created to avoid Session is closed
	 */
	EntityManager getEM();

}