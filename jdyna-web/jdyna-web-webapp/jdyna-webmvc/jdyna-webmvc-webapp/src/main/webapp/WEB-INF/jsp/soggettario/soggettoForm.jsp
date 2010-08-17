<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/scripts.jsp"%>


<c:set var="commandObject" value="${soggetto}" scope="request" />

<form:form commandName="soggetto" method="post">
	<spring:bind path="soggetto.*">
		<c:if test="${not empty status.errorMessages}">
			<div class="error"><c:forEach var="error"
				items="${status.errorMessages}">
	               ${error}<br />
			</c:forEach></div>
		</c:if>
	</spring:bind>


	<c:if test="${not empty status.errorMessages}">
		<div class="error"><c:forEach var="error"
			items="${status.errorMessages}">
                 ${error}<br />
		</c:forEach></div>
	</c:if>
	
	<fieldset><legend><fmt:message
		key="title.soggetto.fieldset.inserimento" /></legend>
			
			<dyna:text  propertyPath="soggetto.voce" labelKey="label.soggetto.voce"/>
			<input type="hidden" id="soggettario" name="soggettario" value="${parent.id}"/>
	
	</fieldset>

<%-- pulsanti di submit --%>
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
<input type="submit" name="_eventId_cancel" value="Indietro"/>
<input type="submit" name="_eventId_salva" value="Salva"/>

</form:form>


 
