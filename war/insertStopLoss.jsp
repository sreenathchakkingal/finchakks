<%@include file="core.jsp" %>

Stop Loss
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>L Return %</td>
		<td>U Return %</td>
		<td>L Price</td>
		<td>U Price</td>
		<td>A Date</td>
		<td>B Date</td>
	</tr>
	<c:forEach items="${stocks}" var="stock">
	<tr>	
		<td><c:out value='${stock.stockName}' /></td>
		<td><c:out value='${stock.lowerReturnPercentTarget}' /></td>
		<td><c:out value='${stock.upperReturnPercentTarget}' /></td>
		<td><c:out value='${stock.lowerSellPriceTarget}' /></td>
		<td><c:out value='${stock.upperSellPriceTarget}' /></td>
		<td><c:out value='${stock.achieveAfterDate}' /></td>
		<td><c:out value='${stock.achieveByDate}' /></td>
	</tr>
	</c:forEach>
</table>

