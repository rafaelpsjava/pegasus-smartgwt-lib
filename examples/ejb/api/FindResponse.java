package br.com.pegasus.solutions.zws.ejb.lib.api;

import java.io.Serializable;
import java.util.List;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public class FindResponse<Entity> implements Serializable {
	private static final long serialVersionUID = 4667141525648289682L;
	private List<Entity> data;
	private Integer totalRows;

	public FindResponse() {
	}

	public FindResponse(List<Entity> data, String totalRows) {
		this.data = data;
		this.totalRows = totalRows == null ? 0 : Integer.valueOf(totalRows);
	}

	public List<Entity> getData() {
		return this.data;
	}

	public Integer getTotalRows() {
		return this.totalRows;
	}
}