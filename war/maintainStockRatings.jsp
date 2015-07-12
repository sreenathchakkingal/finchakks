<%@include file="core.jsp"%>

<form action="maintainStockRatings.do" method="get">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">

		<tr>
			<td>Stock Id -Nse</td>
			<td><input type="text" name="stockName" id="stockName" value="<c:out value="${stock.nseId}" />"><br></td>
		</tr>
		<td><input type="submit" name="Retrieve" value="Retrieve"></td>
	</table>
	<br/><br/> 

	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>SL</td>
			<td>Ratings</td>
			<td>Value</td>
		</tr>
		<c:set var="count" value="1" scope="page" />
		<c:forEach items="${stock.ratingNameToValue}" var="ratingToValue">
			<tr>
				<td><c:out value="${count}"/></td>
				<td><c:out value="${ratingToValue.key}" /></td>
				<td><input type= "text" class="ratingValue" name="<c:out value="${ratingToValue.key}"/>" value="<c:out value="${ratingToValue.value.description}"/>"></td>
			</tr>
			<c:set var="count" value="${count + 1}" scope="page"/>
		</c:forEach>
	</table>

	<br /> <br />
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td><input type="submit" name="addOrUpdate" value="Add/Update" />
			</td>
		</tr>
	</table>

	<br/><br/>
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>Inferences</td>

			<c:forEach items="${stock.ratingInferences}" var="inference">
				<tr>
					<td><c:out value="${inference}" /></td>
				</tr>
			</c:forEach>
		</tr>
	</table>
</form>