<%@include file="core.jsp" %>

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
	<c:if test="${not empty stocksException}">
		<tr>
			<td>Not computing unrealized for these</td>
		</tr>
		<c:forEach items="${stocksException}" var="stock">
			<tr>
				<td><c:out value="${stock.stockName}"/></td>
				<td><c:out value="${stock.exceptionComment}"/></td>
				</td>
			</tr>
		</c:forEach>
	</c:if>
</table>

<table>
<tr> Color Codes </tr>
<tr bgcolor="#2E64FE"><td>Matured and good returns</td></tr>
<tr bgcolor="#7F7F7F"><td>Matured</td></tr>
<tr bgcolor="#FF8000"><td>To be Matured</td></tr>
</table>

<%@include file="profitAndLoss.jsp" %>

<%@include file="unRealizedPnLSummary.jsp" %>

<div id="chart_div" style="width: 1200px; height: 800px;" align="left"></div>

<%@include file="unRealizedPnLDetails.jsp" %>
<p></p>


