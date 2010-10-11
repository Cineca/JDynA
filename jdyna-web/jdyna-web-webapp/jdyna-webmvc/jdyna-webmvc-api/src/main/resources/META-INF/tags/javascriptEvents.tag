<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ attribute name="onclick" required="false"%>
<%@ attribute name="onblur" required="false"%>
<%@ attribute name="onchange" required="false"%>
<%@ attribute name="ondblclick" required="false"%>
<%@ attribute name="onkeydown" required="false"%>
<%@ attribute name="onkeyup" required="false"%>
<%@ attribute name="onkeypress" required="false"%>
<%@ attribute name="onfocus" required="false"%>
<%@ attribute name="onhelp" required="false"%>
<%@ attribute name="onselect" required="false"%>
<%@ attribute name="onmouseup" required="false"%>
<%@ attribute name="onmousedown" required="false"%>
<%@ attribute name="onmouseout" required="false"%>
<%@ attribute name="onmousemove" required="false"%>
<%@ attribute name="onmouseover" required="false"%>

<c:if test="${onclick != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onclick=\"${onclick}\"" />
</c:if>

<c:if test="${onblur != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onblur=\"${onblur}\"" />
</c:if>

<c:if test="${onchange != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onchange=\"${onchange}\"" />
</c:if>

<c:if test="${onclick != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onclick=\"${onclick}\"" />
</c:if>

<c:if test="${ondblclick != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}ondblclick=\"${ondblclick}\"" />
</c:if>

<c:if test="${onkeydown != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onkeydown=\"${onkeydown}\"" />
</c:if>

<c:if test="${onkeyup != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onkeyup=\"${onkeyup}\"" />
</c:if>

<c:if test="${onkeypress != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onkeypress=\"${onkeypress}\"" />
</c:if>

<c:if test="${onfocus != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onfocus=\"${onfocus}\"" />
</c:if>

<c:if test="${onhelp != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onhelp=\"${onhelp}\"" />
</c:if>

<c:if test="${onselect != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onselect=\"${onselect}\"" />
</c:if>

<c:if test="${onmouseup != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onmouseup=\"${onmouseup}\"" />
</c:if>

<c:if test="${onmousedown != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onmousedown=\"${onmousedown}\"" />
</c:if>

<c:if test="${onmouseout != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onmouseout=\"${onmouseout}" />
</c:if>

<c:if test="${onmousemove != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onmousemove=\"${onmousemove}\"" />
</c:if>

<c:if test="${onmouseover != null}">
	<c:set var="javascriptEvents" value="${javascriptEvents}onmouseover=\"${onmouseover}\"" />
</c:if>

<c:out value="${javascriptEvents}" escapeXml="false" />