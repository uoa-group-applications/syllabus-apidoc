<div class="well navigation">

    <ul class="nav nav-list stacked">

        <c:forEach items="${endpointsMap}" var="endpointEntry" varStatus="endpointEntryStatus">
            <li class="nav-header">${endpointEntry.key}</li>
            <c:forEach items="${endpointEntry.value}" var="endpoint">
                <li><a href="#${endpoint.id}">${endpoint.name}</a></li>
            </c:forEach>

            <c:if test="${! endpointEntryStatus.last }">
                <li class="divider"><!----></li>
            </c:if>

        </c:forEach>

    </ul>
</div>
