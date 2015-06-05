package xptopackage.server.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import br.com.pegasus.solutions.zws.gwt.shared.constants.AppConstants;
import br.com.pegasus.solutions.zws.gwt.shared.dto.DTOIF;
import br.com.pegasus.solutions.zws.gwt.shared.dto.core.UserAppDTO;
import br.com.pegasus.solutions.zws.util.DateUtil;
import br.com.pegasus.solutions.zws.util.InstanceUtil;
import br.com.pegasus.solutions.zws.util.ReflectionUtil;

import com.google.gson.Gson;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public class AbstractRemoteServiceServlet<Entity extends Serializable, DTO extends DTOIF> extends RemoteServiceServlet {
	private static final long serialVersionUID = -4270604410832953818L;

	/**
	 * get session
	 * 
	 * @param createNewSessionIfNotExists
	 * @return {@link HttpSession}
	 */
	public HttpSession getSession(boolean createNewSessionIfNotExists) {
		return getThreadLocalRequest().getSession(createNewSessionIfNotExists);
	}

	/**
	 * loggout()
	 * 
	 * @throws Exception
	 */
	public void loggout() throws Exception {
		HttpSession session = getSession(false);
		if (session != null) {
			session.setAttribute("LOGGED_USER", null);
			session.invalidate();
		}
	}

	/**
	 * get logged user
	 * 
	 * @return {@link UserAppDTO}
	 * @throws Exception
	 */
	public UserAppDTO getLoggedUser() throws Exception {
		HttpSession session = getSession(false);
		if (session != null) {
			return (UserAppDTO) session.getAttribute("LOGGED_USER");
		}

		return null;
	}

	/**
	 * trow exception if session expired you must call this method in each
	 * ServiceImpl method that you must check if session expired
	 * 
	 * @throws Exception
	 * @return void
	 */
	public void throwExceptionIfSessionExpired() throws Exception {
		if (getLoggedUser() == null) {
			throw new RuntimeException("session_expired");
		}
	}

	/**
	 * change locale
	 * 
	 * @param locale {@link String}
	 * @throws Exception
	 */
	public void changeLocale(String locale) throws Exception {
		getSession(true).setAttribute("LAST_SELECTED_LOCALE", locale);
	}

	/**
	 * @return {@link String}
	 */
	public String getCurrentLocale() {
		HttpSession session = getSession(false);
		if (session != null && session.getAttribute("LAST_SELECTED_LOCALE") != null) {
			return (String) session.getAttribute("LAST_SELECTED_LOCALE");
		}

		return AppConstants.DEFAULT_LOCALE;
	}

	/**
	 * setDTOWithDomainInstance
	 *
	 * @param fromObj {@link Object}
	 * @param toObj {@link Object}
	 */
	public void buildDTOInstanceFromEntityInstance(DTO dto, Entity entity) {
		if (entity == null || dto == null) {
			throw new IllegalArgumentException("entity or dto cannot be null!");
		}

		new InstanceUtil(entity, dto, false).fillInstance();
	}

	/**
	 * add
	 * 
	 * @param dto DTO
	 * @return DTO
	 * @throws Exception
	 */
	public DTO add(DTO dto) throws Exception {
		throw new RuntimeException("not implemented exception!");
	}

	/**
	 * update
	 * 
	 * @param dto DTO
	 * @return DTO
	 * @throws Exception
	 */
	public DTO update(DTO dto) throws Exception {
		return add(dto);
	}

	/**
	 * remove
	 * 
	 * @param dtosToRemove {@link List}<DTO>
	 * @throws Exception
	 */
	public void remove(List<DTO> dtosToRemove) throws Exception {
		throw new RuntimeException("not implemented exception!");
	}

	/**
	 * return json {@link String}
	 * 
	 * @param dtos {@link List}<DTO>
	 * @return {@link String}
	 * @throws Exception
	 */
	public String toJson(List<DTO> dtos) throws Exception {
		if (dtos != null) {
			Gson gson = new Gson();
			return gson.toJson(dtos);
		}

		return null;
	}

	/**
	 * fetch
	 * 
	 * @param startRow {@link Integer}
	 * @param endRow {@link Integer}
	 * @param sortBy {@link String}
	 * @param filterCriteria {@link Map}
	 * @return {@link List}<DTO>
	 * @throws Exception
	 */
	public List<DTO> fetch(Integer startRow, Integer endRow, String sortBy, Map<String, Object> filterCriteria) throws Exception {
		return new ArrayList<DTO>();
	}

	@SuppressWarnings("unchecked")
	public Entity buildEntityInstanceFromDTO(DTO dto, Entity entity) throws Exception {
		if (entity == null || dto == null) {
			throw new IllegalArgumentException("entity or dto cannot be null!");
		}

		Entity entityBuilded = (Entity) new InstanceUtil(dto, entity, false).fillInstance();
		
		return entityBuilded;
	}

}