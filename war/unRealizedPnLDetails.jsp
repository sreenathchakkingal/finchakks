<%@include file="core.jsp" %>

<p>Details</p>
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Interest Return</td>
		<td>Diff Of Return - Bank</td>
		<td>Quantity</td>
		<td>Buy Date</td>
		<td>Buy Price</td>
		<td>Total Buy Price</td>
		<td>Sell Date</td>
		<td>Sell Price</td>
		<td>Sell Price If Bank</td>
		<td>Total Sell Price</td>
		<td>Total Sell Price If Bank</td>
		<td>Profit/Loss</td>
		<td>Profit If Bank</td>

	</tr>
	<c:forEach items="${stocksDetail}" var="stock">
		
		<c:choose>
			<c:when test="${stock.sellableQuantity gt 0}">
					<tr bgcolor="#2E64FE">
			</c:when>
			<c:when test="${stock.isMoreThanAnYear}">
					<tr bgcolor="#7F7F7F">
			</c:when>
			<c:when test="${stock.isMaturityIsCloseToAnYear}">
					<tr bgcolor="#FF8000">
			</c:when>
			<c:otherwise>
				<tr>
			</c:otherwise>
		</c:choose>
			<td><c:out value="${stock.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${stock.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.bankSellPrice) * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.buyDate}"></c:out></td>
			<td><c:out value="${stock.buyPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.buyPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.sellDate}"></c:out></td>
			<td><c:out value="${stock.sellPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.sellPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.bankSellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>

		</tr>
	</c:forEach>
</table>
