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

import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.formatter.HourCellFormater;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.formatter.IntegerCellFormatter;

import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.DateDisplayFormat;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.widgets.grid.ListGridField;

public final class ListGridFieldUtil {

	private ListGridFieldUtil() {
	}

	/**
	 * format
	 * 
	 * @param listGridFieldType {@link ListGridFieldType}
	 * @param fields {@link ListGridField}
	 * @return void
	 */
	public static void format(ListGridFieldType listGridFieldType, ListGridField... fields) {
		if (listGridFieldType != null && fields != null) {

			if (ListGridFieldType.TIME == listGridFieldType) {
				HourCellFormater hourCellFormater = HourCellFormater.getInstance();

				for (final ListGridField field : fields) {
					field.setType(ListGridFieldType.TIME);
					field.setDateFormatter(DateDisplayFormat.TOUSSHORTDATETIME);
					field.setCellFormatter(hourCellFormater);
				}
			} else if (ListGridFieldType.INTEGER == listGridFieldType) {
				for (ListGridField field : fields) {
					IntegerCellFormatter integerCellFormatter = IntegerCellFormatter.getInstance();

					field.setType(ListGridFieldType.INTEGER);
					field.setCellFormatter(integerCellFormatter);
				}
			}
		}
	}

	/**
	 * format
	 * 
	 * @param fieldType {@link FieldType}
	 * @param fields {@link DataSourceField}
	 * @return void
	 */
	public static void format(FieldType fieldType, DataSourceField... fields) {
		if (fieldType != null && fields != null) {

			if (FieldType.TIME == fieldType) {
				for (final DataSourceField field : fields) {
					field.setType(FieldType.TIME);
				}
			} else if (FieldType.INTEGER == fieldType) {
				for (DataSourceField field : fields) {
					field.setType(FieldType.INTEGER);
				}
			}
		}
	}

}