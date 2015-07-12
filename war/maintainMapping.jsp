<%@include file="core.jsp" %>

<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
	<form action="maintainMapping.do" method="get">
		<tr>
			<td>MoneyControlId</td>
			<td><input type="text" name="moneyControlId"><br></td>
			<td>YahooBoId</td>
			<td><input type="text" name="yahooBoId"><br></td>
			<td>NseId</td>
			<td><input type="text" name="nseId" id="stockName"><br></td>
			<td>BseId</td>
			<td><input type="text" name="bseId" id="bseId"><br></td>
		</tr>
		<td><input type="submit" value="Submit"></td>
	</form>
</table>
<c:if test="${fn:length(allMappingEntries) > 0}">
	<c:set var="count" value="1" scope="page" />
		<form action="maintainMapping.do" method="get">
			<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
				<tr>
					<td>SL</td>
					<td>MoneyControl Id</td>
					<td>YAHOO BO Id</td>
					<td>NSE Id</td>
					<td>BSE Id</td>
				</tr>
				<c:forEach items="${allMappingEntries}" var="eachMappingEntry">
					<tr>
						<td><c:out value="${count}"/></td>
						<td><input type="text" name="moneyControlIds" value="<c:out value="${eachMappingEntry.moneycontrolName}" />"></td>
						<td><input type="text" name="yahooId" value="<c:out value="${eachMappingEntry.yahooName}" />"/> </td>
						<td><input type="text" name="nseId" value="<c:out value="${eachMappingEntry.nseId}" />"/></td>
						<td><input type="text" name="bseId" value="<c:out value="${eachMappingEntry.bseId}" />"/></td>
						<td><input type="checkbox" name="selectedMappings" value="<c:out value="${eachMappingEntry.nseId}"/>"/></td>
					</tr>
					<c:set var="count" value="${count + 1}" scope="page"/>
				</c:forEach>
			</table>
			<input type="hidden" name ="action" value="delete">
			<tr><input type="submit" value="Delete"/></td>
		</form>
</c:if>