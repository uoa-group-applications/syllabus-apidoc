package nz.ac.auckland.syllabus.apidoc;

/**
 * Author: Marnix
 *
 * A description of an endpoint data element
 */
public class EndpointDataElement {

	private String type;
	private String name;
	private String doc;
	private String constraints;

	/**
	 * Initialize data-members
	 *
	 * @param type is the type of element we're adding
	 * @param name is the name of the component we're adding
	 * @param constraints are the constraints that apply to this element
	 */
	public EndpointDataElement(String type, String name, String doc, String constraints) {
		this.type = type;
		this.name = name;
		this.doc = doc;
		this.constraints = constraints;
	}

	public String getConstraints() {
		return constraints;
	}

	public void setConstraints(String constraints) {
		this.constraints = constraints;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}
}
