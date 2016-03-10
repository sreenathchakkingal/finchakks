<!doctype html>
	<head>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.min.js"></script>
		<script	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-touch.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-animate.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/csv.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/pdfmake.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/vfs_fonts.js"></script>
		<script src="http://ui-grid.info/release/ui-grid.js"></script>
		<script src="//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-1.2.1.js"></script>
		<script src="http://ui-grid.info/release/ui-grid-unstable.js"></script>
		<link href="//netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
		<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
		<link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">
				
		<script src="js/myAngular.js"></script>
		<script src="https://apis.google.com/js/client.js?onload=init"></script>
		<link rel="stylesheet" href="js/main.css" type="text/css">
	</head>
	<body>
		<div ng-app="finchakksApp" ng-controller="initializeController">
			<i ng-show="loader.initialLoading" class="btn btn-success"> Loading...
				<i class="fa fa-spinner fa-spin"></i>
			</i>

			<i ng-repeat="exceptionStock in exceptionStocks">
				<h2>
					<tr>
						<td>{{exceptionStock.stockName}} :</td>
						<td>{{exceptionStock.exceptionComment}}</td>
					</tr>
				</h2>
			</i>

			<h3>Black Listed Stocks</h3>
			<div id="grid1" ui-grid-exporter ui-grid="blackListedGrid" class="blackListedGrid" />

			<br />
			Net Returns(in %): {{profitAndLoss.averageReturn*100 | number:2}} 
			<br />
			Net Investment: {{profitAndLoss.totalInvestment | number:0}}
			<br />
			Total Return: {{profitAndLoss.totalReturn | number:0}} 
			<br />
			Total Return(If Bank): {{profitAndLoss.totalReturnIfBank | number:0}}
			<br />
			Net Diff(Stocks vs Bank): {{profitAndLoss.totalReturnVsIfBank |number:0}} 
			<br />

			<h3>N Days History</h3>
			<i ng-show="loader.nDaysHistoryLoading" class="btn btn-success">
				nDaysHistory Loading...
				<i class="fa fa-spinner fa-spin"></i>
			</i> 
			<i ng-show="loader.unrealizedDetailsLoading" class="btn btn-success">
				Unrealized Details Loading...
				<i class="fa fa-spinner fa-spin"></i>
			</i>
			<form>
				<tr>
					<td><button ng-click="refreshAll()">Refresh All</button></td>
					<td>
						<uib-accordion> 
							<uib-accordion-group heading="Show ndaysRefresh">
								<td>Days <input type="text" ng-model="ndaysHistoryInput.numOfDays" value="{{ndaysHistoryInput.numOfDays}}"></td>
								<td>SMA <input type="text" ng-model="ndaysHistoryInput.simpleMovingAverage" value="{{ndaysHistoryInput.simpleMovingAverage}}"></td>
								<td><button ng-click="refreshNdaysHistory(ndaysHistoryInput)">Refresh</button></td>
							</uib-accordion-group> 
							
							<uib-accordion-group heading="Show unrealizedDetails">
								<tr>
									<button ng-click="refreshUnrealizedDetails(unrealizedDetails)">Refresh</button>
								</tr>
								<tr>
									<textarea class="textArea" ng-model="unrealizedDetails.content"></textarea>
								</tr>
							</uib-accordion-group> 
						</uib-accordion></td>
				</tr>
			</form>
			<br />
			
			<div id="grid2" style="word-wrap: normal" ui-grid-exporter ui-grid="nDaysHistoryGrid" class="nDaysHistoryGrid" />

			<h3>Unrealized Details</h3>
			<div id="grid3" style="word-wrap: normal" ui-grid-exporter ui-grid="unrealizedDetailsGrid" ui-grid-grouping class="grid" />
			 
		</div>
	</body>
</html>