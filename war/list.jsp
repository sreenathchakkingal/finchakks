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

		<script src="js/form.js"></script>
		<script src="https://apis.google.com/js/client.js?onload=init"></script>

		<link href="//netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
		<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
		<link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">
		<link rel="stylesheet" href="js/main.css" type="text/css">
	</head>
	<body>
		<div ng-app="myApp" ng-controller="MyCtrl">
			<uib-tabset active="active"> 
				<uib-tab index="0" heading="Data"> 

				<h3>Black Listed Stocks</h3>
				<div class="col-md-12" ng-if="test">
					<div id="grid1" ui-grid-exporter ui-grid="blackListedGrid" />
				</div>
				</uib-tab> 
			
				<uib-tab index="1" heading="Maintenance"> 
					something
				</uib-tab> 
			</uib-tabset>
		</div>
	</body>
</html>