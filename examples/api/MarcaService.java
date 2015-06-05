package xptopackage.client.api.service;

import br.com.pegasus.solutions.smartgwt.lib.client.rpc.api.GenericGwtRpcService;
import br.com.pegasus.solutions.zws.gwt.shared.dto.MarcaDTO;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("marcaService")
public interface MarcaService extends GenericGwtRpcService<MarcaDTO> {

}