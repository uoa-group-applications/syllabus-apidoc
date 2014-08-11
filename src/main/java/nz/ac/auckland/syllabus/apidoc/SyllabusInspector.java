package nz.ac.auckland.syllabus.apidoc;

import nz.ac.auckland.common.stereotypes.UniversityComponent;
import nz.ac.auckland.syllabus.SyllabusContext;
import nz.ac.auckland.syllabus.events.EventHandler;
import nz.ac.auckland.syllabus.events.EventHandlerCollection;
import nz.ac.auckland.syllabus.generator.EventHandlerConfig;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.*;

import static nz.ac.auckland.syllabus.apidoc.utils.GenericUtils.getGenericClassAtIndexForEvent;

/**
 * Author: Marnix
 *
 * The syllabus inspector is able to inspect all the currently registered syllabus events
 * and create an Endpoint class instance from them. These class instances can then be queried
 * for relevant documentation information.
 */
@UniversityComponent
public class SyllabusInspector {

	/**
	 * Index of generic for the input structure of an EventHandler
	 */
	private static final int IDX_INPUT_STRUCT = 0;

	/**
	 * Index of generic for the output structure of an EventHandler
	 */
	private static final int IDX_OUTPUT_STRUCT = 1;

	/**
	 * A list of all available syllabus services
	 */
	@Inject
	private EventHandlerCollection eventHandlerCollection;

	/**
	 * A list of endpoints that each represent a Syllabus event handler
	 */
	private List<Endpoint> endpoint;

	/**
	 * Make sure to create an endpoint structure for each of the loaded Syllabus events
	 */
	@PostConstruct
	public void afterInitialization() {
		endpoint = new ArrayList<Endpoint>();

		// iterate over each handler
		for (EventHandlerConfig handler : eventHandlerCollection.findAll()) {
			Class inputClazz = null;

			// TODO: should push this back into Syllabus Core.
			if (handler.getParamaterTypes().length > 1) {
				if (handler.getParamaterTypes()[0].getTheClass() != SyllabusContext.class) {
					inputClazz = handler.getParamaterTypes()[0].getTheClass();
				} else {
					inputClazz = handler.getParamaterTypes()[1].getTheClass();
				}
			} else if ( handler.getParamaterTypes().length == 1) {
				inputClazz = handler.getParamaterTypes()[0].getTheClass();
			}

			Class outputClazz = handler.getMethod().getReturnType();

			endpoint.add(
				new Endpoint(
					handler.getInstance().getClass(),
					inputClazz,
					outputClazz
				)
			);
		}
	}

	/**
	 * @return a list of the namespaces
	 */
	public Set<String> getNamespaces() {
		Set<String> namespaces = new HashSet<String>();
		for (Endpoint endpoint : this.endpoint) {
			namespaces.add(endpoint.getNamespace());
		}
		return namespaces;
	}

	/**
	 * @return a list of endpoints for a specific namespace
	 */
	public List<Endpoint> getEndpointsInNamespace(String namespace) {
		List<Endpoint> endpointList = new ArrayList<Endpoint>();
		for (Endpoint endpoint : this.endpoint) {
			if (namespace.equals(endpoint.getNamespace())) {
				endpointList.add(endpoint);
			}
		}
		return endpointList;
	}


	/**
	 * @return a mapping of (namespace -> [endpoints])
	 */
	public Map<String, List<Endpoint>> getEndpointsByNamespace() {
		Set<String> namespaces = this.getNamespaces();
		Map<String, List<Endpoint>> endpointsByNs = new LinkedHashMap<String, List<Endpoint>>();

		for (String namespace : namespaces) {
			endpointsByNs.put(namespace, this.getEndpointsInNamespace(namespace));
		}

		return endpointsByNs;
	}

	/**
	 * @return the input class for this event handler
	 */
	protected Class<?> getInputClassForHandler(EventHandler handler) {
		return getGenericClassAtIndexForEvent(handler.getClass(), IDX_INPUT_STRUCT);
	}

	/**
	 * @return the output class for this event handler
	 */
	protected Class<?> getOutputClassForHandler(EventHandler handler) {
		return getGenericClassAtIndexForEvent(handler.getClass(), IDX_OUTPUT_STRUCT);
	}



}
