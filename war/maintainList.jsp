 <%@include file="core.jsp" %>

	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>SL</td>
			<td>NseId</td>
			<td></td>
		</tr>
		<c:set var="count" value="1" scope="page" />
		<c:forEach items="${stocks}" var="stock">
			<tr>
				<td><c:out value="${count}"/></td>
				<td><c:out value="${stock}" /></td>
				<td><input type="checkbox" name="stockIdsToBeRemoved" value="<c:out value="${stock}"/>" /></td>
			</tr>
			<c:set var="count" value="${count + 1}" scope="page"/>
		</c:forEach>
	</table>
	
	
	
</form>
