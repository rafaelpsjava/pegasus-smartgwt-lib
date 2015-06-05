/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
package br.com.pegasus.solutions.zws.ejb.model.persistence.hb.cmt;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.Query;
import org.hibernate.criterion.Example;

import br.com.pegasus.solutions.zws.ejb.lib.CriteriaUtil;
import br.com.pegasus.solutions.zws.ejb.lib.SessionUtil;
import br.com.pegasus.solutions.zws.ejb.lib.api.GenericCMTHBDAO;
import br.com.pegasus.solutions.zws.ejb.lib.exception.ApplicationRuntimeException;
import br.com.pegasus.solutions.zws.ejb.lib.exception.DAOException;
import br.com.pegasus.solutions.zws.ejb.model.constants.ZwsConstants;

@Local(GenericCMTHBDAO.class)
@Stateless(name = ZwsConstants.DAO_STATELESS_COMMON_CMT_PATH + "GenericCMTHBDAO")
@TransactionManagement(TransactionManagementType.CONTAINER)
public class GenericCMTHBDAOImpl implements GenericCMTHBDAO {
	public static final String DEFAULT_PK = "id";
	public static final String DEFAULT_FLAG_FIELD_NAME = "flag_enabled";

	@PersistenceContext(unitName = ZwsConstants.DEFAULT_PU)
	private EntityManager em;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Entity> void saveOrUpdate(Entity entity) throws DAOException {
		try {
			if (em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity) == null) {
				em.persist(entity);
			} else {
				entity = em.merge(entity); // creating new record in boleto
				// SessionUtil.getHbSession(em).saveOrUpdate(entity); //
				// creating new record in boleto
			}

			// SessionUtil.getHbSession(em).saveOrUpdate(entity); // hibernate
			// save

			em.flush();
			em.detach(entity);
		} catch (Exception e) {
			if (e instanceof org.hibernate.NonUniqueObjectException) {
				em.detach(entity);
			} else {
				throw new DAOException(e);
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Entity> void saveOrUpdate(List<Entity> entities) throws DAOException {
		// Session session = SessionUtil.getHbSession(em);
		if (entities != null) {
			for (Entity entity : entities) {
				try {
					if (em.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entity) == null) {
						em.persist(entity);
					} else {
						entity = em.merge(entity);
					}

					// session.saveOrUpdate(entity); // hibernate save
					em.flush();
					em.detach(entity);
				} catch (Exception e) {
					if (e instanceof org.hibernate.NonUniqueObjectException) {
						em.detach(entity);
						em.merge(entity);
					} else {
						throw new DAOException(e);
					}
				}
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Id extends Serializable> void logicExclusionWithHql(Class<?> entityClass, String pk, String flagFieldName, Id id) throws DAOException {
		if (pk == null) {
			pk = DEFAULT_PK;
		}
		if (flagFieldName == null) {
			flagFieldName = DEFAULT_FLAG_FIELD_NAME;
		}
		try {
			Query query = SessionUtil.getHbSession(em).createQuery(
					"update " + entityClass.getName() + (" set " + flagFieldName + " = 0 where " + pk + " = :id"));
			setId(id, query);
			query.executeUpdate();
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Id extends Serializable> void logicExclusionWithHql(Class<?> entityClass, String pk, String flagFieldName, List<Id> ids) throws DAOException {
		if (pk == null) {
			pk = DEFAULT_PK;
		}
		if (flagFieldName == null) {
			flagFieldName = DEFAULT_FLAG_FIELD_NAME;
		}
		try {
			Query query = SessionUtil.getHbSession(em).createQuery(
					"update " + entityClass.getName() + (" set " + flagFieldName + " = 0 where " + pk + " in(:ids) "));
			query.setParameterList("ids", ids);
			query.executeUpdate();
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Entity> void remove(Class<?> entityClass, Entity entity) throws DAOException {
		try {
			if (entity != null) {
				em.merge(entity);

				SessionUtil.getHbSession(em).delete(entity);
				em.flush();
				em.detach(entity);
			}

		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Id extends Serializable> void remove(Class<?> entityClass, List<Id> ids) throws DAOException {
		if (ids != null) {
			for (Id id : ids) {
				try {
					Object entityFind = em.find(entityClass, id);

					if (entityFind != null) {
						em.merge(entityFind);

						em.remove(entityFind);
						em.flush();
					}
				} catch (RuntimeException e) {
					throw new DAOException(e);
				}
			}
		} else {
			throw new ApplicationRuntimeException("ids cannot be null");
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Id extends Serializable> void removeWithHql(Class<?> entityClass, String pk, List<Id> ids) throws DAOException {
		try {
			Query query = SessionUtil.getHbSession(em).createQuery("delete " + entityClass.getName() + " where " + pk + " in(:ids) ");
			query.setParameterList("ids", ids);
			query.executeUpdate();
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Id extends Serializable> void removeById(Class<?> entityClass, Id id) throws DAOException {
		try {
			Object entityFind = em.find(entityClass, id);
			remove(entityClass, entityFind);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public <Id extends Serializable> void removeByIdWithHql(Class<?> entityClass, String pk, Id id) throws DAOException {
		if (pk == null) {
			pk = DEFAULT_PK;
		}
		if (id == null) {
			throw new ApplicationRuntimeException("id cannot be null");
		}
		try {
			Query query = SessionUtil.getHbSession(em).createQuery("delete from " + entityClass.getName() + " where " + pk + " = :id");
			setId(id, query);
			query.executeUpdate();
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Long getRowCount(Class<?> entityClass) throws DAOException {
		try {
			Query countQuery = SessionUtil.getHbSession(em).createQuery("select count(*) from " + entityClass.getName());
			Object countResult = countQuery.uniqueResult();
			if (countResult != null && countResult instanceof Number) {
				return ((Long) countResult);
			}
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
		return 0l;
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public <Entity> List<Entity> findAll(Class<?> entityClass) throws DAOException {
		try {
			Criteria criteria = CriteriaUtil.buildCriteria(em, entityClass);
			return criteria.list();
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public <Entity, Id extends Serializable> Entity findById(Class<?> entityClass, Id id) throws DAOException {
		try {

			return (Entity) SessionUtil.getHbSession(em).load(entityClass, id);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@SuppressWarnings({ "unchecked", "deprecation" })
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public <Entity, Id extends Serializable> Entity findById(Class<?> entityClass, Id id, boolean lock) throws DAOException {
		try {
			Entity entity;
			if (lock) {
				entity = (Entity) SessionUtil.getHbSession(em).load(entityClass, id, LockMode.UPGRADE_NOWAIT);
			} else {
				entity = (Entity) SessionUtil.getHbSession(em).load(entityClass, id);
			}

			return entity;
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public <Entity, Id extends Serializable> Entity findByIdJpa(Class<?> entityClass, Id id, boolean lock) throws DAOException {
		try {
			Entity entity;
			if (lock) {
				entity = (Entity) em.find(entityClass, id, LockModeType.READ);
			} else {
				entity = (Entity) em.find(entityClass, id);
			}

			return entity;
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public <Entity> List<Entity> findByExample(Entity entityExample, String[] excludeProperty) throws DAOException {
		try {
			Criteria criteria = CriteriaUtil.buildCriteria(em, entityExample.getClass());
			Example example = Example.create(entityExample);
			if (excludeProperty != null) {
				for (String exclude : excludeProperty) {
					example.excludeProperty(exclude);
				}
			}
			criteria.add(example);

			return criteria.list();
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	private <Id extends Serializable> void setId(Id id, Query query) throws DAOException {
		if (id instanceof Integer) {
			query.setInteger("id", (Integer) id);
		} else if (id instanceof Long) {
			query.setLong("id", (Long) id);
		} else if (id instanceof BigDecimal) {
			query.setBigDecimal("id", (BigDecimal) id);
		} else {
			throw new ApplicationRuntimeException("invalid id!");
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public EntityManager getEM() {
		return em;
	}
}