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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import br.com.pegasus.solutions.smartgwt.lib.client.util.StringUtil;

import com.google.gwt.json.client.JSONNumber;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONString;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;

public final class ExportUtil {
	public static String FIELD_NAME_CLASS_SEPARATOR = "#";

	private ExportUtil() {
	}

	public static String getExcelHeaders(ListGrid listGrid) {
		String headers = "";
		ListGridField[] fields = listGrid.getFields();
		for (int i = 0; i < fields.length; i++) {
			ListGridField listGridField = fields[i];
			headers = headers + listGridField.getTitle();
			if (i != fields.length - 1) {
				headers = headers + ",";
			}
		}

		return headers;
	}

	public static String getDataAsJson(ListGrid listGrid) {
		JSONObject jsonObj = new JSONObject();

		ListGridRecord[] records = listGrid.getRecords();
		for (int i = 0; i < records.length; i++) {
			ListGridRecord listGridRecord = records[i];
			ListGridField[] listGridFields = listGrid.getFields();
			JSONObject json = new JSONObject();

			for (int j = 0; j < listGridFields.length; j++) {
				ListGridField listGridField = listGridFields[j];
				String name = listGridField.getName();
				if (name.contains(FIELD_NAME_CLASS_SEPARATOR)) {
					name = name.split(FIELD_NAME_CLASS_SEPARATOR)[0];
				}

				String value = listGridRecord.getAttribute(listGridField.getName());
				if (value != null) {
					try {
						BigInteger number = new BigInteger(value);
						json.put(name, new JSONNumber(number.intValue()));
					} catch (Exception e) {
						try {
							BigDecimal decimal = new BigDecimal(value);
							decimal.setScale(2, RoundingMode.HALF_UP);
							json.put(name, new JSONNumber(decimal.doubleValue()));
						} catch (Exception e2) {
							json.put(name, new JSONString(value));
						}
					}
				} else {
					json.put(name, new JSONString(""));
				}
			}

			jsonObj.put("jsonRecord" + (i + 1), json);
		}

		return jsonObj.toString();
	}

	public static String getDataAsXml(ListGrid listGrid) {
		StringBuilder data = new StringBuilder("<xmlData>\n");

		ListGridRecord[] records = listGrid.getRecords();
		for (int i = 0; i < records.length; i++) {
			ListGridRecord listGridRecord = records[i];
			ListGridField[] listGridFields = listGrid.getFields();

			data.append("<data>\n");
			for (int j = 0; j < listGridFields.length; j++) {
				ListGridField listGridField = listGridFields[j];

				String name = listGridField.getName();
				if (name.contains(FIELD_NAME_CLASS_SEPARATOR)) {
					name = name.split(FIELD_NAME_CLASS_SEPARATOR)[0];
				}

				data.append("<" + name + ">");
				data.append(StringUtil.nullAsEmpty(listGridRecord.getAttribute(listGridField.getName())));
				data.append("</" + name + ">\n");
			}
			data.append("</data>\n");
		}
		data.append("</xmlData>");

		return data.toString();
	}

	public static String getExcelLines(ListGrid listGrid) {
		ListGridRecord[] records = listGrid.getRecords();
		String lines = "";

		for (int i = 0; i < records.length; i++) {
			ListGridRecord listGridRecord = records[i];
			ListGridField[] listGridFields = listGrid.getFields();

			String line = "";
			for (int j = 0; j < listGridFields.length; j++) {
				ListGridField listGridField = listGridFields[j];
				line = line + StringUtil.nullAsEmpty(listGridRecord.getAttribute(listGridField.getName()));

				if (j != listGridFields.length - 1) {
					line = line + ",";
				}
			}
			lines = lines + line;
			if (i != records.length - 1) {
				lines = lines + "\n";
			}
		}

		return lines;
	}

	public static List<String> buildExcelHeaders(ListGrid listGrid) {
		List<String> headers = new ArrayList<String>();

		ListGridField[] fields = listGrid.getFields();
		for (int i = 0; i < fields.length; i++) {
			ListGridField listGridField = fields[i];
			headers.add(listGridField.getTitle());
		}

		return headers;
	}

	public static List<List<String>> buildExcelLines(ListGrid listGrid) {
		ListGridRecord[] records = listGrid.getRecords();
		List<List<String>> lines = new ArrayList<List<String>>();

		for (int i = 0; i < records.length; i++) {
			ListGridRecord listGridRecord = records[i];
			ListGridField[] listGridFields = listGrid.getFields();

			List<String> linesTemp = new ArrayList<String>();
			for (int j = 0; j < listGridFields.length; j++) {
				ListGridField listGridField = listGridFields[j];
				linesTemp.add(StringUtil.nullAsEmpty(listGridRecord.getAttribute(listGridField.getName())));
			}
			lines.add(linesTemp);
		}

		return lines;
	}

	public static String buildCsvContent(ListGrid listGrid) {
		StringBuilder stringBuilder = new StringBuilder();

		ListGridField[] fields = listGrid.getFields();
		for (int i = 0; i < fields.length; i++) {
			ListGridField listGridField = fields[i];
			stringBuilder.append("\"");
			stringBuilder.append(listGridField.getTitle());
			stringBuilder.append("\",");
		}
		stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		stringBuilder.append("\n");

		ListGridRecord[] records = listGrid.getRecords();
		for (int i = 0; i < records.length; i++) {
			ListGridRecord listGridRecord = records[i];
			ListGridField[] listGridFields = listGrid.getFields();
			for (int j = 0; j < listGridFields.length; j++) {
				ListGridField listGridField = listGridFields[j];
				stringBuilder.append("\"");
				stringBuilder.append(StringUtil.nullAsEmpty(listGridRecord.getAttribute(listGridField.getName())));
				stringBuilder.append("\",");
			}
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
			stringBuilder.append("\n");
		}

		return stringBuilder.toString();
	}

}