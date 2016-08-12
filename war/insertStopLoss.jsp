<%@include file="core.jsp" %>

Stop Loss
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>R %</td>
		<td>Price</td>
		<td>Date</td>
	</tr>
	<c:forEach items="${stocks}" var="stock">
	<tr>	
		<td><c:out value='${stock.stockName}' /></td>
		<td><c:out value='${stock.targetReturnPercent}' /></td>
		<td><c:out value='${stock.targetSellPrice}' /></td>
		<td><c:out value='${stock.targetDate}' /></td>
	</tr>
	</c:forEach>
</table>

