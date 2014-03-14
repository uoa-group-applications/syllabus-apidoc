<table class="table table-striped">
    <thead>
        <tr>
            <th style="width: 30%">Field</th>
            <th style="width: 20%">Type</th>
            <th style="width: 50%">Constraints</th>
        </tr>
    </thead>
    <tbody>
        <c:choose>
            <c:when test="${empty type.dataElements}">
                <tr>
                    <td colspan="3">
                        <p class="muted">
                            No fields found.
                        </p>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>

                <c:forEach items="${type.dataElements}" var="data">
                    <tr>
                        <td><spring:escapeBody>${data.name}</spring:escapeBody></td>
                        <td><spring:escapeBody>${data.type}</spring:escapeBody></td>
                        <td><spring:escapeBody>${data.constraints}</spring:escapeBody></td>
                    </tr>
                    <c:if test="${data.doc}">
                        <tr>
                            <td><!----></td>
                            <td colspan="2">
                                <p class="muted">
                                    <spring:escapeBody>${data.doc}</spring:escapeBody>
                                </p>
                            </td>
                        </tr>
                    </c:if>
                </c:forEach>

            </c:otherwise>
        </c:choose>
    </tbody>
</table>