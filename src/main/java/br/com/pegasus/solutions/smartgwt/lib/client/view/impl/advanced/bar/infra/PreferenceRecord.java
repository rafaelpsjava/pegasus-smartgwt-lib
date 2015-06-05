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

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class PreferenceRecord extends ListGridRecord {

	public PreferenceRecord() {
	}

	public PreferenceRecord(String pk, String name, String viewState) {
		setPk(pk);
		setName(name);
		setViewState(viewState);
	}

	public String getPk() {
		return getAttributeAsString("pk");
	}

	public void setPk(String pk) {
		setAttribute("pk", pk);
	}

	public String getName() {
		return getAttribute("name");
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getViewState() {
		return getAttribute("viewState");
	}

	public void setViewState(String viewState) {
		setAttribute("viewState", viewState);
	}
}