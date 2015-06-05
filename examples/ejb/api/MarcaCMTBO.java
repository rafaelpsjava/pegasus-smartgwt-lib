package br.com.pegasus.solutions.zws.ejb.model.bo.cmt.api;

import java.math.BigInteger;

import javax.ejb.Local;

import br.com.pegasus.solutions.zws.domain.core.Marca;
import br.com.pegasus.solutions.zws.ejb.lib.api.DefaultBO;
import br.com.pegasus.solutions.zws.ejb.lib.exception.BOException;
import br.com.pegasus.solutions.zws.ejb.model.constants.ZwsConstants;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
@Local
public interface MarcaCMTBO extends DefaultBO<Marca, java.math.BigInteger> {
	public static final String EJB_NAME = ZwsConstants.BO_CMT_COMMON_PATH + "MarcaCMTBO";

	Marca findByIdJpa(BigInteger id, boolean lock) throws BOException;

}	