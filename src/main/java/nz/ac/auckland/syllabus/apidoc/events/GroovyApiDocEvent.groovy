package nz.ac.auckland.syllabus.apidoc.events

import nz.ac.auckland.syllabus.ApiDoc
import nz.ac.auckland.syllabus.events.Event
import nz.ac.auckland.syllabus.events.EventHandler
import nz.ac.auckland.syllabus.payload.EventRequestBase
import nz.ac.auckland.syllabus.payload.EventResponseBase

/**
 * Author: Marnix
 *
 */
@Event(namespace = "apidoc", name="groovy-apidoc-event-test")
class GroovyApiDocEvent {

	public Output handleEvent(Input payload) throws Exception {
		return new Output();
	}

	public static class Input {

		@ApiDoc("API input on variable")
		String input;

		String output;

		public String getInput() {
			return "input";
		}

		@ApiDoc("Api output prefix")
		public String getOutput() {
			return "output";
		}
	}

	public static class Output {
		String echoOutput;

		@ApiDoc("Echoed output")
		public String getEchoOutput() {
			return null;
		}
	}
}