<%@include file="/libs/harbor/components/global.jsp" %>

<c:choose>
    <c:when test="${column.isInherited}">
        <cq:include path="column-par" resourceType="foundation/components/iparsys" />
    </c:when>
    <c:otherwise>
        <cq:include path="column-par" resourceType="foundation/components/parsys" />
    </c:otherwise>
</c:choose>