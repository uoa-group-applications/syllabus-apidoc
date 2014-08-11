package nz.ac.auckland.syllabus.apidoc.events;

import nz.ac.auckland.syllabus.ApiDoc;
import nz.ac.auckland.syllabus.events.Event;

/**
 * Author: Marnix
 *
 * Test event
 */
@ApiDoc("Api documentation")
@Event(namespace = "apidoc", name = "apidoc-event-test")
public class ApiDocEvent{

	public Output handleEvent(Input payload) throws Exception {
		return new Output();
	}

	public static class Input{

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

	public static class Output {
		String echoOutput;

		@ApiDoc("Echoed output")
		public String getEchoOutput() {
			return null;
		}
	}
}
