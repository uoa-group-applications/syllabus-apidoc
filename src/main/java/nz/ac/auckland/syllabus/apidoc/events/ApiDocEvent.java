package nz.ac.auckland.syllabus.apidoc.events;

import nz.ac.auckland.syllabus.ApiDoc;
import nz.ac.auckland.syllabus.events.Event;
import nz.ac.auckland.syllabus.events.EventHandler;
import nz.ac.auckland.syllabus.payload.EventRequestBase;
import nz.ac.auckland.syllabus.payload.EventResponseBase;

/**
 * Author: Marnix
 *
 * Test event
 */
@ApiDoc("Api documentation")
@Event(namespace = "apidoc", name = "apidoc-event-test")
public class ApiDocEvent implements EventHandler<ApiDocEvent.Input, ApiDocEvent.Output> {

	public Output handleEvent(Input payload) throws Exception {
		return new Output();
	}

	public static class Input extends EventRequestBase {

		@ApiDoc("API input")
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

	public static class Output extends EventResponseBase {
		String echoOutput;

		@ApiDoc("Echoed output")
		public String getEchoOutput() {
			return null;
		}
	}
}
