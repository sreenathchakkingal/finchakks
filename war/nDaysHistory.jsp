<%@include file="core.jsp" %>


<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Score</td>
		<td>Investment</td>
		<td>Industry Investment</td>
		<td>Avg Interest Return</td>
		<td>Buy Price</td>
		<td>Duration</td>
		<td>Decrease Simple Moving Avg</td>
		<td>Net Gains</td>
		<td>Industry</td>
		<td>Latest Close Price</td>
		<td>50 day Simple Moving Avg</td>
		<c:forEach items="${stocks[0].dates}" var="date">
			<td><c:out value="${date}" /></td>
		</c:forEach>
	</tr>
	<c:forEach items="${stocks}" var="stock">
		<c:choose>
			<c:when test="${(stock.netNDaysGain < 0) or (stock.simpleMovingAverageAndSellDeltaNormalized <= 1)}">
				<tr bgcolor="#2E64FE">
			</c:when>
			<c:otherwise>
				<tr>
			</c:otherwise>
		</c:choose>
			 <td><a href="http://finchakks.appspot.com/maintainStockRatings.do?stockName=<c:out value='${stock.stockName}'/>" target="_blank">
			<c:out value='${stock.stockName}' /></td></a>
			<td><c:out value='${stock.stockRatingValue.score}' /></td>
			<td><fmt:formatNumber value="${stock.investmentRatio}"  type="percent"  maxIntegerDigits="2" /></td>
			<td><fmt:formatNumber value="${stock.industryInvestmentRatio}"  type="percent"  maxIntegerDigits="3" /></td>
			<td><fmt:formatNumber value="${stock.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.buyPrice}" pattern="#,###.##" /></td>
			<td><c:out value='${stock.duration}' /></td>
			<td><fmt:formatNumber value="${stock.simpleMovingAverageAndSellDeltaNormalized/100}" type="percent"  maxIntegerDigits="2" /></td>
			<td><fmt:formatNumber value="${stock.netNDaysGain}" type="percent"  maxIntegerDigits="2" /></td> 
			<td><c:out value='${stock.industry}' /></td>
			<td><fmt:formatNumber value="${stock.sellPrice}" pattern="##.##" /></td>
			<td><fmt:formatNumber value="${stock.simpleMovingAverage}" pattern="##.##" /></td>
			<c:forEach items="${stock.nDaysGains}" var="nthDayGain">
				<td><fmt:formatNumber value="${nthDayGain.value}" pattern="#,###.##" /></td>
			</c:forEach>
		</tr>
	</c:forEach>
</table>

