 <%@include file="core.jsp" %>

<form action="maintainRating.do" method="get">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>Rating Name</td>
			<td><input type="text" name="ratingToBeAdded" id="ratingName"><br></td>
		</tr>
		<tr><td><input type="submit" value="Delete/Add"/></td></tr>
	</table>

	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>SL</td>
			<td>Rating Names</td>
			<td></td>
		</tr>
		<c:set var="count" value="1" scope="page" />
		<c:forEach items="${ratings}" var="rating">
			<tr>
				<td><c:out value="${count}"/></td>
				<td><c:out value="${rating}" /></td>
				<td><input type="checkbox" name="ratingsToBeRemoved" value="<c:out value="${rating}"/>" /></td>
			</tr>
			<c:set var="count" value="${count + 1}" scope="page"/>
		</c:forEach>
	</table>
	
	
	
</form>
