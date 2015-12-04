<%@include file="core.jsp" %>

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

<form action="count.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="count .do Submit"></td>
		</tr>
	</table>
</form>


<form action="addCount.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
					<td>Num</td>
			<td><input type="text" value="5"><br></td>
			<td><input type="submit" value="add .do Submit"></td>
		</tr>
	</table>
</form>


<form action="addCount.do" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="addCount.do without param"></td>
		</tr>
	</table>
</form>


