package nz.ac.auckland.syllabus.apidoc;

import nz.ac.auckland.syllabus.ApiDoc;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Author: Marnix
 *
 * This class is able to read necessary information from and endpoint request or response object.
 */
public class EndpointData {

	/**
	 * Logger
	 */
	private static final Logger LOG = LoggerFactory.getLogger(EndpointData.class);

	/**
	 * Endpoint class
	 */
	private Class<?> endpointDataClass;

	/**
	 * A list of data elements
	 */
	private List<EndpointDataElement> dataElements;

	/**
	 * Endpoint data class
	 *
	 * @param endpointDataClass is the class to read information from
	 */
	public EndpointData(Class<?> endpointDataClass, List<Class<?>> alsoRead) {
		this.endpointDataClass = endpointDataClass;
		this.dataElements = initDataElements(alsoRead);
	}

	/**
	 * @return the data elements
	 */
	public List<EndpointDataElement> getDataElements() {
		return this.dataElements;
	}

	/**
	 * This method inspects the endpoint data class and generates data elements
	 * for each of the relevant elements
	 *
	 * @return a list of endpoint data element instances
	 */
	public List<EndpointDataElement> initDataElements(List<Class<?>> alsoRead) {

		List<EndpointDataElement> dataElements = new ArrayList<>();

		Method[] methods = this.endpointDataClass.getMethods();
		for (Method method : methods) {
			if (method.getName().startsWith("get") && !bannedMethod(method.getName())) {
				dataElements.add(
						new EndpointDataElement(
								getClassType(method, alsoRead),
								toVariableName(method.getName()),
								getApiDocFor(method),
								getConstraints(method)
						)
				);
			}
		}

		return dataElements;
	}

	/**
	 * Extract enumeration constraints
	 *
	 * @param method is the method to have a look at
	 *
	 * @return a constraints string
	 */
	protected String getConstraints(Method method) {
		Class<?> returnType = method.getReturnType();

		if (returnType.isEnum()) {
			return getEnumerationConstants(returnType);
		}

		Field field = this.getFieldForMethod(method);
		if (field != null && field.getDeclaredAnnotations().length > 0) {
			return getFieldAnnotationConstraints(field);
		}

		return null;
	}


	/**
	 * Boring code to get constraints from the field annotations
	 *
	 * @param field
	 * @return
	 */
	protected String getFieldAnnotationConstraints(Field field) {

		List<String> constraints = new ArrayList<String>();

		if (field.getAnnotation(Email.class) != null) {
			constraints.add("Email");
		}
		if (field.getAnnotation(Future.class) != null) {
			constraints.add("Future date");
		}

		if (field.getAnnotation(NotNull.class) != null) {
			constraints.add("Not null");
		}
		if (field.getAnnotation(Past.class) != null) {
			constraints.add("Past date");
		}
		if (field.getAnnotation(NotEmpty.class) != null) {
			constraints.add("Not empty");
		}
		if (field.getAnnotation(Pattern.class) != null) {
			constraints.add(String.format("Pattern(regexp = '%s')",
					field.getAnnotation(Pattern.class).regexp()
			));
		}
		if (field.getAnnotation(Length.class) != null) {
			constraints.add(String.format("Length(min = %d, max = %d)",
					field.getAnnotation(Length.class).min(),
					field.getAnnotation(Length.class).max()
			));
		}
		if (field.getAnnotation(Size.class) != null) {
			constraints.add(
					String.format(
							"Size(min = %d, max = %d)",
							field.getAnnotation(Size.class).min(),
							field.getAnnotation(Size.class).max()
					)
			);
		}
		if (field.getAnnotation(Range.class) != null) {
			constraints.add(
					String.format(
							"Range(min = %d, max = %d)",
							field.getAnnotation(Range.class).min(),
							field.getAnnotation(Range.class).max()
					)
			);
		}

		if (constraints.size() == 0) {
			return null;
		}

		StringBuffer buff = new StringBuffer();
		Iterator<String> constraintsIt = constraints.iterator();
		while (constraintsIt.hasNext()) {
			String value = constraintsIt.next();
			buff.append(value);
			if (constraintsIt.hasNext()) {
				buff.append(", ");
			}
		}

		return buff.toString();
	}

	/**
	 * Extract the enumeration constants
	 *
	 * @param enumeration is the enumeration
	 * @return is the string with constants
	 */
	protected String getEnumerationConstants(Class<?> enumeration) {
		Object[] constants = enumeration.getEnumConstants();

		int idx = 0;
		StringBuffer strBuff = new StringBuffer();
		for (Object konst : constants) {
			strBuff.append(konst.toString());

			if (idx < constants.length - 1) {
				strBuff.append(", ");
			}

			++idx;
		}

		return strBuff.toString();
	}


	protected String getClassType(Method method, List<Class<?>> alsoRead) {
		Field field = getFieldForMethod(method);
		if (field == null) {
			return "n/a";
		}

		if (List.class.isAssignableFrom(field.getType())) {
			Type generic = field.getGenericType();
			if (listWithSingleGenericType(generic)) {
				Type genericType = ((ParameterizedType) generic).getActualTypeArguments()[0];

				// if the generic type is not specified, we'll just return "list of T"
				if (!(genericType instanceof Class<?>)) {
					return "List of T";
				}
				else {
					Class genericClass = (Class) genericType;
					if (!skipForInspection(genericClass)) {
						alsoRead.add(genericClass);
					}
					return "List of ".concat(genericClass.getSimpleName());
				}
			}
			else {
				return "List";
			}
		}
		else {

			if (!skipForInspection(method.getReturnType())) {
				alsoRead.add(method.getReturnType());
			}

			return method.getReturnType().getSimpleName();
		}

	}

	/**
	 * Is this a simple type?
	 * @param type
	 * @return
	 */
	protected boolean skipForInspection(Class<?> type) {
		return
			type.isEnum() ||
			type.isArray() ||
			type.isPrimitive() ||
			type == Integer.class || type == Double.class || type == Byte.class ||
			type == Boolean.class || (String.class.isAssignableFrom(type)) || type == Long.class ||
			type == Map.class || type == Object.class
		;

	}

	private boolean listWithSingleGenericType(Type generic) {
		return
			generic instanceof ParameterizedType &&
			((ParameterizedType) generic).getActualTypeArguments().length == 1;
	}

	/**
	 * @return the api documentation for this method, tries to look on method, or related variable
	 */
	protected String getApiDocFor(Method method) {
		ApiDoc doc = method.getAnnotation(ApiDoc.class);
		if (doc == null) {
			try {
				Field field = this.getFieldForMethod(method);
				if (field != null) {
					doc = field.getAnnotation(ApiDoc.class);
				}
			}
			catch (Exception ex) {
				LOG.info("Couldn't find field for method: " + method.getName());
				// field doesn't exist.
			}
		}

		return doc != null ? doc.value() : null;
	}


	/**
	 * @return the field that belongs to this getter method
	 */
	protected Field getFieldForMethod(Method method) {
		try {
			String varName = this.toVariableName(method.getName());
			Field field = this.endpointDataClass.getDeclaredField(varName);
			return field;
		}
		catch (Exception ex) {
			// field doesn't exist.
			return null;
		}
	}

	/**
	 * @return true if we should skip this method
	 */
	protected boolean bannedMethod(String methodName) {
		return (
				methodName.equals("getClass") ||
				methodName.equals("getMetaClass") ||
				methodName.equals("getProperty")
		);
	}

	/**
	 * @return the variable name of this get method
	 */
	protected String toVariableName(String getName) {
		return
			("" + getName.charAt(3)).toLowerCase() +
			getName.substring(4);
	}


	/**
	 * @return the endpoint data structure title
	 */
	public String getClassName() {
		return this.endpointDataClass.getSimpleName();
	}

}
