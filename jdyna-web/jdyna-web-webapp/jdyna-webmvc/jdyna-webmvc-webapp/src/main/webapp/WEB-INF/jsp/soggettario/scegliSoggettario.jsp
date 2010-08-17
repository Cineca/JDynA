<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/scripts.jsp"%>


<c:set var="commandObject" value="${widget}" scope="request" />

SCEGLI SOGGETTARI O CREANE UNO NUOVO:
<form:form commandName="widget" action="flusso.flow" method="post">

<c:forEach items="${soggettari}" var="elemento">			
<dyna:checkbox  value="${elemento.id}"  propertyPath="widget.soggettari" labelKey="${elemento.nome}" /><br/>
</c:forEach>

<%-- pulsanti di submit --%>
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
<input type="submit" name="_eventId_cancel" value="Annulla"/>
<input type="submit" name="_eventId_nuovo" value="Crea Nuovo Soggettario"/>
<input type="submit" name="_eventId_avanti" value="Avanti"/>

</form:form>