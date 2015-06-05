package br.com.pegasus.solutions.zws.ejb.model.persistence.hb.cmt.api;

import java.math.BigInteger;

import javax.ejb.Local;

import br.com.pegasus.solutions.zws.ejb.lib.api.DefaultDAO;
import br.com.pegasus.solutions.zws.ejb.lib.exception.DAOException;
import br.com.pegasus.solutions.zws.domain.core.Marca;
import br.com.pegasus.solutions.zws.ejb.model.constants.ZwsConstants;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
@Local
public interface MarcaCMTDAO extends DefaultDAO<Marca, java.math.BigInteger> {
	public static final String EJB_NAME = ZwsConstants.DAO_STATELESS_COMMON_CMT_PATH + "MarcaCMTDAO";

	Marca findByIdJpa(BigInteger id, boolean lock) throws DAOException;

}