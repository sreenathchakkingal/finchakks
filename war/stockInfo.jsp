<%@include file="core.jsp" %>

message : ${message}

Stock Name : <c:out value = "${stock.stockName}"/>
<br/>
Stock Number of Ups: <c:out value = "${stock.movement.ups}"/>
<br/>
Stock Number of Downs: <c:out value = "${stock.movement.downs}"/>
<br/>
Average Returns(per year): <fmt:formatNumber value="${stock.averageInterestReturn}" pattern="##.##"/>
<br/>
Dividend Frequency:  Every  <c:out value = "${stock.divdendFrequencyInMonths}"/> Months

Returns : 
<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<c:forEach items="${stock.interestReturns}" var="interestReturn">
		<tr>
			<td><c:out value="${interestReturn.dateFromToDateTo}"></c:out></td>
			<td><fmt:formatNumber value="${interestReturn.interestRateForThePeriod}" pattern="##.##"/></td>
		</tr>
	</c:forEach>
</table>

<a href=<c:out value="${screenerUrl}"/> target="_blank">Screener</a>
<br/> 
<a href="http://www.4-traders.com" target="_blank">4-Trader</a>
<br/>

Dividends : 
<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<c:forEach items="${stock.dividends}" var="dividend">
		<tr>
			<td><c:out value="${dividend.date}"></c:out></td>
			<td><fmt:formatNumber value="${dividend.value}" pattern="##.##"/></td>
		</tr>
	</c:forEach>
</table>
