package nz.ac.auckland.syllabus.apidoc;

import nz.ac.auckland.stencil.Path;
import nz.ac.auckland.stencil.Stencil;
import nz.ac.auckland.stencil.StencilService;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * Author: Marnix
 */
@Path("/livedoc")
public class ApiDocStencil implements Stencil {

	/**
	 * Stencil service
	 */
	@Inject private StencilService stencilService;

	@Inject private SyllabusInspector inspector;

	/**
	 * The hook to run when a page has been matched.
	 *
	 * @param request is the httpservletrequest for this particular incoming request
	 * @param response is the httpservletresponse instance for this particular incoming request
	 * @param pathParameters is a key/value-mapping that contains path parameters
	 */
	@Override
	public void render(HttpServletRequest request, HttpServletResponse response, Map<String, String> pathParameters) {

		Map<String, Object> modelMap = new HashMap<String, Object>();
		modelMap.put("title", "Live API doc");
		modelMap.put("namespaces", inspector.getNamespaces());
		modelMap.put("endpointsMap", inspector.getEndpointsByNamespace());

		this.stencilService.renderJsp(request, response, "/WEB-INF/jsp/apidoc/apidoc.jsp", modelMap);

	}
}
