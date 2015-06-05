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

import java.math.BigDecimal;
import java.math.RoundingMode;

import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.util.StringUtil;

import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.KeyUpEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyUpHandler;
import com.smartgwt.client.widgets.form.validator.LengthRangeValidator;
import com.smartgwt.client.widgets.form.validator.RegExpValidator;
import com.smartgwt.client.widgets.form.validator.Validator;

public final class ValidatorUtil {

	private ValidatorUtil() {
	}

	/**
	 * get mail validator
	 * 
	 * @return {@link Validator}
	 */
	public static Validator getMailValidator() {
		Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);
		RegExpValidator emailValidator = new RegExpValidator();
		emailValidator.setExpression("^([a-zA-Z0-9_.\\-+])+@(([a-zA-Z0-9\\-])+\\.)+[a-zA-Z0-9]{2,4}$");
		emailValidator.setErrorMessage(StringUtil.replaceHtmlSpecialCharsSequence(dc.get("invalid_email_address")));

		return emailValidator;
	}

	/**
	 * build max min validator
	 * 
	 * @param min
	 *            int
	 * @param max
	 *            int
	 * @return {@link Validator}
	 */
	public static Validator buildMaxMinValidator(int min, int max) {
		LengthRangeValidator lengthRangeValidator = new LengthRangeValidator();
		lengthRangeValidator.setMin(min);
		lengthRangeValidator.setMax(max);

		return lengthRangeValidator;
	}

	/**
	 * verify on change if is bigdecimal
	 * 
	 * @param field
	 *            {@link TextItem}
	 */
	public static void verifyOnChangeIfIsBigDecimal(final TextItem... fields) {
		if (fields != null) {
			for (final TextItem field : fields) {
				field.addKeyUpHandler(new KeyUpHandler() {
					@Override
					public void onKeyUp(KeyUpEvent event) {
						if (!StringUtil.isEmpty(field.getValueAsString())) {
							try {
								field.setValue(field.getValueAsString().replaceAll("\\.+", "\\.").replaceAll("\\,+", "\\.").replaceAll("[a-zA-Z ]*", ""));

								if (!field.getValueAsString().endsWith(".")) {
									BigDecimal bigDecimal = new BigDecimal(field.getValueAsString());
									bigDecimal.setScale(2, RoundingMode.HALF_UP);
									field.setValue(bigDecimal.toString());
								}

							} catch (Exception e) {
								field.setValue("");
							}
						}
					}
				});
			}
		}
	}
}
