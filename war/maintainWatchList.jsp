 <%@include file="core.jsp" %>

<form action="maintainWatchList.do" method="get">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>Stock Id -Nse</td>
			<td><input type="text" name="stockIdToBeAdded" id="stockName"><br></td>
		</tr>
		<tr><td><input type="submit" value="Delete/Add"/></td></tr>
	</table>

	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>SL</td>
			<td>WatchList NseId</td>
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
