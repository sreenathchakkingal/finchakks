 <%@include file="core.jsp" %>

<form action="maintainList.do" method="get">
	<table BORDER="1" CELLPADDING="3" CELLSPACING="1">
		<tr>
			<td>Stock Id -Nse</td>
			<td><input type="text" name="stockIdToBeAdded" id="stockName"><br></td>
		</tr>
		<tr>
		<input type="hidden" name="action" value="blackList" />
		<td><input type="submit" value="Delete/Add"/></td>
		</tr>
	</table>
	
	<c:import url="./maintainList.jsp" />
</form>
