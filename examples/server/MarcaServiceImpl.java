package xptopackage.server.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.annotation.WebServlet;

import br.com.pegasus.solutions.zws.domain.core.Marca;
import br.com.pegasus.solutions.zws.ejb.model.bo.cmt.api.MarcaCMTBO;
import br.com.pegasus.solutions.zws.ejb.lib.api.FindResponse;
import br.com.pegasus.solutions.zws.gwt.shared.dto.MarcaDTO;
import br.com.pegasus.solutions.zws.gwt.shared.lib.rpc.GenericGwtRpcList;
import br.com.pegasus.solutions.zws.util.ErrorMessage;
import br.com.pegasus.solutions.zws.util.FilterDataSmartGwt;
import br.com.pegasus.solutions.zws.gwt.shared.constants.AppConstants;
import br.com.pegasus.solutions.zws.gwt.client.api.service.MarcaService;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/

@WebServlet(value = "/br.com.pegasus.solutions.zws.gwt.Application/marcaService")
public class MarcaServiceImpl extends AbstractRemoteServiceServlet<Marca, MarcaDTO> implements MarcaService {
	private static final long serialVersionUID = -3559824925322648054L;
	@EJB
	private MarcaCMTBO marcaBO;

	public MarcaServiceImpl() {
		super();
	}

	@Override
	public MarcaDTO add(MarcaDTO marcaDTO) throws Exception {
		throwExceptionIfSessionExpired();
		
		try {
			Marca marca = buildEntityInstanceFromDTO(marcaDTO, new Marca());
			marcaBO.saveOrUpdate(marca, getCurrentLocale());
			buildDTOInstanceFromEntityInstance(marcaDTO, marca);
			
			return marcaDTO;
		} catch (Exception e) {
			throw new Exception(ErrorMessage.formatMsg(e));
		}
	}

	@Override
	public MarcaDTO update(MarcaDTO marcaDTO) throws Exception {
		throwExceptionIfSessionExpired();
			
		return add(marcaDTO);
	}

	@Override
	public void remove(List<MarcaDTO> marcaDTOs) throws Exception {
		throwExceptionIfSessionExpired();
		
		if(marcaDTOs != null ) {
			try {
				List<java.math.BigInteger> ids = new ArrayList<java.math.BigInteger>();
				for (MarcaDTO marcaDTO : marcaDTOs) {
					Marca marca = buildEntityInstanceFromDTO(marcaDTO, new Marca());
					java.math.BigInteger id = marca.getId();
					
					ids.add(id);
				}
			
				marcaBO.removeByIds(ids);
			} catch (Exception e) {
				throw new Exception(ErrorMessage.formatMsg(e));
			}
		}
	}

	@Override
	public List<MarcaDTO> fetch(Integer startRow, Integer endRow, final String sortBy, Map<String, Object> filterCriteria) throws Exception {
		throwExceptionIfSessionExpired();
	
		try {
			GenericGwtRpcList<MarcaDTO> outList = new GenericGwtRpcList<MarcaDTO>();
			Marca marcaCriteria = new Marca();
			new FilterDataSmartGwt<Marca>().fillInstance(marcaCriteria, filterCriteria, true);

			FindResponse<Marca> marcaFindResponse = marcaBO.findByCriteria(marcaCriteria, startRow, endRow);
			if (marcaFindResponse != null) {
				for (Marca marca : marcaFindResponse.getData()) {
					MarcaDTO marcaDTO = new MarcaDTO();
					buildDTOInstanceFromEntityInstance(marcaDTO, marca);
					
					outList.add(marcaDTO);
				}
			}
			
			outList.setTotalRows(marcaFindResponse.getTotalRows());
			
			return outList;
		} catch (Exception e) {
			throw new Exception(ErrorMessage.formatMsg(e));
		}
	}
	
}