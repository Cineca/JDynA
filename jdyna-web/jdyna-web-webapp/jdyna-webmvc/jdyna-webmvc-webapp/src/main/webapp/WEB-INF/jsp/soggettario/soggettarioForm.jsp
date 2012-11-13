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

<%--VALIDATA XHTML 1.1 --%>

<c:set var="commandObject" value="${soggettario}" scope="request" />
<content  tag="breadcrumbs">
<ul class='breadcrumbs'>
<li>
<a href='${root}/soggettario/list.htm' >lista soggettari</a>  
</li>
<c:if test='${empty soggettario_id}'>
<li>
nuovo
</li>
</c:if>
<c:if test='${!empty soggettario_id}'>
<li>
<a href='${root}/soggettario/details.htm?id=${soggettario.id}' >${soggettario.nome}</a>  
</li>
<li>
modifica
</li>
</c:if>
</li>
</ul>
</content>
<form:form commandName="soggettario" method="post">
	<spring:bind path="soggettario.*">
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
		key="title.soggettario.fieldset.inserimento" /></legend>
			<c:set var="index" value="0" target="java.lang.Integer" />

			<dyna:text propertyPath="soggettario.nome" labelKey="label.soggettario.nome"/>
			<div class="dynaClear">
				&nbsp;
				</div>
			<dyna:text propertyPath="soggettario.descrizione" labelKey="label.soggettario.descrizione"/>
			<div class="dynaClear">
				&nbsp;
				</div>
			<dyna:boolean propertyPath="soggettario.chiuso" labelKey="label.soggettario.chiuso"/>
			<div class="dynaClear">
				&nbsp;
				</div>
			<dyna:boolean propertyPath="soggettario.attivo" labelKey="label.soggettario.attivo"/>
	
	</fieldset>


<%-- pulsanti di submit --%>
<p>
<input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}" />
<input type="submit" name="_eventId_cancel" value="Indietro"/>
<input type="submit" name="_eventId_creaSoggetto" value="Crea Soggetto"/>
<input type="submit" name="_eventId_salva" value="Salva"/>
</p>
</form:form>
