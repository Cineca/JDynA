<%-- 

	This tag library is based on tutorial by Ted Bergeron.
	Tutorial link:
	http://www.triview.com/articles/hibernate/validator/canmeetyourneeds.html

--%>
<%@ attribute name="propertyPath" required="true"%>
<%@ attribute name="collection" required="true" type="java.util.Collection"%>
<%@ attribute name="size" required="false" type="java.lang.Integer"%>
<%@ attribute name="width" required="false" type="java.lang.Integer"%>
<%@ attribute name="required" required="false" type="java.lang.Boolean"%>
<%@ attribute name="labelKey" required="false"%>
<%@ attribute name="mode" required="false" type="java.lang.String"%>
<%@ attribute name="addModeType" required="false" type="java.lang.String"%>
<%@ attribute name="editModeType" required="false" type="java.lang.String"%>
<%@ attribute name="layoutMode" required="false" type="java.lang.String"%>
<%@ attribute name="onchange" required="false" type="java.lang.String"%>
<%@ attribute name="isEuro" required="false" type="java.lang.Boolean" %>
<%@ attribute name="isPercent" required="false" type="java.lang.Boolean" %>
<%@ attribute name="autoDisplay" required="false" type="java.lang.Boolean" %>
<%@ attribute name="cssClass" required="false" type="java.lang.String" %>
<%@ attribute name="multiple" required="false" type="java.lang.Boolean" %>
<%@ attribute name="nullValue" required="false" type="java.lang.String" %>

<%-- 
autoDisplay: default: true. Se true in caso di modetype=display ricava la property senza id da propertypath
 e visualizza se presente property.displayValue
--%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%@ taglib uri="jdynatags" prefix="dyna"%>
<%-- TODO: Wrap field in normal mode with a <div class="field"> --%>

<%--
 addModeType="enabled, disabled, display, hidden", editModeType="enabled, disabled, display, hidden"

 enabled  = Default.  Editable form field.
 disabled = Form field that has been grayed out.    (Set this with disabled="disabled" just like the selected attribute.)
 display  = Plain text representation.  Not a form field. (May have to pair with hidden field to keep springMvc happy)
 hidden   = Hidden form field.  (Not implemented.  Maybe better to force page to use our hidden tag instead.
 --%>

<%-- LayoutMode values are:
normal - Standard layout, wrap field in a div.
table  - Simple form with table for layout.  Seperate label and input with td.
grid   - Form is composed of a table grid.  No label, no seperators, no div.
SUARDI EDIT
p      - paragrafi
--%>

<c:if test="${layoutMode == null || (layoutMode != 'table' && layoutMode != 'grid' && layoutMode != 'normal')}">
	<c:set var="layoutMode" value="p"/>
</c:if>

<c:choose>
	<c:when test="${mode == 'add'}">
		<c:set var="mode" value="ADD" />
	</c:when>
	<c:when test="${mode == 'edit'}">
		<c:set var="mode" value="EDIT" />
	</c:when>
</c:choose>

<c:if test="${mode == null || (mode != 'EDIT' && mode != 'ADD')}">
	<c:set var="mode" value="ADD" />
	<%-- Default to ADD --%>
</c:if>

<c:if test="${autoDisplay == null || empty autoDisplay}">
	<c:set var="autoDisplay" value="true"/>
</c:if>

<c:choose>
	<c:when test="${mode == 'EDIT'}">
		<c:if
			test="${editModeType == null || (editModeType != 'disabled' && editModeType != 'display' && editModeType != 'hidden')}">
			<c:set var="editModeType" value="enabled" />
		</c:if>
		<c:set var="currentModeType" value="${editModeType}" />
	</c:when>
	<c:otherwise>
		<c:if
			test="${addModeType == null || (addModeType != 'disabled' && addModeType != 'display' && addModeType != 'hidden')}">
			<c:set var="addModeType" value="enabled" />
		</c:if>
		<c:set var="currentModeType" value="${addModeType}" />
	</c:otherwise>
</c:choose>

<c:set var="propertyName" value="${dyna:getPropertyName(propertyPath)}"/>

<c:if test="${autoDisplay && currentModeType == 'display'}">
	<%-- controllo che non sia null (altrimenti non posso fare object.displayValue --%>
	<c:set var="objectPath" value="${dyna:getObjectPath(propertyPath)}" />
	<spring:bind path="${objectPath}">
		<c:set var="object" value="${status.value}" />
		<c:if test="${object == null}">
			<%-- Bind ignores the command object prefix, so simple properties of the command object return null above. --%>
			<c:set var="object" value="${commandObject}" />
			<%-- We depend on the controller adding this to request. --%>
		</c:if>
	</spring:bind>
	<spring:bind path="${propertyPath}">
		<c:if test="${not empty status.value}">
			<c:set
				var="propertyPath"
				value="${dyna:getPropertyPathNoId(propertyPath)}">
			</c:set>
		</c:if>
	</spring:bind>
</c:if>

<c:set var="objectPath" value="${dyna:getObjectPath(propertyPath)}" />

<spring:bind path="${objectPath}">
	<c:set var="object" value="${status.value}" />
	<c:if test="${object == null}">
		<%-- Bind ignores the command object prefix, so simple properties of the command object return null above. --%>
		<c:set var="object" value="${commandObject}" />
		<%-- We depend on the controller adding this to request. --%>
	</c:if>
</spring:bind>

<c:choose>
	<c:when test="${required == null || required == false || currentModeType == 'display'}">
		<c:set var="labelClass" value="optional"/>
		<c:set var="labelAfter" value=""/>
	</c:when>
	<c:otherwise>
		<c:set var="labelClass" value="required"/>
		<c:set var="labelAfter"><span class="required"><fmt:message key="prompt.required" /></span></c:set>
	</c:otherwise>
</c:choose>
<%-- SUARDI EDIT FOR P --%>
<c:if test="${layoutMode == 'p'}">
	<c:set var="labelClass" value="left"/>

	<c:if test="${empty cssClass && small}">
		<c:set var="cssClass" value="comboSmall"/>
	</c:if>
	<c:if test="${empty cssClass && not small}">
		<c:set var="cssClass" value="combo"/>
	</c:if>
</c:if>
<%--
<c:if test="${multiple}">
<spring:bind path="${propertyPath}[0]">
${status.value[0]}a
</spring:bind>
<spring:bind path="${propertyPath}[1]">
${status.value[0]}b
</spring:bind>
<spring:bind path="${propertyPath}[2]">
${status.value[0]}c
</spring:bind>
</c:if>
--%>

<c:if test="${size == null}">
	<c:set var="size" value="1" />
</c:if>

<spring:bind path="${propertyPath}">
	<c:if test="${layoutMode == 'normal'}">
		<div class="field">
	</c:if>
	<c:if test="${layoutMode == 'p'}">
		<p>
	</c:if>
	<c:choose>
		<c:when test="${layoutMode == 'table'}">
			<c:choose>
				<c:when test="${labelKey == null}">
					<td><label for="${status.expression}" class="${labelClass}">
						<fmt:message key="label.${propertyName}"/>
						<c:out value="${labelAfter}" escapeXml="false"/>
					</label></td>
				</c:when>
				<c:when test="${labelKey == 'none'}">
					<%--Non fare nulla --%>
				</c:when>
				<c:otherwise>
					<td><label for="${status.expression}" class="${labelClass}">
						<fmt:message key="${labelKey}"/>
						<c:out value="${labelAfter}" escapeXml="false"/>
					</label></td>
				</c:otherwise>
			</c:choose>
		</c:when>

		<c:when test="${layoutMode == 'grid'}">
			<%-- BY SUARDI --%>
			<%--c:if test="${labelKey != null && labelKey != 'none'}">
                <label for="${status.expression}" class="${labelClass}"><c:out value="${labelAfter}" escapeXml="false"/><fmt:message key="${labelKey}"/></label>
            </c:if--%>
		</c:when>

		<c:otherwise> <%-- normal & p mode --%>
			<c:choose>
				<c:when test="${labelKey == null}">
					<label for="${status.expression}" class="${labelClass}">
						<fmt:message key="label.${propertyName}"/>
						<c:out value="${labelAfter}" escapeXml="false"/>
					</label>
				</c:when>
				<c:when test="${labelKey == 'none'}">
					<label for="${status.expression}" class="${labelClass}">
						<c:out value="${labelAfter}" escapeXml="false"/>
					</label>
				</c:when>
				<c:otherwise>
					<label for="${status.expression}" class="${labelClass}">
						<fmt:message key="${labelKey}"/>
						<c:out value="${labelAfter}" escapeXml="false"/>
					</label>
				</c:otherwise>
			</c:choose>
		</c:otherwise>
	</c:choose>

	<c:if test="${layoutMode == 'table'}">
		<td>
	</c:if>
	<c:choose>
		<c:when test="${currentModeType == 'display'}">
			<c:if test="${layoutMode != 'p'}">
	            <span class="visualize">
	            	${status.value}
	            </span>			&nbsp;
			</c:if>
			<c:if test="${layoutMode == 'p'}">
            	${status.value}			&nbsp;
			</c:if>
		</c:when>
		<c:otherwise>
			<select name="${status.expression}" id="${status.expression}"
				size="${size}" class="${cssClass}" 
				<c:if test="${currentModeType == 'disabled'}"> disabled="disabled"</c:if>
				<c:if test="${width != null}"> style="width: ${width}ex"</c:if>
				<c:if test="${multiple}"> multiple="multiple"</c:if>
				<c:if test="${not empty onchange}"> onchange=${onchange}</c:if>
				<c:if test="${layoutMode == 'p'}"> class="combo"</c:if>>
				<c:if test="${not empty nullValue}">
				<option value="">${nullValue}</option> 
				</c:if>
				<c:if test="${collection != null}">
					<c:forEach var="item" items="${collection}" varStatus="loop">
						<%-- TODO: if test needs to work for Enum, LookupObject, etc. mapped by propertyEditors --%>
						<%-- Comparison is iterate over collection of Selectable.  Compare String id to mapped object.  I'll assert that the mapped object implements Selectable also --%>
						<%--
                       item = ${item};
                       item == status.value = ${item == status.value};
                       item.class.name = ${item.class.name}
                       item.identifyingValue = ${item.identifyingValue};
                        status.value.identifyingValue = ${status.value.identifyingValue}
                        status.value.id = ${status.value.id}
                        status.value = ${status.value}
                        status.value.class.name = ${status.value.class.name}
                        TODO: status.value is a String instance. That's the problem.
                        status.class.name = ${status.class.name}
                        --%>
						<c:set var="optionSelected" value="false"/>
									
					<c:catch var="eccezione">
					<c:if test="${not multiple}">
					<%-- prima c'era solo un if ora è stato modificato per gli alberi classificatori--%>
					    <c:choose>
						<c:when test="${status.value.class eq 'class it.cilea.osd.jdyna.model.AlberoClassificatorio' || status.value.class eq 'class it.cilea.osd.jdyna.model.Soggettario'}">
						    <c:if test="${status.value.identifyingValue!=null && item.identifyingValue == status.value.identifyingValue}">>
								<c:set var="optionSelected" value="true" />
							</c:if>
						</c:when>
						<c:otherwise>
						<%-- questo if è quello originale ma che non permetteva di selezionare gli alberi classificatori già settati  --%>
						    <c:if test="${item.identifyingValue == status.value}">>
								<c:set var="optionSelected" value="true" />
							</c:if>
						</c:otherwise>
						</c:choose>
					</c:if>
					<c:if test="${multiple}">
					 	<c:choose>
						<c:when test="${status.value.class eq 'class it.cilea.osd.jdyna.model.AlberoClassificatorio' || status.value.class eq 'class it.cilea.osd.jdyna.model.Soggettario'}">
						 <c:forEach items="${status.value.identifyingValue}" var="valoreSingolo">
							<c:if test="${item.identifyingValue == valoreSingolo}">
								<c:set var="optionSelected" value="true" />
							</c:if>
						</c:forEach>						 
						</c:when>
						<c:otherwise>
						 <c:forEach items="${status.value}" var="valoreSingolo">
							<c:if test="${item.identifyingValue == valoreSingolo}">
								<c:set var="optionSelected" value="true" />
							</c:if>
						</c:forEach>
						</c:otherwise>
						</c:choose>
					</c:if>
					<option value="${item.identifyingValue}"
						<c:if test="${optionSelected == 'true'}"> selected="selected"</c:if>>${item.displayValue}</option>
					</c:catch>
					
					
					
					<c:if test="${eccezione != null}">
						<c:if test="${not multiple}">
							<c:if test="${item== status.value}">
								<c:set var="optionSelected" value="true" />
							</c:if>
						</c:if>
						<c:if test="${multiple}">
							<c:forEach items="${status.value}" var="valoreSingolo">
								<c:if test="${item == valoreSingolo}">
									<c:set var="optionSelected" value="true" />
								</c:if>
							</c:forEach>
						</c:if>						
						<option value="${item}"
							<c:if test="${optionSelected == 'true'}"> selected="selected"</c:if>>${item}</option>
					</c:if>					
				</c:forEach>
				</c:if>
			</select>
			
			<c:if test="${isPercent}">
				&nbsp;<fmt:message key="prompt.percent"/>
			</c:if>
			<c:if test="${isEuro}">
				&nbsp;<fmt:message key="prompt.euro"/>
			</c:if>
			<c:if test="${layoutMode!='p'}">
				<span class="icons"><jsp:doBody/></span>
			</c:if>
			<c:if test="${status.errorMessage != null && status.errorMessage != ''}">
				<span class="fieldError">
					<img id="${status.expression}_error"
						src="<c:url value="/image/icons/error.png"
						alt="error"/>
					${status.errorMessage}
				</span>
			</c:if>
		</c:otherwise>
	</c:choose>

	<c:if test="${layoutMode == 'table'}">
		</td>
	</c:if>
	<c:if test="${layoutMode == 'normal'}">
		</div>
	</c:if>
	<c:if test="${layoutMode == 'p'}">
		</p>
	</c:if>
</spring:bind>


<%-- TODO: Do we want to insert a Select a value/null entry here, or rely on the passed collection containing that?  Use NotNull annotation for required="true" --%>

