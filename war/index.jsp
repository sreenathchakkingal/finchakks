<%@include file="core.jsp" %>

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
	
	<c:forEach items="${blackListedStocks}" var="blackListedStock">
		<tr>
			<td><c:out value="${blackListedStock.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${blackListedStock.returnTillDate}" pattern="#,###" /></td>
			<td><fmt:formatNumber value="${stockSummary.impactOnAverageReturn}"  type="percent" maxIntegerDigits="2" maxFractionDigits="2"/></td>
			<td><fmt:formatNumber value="${blackListedStock.quantity}"  pattern="#,###"/></td>
			<td><fmt:formatNumber value="${blackListedStock.totalReturn-stockSummary.totalReturnIfBank}" pattern="#,###" /></td>
			<td><fmt:formatNumber value="${blackListedStock.totalInvestment}" pattern="#,##,###" /></td>
			<td><fmt:formatNumber value="${blackListedStock.totalReturn}" pattern="#,###" /></td>
			<td><fmt:formatNumber value="${blackListedStock.totalReturnIfBank}" pattern="#,###" /></td>
		</tr>
	</c:forEach>
	
</table>
<p></p>

<%@include file="profitAndLoss.jsp" %>
<p></p>

<%@include file="nDaysHistory.jsp" %>
<p></p>

<%@include file="unRealizedPnLDetails.jsp" %>
<p></p>

<%@include file="unRealizedPnLSummary.jsp" %>
<p></p>

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="nDaysHistory.do" method="get" target="_blank">
		<tr>
			<td>Number Of Days:</td>
			<td><input type="text" name="numOfDays" value="6"><br></td>
		</tr>
		<tr>
			<td>Compare Againt SMA(in days):</td>
			<td><input type="text" name="simpleMovingAverage" value="50"><br></td>
		</tr>
		<tr>
			<td><input type="submit" value="Submit"></td>
		</tr>
	</form>
</table>
<p></p>

<form action="maintainStockRatings.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="Maintain Stock Ratings"></td>
		</tr>
	
	</table>
</form>

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="unRealizedPnL.do" method="POST" enctype="multipart/form-data" target="_blank">
		<tr>
			<td><input type="file" name="file" accept=".csv"></td>
		</tr>
		<tr>
		<input type="hidden" name="triggerFrom" value="manual"/> 
			<td><input type="submit" value="UnRealized PnL"></td>
		</tr>
	</form>
</table>

<p></p>
<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="realizedPnL.do" method="get" target="_blank">
		<tr>
			<td><input type="submit" value="Realized PnL"></td>
		</tr>
	</form>
</table>

<p></p>
<form action="maintainList.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<input type="hidden" name="action" value="watchList" />
			<td><input type="submit" value="Maintain Watch list"></td>
		</tr>
	</table>
</form>
<p></p>

<form action="maintainList.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<input type="hidden" name="action" value="blackList" />
			<td><input type="submit" value="Maintain Black list"></td>
		</tr>
	</table>
</form>

<form action="maintainMapping.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<td><input type="submit" value="Maintain Stock Id Mappings"></td>
		</tr>

	</table>
</form>
<p></p>

<form action="maintainRating.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="Maintain Ratings"></td>
		</tr>
	
	</table>
</form>

<form action="stockInfo.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<td>Get Info For :</td>
			<td><input type="text" id="stockName" name="stockName"><br></td>
		</tr>
		<tr>
			<td>Number Of Years :</td>
			<td><input type="text" name="numberOfYears" value="10"><br></td>
		</tr>

		<tr>
			<td><input type="submit" value="Submit"></td>
		</tr>
	</table>
	<textarea name="historicalClosePrices" cols="40" rows="20"></textarea>
</form>
<p></p>


<br/>
<hr> Testing Zone</hr>

<form action="combineNDaysHistoryAndUnrealized" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="combineNDaysHistoryAndUnrealized"></td>
		</tr>
	</table>
</form>

