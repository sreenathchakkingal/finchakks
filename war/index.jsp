<%@include file="core.jsp" %>

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="nDaysHistory" method="get" target="_blank">
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
<form action="stockInfo" method="get" target="_blank">
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

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="unRealizedPnL" method="POST" enctype="multipart/form-data" target="_blank">
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
	<form action="realizedPnL" method="get" target="_blank">
		<tr>
			<td><input type="submit" value="Realized PnL"></td>
		</tr>
	</form>
</table>

<p></p>
<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="nMonthsLow" method="get" target="_blank">
		<tr>
			<td>Number Of Months:</td>
			<td><input type="text" name="numOfMonths"><br></td>
		</tr>
		<tr>
			<td><input type="submit" value="Submit"></td>
		</tr>
	</form>
</table>

<p></p>

<form action="maintainWatchList" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<td><input type="submit" value="Maintain Watch list"></td>
		</tr>
	</table>
</form>

<form action="maintainMapping" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<td><input type="submit" value="Maintain Stock Id Mappings"></td>
		</tr>

	</table>
</form>
<p></p>

<form action="maintainRating" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="Maintain Ratings"></td>
		</tr>
	
	</table>
</form>
<form action="maintainStockRatings" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="Maintain Stock Ratings"></td>
		</tr>
	
	</table>
</form>

<br/>
<hr> Testing Zone</hr>

<form action="syncStockRatings" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="Sync Stock Ratings"></td>
		</tr>
	
	</table>
</form>


<form action="test" method="get" target="_blank">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" value="Test Db"></td>
		</tr>
	
	</table>
</form>




