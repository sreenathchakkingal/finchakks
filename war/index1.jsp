<!doctype html>
<head>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.min.js"></script>

    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-touch.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-animate.js"></script>
    <script src="http://ui-grid.info/docs/grunt-scripts/csv.js"></script>
    <script src="http://ui-grid.info/docs/grunt-scripts/pdfmake.js"></script>
    <script src="http://ui-grid.info/docs/grunt-scripts/vfs_fonts.js"></script>
    <script src="http://ui-grid.info/release/ui-grid.js"></script>
    <link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">
    <link rel="stylesheet" href="js/main.css" type="text/css">
    
<script src="js/myAngular.js"></script>
<script src="https://apis.google.com/js/client.js?onload=init"></script>
</head>
<body>
	
<div ng-app="finchakksApp" ng-controller="initializeController">
	<h3>Black Listed Stocks </h3>
	<div id="grid1" ui-grid-exporter ui-grid="blackListedGrid" class="blackListedGrid"/>

<br/>
		Net Returns(in %): {{profitAndLoss.averageReturn*100 | number:2}}
<br/><br/>
		Net Investment: {{profitAndLoss.totalInvestment | number:0}}
<br/><br/>
		Total Return: {{profitAndLoss.totalReturn | number:0}}
<br/><br/>
		Total Return(If Bank): {{profitAndLoss.totalReturnIfBank | number:0}}
<br/><br/>
		Net Diff(Stocks vs Bank): {{profitAndLoss.totalReturnVsIfBank | number:0}}
<br/><br/>


	<h3>N Days History</h3>
	
		<form>
			<tr>
				<td>Days <input type="text" ng-model="ndaysHistoryInput.numOfDays" value="{{ndaysHistoryInput.numOfDays}}"></td>
				<td>SMA <input type="text" ng-model="ndaysHistoryInput.simpleMovingAverage" value="{{ndaysHistoryInput.simpleMovingAverage}}"></td>
				<td><button ng-click="refreshNdaysHistory(ndaysHistoryInput)">Refresh</button></td>
			</tr>
		</form> 
		<br/>
	<div id="grid2" style="word-wrap: normal" ui-grid-exporter ui-grid="nDaysHistoryGrid" class="nDaysHistoryGrid"/>

	<h3>Unrealized Details</h3>
	<div id="grid3" style="word-wrap: normal" ui-grid-exporter ui-grid="unrealizedDetailsGrid" ui-grid-grouping  class="grid"/>



</div>

</html>