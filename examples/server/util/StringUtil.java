package xptopackage.server.util;

import java.util.HashMap;
import java.util.Map;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public final class StringUtil {

	private StringUtil() {
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
		return isNotEmpty(value) ? Integer.valueOf(Integer.parseInt(value)) : null;
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
	 * return all first token char upper
	 * 
	 * @param str {@link String}
	 * @return {@link String}
	 */
	public static String allFirstTokenCharUpper(String str) {
		if (str != null) {
			String[] tokens = str.split(" ");
			if (tokens != null) {
				StringBuilder result = new StringBuilder("");
				for (String token : tokens) {
					result.append(firstCharUpper(token) + " ");
				}
				return result.toString();
			}
		}

		return null;
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
	 * get string from object as string
	 * 
	 * @param object {@link Object}
	 * @return {@link String}
	 */
	public static String getAsString(Object object) {
		if (object != null) {
			if (object instanceof String) {
				return ((String) object).toString();
			} else if (object instanceof StringBuilder) {
				return ((StringBuilder) object).toString();
			} else if (object instanceof StringBuffer) {
				return ((StringBuffer) object).toString();
			}
		}

		return null;
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
	 * return the string in reverse order
	 * 
	 * @param str {@link String}
	 * @return {@link String}
	 */
	public static String reverse(String str) {
		return str != null ? new StringBuilder(str).reverse().toString() : str;
	}

	/**
	 * return true if the string is equal in any texts
	 * 
	 * @param string {@link String}
	 * @param texts {@link String}...
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
	 * return true if the string is equals to any equalsWithTex
	 * 
	 * @param string {@link String}
	 * @param equalsWithText {@link String}...
	 * @return boolean
	 */
	public static boolean equalsWith(String string, String... equalsWithText) {
		if (equalsWithText != null) {
			for (String str : equalsWithText) {
				if (string.equals(str)) {
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
	 * @param s {@link String}
	 * @return {@link String}
	 */
	public static String removeAccent(final String s) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < s.length(); ++i) {
			char ch = s.charAt(i);
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

	/**
	 * return getCountOfIncidence
	 * 
	 * @return int
	 */
	public static int countIncidence(char c, String str) {
		if (str != null) {
			int count = 0;
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) == c) {
					count++;
				}
			}

			return count;
		}

		return 0;
	}

	/**
	 * return getCountOfIncidence
	 * 
	 * @return int
	 */
	public static int countIncidence(String text, String inString, String tokenToSplit) {
		if (text != null && inString != null) {
			String[] split = inString.split(tokenToSplit);
			int count = 0;
			if (split != null) {
				for (String token : split) {
					if (token.equals(text)) {
						count++;
					}
				}
			}
			return count;
		}

		return 0;
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