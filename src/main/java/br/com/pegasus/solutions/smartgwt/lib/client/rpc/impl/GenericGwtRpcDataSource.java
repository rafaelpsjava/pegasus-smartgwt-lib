/*
 * Copyright 2015 Pegasus Solutions
 * @author Rafael Peres dos Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * ================================================================================
 *
 * Direitos autorais 2015 Pegasus Solutions
 * @author Rafael Peres dos Santos
 * 
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode usar
 * esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que
 * haja exigência legal ou acordo por escrito, a distribuição de software sob
 * esta LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER
 * TIPO, sejam expressas ou tácitas. Veja a LICENÇA para a redação específica a
 * reger permissões e limitações sob esta LICENÇA.
 *
 */
package br.com.pegasus.solutions.smartgwt.lib.client.rpc.impl;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.pegasus.solutions.smartgwt.lib.client.rpc.api.GenericGwtRpcList;
import br.com.pegasus.solutions.smartgwt.lib.client.rpc.api.GenericGwtRpcService;
import br.com.pegasus.solutions.smartgwt.lib.client.rpc.api.GenericGwtRpcServiceAsync;
import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.PagingToolBar;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCResponse;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.JSOHelper;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tree.TreeNode;
import com.smartgwt.client.widgets.viewer.DetailViewerRecord;

/**
 * Generic abstract {@link GwtRpcDataSource} implementation, supporting
 * server-side paging and sorting. Extend this class if you want to create a GWT
 * RPC DataSource for SmartGWT. This is based on the {@link GwtRpcDataSource}
 * example provided in the smartgwt-extensions.
 * 
 * In order to use this class, you have to implement both
 * {@link GenericGwtRpcService} and {@link GenericGwtRpcServiceAsync} provided
 * in the same package. To use paging ({@link FetchMode#PAGED}), you'll have to
 * return a {@link GenericGwtRpcList} from the fetch()-method of your
 * {@link GenericGwtRpcService}-implementation.
 * 
 * @param <D>
 *            type of the transfer object holding the data (will most likely be
 *            a simple POJO), must implement {@link Serializable} or
 *            {@link IsSerializable} .
 * @param <R>
 *            any extension of {@link Record}, such as {@link ListGridRecord},
 *            {@link DetailViewerRecord} or {@link TreeNode} to use with your
 *            SmartGWT widget.
 * @param <SA>
 *            the asynchronous version of your service. Extend
 *            {@link GenericGwtRpcService} and then
 *            {@link GenericGwtRpcServiceAsync} to implement it.
 * 
 * @see GwtRpcDataSource
 * @see GenericGwtRpcService
 * @see GenericGwtRpcServiceAsync
 * @see GenericGwtRpcList
 */
public abstract class GenericGwtRpcDataSource<D, R extends Record, SA extends GenericGwtRpcServiceAsync<D>> extends GwtRpcDataSource {
	private GenericGwtRpcServiceAsync<D> serviceAsync;

	private static final int START_ROW = 0;
	private static final int END_ROW = 500;
	private BooleanCallback sessionTimeOutCallback;

	public GenericGwtRpcDataSource() {
		super();
		setDataSourceFields();
		serviceAsync = getServiceAsync();
		this.sessionTimeOutCallback = new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				History.newItem("main", false);
				com.google.gwt.user.client.Window.Location.reload();
			}
		};
	}

	public GenericGwtRpcDataSource(String id) {
		super();
		setID(id);
		setDataSourceFields();
		serviceAsync = getServiceAsync();
	}

	/**
	 * @return a list of {@link DataSourceField}, used to define the fields of
	 *         your {@link DataSource}. NOTE: Make sure to set a primary key, as
	 *         some problems might occur if it's omitted.
	 */
	public abstract List<DataSourceField> getDataSourceFields();

	/**
	 * Copies values from the {@link Record} to the data object.
	 * 
	 * @param from
	 *            the {@link Record} to copy from.
	 * @param to
	 *            the data object to copy to.
	 */
	public abstract void copyValues(R from, D to);

	/**
	 * Copies values from the data object to the {@link Record}.
	 * 
	 * @param from
	 *            the data object to copy from.
	 * @param to
	 *            the {@link Record} to copy to.
	 */
	public abstract void copyValues(D from, R to);

	/**
	 * @return the {@link GenericGwtRpcServiceAsync} to use, created using
	 *         <code>GWT.create(YourGenericGwtRpcDataSourceService.class)</code>
	 *         .
	 * 
	 *         This is unfortunately necessary as <code>GWT.create()</code> only
	 *         allows class literal as argument. We cannot create a class
	 *         literal from a parameterized type because it has no exact runtime
	 *         type representation, which is due to type erasure at compile
	 *         time.
	 */
	public abstract SA getServiceAsync();

	/**
	 * @return a new instance of your {@link Record}, such as <code>new
	 * Record()</code> or <code>new ListGridRecord()</code>.
	 * 
	 *         This method is needed because we cannot instantiate parameterized
	 *         types at runtime. It also increases flexibility as we can pass
	 *         more complex default objects.
	 */
	public abstract R getNewRecordInstance();

	/**
	 * @return a new instance of your data object, such as <code>new
	 * YourDataObject()</code>.
	 * 
	 *         This method is needed because we cannot instantiate parameterized
	 *         types at runtime. It also increases flexibility as we can pass
	 *         more complex default objects.
	 */
	public abstract D getNewDataObjectInstance();

	@Override
	@SuppressWarnings("unchecked")
	public void executeFetch(final String requestId, final DSRequest request, final DSResponse response) throws Exception {
		final Integer startRow = request.getStartRow();
		final Integer endRow = request.getEndRow();

		Criteria criteria = request.getCriteria();
		Map<String, String> criterias = new HashMap<String, String>();
		if (criteria != null) {
			@SuppressWarnings("rawtypes")
			Map values = criteria.getValues();
			criterias = values;
		}

		serviceAsync.fetch(startRow, endRow, request.getAttribute("sortBy"), criterias, new AsyncCallback<List<D>>() {
			public void onFailure(Throwable e) {
				showErrorMsg(response, e);
				processResponse(requestId, response);
			}

			public void onSuccess(List<D> result) {
				List<R> records = new ArrayList<R>();
				for (D data : result) {
					R newRec = getNewRecordInstance();
					copyValues(data, newRec);
					records.add(newRec);
				}

				if (startRow != null && endRow != null && result instanceof GenericGwtRpcList<?>) {
					Integer totalRows = ((GenericGwtRpcList<D>) result).getTotalRows();
					response.setStartRow(START_ROW);

					if (totalRows == null) {
						throw new NullPointerException("totalRows cannot be null when using GenericGwtRpcList");
					}

					// if you use the same datasource instance in two views when
					// you
					// fetch data in wich one will update two pagingtoolbar it's
					// not good, to avoid this always create a new datasource
					// instance for each view, this means avoid singleton
					// instance in this case
					if (getPagingToolbar() != null) {
						getPagingToolbar().updateInfo(totalRows);
					}

					response.setEndRow(END_ROW);
					/**
					 * this is the real total rows, because gwt use totalRows
					 * for dinamic paginator based on scroll, the attribute
					 * TOTAL_ROWS that i create is for the amount of rows per
					 * page based on page per amount of max data per page
					 */
					response.setAttribute("TOTAL_ROWS", totalRows);
					response.setTotalRows(result.size());
				}

				response.setData(records.toArray(new Record[records.size()]));
				processResponse(requestId, response);
			}
		});
	}

	@Override
	public void executeAdd(final String requestId, final DSRequest request, final DSResponse response) throws Exception {
		// retrieve record which should be added.
		R newRec = getNewRecordInstance();
		newRec.setJsObj(request.getData());
		D data = getNewDataObjectInstance();
		copyValues(newRec, data);

		serviceAsync.add(data, new AsyncCallback<D>() {
			public void onFailure(Throwable e) {
				showErrorMsg(response, e);
				processResponse(requestId, response);
			}

			public void onSuccess(D result) {
				R newRec = getNewRecordInstance();
				copyValues(result, newRec);
				response.setData(new Record[] { newRec });
				processResponse(requestId, response);
			}
		});
	}

	private void showErrorMsg(final DSResponse response, Throwable e) {
		if ("session_expired".equals(e.getMessage())) {
			MessageUtil.showError(Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE).get("session_expired_login_again"),
					sessionTimeOutCallback);
		} else {
			response.setStatus(RPCResponse.STATUS_FAILURE);
			response.setAttribute("data", e.getMessage());
		}
	}

	@Override
	public void executeUpdate(final String requestId, final DSRequest request, final DSResponse response) throws Exception {
		// retrieve record which should be updated.
		R editedRec = getEditedRecord(request);
		D data = getNewDataObjectInstance();
		copyValues(editedRec, data);
		serviceAsync.update(data, new AsyncCallback<D>() {
			public void onFailure(Throwable e) {
				showErrorMsg(response, e);
				processResponse(requestId, response);
			}

			public void onSuccess(D result) {
				R updatedRec = getNewRecordInstance();
				copyValues(result, updatedRec);
				response.setData(new Record[] { updatedRec });
				processResponse(requestId, response);
			}
		});
	}

	@Override
	public void executeRemove(final String requestId, final DSRequest request, final DSResponse response) throws Exception {
		// retrieve record which should be removed.
		try {
			final R rec = getNewRecordInstance();
			rec.setJsObj(request.getData());
			D data = getNewDataObjectInstance();
			copyValues(rec, data);

			response.setData(new Record[] { rec });
			processResponse(requestId, response);
		} catch (Exception e) {
			showErrorMsg(response, e);
		}
	}

	private R getEditedRecord(DSRequest request) {
		// creating new record for combining old values with changes
		R newRecord = getNewRecordInstance();
		// retrieving values before edit
		if (request.getOldValues() != null) {
			JavaScriptObject oldValues = request.getOldValues().getJsObj();
			// copying properties from old record
			JSOHelper.apply(oldValues, newRecord.getJsObj());
		}
		// retrieving changed values
		JavaScriptObject changedData = request.getData();
		// apply changes
		JSOHelper.apply(changedData, newRecord.getJsObj());

		return newRecord;
	}

	private void setDataSourceFields() {
		List<DataSourceField> fields = getDataSourceFields();
		if (fields != null) {
			for (DataSourceField field : fields) {
				addField(field);
			}
		}
	}

	public abstract Dictionary getDC();

	public abstract void setPagingToolbar(PagingToolBar pagingToolBar);

	public abstract PagingToolBar getPagingToolbar();

	public String getDicValue(String key) {
		return getDC() != null ? getDC().get(key) : "";
	}

}