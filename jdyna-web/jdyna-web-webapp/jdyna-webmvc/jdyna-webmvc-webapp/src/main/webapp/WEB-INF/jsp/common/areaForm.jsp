<%--VALIDATA XHTML 1.1 --%>

<%@ include file="/common/taglibs.jsp"%>

<head>
	<title>Creazione di un' area per l'oggetto</title>
</head>

<br/>
<form:form commandName="area" method="post">
	<p>
	<%-- pulsanti di submit --%>
	<input type="submit" name="cancel"
		value="<fmt:message key='button.back' />" />
	<input type="submit" name="save"
		value="<fmt:message key='button.save'/>" onclick="send()" />
	</p>

		
	<spring:bind path="area">
		<c:if test="${not empty status.errorMessages}">
			<div class="error"><c:forEach var="error"
				items="${status.errorMessages}">
	               ${error}
			</c:forEach></div>
		</c:if>
	</spring:bind>

<div id='contenuto'> 
    <dyna:text propertyPath="area.title"
		labelKey="label.area.title" /><div class="dynaClear">&nbsp;
		</div>
	    <dyna:text size="5" propertyPath="area.priorita"
		labelKey="label.area.priorita" /><div class="dynaClear">&nbsp;
		</div>

	<input type="hidden" name="mascheraxxx" id="mascheraxxx" /><div class="dynaClear">&nbsp;
		</div>


	<div style="height:200px;margin-bottom: auto;" class="parent">
	<div style="float:left;" class="first">
	<label>Tipologie di Proprieta visibili sull'area</label>
	<ul class="sortable boxy" id="firstlist">
		
		<c:if test="${listaTipologieArea != null}">
			<c:forEach items="${listaTipologieArea}" var="elemento">
				<li class="green" id="firstlist_${elemento.id}">${elemento.shortName}</li>
			</c:forEach>
		</c:if>
	</ul>
	</div>


	<div style="float:right;">
	<label>Maschera Tipologie di Proprieta</label>
	<ul class="sortable boxier" id="secondlist">
		
		<c:forEach items="${area.maschera}" var="elemento">
			<li class="green" id="secondlist_${elemento.id}">${elemento.shortName}</li>
		</c:forEach>
	</ul>
	</div>
	</div>

	<script type="text/javascript">
		dragANDdrop();  	
	</script>



</div>
</form:form>
