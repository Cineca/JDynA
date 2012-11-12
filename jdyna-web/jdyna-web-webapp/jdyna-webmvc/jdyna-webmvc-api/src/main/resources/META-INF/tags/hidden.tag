<%--
-JDynA, Dynamic Metadata Management for Java Domain Object
-
- Copyright (c) 2008, CILEA and third-party contributors as
- indicated by the @author tags or express copyright attribution
- statements applied by the authors.  All third-party contributions are
- distributed under license by CILEA.
-
- This copyrighted material is made available to anyone wishing to use, modify,
- copy, or redistribute it subject to the terms and conditions of the GNU
- Lesser General Public License v3 or any later version, as published 
- by the Free Software Foundation, Inc. <http://fsf.org/>.
-
- This program is distributed in the hope that it will be useful,
- but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
- or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
- for more details.
-
-  You should have received a copy of the GNU Lesser General Public License
-  along with this distribution; if not, write to:
-  Free Software Foundation, Inc.
-  51 Franklin Street, Fifth Floor
-  Boston, MA  02110-1301  USA
--%>
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
