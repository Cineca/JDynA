<%--
JDynA, Dynamic Metadata Management for Java Domain Object

 Copyright (c) 2008, CILEA and third-party contributors as
 indicated by the @author tags or express copyright attribution
 statements applied by the authors.  All third-party contributions are
 distributed under license by CILEA.

 This copyrighted material is made available to anyone wishing to use, modify,
 copy, or redistribute it subject to the terms and conditions of the GNU
 Lesser General Public License v3 or any later version, as published 
 by the Free Software Foundation, Inc. <http://fsf.org/>.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 for more details.

  You should have received a copy of the GNU Lesser General Public License
  along with this distribution; if not, write to:
  Free Software Foundation, Inc.
  51 Franklin Street, Fifth Floor
  Boston, MA  02110-1301  USA
--%>

<%@ include file="/common/taglibs.jsp"%>

<link rel="stylesheet" type="text/css" media="screen,projection,print"
	href="<c:url value='/css/style_autocomplete.css'/>" />

<c:set var="commandObject" value="${albero}" scope="request" />

<content  tag="breadcrumbs">
<ul class='breadCumps'>
<c:forEach items="${breadCumbs}" var="link">
<li>
<c:if test='${!empty link.key}'> 
<a href='${link.key}'>${link.value}</a>
</c:if>
<c:if test='${empty link.key}'> 
${link.value}
</c:if>
</li>
</c:forEach>
</ul>
</content>
<form:form commandName="gestioneAlbero" action="flusso.flow" method="post" >
	<spring:bind path="gestioneAlbero">
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
	
	
	<fieldset><legend><fmt:message key="title.albero.fieldset.inserimento" /></legend>
	
		

	<div style="display: none;" class="helpTip" id="nome">
		<img src="${root}/images/delete.gif" onclick="hideHelp('nome')" class="close"/>
			
			<p><fmt:message key="label.albero.help.nome" /></p>
	
	</div>  
	<div class="dynaField">
		<span class="dynaLabel" style="min-width: 21em;">
		<fmt:message key="label.albero.nome"/>
		<img src="${root}/images/help.gif" onclick="showHelp('nome')" class="help"/>
		</span>
		
	</div>
	<div class="dynaFieldValue">
	<dyna:text  propertyPath="gestioneAlbero.nome" label=" " required="true"
				size="30"/>
	</div>
	<div class="dynaClear">
		&nbsp;
		</div>
		

	<div style="display: none;" class="helpTip" id="descrizione">
		<img src="${root}/images/delete.gif" onclick="hideHelp('descrizione')" class="close"/>
			
			<p><fmt:message key="label.albero.help.descrizione" /></p>
	
	</div>  
	<div class="dynaField">
		<span class="dynaLabel" style="min-width: 21em;">
		<fmt:message key="label.albero.descrizione"/>
		<img src="${root}/images/help.gif" onclick="showHelp('descrizione')" class="help"/>
		</span>
	</div>

	<div class="dynaFieldValue">
	<dyna:textarea  propertyPath="gestioneAlbero.descrizione" label="" cols="50" rows="10"/>										
	</div>
	<div class="dynaClear">
		&nbsp;
	</div>
		<dyna:boolean propertyPath="gestioneAlbero.codiceSignificativo" labelKey="label.albero.isCodiceSignificativo" helpKey="help.albero.isCodiceSignificativo"></dyna:boolean>
	<c:if test="${albero.flat}">
		<dyna:boolean propertyPath="gestioneAlbero.flat" labelKey="label.albero.flat" helpKey="help.albero.flat"></dyna:boolean>
	</c:if>

	</fieldset>

	<%-- pulsanti di submit --%>

	<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}" />
	<input type="submit" name="_eventId_exit" value="Annulla"/>
	<input type="submit" name="_eventId_avanti" value="Salva" />

</form:form>
