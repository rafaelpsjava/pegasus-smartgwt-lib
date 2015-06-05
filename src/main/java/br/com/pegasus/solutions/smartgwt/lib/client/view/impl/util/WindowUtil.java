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

public final class WindowUtil {

	/**
	 * open page in new tab
	 * 
	 * safari open a new browser by default, you should change this in
	 * preferences, tabs, Open pages in tabs instead of windows: Automatically"
	 * 
	 * @param url
	 *            {@link String}
	 * @return void
	 */
	public static void openPageInNewTab(String url) {
		com.google.gwt.user.client.Window.open(url, "_blank", "");
	}

	/**
	 * open page in new tab null
	 * 
	 * detail when you call this function in addClickHandler of ToolStripButton
	 * this open a page in the current tab
	 * 
	 * @param url
	 *            {@link String}
	 * @return void
	 */
	public static void openPageInNewTabNull(String url) {
		com.google.gwt.user.client.Window.open(url, null, null);
	}

	/**
	 * 
	 * open page in new tab self
	 * 
	 * @param url
	 *            {@link String}
	 * @return void
	 */
	public static void openPageInNewTabSelf(String url) {
		com.google.gwt.user.client.Window.open(url, "_self", "");
	}

}