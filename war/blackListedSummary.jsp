<!-- this should be exactly the same as unrealized summary except for the heading - BlackListed
and <c:forEach items="${blackListedStocks}" var="stockSummary"> instead of <c:forEach items="${stocksSummary}" var="stockSummary">
keeping this like this until i know how to switch this based on a flag from the server side. 
-->
BlackListed
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Avg Interest Return</td>
		<td>Impact on Avg Return</td>
		<td>Sellable Quantity
		<td>Diff Of Return - Bank</td>
		<td>Total Investment</td>
		<td>Total Return</td>
		<td>Total Return If Bank</td>
	</tr>
	<tr>
	<c:forEach items="${blackListedStocks}" var="stockSummary">
		
			<td><c:out value="${stockSummary.stockName}"/>
			<td><fmt:formatNumber value="${stockSummary.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.impactOnAverageReturn}"  type="percent" maxIntegerDigits="2" maxFractionDigits="2"/></td>
			<td>
			<fmt:formatNumber value="${stockSummary.sellableQuantity}"  pattern="#,###"/>
	 		out of
			<fmt:formatNumber value="${stockSummary.quantity}"  pattern="#,###.##"/>
			</td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn-stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalInvestment}" pattern="#,##,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
	</c:forEach>
	</tr>
	
</table>
