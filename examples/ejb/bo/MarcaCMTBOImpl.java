/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
package br.com.pegasus.solutions.zws.ejb.model.bo.cmt;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import br.com.pegasus.solutions.zws.domain.core.Marca;
import br.com.pegasus.solutions.zws.util.DateUtil;
import br.com.pegasus.solutions.zws.util.FieldValidator;
import br.com.pegasus.solutions.zws.ejb.lib.api.FindResponse;
import br.com.pegasus.solutions.zws.ejb.lib.exception.ApplicationRuntimeException;
import br.com.pegasus.solutions.zws.util.exception.ValidationException;
import br.com.pegasus.solutions.zws.ejb.lib.exception.BOException;
import br.com.pegasus.solutions.zws.ejb.lib.exception.DAOException;
import br.com.pegasus.solutions.zws.ejb.model.bo.cmt.api.MarcaCMTBO;
import br.com.pegasus.solutions.zws.ejb.model.persistence.hb.cmt.api.MarcaCMTDAO;

@Stateless(name = MarcaCMTBO.EJB_NAME)
@TransactionManagement(TransactionManagementType.CONTAINER)
public class MarcaCMTBOImpl implements MarcaCMTBO {
	@EJB
	private MarcaCMTDAO marcaCMTDAO;
	
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveOrUpdate(Marca marca, String locale) throws BOException, ValidationException {
		try {
			FieldValidator.validate(marca, DateUtil.LOCALES.get(locale));

			marcaCMTDAO.saveOrUpdate(marca);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveOrUpdate(List<Marca> marcas, String locale) throws BOException, ValidationException {
		if(marcas != null) {
			for (Marca marca : marcas) {
				saveOrUpdate(marca, locale);
			}
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void remove(Marca marca) throws BOException {
		try {
			marcaCMTDAO.remove(marca);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeByIds(List<java.math.BigInteger> ids) throws BOException {
		try {
			marcaCMTDAO.removeByIds(ids);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void removeById(java.math.BigInteger id) throws BOException {
		try {
			marcaCMTDAO.removeById(id);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Long getRowCount() throws BOException {
		try {
			return marcaCMTDAO.getRowCount();
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca find(Marca marcas) throws BOException {
		try {
			return marcaCMTDAO.find(marcas);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Marca> findAll() throws BOException {
		try {
			return marcaCMTDAO.findAll();
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FindResponse<Marca> findByCriteria(Marca marca) throws BOException {
		try {
			return marcaCMTDAO.findByCriteria(marca, null, null);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}

	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public FindResponse<Marca> findByCriteria(Marca marca, Integer firstRow, Integer lastRow) throws BOException {
		try {
			return marcaCMTDAO.findByCriteria(marca, firstRow, lastRow);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca findById(java.math.BigInteger id, boolean isLock) throws BOException {
		try {
			return marcaCMTDAO.findById(id, isLock);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
	@Override
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Marca findByIdJpa(java.math.BigInteger id, boolean lock) throws BOException {
		try {
			return marcaCMTDAO.findByIdJpa(id, lock);
		} catch (RuntimeException e) {
			throw new ApplicationRuntimeException(e);
		} catch (DAOException e) {
			throw new BOException(e);
		}
	}
	
}