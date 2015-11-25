<%@include file="core.jsp" %>
<%@include file="exception.jsp" %>


<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Score</td>
		<td>Investment</td>
		<td>Industry Investment</td>
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
			<td><fmt:formatNumber value="${stock.industryInvestmentRatio}"  type="percent"  maxIntegerDigits="2" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice-stock.simpleMovingAverage)/stock.simpleMovingAverage}" type="percent"  maxIntegerDigits="2" /></td>
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




<!-- 

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<tr>
		<c:forEach items="${dates}" var="date">
				<td><c:out value="${date}" /></td>
		</c:forEach>
	</tr>
	<c:forEach items="${nDaysPrices}" var="oneStockPrices">
		<tr>
			<c:choose>
				<c:when test="${oneStockPrices.passedThresholdCriterial}">
					<tr bgcolor="#2E64FE">
				</c:when>
				<c:otherwise>
					<tr>
				</c:otherwise>
			</c:choose>
			
			<td><a href="http://finchakks.appspot.com/stockInfo?stockName=<c:out value='${oneStockPrices.stockName}'/>" target="_blank">
			<c:out value='${oneStockPrices.stockName}' /></td></a>
			<c:forEach items="${oneStockPrices.dateToCloseValue}" var="dateToCloseValues">
				<td><c:out value="${dateToCloseValues.value}" /></td>
			</c:forEach>
		</tr>
		
	</c:forEach>
</table>

 -->
