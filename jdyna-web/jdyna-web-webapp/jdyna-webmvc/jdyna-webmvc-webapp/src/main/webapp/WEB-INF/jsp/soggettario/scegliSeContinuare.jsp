<%@ page session="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%@ include file="/common/taglibs.jsp"%>
<%@ include file="/common/scripts.jsp"%>


<form action="flusso.flow" method="post">
Continuare nell'inserimento di soggetti nel soggettario?
<br/>

	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}">
	
	<input type="submit" name="_eventId_salva" value="Salva e Esci" />
	<input type="submit" name="_eventId_avanti" value="Continua"/>
	
</form>