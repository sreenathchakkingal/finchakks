<%@include file="core.jsp" %>
<%@include file="exception.jsp" %>

  <head>
    <script type="text/javascript" src="https://www.google.com/jsapi"></script>
    <script type="text/javascript">
		google.load("visualization", "1", {packages:["corechart"]});
		google.setOnLoadCallback(drawChart);
		function drawChart() {
			var obj =${stockInvestmentChart};
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Name');
			data.addColumn('number', 'Investment');
			data.addColumn('number', 'Return');
    	  
			for (var key in obj) {
				if (obj.hasOwnProperty(key)) {
					data.addRow([key, parseInt(obj[key].invested), parseInt(obj[key].returns) ]);
   		  		}
			}
    	  
	        var options = {
	  	          title: 'Investment/Return',
	        	  colors: ['blue','green'],
	  	          hAxis: {title: 'Stocks', titleTextStyle: {color: 'green'}}
	  	        };
        
        
        var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
        chart.draw(data, options);
      }
    </script>
  </head>
  
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" >
	<tr>
		<td>Not computing unrealized for these</td>
	</tr>
	<c:forEach items="${stocksException}" var="stock">
		<tr>
			<td><c:out value="${stock.stockName}"></c:out></td>
		</tr>
	</c:forEach>
</table>

<table>
<tr> Color Codes </tr>
<tr bgcolor="#2E64FE"><td>Matured and good returns</td></tr>
<tr bgcolor="#7F7F7F"><td>Matured</td></tr>
<tr bgcolor="#FF8000"><td>To be Matured</td></tr>
</table>  
  

<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Avg Interest Return</td>
		<td>Sellable Quantity
		<td>Diff Of Return - Bank</td>
		<td>Total Investment</td>
		<td>Total Return</td>
		<td>Total Return If Bank</td>
	</tr>
	
	<c:forEach items="${stocksSummary}" var="stockSummary">
		<c:choose>
			<c:when test="${stockSummary.sellableQuantity gt 0}">
					<tr bgcolor="#2E64FE">
			</c:when>
			<c:otherwise>
				<tr>
			</c:otherwise>
		</c:choose>
			<td><c:out value="${stockSummary.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${stockSummary.returnTillDate}" pattern="#,###.##" /></td>
			<td><c:out value="${stockSummary.sellableQuantity} out of ${stockSummary.quantity}"></c:out></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn-stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalInvestment}" pattern="#,##,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturn}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stockSummary.totalReturnIfBank}" pattern="#,###.##" /></td>
			<c:set var="sum" value="${sum + stockSummary.totalReturn-stockSummary.totalReturnIfBank}" />
			<c:set var="totalInvestment" value="${totalInvestment + stockSummary.totalInvestment}" />
			<c:set var="averageReturn" value="${averageReturn + stockSummary.returnTillDate*stockSummary.totalInvestment}" />
		</tr>
	</c:forEach>
	<br>
	Net Diff(Stocks vs Bank): <fmt:formatNumber value="${sum}" pattern="#,###.##" />
	<br/>
	Net Investment: <fmt:formatNumber value="${totalInvestment}" pattern="#,###.##" />
	<br/>
	Net Returns: <fmt:formatNumber value="${averageReturn/totalInvestment}" pattern="#,###.##" />
	<br/>
</table>
  
  
    <div id="chart_div" style="width: 1200px; height: 800px;" align="left"></div>

<p>Details</p>
<table BORDER="1" CELLPADDING="3" CELLSPACING="1" class="sortable">
	<tr>
		<td>Stock Name</td>
		<td>Interest Return</td>
		<td>Diff Of Return - Bank</td>
		<td>Quantity</td>
		<td>Buy Date</td>
		<td>Buy Price</td>
		<td>Total Buy Price</td>
		<td>Sell Date</td>
		<td>Sell Price</td>
		<td>Sell Price If Bank</td>
		<td>Total Sell Price</td>
		<td>Total Sell Price If Bank</td>
		<td>Profit/Loss</td>
		<td>Profit If Bank</td>

	</tr>
	<c:forEach items="${stocks}" var="stock">
		
		<c:choose>
			<c:when test="${stock.sellableQuantity gt 0}">
					<tr bgcolor="#2E64FE">
			</c:when>
			<c:when test="${stock.isMoreThanAnYear}">
					<tr bgcolor="#7F7F7F">
			</c:when>
			<c:when test="${stock.isMaturityIsCloseToAnYear}">
					<tr bgcolor="#FF8000">
			</c:when>
			<c:otherwise>
				<tr>
			</c:otherwise>
		</c:choose>
			<td><c:out value="${stock.stockName}"></c:out></td>
			<td><fmt:formatNumber value="${stock.returnTillDate}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.bankSellPrice) * stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.quantity}"></c:out></td>
			<td><c:out value="${stock.buyDate}"></c:out></td>
			<td><c:out value="${stock.buyPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.buyPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><c:out value="${stock.sellDate}"></c:out></td>
			<td><c:out value="${stock.sellPrice}"></c:out></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.sellPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${stock.bankSellPrice * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.sellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>
			<td><fmt:formatNumber value="${(stock.bankSellPrice- stock.buyPrice) * stock.quantity}" pattern="#,###.##" /></td>

		</tr>
	</c:forEach>
</table>
