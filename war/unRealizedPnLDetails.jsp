<%@include file="core.jsp" %>
<%@include file="exception.jsp" %>
<p>Details</p>
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Interest Return</td>
		<td>Quantity</td>
		<td>Buy Date</td>
		<td>Buy Price</td>
		<td>Total Buy Price</td>
		<td>Sell Price</td>
		<td>Sell Price If Bank</td>
		<td>Total Sell Price</td>
		<td>Profit/Loss</td>
	</tr>
	<c:forEach items="${stocks}" var="stock">
			<td><c:out value="${stock.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${stock.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.buyDate}"></c:out></td>
			<td><c:out value="${stock.buyPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.buyPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.sellPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.sellPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>
		</tr>
	</c:forEach>
</table>