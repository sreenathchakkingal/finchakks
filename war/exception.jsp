<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<tr>
		<td>No info obtained for these stocks from Quandl</td>
	</tr>
	<c:forEach items="${stocks}" var="stock">
		<c:if test="${stock.isException}">
		<tr>
			<td><c:out value="${stock.stockName}"></c:out></td>
		</tr>
		</c:if>
	</c:forEach>
</table>