<%@ attribute name="propertyPath" required="true" %>
<%@ attribute name="value" required="false" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="jdynatags" prefix="dyna"%>

<c:set var="objectPath" value="${dyna:getObjectPath(propertyPath)}"/>

<spring:bind path="${objectPath}">
    <c:set var="object" value="${status.value}"/>
    <c:if test="${object == null}">
        <%-- Bind ignores the command object prefix, so simple properties of the command object return null above. --%>
        <c:set var="object" value="${commandObject}"/> <%-- We depend on the controller adding this to request. --%>
    </c:if>
</spring:bind>

<c:choose>
	<c:when test="${value != null}">
		<spring:bind path="${propertyPath}">
			<input type="hidden" name="${status.expression}"
				value="${value}" id="${status.expression}" />
			<jsp:doBody />
		</spring:bind>
	</c:when>
	<c:otherwise>
		<spring:bind path="${propertyPath}">
		    <input type="hidden" name="${status.expression}" value="${status.value}" id="${status.expression}"/>
		    <jsp:doBody/>
		</spring:bind>
	</c:otherwise>
</c:choose>