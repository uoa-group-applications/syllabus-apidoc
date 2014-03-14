package nz.ac.auckland.syllabus.apidoc.utils;

import nz.ac.auckland.syllabus.events.EventHandler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author: Marnix
 *
 * Generic related utility functions
 */
public class GenericUtils {

	private GenericUtils() {
		// helper
	}


	/**
	 * Dig up the inheritance tree to find the actual class at which the event handler is defined
	 *
	 * @param leafClass is the leafclass to start digging up from
	 * @return a parameterized type instance that defines the eventhandler.
	 */
	public static ParameterizedType getEventHandlerClass(Class<?> leafClass) {

		for (Type interfaceType : leafClass.getGenericInterfaces()) {
			if (isEventHandlerInterface(interfaceType)) {
				return (ParameterizedType) interfaceType;
			}
		}

		return null;
	}

	/**
	 * @return true if this is the event handler level we're interested in
	 */
	public static boolean isEventHandlerInterface(Type interfaceType) {
		return
			interfaceType instanceof ParameterizedType &&
			((ParameterizedType) interfaceType).getRawType() == EventHandler.class;
	}

	/**
	 * Get the first parameterized type
	 *
	 * @param leafClass is the class to start at
	 * @return the parameterized type or null
	 */
	public static ParameterizedType getFirstParameterizedType(Class<?> leafClass) {
		for (Type interfaceType : leafClass.getGenericInterfaces()) {
			if (interfaceType instanceof ParameterizedType) {
				return (ParameterizedType) interfaceType;
			}
		}

		return null;
	}

	/**
	 * Get the class instance of a specific generic index in the baseclass passed to this method.
	 *
	 * @param baseClass is the class to read from
	 * @param genericIdx is the index of the generic parameter's class we want
	 * @return the generic class
	 */
	public static Class<?> getGenericClassAtIndex(ParameterizedType baseClass, int genericIdx) {
		if (baseClass == null) {
			throw new IllegalArgumentException("Base class cannot be null");
		}
		if (genericIdx < 0) {
			throw new IllegalArgumentException("The generic index cannot be smaller than zero");
		}

		// make sure we can actually retrieve the index we need
		if (baseClass.getActualTypeArguments().length <= genericIdx) {
			throw new IllegalArgumentException("Index cannot be retrieved, type does not have enough");
		}

		// get the correct generic instance, drill up a level when it's a parameterized type (generic in generic)
		Type genInstance = baseClass.getActualTypeArguments()[genericIdx];
		if (genInstance instanceof ParameterizedType) {
			genInstance = ((ParameterizedType) genInstance).getRawType();
		}

		// return its class
		return (Class<?>) genInstance;
	}

	/**
	 * Get the class instance of a specific generic index in the baseclass passed to this method.
	 *
	 * @param baseClass is the class to read from
	 * @param genericIdx is the index of the generic parameter's class we want
	 * @return the generic class
	 */
	public static Class<?> getGenericClassAtIndexForEvent(Class<?> baseClass, int genericIdx) {
		if (baseClass == null) {
			throw new IllegalArgumentException("Base class cannot be null");
		}
		if (genericIdx < 0) {
			throw new IllegalArgumentException("The generic index cannot be smaller than zero");
		}

		// get to the level at which this class is defined as an EventHandler
		ParameterizedType eventHandlerType = getEventHandlerClass(baseClass);
		if (eventHandlerType == null) {
			throw new IllegalStateException(
					"Could not find event handler class for this base class, not an EventHandler."
			);
		}

		// make sure we can actually retrieve the index we need
		if (eventHandlerType.getActualTypeArguments().length <= genericIdx) {
			throw new IllegalArgumentException("Index cannot be retrieved, type does not have enough");
		}

		// get the correct generic instance, drill up a level when it's a parameterized type (generic in generic)
		Type genInstance = eventHandlerType.getActualTypeArguments()[genericIdx];
		if (genInstance instanceof ParameterizedType) {
			genInstance = ((ParameterizedType) genInstance).getRawType();
		}

		// return its class
		return (Class<?>) genInstance;
	}

}
