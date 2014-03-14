package nz.ac.auckland.syllabus.apidoc;

import nz.ac.auckland.syllabus.ApiDoc;
import nz.ac.auckland.syllabus.events.Event;

import java.util.*;

/**
 * Author: Marnix
 *
 * Contains endpoint information
 */
public class Endpoint {

	//
	// Classes the endpoint wrapper can read from
	//

	private Class<?> endpoint;
	private Class<?> inputStruct;
	private Class<?> outputStruct;

	/**
	 * Wrapper for endpoint input data
	 */
	private List<EndpointData> inputDataList;

	/**
	 * Wrapper for endpoint output data
	 */
	private List<EndpointData> outputDataList;

	/**
	 * Initialize data-members
	 *
	 * @param endpoint is the endpoint class to inspect
	 * @param inputStruct is the input structure class for this endpoint.
	 * @param outputStruct is the output structure class for this endpoint.
	 */
	public Endpoint(Class<?> endpoint, Class<?> inputStruct, Class<?> outputStruct) {

		this.endpoint = endpoint;

		this.inputStruct = inputStruct;
		this.outputStruct = outputStruct;

		this.inputDataList = this.getInputDataList();
		this.outputDataList = this.getOutputDataList();

	}

	/**
	 * @return the unique endpoint identifier
	 */
	public String getId() {
		return Integer.toString(this.endpoint.hashCode());
	}

	/**
	 * @return the purpose of the event (@ApiDoc contents)
	 */
	public String getPurpose() {
		ApiDoc apiDocAnnotation = this.endpoint.getAnnotation(ApiDoc.class);
		if (apiDocAnnotation == null) {
			return null;
		}
		return apiDocAnnotation.value();
	}

	/**
	 * @return the namespace of this event
	 */
	public String getNamespace() {
		Event eventAnnotation = this.endpoint.getAnnotation(Event.class);
		if (eventAnnotation == null) {
			return "n/a";
		}
		return eventAnnotation.namespace();
	}


	/**
	 * @return the name of the event
	 */
	public String getName() {
		Event eventAnnotation = this.endpoint.getAnnotation(Event.class);
		if (eventAnnotation == null) {
			return "n/a";
		}
		return eventAnnotation.name();
	}

	/**
	 * @return the input data structure, create a new instance if necessary
	 */
	public List<EndpointData> getInputDataList() {
		if (this.inputDataList == null) {

			this.inputDataList = new ArrayList<EndpointData>();

			List<Class<?>> alsoRead = new ArrayList<Class<?>>();
			alsoRead.add(this.inputStruct);

			// not empty? process next.
			while (alsoRead.size() > 0) {
				Class<?> currentInput = alsoRead.get(0);
				alsoRead.remove(0);
				this.inputDataList.add(new EndpointData(currentInput, alsoRead));
			}
		}

		return this.inputDataList;
	}


	/**
	 * @return the output data structure, create a new instance if necessary
	 */
	public List<EndpointData> getOutputDataList() {
		if (this.outputDataList == null) {

			this.outputDataList = new ArrayList<EndpointData>();

			List<Class<?>> alsoRead = new ArrayList<Class<?>>();
			alsoRead.add(this.outputStruct);

			// not empty? process next.
			while (alsoRead.size() > 0) {
				Class<?> currentOutput = alsoRead.get(0);
				alsoRead.remove(0);
				this.outputDataList.add(new EndpointData(currentOutput, alsoRead));
			}
		}

		return this.outputDataList;
	}

}
