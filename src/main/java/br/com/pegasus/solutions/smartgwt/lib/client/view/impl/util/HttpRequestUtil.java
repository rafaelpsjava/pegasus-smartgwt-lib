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
package br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util;

import br.com.pegasus.solutions.smartgwt.lib.client.util.StringUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.IRequestBuilderFailedAction;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.IRequestBuilderSucessAction;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Window;

public final class HttpRequestUtil {

	private HttpRequestUtil() {
	}

	/**
	 * open windown
	 * 
	 * @param url {@link String}
	 * @param name {@link String}
	 * @return void
	 */
	public static void openWindow(String url, String name) {
		Window.open(url, name, null);
	}

	/**
	 * open windown
	 * 
	 * @param servlet {@link String}
	 * @param params {@link String}
	 * @param name {@link String}
	 * @return void
	 */
	public static void openWindow(String servlet, String params, String name) {
		String url = "";
		if (!StringUtil.isEmpty(servlet)) {
			url = servlet;
			if (!StringUtil.isEmpty(params)) {
				url = url + "?" + params;
			}
		} else if (!StringUtil.isEmpty(params)) {
			url = url + params;
		}

		openWindow(GWT.getModuleBaseURL() + url, name);
	}

	/**
	 * make servlet request
	 * 
	 * @param servletName {@link String}
	 * @param params {@link String}
	 * @param iRequestBuilderSucessAction {@link IRequestBuilderFailedAction}
	 * @param iFailedAction {@link IRequestBuilderFailedAction}
	 * @throws RequestException
	 * @return void
	 */
	public static void doPostServletRequest(String servletName, String params, final IRequestBuilderSucessAction iRequestBuilderSucessAction,
			final IRequestBuilderFailedAction iFailedAction) throws RequestException {

		doRequest(iRequestBuilderSucessAction, iFailedAction, GWT.getModuleBaseURL() + servletName, params, RequestBuilder.POST);
	}

	/**
	 * do request
	 * 
	 * @param servletName {@link String}
	 * @param params {@link String}
	 * @param iRequestBuilderSucessAction {@link IRequestBuilderFailedAction}
	 * @param iFailedAction {@link IRequestBuilderFailedAction}
	 * @throws RequestException
	 * @return void
	 */
	private static void doRequest(final IRequestBuilderSucessAction iRequestBuilderSucessAction, final IRequestBuilderFailedAction iFailedAction, String url,
			String params, RequestBuilder.Method method) throws RequestException {

		RequestBuilder requestBuilder = new RequestBuilder(method, url);
		requestBuilder.setHeader("Content-type", "application/x-www-form-urlencoded");
		requestBuilder.sendRequest(params, new RequestCallback() {
			public void onResponseReceived(Request request, Response response) {
				if (200 == response.getStatusCode() && iRequestBuilderSucessAction != null) {
					iRequestBuilderSucessAction.executeAction(request, response);
				}
			}

			public void onError(Request request, Throwable exception) {
				if (iFailedAction != null) {
					iFailedAction.executeAction(request, exception);
				} else {
					MessageUtil.showError(exception.getMessage());
				}
			}
		});
	}
}