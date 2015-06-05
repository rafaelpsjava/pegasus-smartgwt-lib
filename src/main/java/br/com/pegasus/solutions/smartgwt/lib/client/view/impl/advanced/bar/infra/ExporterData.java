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
package br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.infra;

import java.util.LinkedHashMap;

import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Window;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;

public final class ExporterData {
	private static final Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);
	private static final IButton callExportServlet = new IButton("CallExportServlet");

	private ExporterData() {
	}

	static {
		callExportServlet.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				Window.open(GWT.getModuleBaseURL() + "ExportServlet" + "?exportServletType=LGD", "_parent", "location=no");
			}
		});
	}

	public static VLayout buildLayout(final ListGrid listGrid, final String fileName) {
		final DynamicForm exportForm = new DynamicForm();
		exportForm.setWidth(300);

		LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
		valueMap.put("xml", "XML");
		valueMap.put("json", "JSON");
		valueMap.put("csv", "CSV (Excel)");
		valueMap.put("xls", "XLS (Excel97)");
		valueMap.put("ooxml", "XLSX (Excel2007/OOXML)");

		SelectItem exportTypeItem = new SelectItem("exportType", dc.get("format"));
		exportTypeItem.setWidth("*");
		exportTypeItem.setDefaultToFirstOption(true);
		exportTypeItem.setValueMap(valueMap);

		exportForm.setItems(new FormItem[] { exportTypeItem });

		IButton exportButton = new IButton(dc.get("export"));
		exportButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				String exportAs = (String) exportForm.getField("exportType").getValue();

				ExporterData.export(fileName, listGrid, exportAs, false);
			}
		});
		HLayout formLayout = new HLayout();
		formLayout.addMember(exportForm);
		formLayout.addMember(exportButton);

		VLayout vLayout = new VLayout();
		vLayout.setAutoHeight();
		vLayout.addMember(formLayout);

		return vLayout;
	}

	private static void export(String fileName, ListGrid listGrid, String exportAs, boolean showInWindow) {
		String params = "exportServletType=LGD&fileName=" + URL.encodeQueryString(fileName) + "&showInWindow="
				+ URL.encodeQueryString(new StringBuilder().append(showInWindow).toString()) + "&exportAs=" + exportAs;

		if ("json".equals(exportAs)) {
			params = params + buildContent(ExportUtil.getDataAsJson(listGrid));
		} else if (("csv".equals(exportAs)) || ("xls".equals(exportAs))) {
			params = params + buildContent(ExportUtil.buildCsvContent(listGrid));
		} else if ("ooxml".equals(exportAs)) {
			params = params + "&headers=" + URL.encodeQueryString(ExportUtil.getExcelHeaders(listGrid)) + "&lines="
					+ URL.encodeQueryString(ExportUtil.getExcelLines(listGrid));
		} else if ("xml".equals(exportAs)) {
			params = params + buildContent(ExportUtil.getDataAsXml(listGrid));
		}

		processRequest(params);
	}

	private static String buildContent(String content) {
		return "&content=" + URL.encodeQueryString(content);
	}

	private static void processRequest(String params) {
		try {
			RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.POST, GWT.getModuleBaseURL() + "ExportServlet");
			requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");

			requestBuilder.sendRequest(params, new RequestCallback() {
				public void onResponseReceived(Request request, Response response) {
					if (200 == response.getStatusCode()) {
						ExporterData.callExportServlet.fireEvent(new ClickEvent(ExporterData.callExportServlet.getJsObj()));
					} else {
						MessageUtil.showError("An Error occurred response status code: " + response.getStatusCode());
					}
				}

				public void onError(Request request, Throwable exception) {
					MessageUtil.showError(exception.getMessage());
				}
			});
		} catch (RequestException e) {
			MessageUtil.showError(e.getMessage());
		}
	}

}