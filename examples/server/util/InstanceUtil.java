package x.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  @author Rafael Peres dos Santos - Pegasus Solutions
 *
 * big methods just for performance reasons
*/

public final class InstanceUtil {
	private static final int STATIC_MODIFIER = 26;
	private static final String OBJ_SEPARATOR = "_";

	private Object toInstance;
	private Object fromInstance;
	private boolean printException;
	private boolean fromInstanceIsDTO = false;

	private Class<?> fromClazz;
	private Class<?> toClazz;
	private Field[] fromFields;

	private Method valueMethod;
	private Object fromValue;
	private Map<Field, Boolean> mapFieldsToSetValues;
	private Map<String, Object> mapFieldsToSetValuesObjectRec;

	public InstanceUtil(Object fromInstance, Object toInstance, boolean printException) {
		this.fromInstance = fromInstance;
		this.toInstance = toInstance;
		this.printException = printException;

		if (fromInstance != null && toInstance != null) {
			this.fromClazz = fromInstance.getClass();
			this.toClazz = toInstance.getClass();
			this.fromFields = fromClazz.getDeclaredFields();
			fromInstanceIsDTO = fromClazz.getSimpleName().endsWith("DTO");
		}
	}

	/**
	 * fill the toInstance properties using fromInstance
	 * 
	 * @return {@link Object}
	 */
	public Object fillInstance() {
		if (fromInstance != null && toInstance != null) {
			if (fromFields != null) {
				for (Field fromField : fromFields) {
					try {
						String fieldName = fromField.getName();
						this.valueMethod = null;
						this.fromValue = null;

						this.mapFieldsToSetValues = null;
						this.mapFieldsToSetValuesObjectRec = null;

						if (fromField.getModifiers() != STATIC_MODIFIER) {
							this.valueMethod = fromClazz.getMethod(StringUtil.buildGetMethodName(fieldName));
							valueMethod.setAccessible(true);
							this.fromValue = valueMethod.invoke(fromInstance);

							if (fromValue != null) {
								if (fromInstanceIsDTO) {
									int idxFirstUnderscore = fieldName.indexOf(OBJ_SEPARATOR);

									if (idxFirstUnderscore < 0) {
										if (FieldUtil.isBasicJavaType(fromField)) {
											fillBasePropertyValue(fieldName);
										} else {
											setObjectRec(fromValue, fieldName);
										}
									} else {
										Object instanceFilled = getInstanceFilled(fieldName, fromValue);

										String firstFieldName = fieldName.substring(0, fieldName.indexOf(OBJ_SEPARATOR));
										ReflectionUtil.setValue(toInstance, firstFieldName, instanceFilled);
									}
								} else {
									if (FieldUtil.isBasicJavaType(fromField)) {
										Field toField = toClazz.getDeclaredField(fieldName);
										if (valueMethod.getReturnType() != toField.getType()) {
											fromValue = cast(fromValue, toField.getType());
										}

										Method method = toClazz.getMethod(StringUtil.buildSetMethodName(fieldName), toField.getType());
										method.setAccessible(true);
										method.invoke(toInstance, fromValue);
									} else {
										setObjectRec(fromValue, fieldName);

										setFieldsRec(fromValue, "");
									}
								}
							}
						}
					} catch (Exception e) {
						if (printException) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		return toInstance;
	}

	private void setObjectRec(Object object, String fieldName) {
		if (object != null) {
			Field[] fields = object.getClass().getDeclaredFields();
			if (fields != null) {
				for (Field fromField : fields) {
					if (fromField.getModifiers() != STATIC_MODIFIER) {
						String fromFieldName = fromField.getName();
						if (FieldUtil.isBasicJavaType(fromField)) {
							try {
								Object valueFrom = ReflectionUtil.getValue(object, fromFieldName);
								if (valueFrom != null) {
									if (!fromInstanceIsDTO) {
										String sufix = OBJ_SEPARATOR + fromField.getName();

										Field toField = toInstance.getClass().getDeclaredField(fieldName + sufix);
										if (fromField.getType() != toField.getType()) {
											valueFrom = cast(valueFrom, toField.getType());
										}
										ReflectionUtil.setValue(toInstance, fieldName + sufix, valueFrom);

									} else {
										String[] tokens = fieldName.split(OBJ_SEPARATOR);
										Method method1 = toInstance.getClass().getMethod(StringUtil.buildGetMethodName(tokens[0]));
										Object object1 = method1.invoke(toInstance);
										Object instance = object1 == null ? method1.getReturnType().newInstance() : object1;
										Object lastInstance = instance;

										if (tokens.length == 1) {
											Field toField = instance.getClass().getDeclaredField(fromFieldName);
											if (FieldUtil.isBasicJavaType(toField)) {
												if (fromField.getType() != toField.getType()) {
													valueFrom = cast(valueFrom, toField.getType());
												}

												ReflectionUtil.setValue(instance, fromFieldName, valueFrom);
												ReflectionUtil.setValue(toInstance, fieldName, instance);
											}
										} else {
											for (int i = 1; i < tokens.length; i++) {
												String methodXYName = StringUtil.buildGetMethodName(tokens[i]);
												Method methodXY = lastInstance == null ? instance.getClass().getMethod(methodXYName) : lastInstance.getClass()
														.getMethod(methodXYName);
												if (methodXY != null) {
													Object currentObjectXY = methodXY.invoke(lastInstance);
													currentObjectXY = currentObjectXY != null ? currentObjectXY : methodXY.getReturnType().newInstance();

													if (i == (tokens.length - 1)) {
														ReflectionUtil.setValue(currentObjectXY, fromFieldName, valueFrom);
													}

													ReflectionUtil.setValue(lastInstance, tokens[i], currentObjectXY);
													if (lastInstance != null) {
														lastInstance = currentObjectXY;
													}
												}
											}
										}
									}
								}
							} catch (Exception e) {
							}
						} else {
							try {
								Object obj = ReflectionUtil.getValue(object, fromFieldName);
								if (obj == null && object != null) {
									Method method = object.getClass().getMethod(StringUtil.buildGetMethodName(fromFieldName));
									obj = method.getReturnType().newInstance();
								}
								// setObjectRec(obj, fieldName + OBJ_SEPARATOR +
								// fromField.getName());

								if (mapFieldsToSetValuesObjectRec == null) {
									mapFieldsToSetValuesObjectRec = new HashMap<String, Object>();
								}

								String key = fieldName + OBJ_SEPARATOR + fromField.getName();

								int countOfIncidence = StringUtil.countIncidence(fromField.getName(), key, OBJ_SEPARATOR);
								if (countOfIncidence <= 2) {
									if (!mapFieldsToSetValuesObjectRec.containsKey(key)) {
										List<Object> values = new ArrayList<Object>();
										values.add(obj);
										values.add(Boolean.FALSE);

										mapFieldsToSetValuesObjectRec.put(key, values);
									}
								}
							} catch (Exception e) {
								if (printException) {
									e.printStackTrace();
								}
							}
						}
					}
				}
			}

			if (mapFieldsToSetValuesObjectRec != null && mapFieldsToSetValuesObjectRec.size() > 0) {
				for (String fieldPath : mapFieldsToSetValuesObjectRec.keySet()) {
					@SuppressWarnings("unchecked")
					List<Object> values = (List<Object>) mapFieldsToSetValuesObjectRec.get(fieldPath);
					if (values != null) {
						Object obj = values.get(0);
						Boolean taskDone = (Boolean) values.get(1);
						if (!taskDone) {
							values.set(1, Boolean.TRUE);
							mapFieldsToSetValuesObjectRec.put(fieldPath, values);

							setObjectRec(obj, fieldPath);
						}
					}
				}
			}
		}
	}

	/**
	 * setFieldsRec
	 * 
	 * Date as Date, Number as String, Boolean as Boolean, String as String
	 * fields types are set of an user object
	 * 
	 * @param object {@link Object}
	 * @param path {@link String}
	 * @throws Exception
	 */
	private void setFieldsRec(Object object, String path) throws Exception {
		if (object != null) {
			Field[] fields = object.getClass().getDeclaredFields();

			if (fields != null) {
				for (Field field : fields) {
					if (field.getModifiers() != STATIC_MODIFIER) {
						if (FieldUtil.isBasicJavaType(field)) {
							String fieldName = null;
							if (path.equals("")) {
								fieldName = StringUtil.firstCharLower(object.getClass().getSimpleName()) + OBJ_SEPARATOR + field.getName();
							} else {
								fieldName = path + OBJ_SEPARATOR + StringUtil.firstCharLower(field.getName());
							}

							Method getValueMethod = object.getClass().getMethod(StringUtil.buildGetMethodName(field.getName()));
							Object value = getValueMethod.invoke(object);

							// just set values for Number or String or Date
							// instances
							if (value != null) {
								try {
									// value = getValueConverted(value);
									try {
										Class<?> toClassFieldType = toClazz.getDeclaredField(fieldName).getType();
										if (!getValueMethod.getReturnType().equals(toClassFieldType)) {
											value = cast(value, toClassFieldType);
										}

										Method method = toClazz.getMethod(StringUtil.buildSetMethodName(fieldName), getValueMethod.getReturnType());
										method.setAccessible(true);
										method.invoke(toInstance, value);

									} catch (Exception e) {

										Method[] methods = toClazz.getMethods();
										if (methods != null) {
											Method methodFound = null;
											for (Method method : methods) {
												try {
													String methodNameToCheck = method.getName().replace("get", "").replace("set", "");
													if (methodNameToCheck.contains(OBJ_SEPARATOR)) {
														methodNameToCheck = methodNameToCheck.split(OBJ_SEPARATOR)[0];
														Method methodToCheck = fromClazz.getMethod(StringUtil.buildGetMethodName(methodNameToCheck));

														if (methodToCheck.getReturnType().equals(object.getClass())) {
															methodFound = methodToCheck;
															break;
														}
													}
												} catch (Exception ex) {
												}
											}
											if (methodFound != null) {
												String methodName = null;
												if (path.equals("")) {
													methodName = methodFound.getName().replace("get", "") + OBJ_SEPARATOR + field.getName();
												} else {
													methodName = methodFound.getName().replace("get", "") + OBJ_SEPARATOR
															+ path.substring(path.indexOf(OBJ_SEPARATOR) + 1) + OBJ_SEPARATOR + field.getName();
												}
												Method method = toClazz.getMethod(StringUtil.buildSetMethodName(methodName), value.getClass());
												method.setAccessible(true);
												method.invoke(toInstance, value);
											}
										}
									}
								} catch (Exception e) {
									if (printException) {
										e.printStackTrace();
									}
								}
							}
						} else {
							if (mapFieldsToSetValues == null) {
								mapFieldsToSetValues = new HashMap<Field, Boolean>();
							}
							mapFieldsToSetValues.put(field, false);
						}
					}
				}
			}
			if (mapFieldsToSetValues != null) {
				for (Field field : mapFieldsToSetValues.keySet()) {
					if (!mapFieldsToSetValues.get(field)) {
						mapFieldsToSetValues.put(field, true);
						prepareToCallSetFieldsRec(object, path, field);
					}
				}
			}
		}
	}

	/**
	 * prepareToCallSetFieldsRec
	 * 
	 * @param object {@link Object}
	 * @param path {@link String}
	 * @param field {@link Field}
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws Exception
	 */
	private void prepareToCallSetFieldsRec(Object object, String path, Field field) throws NoSuchMethodException, IllegalAccessException,
			InvocationTargetException, Exception {
		Method methodValue = object.getClass().getMethod(StringUtil.buildGetMethodName(field.getName()));
		Object value = methodValue.invoke(object);

		if (value != null) {
			String objSimpleName = StringUtil.firstCharLower(value.getClass().getSimpleName());

			boolean hasCiclicReference = false;
			if (!"".equals(path)) {
				hasCiclicReference = false;

				int a = path.indexOf(objSimpleName + OBJ_SEPARATOR);
				int b = path.indexOf(OBJ_SEPARATOR + objSimpleName);
				int c = path.lastIndexOf(OBJ_SEPARATOR + objSimpleName);

				// two levels of the same object are allowed not more
				hasCiclicReference = (a >= 0 && b >= 0 && c > b);
			}

			if (value != null && !hasCiclicReference) {
				if ("".equals(path)) {
					path = StringUtil.firstCharLower(object.getClass().getSimpleName()) + OBJ_SEPARATOR
							+ StringUtil.firstCharLower(value.getClass().getSimpleName());

					setFieldsRec(value, path);

				} else {
					setFieldsRec(value, path + OBJ_SEPARATOR + StringUtil.firstCharLower(value.getClass().getSimpleName()));
				}
			}
		}
	}

	/**
	 * 
	 * @param fieldName {@link String}
	 * @param idxFirstUnderscore int
	 * @param idxLastUnderscore int
	 * @param fromValue {@link Object}
	 * @return {@link Object}
	 * @throws Exception
	 */
	public Object getInstanceFilled(String fieldName, Object fromValue) throws Exception {
		int idxFirstUnderscore = fieldName.indexOf(OBJ_SEPARATOR), idxLastUnderscore = fieldName.lastIndexOf(OBJ_SEPARATOR);
		int startIdx = 0;
		int temp = 0;
		boolean firstTime = true;
		Object firstObject = null, lastObject = null;

		for (int i = idxFirstUnderscore; i > 0 && startIdx < idxLastUnderscore && i <= idxLastUnderscore;) {
			String objectClazzName = fieldName.substring(startIdx, i);
			String currentObjectName = fieldName.substring(startIdx, i);

			if (firstTime) {
				// get first object in to instance if has a value do not create
				// a new instance just use the old
				Object value = ReflectionUtil.getValue(toInstance, currentObjectName);
				if (value != null) {
					firstObject = value;
				} else {
					Method method = toClazz.getMethod(StringUtil.buildGetMethodName(objectClazzName));
					method.setAccessible(true);
					Object newInstance = method.getReturnType().newInstance();
					firstObject = newInstance;
				}
				lastObject = firstObject;
				firstTime = false;
			} else {
				// get in last object if has a value do not create a new
				// instance just use the old
				Object value = ReflectionUtil.getValue(lastObject, currentObjectName);
				if (value != null) {
					ReflectionUtil.setValue(lastObject, currentObjectName, value);
					lastObject = value;
				} else {
					Method method = lastObject.getClass().getMethod(StringUtil.buildGetMethodName(objectClazzName));

					method.setAccessible(true);
					Object newInstance = method.getReturnType().newInstance();

					ReflectionUtil.setValue(lastObject, currentObjectName, newInstance);
					lastObject = newInstance;
				}
			}

			temp = i;
			i = fieldName.indexOf(OBJ_SEPARATOR, i + 1);

			if (i > 0 && temp != i) {
				startIdx = temp + 1;
			}
		}

		// in the last object set from value
		String lastFieldName = fieldName.substring(fieldName.lastIndexOf(OBJ_SEPARATOR) + 1);
		ReflectionUtil.setValue(lastObject, lastFieldName, fromValue);

		return firstObject;
	}

	/**
	 * @param fieldName {@link String}
	 * @throws Exception
	 */
	private void fillBasePropertyValue(String fieldName) throws Exception {
		Field toField = toClazz.getDeclaredField(fieldName);
		if (valueMethod.getReturnType() != toField.getType()) {
			fromValue = cast(fromValue, toField.getType());
		}

		Method toSetMethod = toClazz.getMethod(StringUtil.buildSetMethodName(fieldName), toField.getType());
		toSetMethod.setAccessible(true);
		toSetMethod.invoke(toInstance, fromValue);
	}

	/**
	 * return the string as a class content if it any probaly throw a
	 * runtimeexception if you pass a incorrect content of class, example if you
	 * call like IntanceUtil.buildInstance(Integer.class, "aaaa"); it will throw
	 * an runtime exception
	 * 
	 * @param valueObj {@link Object}
	 * @param type {@link Class}<?>
	 * @return {@link Object}
	 */
	public static Object cast(Object valueObj, Class<?> type) {
		if (type == null) {
			throw new RuntimeException("the type is required!");
		}
		if (valueObj != null) {
			if (valueObj instanceof Number && type.equals(String.class)) {
				return ((Number) valueObj).toString();
			} else if (valueObj instanceof String) {
				String value = (String) valueObj;
				if (value != null) {
					if (type.equals(String.class)) {
						return value;
					} else if (type.equals(Character.class)) {
						return value.toString().charAt(0);
					} else if (type.equals(BigInteger.class)) {
						return new BigInteger(value);

					} else if (type.equals(BigDecimal.class)) {
						return new BigDecimal(value);

					} else if (type.equals(Integer.class)) {
						return Integer.parseInt(value);
					} else if (type.equals(Long.class)) {
						return new Long(value);

					} else if (type.equals(Float.class)) {
						return new Float(value);

					} else if (type.equals(Double.class)) {
						return new Double(value);

					} else if (type.equals(Boolean.class)) {
						return Boolean.valueOf(value);

					} else if (type.equals(StringBuilder.class)) {
						return new StringBuilder((String) value);

					} else if (type.equals(StringBuffer.class)) {
						return new StringBuffer((String) value);

					}
				}
			} else if (valueObj instanceof Boolean && type.equals(String.class)) {
				return valueObj.toString();

			} else if (valueObj instanceof Date) {
				return (Date) valueObj;

			} else if (valueObj instanceof Character && (type.equals(Character.class) || type.equals(String.class))) {
				return valueObj.toString();

			}
		}
		return valueObj;
	}

}
