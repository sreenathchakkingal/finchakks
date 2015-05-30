<%@include file="core.jsp"%>

<form action="maintainStockRatings" method="get">
		<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<td>Stock Id -Nse</td>
			<td><input type="text" name="stockName" id="stockName" value="<c:out value="${stockRatingValue.stockName}" />"><br></td>
		</tr>
		<td><input type="submit" name="Retrieve" value="Retrieve"></td>
	</table>
	<br/><br/> 
	
		<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>Testing 2</td>
			<td><input type="text" name="ratingValues" class="ratingValue"><br></td>
			<td><input type="text" name="ratingValues" class="ratingValue"><br></td>
			<td><input type="text" name="ratingValues" class="ratingValue"><br></td>
			<td><input type="text" name="ratingValues" class="ratingValue"><br></td>
			
		</tr>
		<td><input type="submit" name="Retrieve" value="Retrieve"></td>
	</table>
	

</form>