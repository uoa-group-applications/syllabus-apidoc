<h1>${title}</h1>
<p>
    This is live application documentation for the current running API.
</p>

<p>Published namespaces:</p>
<ol>
    <c:forEach items="${namespaces}" var="namespace">
        <li><a href="#${namespace}">${namespace}</a></li>
    </c:forEach>
</ol>

<c:forEach items="${endpointsMap}" var="endpointEntry" varStatus="endpointEntryStatus">
    <a name="${endpointEntry.key}"><!----></a>
    <h2>${endpointEntry.key}</h2>

    <p>An overview of the endpoints found in the <code>${endpointEntry.key}</code> namespace</p>

    <c:forEach items="${endpointEntry.value}" var="endpoint">
        <div class="endpoint">
            <h4><a name="${endpoint.id}"><!----></a>${endpoint.name}</h4>
            <p>
                <c:choose>
                    <c:when test="${!empty endpoint.purpose}">
                        <spring:escapeBody>${endpoint.purpose}</spring:escapeBody>
                    </c:when>
                    <c:otherwise>
                        This endpoint does not have a <code>@ApiDoc</code> annotation, so no documentation
                        can be displayed at this point.
                    </c:otherwise>
                </c:choose>
            </p>

            <c:forEach items="${endpoint.inputDataList}" var="inputData" varStatus="status">

                <c:choose>

                    <c:when test="${status.first}">
                        <%-- show the input elements --%>
                        <p class="with-icon">
                            <i class="icon-arrow-right"></i> Input type ${inputData.className}:
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p>${inputData.className}:</p>
                    </c:otherwise>
                </c:choose>

                <c:set var="type" value="${inputData}" />
                <%@ include file="_typeDescription.jsp" %>
            </c:forEach>

            <c:forEach items="${endpoint.outputDataList}" var="outputData" varStatus="status">

                <c:choose>

                    <c:when test="${status.first}">
                        <%-- show the output elements --%>
                        <p class="with-icon">
                            <i class="icon-arrow-left"></i> Output type ${outputData.className}:
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p>${outputData.className}:</p>
                    </c:otherwise>
                </c:choose>

                <c:set var="type" value="${outputData}" />
                <%@ include file="_typeDescription.jsp" %>
            </c:forEach>
        </div>

    </c:forEach>

</c:forEach>