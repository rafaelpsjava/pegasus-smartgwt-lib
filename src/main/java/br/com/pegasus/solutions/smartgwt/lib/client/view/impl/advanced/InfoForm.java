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
package br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced;

import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.PegasusDynamicForm;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextItem;

public class InfoForm {
	private TextItem firstField = null;
	private TextItem secondField = null;
	private DynamicForm form = null;

	public InfoForm(String firstFieldLabel, String secondFieldLabel, final boolean... disabledField) {
		this.firstField = new TextItem("firstField", firstFieldLabel);
		this.firstField.setWidth(130);

		this.secondField = new TextItem("secondField", secondFieldLabel);
		this.secondField.setWidth(100);

		this.form = new PegasusDynamicForm(335, 100);
		this.form.setAlign(Alignment.RIGHT);
		this.form.setNumCols(4);
		this.form.addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				if (disabledField != null && disabledField.length >= 2) {
					firstField.setDisabled(disabledField[0]);
					secondField.setDisabled(disabledField[1]);
				} else {
					firstField.setDisabled(true);
					secondField.setDisabled(true);
				}
			}
		});
		this.form.setFields(this.firstField, this.secondField);
	}

	/**
	 * enable number filter
	 * 
	 * @param onFirstField
	 *            boolean
	 * @param onSecondField
	 *            boolean
	 * @return void
	 */
	public void enableNumberFilter(boolean onFirstField, boolean onSecondField) {
		if (onFirstField) {
			this.firstField.setKeyPressFilter("[0-9]");
		}
		if (onSecondField) {
			this.secondField.setKeyPressFilter("[0-9]");
		}
	}

	public void setWidth(int widthFirstField, int widthSecondField) {
		this.firstField.setWidth(widthFirstField);
		this.secondField.setWidth(widthSecondField);
		this.form.setWidth(widthFirstField + widthSecondField + 105);
	}

	public TextItem getFirstField() {
		return this.firstField;
	}

	public TextItem getSecondField() {
		return this.secondField;
	}

	public void setFirstFieldText(String text) {
		this.firstField.setValue(text);
	}

	public void setSecondText(String text) {
		this.secondField.setValue(text);
	}

	public String getFirstFieldValue() {
		return this.firstField.getValueAsString();
	}

	public String getSecondFieldValue() {
		return this.secondField.getValueAsString();
	}

	public DynamicForm getForm() {
		return this.form;
	}

}