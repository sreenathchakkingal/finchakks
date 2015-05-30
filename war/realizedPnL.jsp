<%@include file="core.jsp" %>

<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Avg Interest Return</td>
		<td>Diff Of Return - Bank</td>
		<td>Total Investment</td>
		<td>Total Return</td>
		<td>Total Return If Bank</td>
		<td>Quantity</td>
	</tr>
	<c:set var="sum" value="${0}" />
	<c:forEach items="${stocksSummary}" var="stockSummary">
			<tr>
			<td><c:out value="${stockSummary.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${stockSummary.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn-stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalInvestment}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.quantity}" pattern="#,###.##" /></td>
			<c:set var="sum" value="${sum + stockSummary.totalReturn-stockSummary.totalReturnIfBank}" />
			<c:set var="totalInvestment" value="${totalInvestment + stockSummary.totalInvestment}" />
			<c:set var="averageReturn" value="${averageReturn + stockSummary.returnTillDate*stockSummary.totalInvestment}" />
		</tr>
	</c:forEach>
	Net Diff(Stocks vs Bank): <fmt:formatNumber value="${sum}" pattern="#,###.##" />
	<br/>
	Net Investment: <fmt:formatNumber value="${totalInvestment}" pattern="#,###.##" />
	<br/>
	Net Returns: <fmt:formatNumber value="${averageReturn/totalInvestment}" pattern="#,###.##" />
	<br/>
</table>
  
  
<p>Details</p>
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Interest Return</td>
		<td>Diff Of Return - Bank</td>
		<td>Quantity</td>
		<td>Buy Date</td>
		<td>Sell Date</td>
		<td>Buy Price</td>
		<td>Sell Price</td>
		<td>Sell Price If Bank</td>
		<td>Total Buy Price</td>
		<td>Total Sell Price</td>
		<td>Total Sell Price If Bank</td>
		<td>Return</td>
		<td>Return If Bank</td>

	</tr>
	<c:forEach items="${stocks}" var="stock">
		<tr>
			<td><c:out value="${stock.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${stock.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.bankSellPrice) * stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.quantity}"></c:out></td>
			
			<td><c:out value="${stock.buyDate}"></c:out></td>
			<td><c:out value="${stock.sellDate}"></c:out></td>
			
			<td><c:out value="${stock.buyPrice}"></c:out></td>
			<td><c:out value="${stock.sellPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice}" pattern="#,###.##" /></td>
			
			<td><fmt:formatNumber value="${stock.buyPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.sellPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice * stock.quantity}" pattern="#,###.##" /></td>
			
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.bankSellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>
		</tr>
	</c:forEach>
</table>
