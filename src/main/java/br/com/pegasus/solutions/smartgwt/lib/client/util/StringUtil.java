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
package br.com.pegasus.solutions.smartgwt.lib.client.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class StringUtil {
	
	private StringUtil() {
	}

	/**
	 * found max string length
	 * 
	 * @param string {@link String}
	 * @return int
	 */
	private int foundMaxStringLength(String string) {
		String[] tokens = string.split(",");
		if (tokens != null && tokens.length > 0) {
			int maxStringLength = tokens[0].length();
			for (int i = 1; i < tokens.length; i++) {
				if (maxStringLength < tokens[i].length()) {
					maxStringLength = tokens[i].length();
				}
			}
			return maxStringLength;
		}

		return 0;
	}

	/**
	 * Convert all tag symbols ( &lt; and &gt; ) into displayable HTML by
	 * changing them to &amp;lt; and &amp;gt; respectively.
	 * 
	 * @param str the string to convert
	 * @param prefix text to tack onto the beginning of result (eg:
	 * "&lt;PRE&gt;")
	 * @param suffix text to tack onto the end of result (eg: "&lt;/PRE&gt;")
	 * @return prefix + converted text + suffix as a single string
	 */
	public static native String convertTags(String str, String prefix, String suffix)/*-{
																						return str == null ? null : new $wnd.String(str).convertTags(prefix, suffix);
																						}-*/;

	/**
	 * Convert plain text into into displayable HTML. <p/> This prevents
	 * HTML-special characters like &lt; and &gt; from being interpreted as
	 * tags, and preserves line breaks and extra spacing.
	 * 
	 * <pre> converts to -------- --------------------------- & &amp; < &lt; >
	 * &gt; \r,\n,\r\n1space <BR>&nbsp; \r,\n,\r\n <BR> \t
	 * &nbsp;&nbsp;&nbsp;&nbsp; 2 spaces 1space&nbsp; </pre>
	 * 
	 * @param str the string to convert
	 * @return the plain text into into displayable HTM
	 */
	public static native String asHTML(String str)/*-{
													return str == null ? null : new $wnd.String(str).asHTML();
													}-*/;

	/**
	 * Convert plain text into into displayable HTML. <p/> This prevents
	 * HTML-special characters like &lt; and &gt; from being interpreted as
	 * tags, and preserves line breaks and extra spacing.
	 * 
	 * <pre> converts to -------- --------------------------- & &amp; < &lt; >
	 * &gt; \r,\n,\r\n1space <BR>&nbsp; \r,\n,\r\n <BR> \t
	 * &nbsp;&nbsp;&nbsp;&nbsp; 2 spaces 1space&nbsp; </pre>
	 * 
	 * @param str the string to convert
	 * @param noAutoWrap
	 * @return the plain text into into displayable HTM
	 */
	public static native String asHTML(String str, boolean noAutoWrap)/*-{
																		return str == null ? null : new $wnd.String(str).asHTML(noAutoWrap);
																		}-*/;

	/**
	 * Reverses {@link #asHTML(String)}.
	 * 
	 * @param str the input str
	 * @return unescaped HTML
	 */
	public static native String unescapeHTML(String str)/*-{
														return str == null ? null : new $wnd.String(str).unescapeHTML();
														}-*/;

	/**
	 * Escapes special characters in XML values - so called 'unparsed data'
	 * 
	 * <pre> " -> &quot; ' -> &apos; & -> &amp; < -> &lt; > -> &gt; \r ->
	 * &x000D; </pre>
	 * 
	 * @param str the String to escape
	 * @return xml safe String
	 */
	public static native String makeXMLSafe(String str)/*-{
														return str == null ? null : $wnd.isc.makeXMLSafe(str);
														}-*/;

	/**
	 * Abbreviates a String using ellipses. StringUtil.abbreviate("abcdefg", 6)
	 * = "abc..."
	 * 
	 * @param str the String to abbreviate
	 * @param maxWidth maximum length of result String, must be at least 4
	 * @return the abbreviated String
	 * @throws IllegalArgumentException when the width is too small
	 */
	public static String abbreviate(String str, int maxWidth) {
		if (str == null) {
			return null;
		}
		if (str.length() < maxWidth) {
			return str;
		}
		if (maxWidth < 4) {
			throw new IllegalArgumentException("Minimum required width is 4");
		}

		return str.substring(0, maxWidth - 3) + "...";
	}

	/**
	 * null as empty
	 * 
	 * @param obj {@link String}
	 * @return {@link Object}
	 */
	public static String nullAsEmpty(String obj) {
		if (obj != null && obj instanceof String) {
			return (String) obj;
		}

		return "";
	}

	/**
	 * split 
	 * 
	 * @param string {@link String}
	 * @param separator char
	 * @return {@link String}[]
	 */
	public static String[] split(String string, char separator) {
		List<String> tokens = new ArrayList<String>();
		int idxBeforeComma = 0;
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == separator) {
				tokens.add(string.substring(idxBeforeComma, i));
				idxBeforeComma = i + 1;
			}
		}

		return tokens.toArray(new String[] {});
	}

	/**
	 * return true if is not empty the str
	 * 
	 * @param str {@link String}
	 * @return boolean
	 */
	public static boolean isNotEmpty(String str) {
		return str != null && !str.trim().isEmpty();
	}

	/**
	 * return true if is empty the str
	 * 
	 * @param str {@link String}
	 * @return boolean
	 */
	public static boolean isEmpty(String str) {
		return str == null || str.trim().isEmpty();
	}

	/**
	 * return true if is empty the CharSequence
	 * 
	 * @param cs {@link CharSequence}
	 * @return boolean
	 */
	public static boolean isEmpty(CharSequence cs) {
		return cs == null || cs.length() == 0;
	}

	/**
	 * return true if the str has the quantityOfChars
	 * 
	 * @param str {@link String}
	 * @param quantityOfChars int
	 * @return boolean
	 */
	public static boolean hasAtLeast(String str, int quantityOfChars) {
		return isNotEmpty(str) && str.trim().length() >= quantityOfChars;
	}

	/**
	 * convert the value to integer , it may throw a ParseException
	 * 
	 * @param value {@link String}
	 * @return {@link Integer}
	 */
	public static Integer convertStringToInteger(String value) {
		return isNotEmpty(value) ? Integer.parseInt(value) : null;
	}

	/**
	 * return the str with the firstcharupper
	 * 
	 * @param str {@link String}
	 * @return {@link String}
	 */
	public static String firstCharUpper(String str) {
		return isNotEmpty(str) ? str.substring(0, 1).toUpperCase() + str.substring(1) : str;
	}

	/**
	 * return str with the firstcharlower
	 * 
	 * @param str {@link String}
	 * @return {@link String}
	 */
	public static String firstCharLower(String str) {
		if (str.length() == 0) {
			throw new IllegalArgumentException("The String must have the length higher than 0");
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1, str.length());
	}

	/**
	 * return empty string as null
	 * 
	 * @param string {@link String}
	 * @return {@link String}
	 */
	public static String emptyAsNull(String string) {
		return isEmpty(string) ? null : string;
	}

	/**
	 * return null string as empty
	 * 
	 * @param text {@link String}
	 * @return {@link String}
	 */
	public static String getNullAsEmptyString(String text) {
		return text == null ? "" : text;
	}

	/**
	 * return true if the string is equal in any texts
	 * 
	 * @param string {@link String}
	 * @param texts {@link String}
	 * @return boolean
	 */
	public static boolean isEqual(String string, String... texts) {
		if (texts != null) {
			for (String text : texts) {
				if (string.equals(text)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * return true if the string endsWith in any endsWithText
	 * 
	 * @param string {@link String}
	 * @param endsWithText {@link String}...
	 * @return boolean
	 */
	public static boolean endsWith(String string, String... endsWithText) {
		if (endsWithText != null) {
			for (String endWithText : endsWithText) {
				if (string.endsWith(endWithText)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * return true if the string contains any texts
	 * 
	 * @param string {@link String}
	 * @param texts {@link String}...
	 * @return boolean
	 */
	public static boolean contains(String string, String... texts) {
		if (texts != null) {
			for (String text : texts) {
				if (string.contains(text)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * remove the start text from the string
	 * 
	 * @param str {@link String}
	 * @param remove {@link String}
	 * @return {@link String}
	 */
	public static String removeStart(String str, String remove) {
		if (isEmpty(str) || isEmpty(remove)) {
			return str;
		}
		if (str.startsWith(remove)) {
			return str.substring(remove.length());
		}
		return str;
	}

	/**
	 * build get method name and return it
	 * 
	 * @param fieldName {@link String}
	 * @return {@link String}
	 */
	public static String buildGetMethodName(String fieldName) {
		return "get" + StringUtil.firstCharUpper(fieldName);
	}

	/**
	 * build set method name and return it
	 * 
	 * @param fieldName {@link String}
	 * @return {@link String}
	 */
	public static String buildSetMethodName(String fieldName) {
		return "set" + StringUtil.firstCharUpper(fieldName);
	}

	/**
	 * replace html special chars to punctuation
	 * 
	 * @param str {@link String}
	 * @return {@link String}
	 */
	public static String replaceHtmlSpecialCharsSequence(String str) {
		if (isNotEmpty(str)) {
			for (String specialHtmlChar : specialHtmlCharactersMap.keySet()) {
				str = str.replace(specialHtmlChar, specialHtmlCharactersMap.get(specialHtmlChar));
			}
		}
		return str;
	}

	/**
	 * remove accent
	 * 
	 * @param str {@link String}
	 * @return {@link String}
	 */
	public static String removeAccent(final String str) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); ++i) {
			char ch = str.charAt(i);
			if (ch < 256) {
				sb.append(tabelaAcento[ch]);
			} else {
				sb.append(ch);
			}
		}
		return sb.toString();
	}

	/**
	 * return a string replacing the backward slashs to foward slashs
	 * 
	 * @param path {@link String}
	 * @return {@link String}
	 */
	public static String forwardSlash(String path) {
		if (path != null) {
			while (path.contains("\\")) {
				path = path.replace("\\", "/");
			}
		}

		return path;
	}

	/**
	 * return a string replacing the forward slashs to backward slashs
	 * 
	 * @param path {@link String}
	 * @return {@link String}
	 */
	public static String backwardSlash(String path) {
		if (path != null) {
			while (path.contains("/")) {
				path = path.replace("/", "\\");
			}
		}

		return path;
	}

	public static final String acentuado = "çÇáéíóúýÁÉÍÓÚÝàèìòùÀÈÌÒÙãõñäëïöüÿÄËÏÖÜÃÕÑâêîôûÂÊÎÔÛ";
	public static final String semAcento = "cCaeiouyAEIOUYaeiouAEIOUaonaeiouyAEIOUAONaeiouAEIOU";
	private static Map<String, String> specialHtmlCharactersMap = new HashMap<String, String>();
	private static char[] tabelaAcento = new char[256];
	static {
		synchronized (StringUtil.class) {
			for (int i = 0; i < tabelaAcento.length; ++i) {
				tabelaAcento[i] = (char) i;
			}
			for (int i = 0; i < acentuado.length(); ++i) {
				tabelaAcento[acentuado.charAt(i)] = semAcento.charAt(i);
			}
		}
	}

	static {
		synchronized (StringUtil.class) {
			specialHtmlCharactersMap.put("&#39;", "'");
			specialHtmlCharactersMap.put("&quot;", "\"");
			specialHtmlCharactersMap.put("&ldquo;", "“");
			specialHtmlCharactersMap.put("&rdquo;", "”");
			specialHtmlCharactersMap.put("&lsquo;", "‘");
			specialHtmlCharactersMap.put("&rsquo;", "’");
			specialHtmlCharactersMap.put("&ndash;", "–");
			specialHtmlCharactersMap.put("&mdash;", "—");
			specialHtmlCharactersMap.put("&iexcl;", "¡");
			specialHtmlCharactersMap.put("&iquest;", "¿");
			specialHtmlCharactersMap.put("&AElig;", "Æ");
			specialHtmlCharactersMap.put("&aelig;", "æ");
			specialHtmlCharactersMap.put("&Auml;", "Ä");
			specialHtmlCharactersMap.put("&Euml;", "Ë");
			specialHtmlCharactersMap.put("&Iuml;", "Ï");
			specialHtmlCharactersMap.put("&Ouml;", "Ö");
			specialHtmlCharactersMap.put("&Uuml;", "Ü");
			specialHtmlCharactersMap.put("&amp;", "&");
			specialHtmlCharactersMap.put("&gt;", ">");
			specialHtmlCharactersMap.put("&lt;", "<");
			specialHtmlCharactersMap.put("&middot;", ".");
			specialHtmlCharactersMap.put("&reg;", "®");
			specialHtmlCharactersMap.put("&aacute;", "á");
			specialHtmlCharactersMap.put("&Aacute;", "Á");
			specialHtmlCharactersMap.put("&agrave;", "à");
			specialHtmlCharactersMap.put("&Agrave;", "À");
			specialHtmlCharactersMap.put("&acirc;", "â");
			specialHtmlCharactersMap.put("&Acirc;", "Â");
			specialHtmlCharactersMap.put("&aring;", "å");
			specialHtmlCharactersMap.put("&Aring;", "Å");
			specialHtmlCharactersMap.put("&atilde;", "ã");
			specialHtmlCharactersMap.put("&Atilde;", "Ã");
			specialHtmlCharactersMap.put("&auml;", "ä");
			specialHtmlCharactersMap.put("&ccedil;", "ç");
			specialHtmlCharactersMap.put("&Ccedil;", "Ç");
			specialHtmlCharactersMap.put("&eacute;", "é");
			specialHtmlCharactersMap.put("&Eacute;", "É");
			specialHtmlCharactersMap.put("&egrave;", "è");
			specialHtmlCharactersMap.put("&Egrave;", "È");
			specialHtmlCharactersMap.put("&ecirc;", "ê");
			specialHtmlCharactersMap.put("&Ecirc;", "Ê");
			specialHtmlCharactersMap.put("&iacute;", "í");
			specialHtmlCharactersMap.put("&Iacute;", "Í");
			specialHtmlCharactersMap.put("&igrave;", "ì");
			specialHtmlCharactersMap.put("&Igrave;", "Ì");
			specialHtmlCharactersMap.put("&icirc;", "î");
			specialHtmlCharactersMap.put("&Icirc;", "Î");
			specialHtmlCharactersMap.put("&ntilde;", "ñ");
			specialHtmlCharactersMap.put("&Ntilde;", "Ñ");
			specialHtmlCharactersMap.put("&oacute;", "ó");
			specialHtmlCharactersMap.put("&Oacute;", "Ó");
			specialHtmlCharactersMap.put("&ograve;", "ò");
			specialHtmlCharactersMap.put("&Ograve;", "Ò");
			specialHtmlCharactersMap.put("&ocirc;", "ô");
			specialHtmlCharactersMap.put("&Ocirc;", "Ô");
			specialHtmlCharactersMap.put("&otilde;", "õ");
			specialHtmlCharactersMap.put("&Otilde;", "Õ");
			specialHtmlCharactersMap.put("&uacute;", "ú");
			specialHtmlCharactersMap.put("&Uacute;", "Ú");
			specialHtmlCharactersMap.put("&ugrave;", "ù");
			specialHtmlCharactersMap.put("&Ugrave;", "Ù");
			specialHtmlCharactersMap.put("&ucirc;", "û");
			specialHtmlCharactersMap.put("&Ucirc;", "Û");
			specialHtmlCharactersMap.put("&uuml;", "ü");
			specialHtmlCharactersMap.put("&yuml;", "ÿ");
			specialHtmlCharactersMap.put("&Ocirc;", "Ô");
			specialHtmlCharactersMap.put("&#180;", "´");
			specialHtmlCharactersMap.put("&#96;", "`");
			specialHtmlCharactersMap.put("&cent;", "¢");
			specialHtmlCharactersMap.put("&copy;", "©");
			specialHtmlCharactersMap.put("&divide;", "÷");
			specialHtmlCharactersMap.put("&micro;", "µ");
			specialHtmlCharactersMap.put("&plusmn;", "±");
			specialHtmlCharactersMap.put("&euro;", "€");
			specialHtmlCharactersMap.put("&pound;", "£");
			specialHtmlCharactersMap.put("&sect;", "§");
			specialHtmlCharactersMap.put("&trade;", "™");
			specialHtmlCharactersMap.put("&yen;", "¥");
			specialHtmlCharactersMap.put("&para;;", "¶");
			specialHtmlCharactersMap.put("&laquo;", "«");
			specialHtmlCharactersMap.put("&raquo;", "»");
			specialHtmlCharactersMap.put("&nbsp;", " ");
		}
	}

}