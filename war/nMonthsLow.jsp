<%@include file="core.jsp" %>
<%@include file="exception.jsp" %>

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<tr>
		<td>Stock Name</td>
		<td>Decrease (in %)</td>
		<td>Lastest Date</td>
		<td>Nth Day</td>
		<td>Lastest Close Price</td>
		<td>Nth Day Close Price</td>
	</tr>

	<c:forEach items="${stocks}" var="stock">
		<c:if test="${not stock.isException}">
			<tr>
				<td>
					<a href="http://finchakks.appspot.com/stockInfo?stockName=<c:out value='${stock.stockName}'/>" target="_blank">
						<c:out value='${stock.stockName}' />
					</a>
				</td>
				<!-- <td><fmt:formatNumber value="${(stock.simpleMovingAverage-stock.sellPrice)/stock.simpleMovingAverage*100}" pattern="##.##" /></td>  -->
				<td><fmt:formatNumber value="${stock.simpleMovingAverageAndSellDeltaNormalized}" pattern="##.##" /></td>
				<td><c:out value="${stock.sellDate}"></c:out></td>
				<td><c:out value="${stock.buyDate}"></c:out></td>
				<td><fmt:formatNumber value="${stock.sellPrice}" pattern="##.##" /></td>
				<td><fmt:formatNumber value="${stock.simpleMovingAverage}" pattern="##.##" /></td>
			</tr>
	</c:if>
	</c:forEach>
</table>