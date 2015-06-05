package xptopackage.server.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
*/
public final class ReflectionUtil {

	private ReflectionUtil() {
	}

	/**
	 * return the value of a field of a object
	 * 
	 * @param objectInstance {@link Object}
	 * @param fieldName {@link String}
	 * @return {@link Object}
	 */
	public static Object getValue(Object objectInstance, String fieldName) throws Exception {
		Class<?> entityObjectClass = objectInstance.getClass();
		Method getFieldValueMethod = entityObjectClass.getMethod(StringUtil.buildGetMethodName(fieldName));
		getFieldValueMethod.setAccessible(true);
		Object value = getFieldValueMethod.invoke(objectInstance);

		return value;
	}

	/**
	 * set value of field of a objectInstance using the value, you must inform
	 * the correct fieldtype and pass the value with the same type
	 * 
	 * @param objectInstance {@link Object}
	 * @param fieldName {@link String}
	 * @param value {@link Object}
	 * @return void
	 */
	public static void setValue(Object objectInstance, String fieldName, Object value) throws Exception {
		Class<?> objectClass = objectInstance.getClass();
		Field field = objectClass.getDeclaredField(fieldName);
		Class<?> fieldType = field.getType();
		if (value != null && !value.getClass().equals(fieldType)) {
			value = InstanceUtil.cast(value, fieldType);
		}

		Method setFieldValueMethod = objectClass.getMethod(StringUtil.buildSetMethodName(fieldName), fieldType);

		setFieldValueMethod.setAccessible(true);
		setFieldValueMethod.invoke(objectInstance, value);
	}

}