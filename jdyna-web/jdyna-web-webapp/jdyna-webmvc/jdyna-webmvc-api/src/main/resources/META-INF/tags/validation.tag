<%@ attribute name="propertyPath" required="true"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%@ include file="/META-INF/taglibs4dynatag.jsp"%>

<spring:bind path="${propertyPath}">
	<c:if
		test="${empty status.errorMessage}">
		<c:set var="visibility" value="style=\"display: none;\"" />
	</c:if>	
		<span class="fieldError" id="error${status.expression}" ${visibility}><img
			id="errorImage${status.expression}" src="<c:url value="/image/icons/error.png"/>" alt="error"/>
			<a id="errorMsg${status.expression}" >${status.errorMessage}</a></span>
	
</spring:bind>
