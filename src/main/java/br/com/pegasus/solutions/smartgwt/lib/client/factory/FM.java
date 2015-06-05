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
package br.com.pegasus.solutions.smartgwt.lib.client.factory;

import java.util.Date;
import java.util.LinkedHashMap;

import br.com.pegasus.solutions.smartgwt.lib.client.rpc.impl.GenericGwtRpcDataSource;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.field.CheckboxItem;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.field.FindTextItem;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.field.NumericStringField;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.History;
import com.smartgwt.client.data.fields.DataSourceBooleanField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceIntegerField;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.fields.BooleanItem;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.FormItemIcon;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

@SuppressWarnings("rawtypes")
public class FM {

	private FM() {
	}

	public static ListGridField newListGridField(GenericGwtRpcDataSource ds, String fieldId) {
		return new ListGridField(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replace("id_", "")));
	}

	public static DataSourceTextField newPKDataSourceTextField(Dictionary dc, String fieldId) {
		DataSourceTextField field = newDataSourceTextField(dc.get(fieldId), true, false);
		return field;
	}

	public static DataSourceDateField newPKDataSourceDateField(Dictionary dc, String fieldId) {
		DataSourceDateField field = new DataSourceDateField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		field.setPrimaryKey(true);
		field.setRequired(true);

		return field;
	}

	public static DataSourceBooleanField newPKDataSourceBooleanField(Dictionary dc, String fieldId) {
		DataSourceBooleanField field = new DataSourceBooleanField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		field.setPrimaryKey(true);
		field.setRequired(true);

		return field;
	}

	public static DataSourceTextField newDataSourceTextField(String fieldId) {
		DataSourceTextField field = newDataSourceTextField(fieldId, false, false);
		return field;
	}

	public static DataSourceTextField newDataSourceTextField(String fieldId, boolean isPrimaryKey) {
		DataSourceTextField field = newDataSourceTextField(fieldId, isPrimaryKey, isPrimaryKey);
		return field;
	}

	public static DataSourceTextField newDataSourceTextField(String fieldId, boolean isPrimaryKey, boolean isRequired) {
		DataSourceTextField field = new DataSourceTextField(fieldId);
		field.setPrimaryKey(isPrimaryKey);
		field.setRequired(isRequired);

		return field;
	}

	public static DataSourceDateField newDataSourceDateField(Dictionary dc, String fieldId) {
		DataSourceDateField field = new DataSourceDateField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		return field;
	}

	public static DataSourceDateField newDataSourceDateField(Dictionary dc, String fieldId, boolean isRequired) {
		DataSourceDateField field = new DataSourceDateField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		field.setRequired(isRequired);
		return field;
	}

	public static DataSourceBooleanField newDataSourceBooleanField(Dictionary dc, String fieldId) {
		DataSourceBooleanField field = new DataSourceBooleanField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		return field;
	}

	public static DataSourceBooleanField newDataSourceBooleanField(Dictionary dc, String fieldId, boolean isRequired) {
		DataSourceBooleanField field = new DataSourceBooleanField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		field.setRequired(isRequired);
		return field;
	}

	public static DataSourceIntegerField newDataSourceIntegerField(Dictionary dc, String fieldId, boolean isRequired) {
		DataSourceIntegerField field = new DataSourceIntegerField(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
		field.setRequired(isRequired);
		return field;
	}

	public static IntegerItem newIntegerItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new IntegerItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static TextItem newTextItem(Dictionary dc, String fieldId) {
		return new TextItem(dc.get(fieldId), dc.get(fieldId.replaceFirst("id_", "")));
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new TextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static TextItem newTextItem(boolean isToSetTitle, GenericGwtRpcDataSource ds, String fieldId) {
		if (isToSetTitle) {
			return new TextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		}

		return new TextItem(ds.getDicValue(fieldId), "");
	}

	public static TextAreaItem newTextAreaItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new TextAreaItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static TextAreaItem newTextAreaItem(GenericGwtRpcDataSource ds, String fieldId, boolean isRequired, boolean isVisible) {
		TextAreaItem textAreaItem = new TextAreaItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		textAreaItem.setRequired(isRequired);
		textAreaItem.setVisible(isVisible);

		return textAreaItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, boolean isRequired) {
		TextItem textItem = newTextItem(ds, fieldId);
		textItem.setRequired(isRequired);
		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, boolean isRequired, boolean isVisible) {
		TextItem textItem = newTextItem(ds, fieldId);
		textItem.setRequired(isRequired);
		textItem.setVisible(isVisible);
		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int width, boolean isRequired, boolean isVisible) {
		TextItem textItem = newTextItem(ds, fieldId);
		textItem.setRequired(isRequired);
		textItem.setVisible(isVisible);
		textItem.setWidth(width);

		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int width, int length, boolean isRequired, boolean isVisible) {
		TextItem textItem = newTextItem(ds, fieldId);
		textItem.setRequired(isRequired);
		textItem.setVisible(isVisible);
		textItem.setWidth(width);
		textItem.setLength(length);

		return textItem;
	}

	public static PasswordItem newPasswordItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new PasswordItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static PasswordItem newPasswordItem(GenericGwtRpcDataSource ds, String fieldId, int width) {
		PasswordItem passwordItem = new PasswordItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		passwordItem.setWidth(width);

		return passwordItem;
	}

	public static PasswordItem newPasswordItem(GenericGwtRpcDataSource ds, String fieldId, boolean isRequired) {
		PasswordItem passwordItem = newPasswordItem(ds, fieldId);
		passwordItem.setRequired(isRequired);

		return passwordItem;
	}

	public static PasswordItem newPasswordItem(GenericGwtRpcDataSource ds, String fieldId, int width, boolean isRequired) {
		PasswordItem passwordItem = newPasswordItem(ds, fieldId, width);
		passwordItem.setRequired(isRequired);

		return passwordItem;
	}

	public static BooleanItem newBooleanItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new BooleanItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static BooleanItem newBooleanItem(GenericGwtRpcDataSource ds, String fieldId, boolean isRequired) {
		BooleanItem booleanItem = newBooleanItem(ds, fieldId);
		booleanItem.setRequired(isRequired);
		return booleanItem;
	}

	public static DateItem newDateField(GenericGwtRpcDataSource ds, String fieldId) {
		DateItem dateItem = new DateItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		Date date = getDateYear0();
		dateItem.setStartDate(date);

		Date endDate = getDateYear3000();
		dateItem.setEndDate(endDate);

		dateItem.setUseTextField(true);
		dateItem.setEnforceDate(false);
		dateItem.setValue((String) null);

		return dateItem;
	}

	@SuppressWarnings("deprecation")
	public static Date getDateYear0() {
		Date date = new Date();
		date.setYear(-1899);
		return date;
	}

	@SuppressWarnings("deprecation")
	public static Date getToday() {
		Date date = new Date();
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);

		return date;
	}

	@SuppressWarnings("deprecation")
	public static Date getDateYear3000() {
		Date endDate = new Date();
		endDate.setYear(1100);
		return endDate;
	}

	public static DateItem newDateField(GenericGwtRpcDataSource ds, String fieldId, boolean isRequired) {
		DateItem dateItem = newDateField(ds, fieldId);
		dateItem.setRequired(isRequired);

		return dateItem;
	}

	public static NumericStringField newNumericStringField(GenericGwtRpcDataSource ds, String fieldId) {
		return new NumericStringField(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static NumericStringField newNumericStringField(GenericGwtRpcDataSource ds, String fieldId, boolean isVisible) {
		NumericStringField numericStringField = newNumericStringField(ds, fieldId);
		numericStringField.setVisible(isVisible);
		return numericStringField;
	}

	public static CheckboxItem newCheckBoxItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new CheckboxItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static NumericStringField newNumericStringField(GenericGwtRpcDataSource ds, String fieldId, boolean isVisible, boolean isRequired) {
		NumericStringField newNumericStringField = newNumericStringField(ds, fieldId, isVisible);
		newNumericStringField.setRequired(isRequired);
		return newNumericStringField;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int widht) {
		TextItem textItem = new TextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		textItem.setWidth(widht);

		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int widht, boolean isRequired) {
		TextItem textItem = newTextItem(ds, fieldId, widht);
		textItem.setRequired(isRequired);
		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int widht, String defaultValue) {
		TextItem textItem = newTextItem(ds, fieldId, widht);
		textItem.setDefaultValue(defaultValue);

		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int widht, String defaultValue, boolean isVisible) {
		TextItem textItem = newTextItem(ds, fieldId, widht, defaultValue);
		textItem.setVisible(isVisible);

		return textItem;
	}

	public static TextItem newTextItem(GenericGwtRpcDataSource ds, String fieldId, int widht, String defaultValue, boolean isVisible, boolean isRequired) {
		TextItem textItem = newTextItem(ds, fieldId, widht, defaultValue, isVisible);
		textItem.setRequired(isRequired);
		return textItem;
	}

	public static SelectItem newSelectItem(GenericGwtRpcDataSource ds, String fieldId, LinkedHashMap<String, String> gwtVersionMap) {
		SelectItem selectItem = new SelectItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		selectItem.setValueMap(gwtVersionMap);
		return selectItem;
	}

	public static SelectItem newSelectItem(GenericGwtRpcDataSource ds, String fieldId, LinkedHashMap<String, String> gwtVersionMap, boolean isRequired) {
		SelectItem selectItem = newSelectItem(ds, fieldId, gwtVersionMap);
		selectItem.setRequired(isRequired);
		return selectItem;
	}

	public static ToolStripButton builToolStripButton(String title, String icon, ClickHandler clickHandler) {
		ToolStripButton buttonItem = new ToolStripButton(title, icon);
		buttonItem.addClickHandler(clickHandler);

		return buttonItem;
	}

	public static TextAreaItem newTextAreaItem(GenericGwtRpcDataSource ds, String fieldId, int width, boolean isRequired, boolean isVisible) {
		TextAreaItem textAreaItem = new TextAreaItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
		textAreaItem.setRequired(isRequired);
		textAreaItem.setVisible(isVisible);
		textAreaItem.setWidth(width);

		return textAreaItem;
	}

	public static Progressbar buildProgressBar() {
		return buildProgressBar(14, 175);
	}

	public static Progressbar buildProgressBar(int length) {
		return buildProgressBar(14, length);
	}

	public static Progressbar buildProgressBar(int height, int length) {
		Progressbar progressbar = new Progressbar();
		progressbar.setVertical(false);
		progressbar.setHeight(height);
		progressbar.setLength(length);

		return progressbar;
	}

	public static BooleanCallback newSessionTimeOutCallback() {
		BooleanCallback sessionTimeOutCallback = new BooleanCallback() {
			@Override
			public void execute(Boolean value) {
				History.newItem("main", false);
				com.google.gwt.user.client.Window.Location.reload();
			}
		};

		return sessionTimeOutCallback;
	}

	public static FormItemIcon newPercentIcon() {
		FormItemIcon icon = new FormItemIcon();
		icon.setSrc("icons/16/percent_background_green.png");

		return icon;
	}

	public static FindTextItem newFindTextItem(GenericGwtRpcDataSource ds, String fieldId) {
		return new FindTextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")));
	}

	public static FindTextItem newFindTextItem(GenericGwtRpcDataSource ds, String fieldId, boolean disabled) {
		return new FindTextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")), disabled);
	}

	public static FindTextItem newFindTextItem(GenericGwtRpcDataSource ds, String fieldId, boolean disabled, String nodeId) {
		return new FindTextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")), disabled, nodeId);
	}

	public static FindTextItem newFindTextItem(GenericGwtRpcDataSource ds, String fieldId, boolean disabled, boolean isVisible) {
		FindTextItem textItem = new FindTextItem(ds.getDicValue(fieldId), ds.getDicValue(fieldId.replaceFirst("id_", "")), disabled);
		textItem.setVisible(isVisible);
		return textItem;
	}

}