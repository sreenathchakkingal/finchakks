Summary
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Avg Interest Return</td>
		<td>Sellable Quantity
		<td>Diff Of Return - Bank</td>
		<td>Total Investment</td>
		<td>Total Return</td>
		<td>Total Return If Bank</td>
	</tr>
	
	<c:forEach items="${stocksSummary}" var="stockSummary">
		<c:choose>
			<c:when test="${stockSummary.sellableQuantity gt 0}">
					<tr bgcolor="#2E64FE">
			</c:when>
			<c:otherwise>
				<tr>
			</c:otherwise>
		</c:choose>
	 	
			<td><c:out value="${stockSummary.stockName}"></c:out></td> 
			<td><fmt:formatNumber value="${stockSummary.returnTillDate}" pattern="#,###.##" /></td>
			<td>
			<fmt:formatNumber value="${stockSummary.sellableQuantity}"  pattern="#,###"/>
	 		out of
			<fmt:formatNumber value="${stockSummary.quantity}"  pattern="#,###.##"/>
			</td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn-stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalInvestment}" pattern="#,##,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
		</tr>
	
	</c:forEach>
	
</table>
