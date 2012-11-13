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
