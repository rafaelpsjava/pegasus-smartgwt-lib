/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
package br.com.pegasus.solutions.zws.ejb.model.persistence.hb.cmt;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;

import br.com.pegasus.solutions.zws.domain.core.Marca;
import br.com.pegasus.solutions.zws.ejb.model.persistence.hb.cmt.api.MarcaCMTDAO;
import br.com.pegasus.solutions.zws.ejb.lib.api.FindResponse;
import br.com.pegasus.solutions.zws.ejb.lib.CriteriaUtil;
import br.com.pegasus.solutions.zws.util.CollectionUtil;
import br.com.pegasus.solutions.zws.ejb.lib.api.GenericCMTHBDAO;
import br.com.pegasus.solutions.zws.ejb.lib.exception.DAOException;

@Stateless(name = MarcaCMTDAO.EJB_NAME)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MarcaCMTDAOImpl implements MarcaCMTDAO {
	private static final Class<?> entityClass = Marca.class;

	@EJB
	private GenericCMTHBDAO genericHBDAO;

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void saveOrUpdate(Marca marca) throws DAOException {
		try {
			genericHBDAO.saveOrUpdate(marca);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void saveOrUpdate(List<Marca> marcas) throws DAOException {
		try {
			genericHBDAO.saveOrUpdate(marcas);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void remove(Marca entity) throws DAOException {
		try {
			genericHBDAO.remove(entityClass, entity);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void removeById(java.math.BigInteger id) throws DAOException {
		try {
			genericHBDAO.removeById(entityClass, id);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.SUPPORTS)
	public void removeByIds(List<java.math.BigInteger> ids) throws DAOException {
		try {
			genericHBDAO.remove(entityClass, ids);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Long getRowCount() throws DAOException {
		try {
			return genericHBDAO.getRowCount(entityClass);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca findById(java.math.BigInteger id) throws DAOException {
		try {
			return genericHBDAO.findById(entityClass, id);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca findByIdJpa(java.math.BigInteger id, boolean lock) throws DAOException {
		try {
			return genericHBDAO.findByIdJpa(entityClass, id, lock);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca find(Marca marca) throws DAOException {
		try {
			Criteria criteria = CriteriaUtil.buildCriteria(genericHBDAO.getEM(), entityClass);
			CriteriaUtil.addRestrictions(marca, criteria, MatchMode.EXACT);
	
			return (Marca) CollectionUtil.getFirst(criteria.list());
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Marca> findAll() throws DAOException {
		try {
			return genericHBDAO.findAll(entityClass);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca findById(java.math.BigInteger id, boolean lock) throws DAOException {
		try {
			return (Marca) genericHBDAO.findById(entityClass, id, lock);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Marca> findByExample(Marca exampleInstance, String[] excludeProperty) throws DAOException {
		try {
			return genericHBDAO.findByExample(exampleInstance, excludeProperty);
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FindResponse<Marca> findByCriteria(Marca marca, Integer firstRow, Integer lastRow) throws DAOException {
		try {
			Criteria criteria = CriteriaUtil.buildCriteria(genericHBDAO.getEM(), entityClass, firstRow, lastRow);
			try {
				CriteriaUtil.addRestrictions(marca, criteria, MatchMode.ANYWHERE);
			} catch (Exception e) {
				throw new DAOException(e);
			}
		
			return new FindResponse<Marca>(criteria.list(), getRowCount().toString());
		} catch (RuntimeException e) {
			throw new DAOException(e);
		}
	}

}