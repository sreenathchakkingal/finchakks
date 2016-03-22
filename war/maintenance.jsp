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
				
		<script src="js/maintainance.js"></script>
		<script src="https://apis.google.com/js/client.js?onload=init"></script>
		<link rel="stylesheet" href="js/main.css" type="text/css">

 		<%@include file="angularcore.jsp" %>		

	</head>
	<body>
		<div ng-app="maintainanceApp" ng-controller="maintainanceController">
			Nse Id: <input type="text" ng-model="scripInput.nseId" id="stockName">

			<button ng-click="retrieveScripDetails()">Retrieve</button>
			<br>
						
			<button ng-click="retriveInterestingScrips()">Retrieve Interesting Scrips</button>
			<i ng-show="loader.retrivingInterestingScrips" class="btn btn-success">
				Retrieving Interesting ScripDetails ...
				<i class="fa fa-spinner fa-spin"></i>
			</i> 

			<i ng-show="loader.scripDetailsLoading" class="btn btn-success">
				ScripDetails Loading...
				<i class="fa fa-spinner fa-spin"></i>
			</i> 

			<h3>Scrip Details</h3>
			<div id="grid1"  ui-grid-edit  ui-grid-row-edit  ui-grid-save-state ui-grid-selection ui-grid-cellNav ui-grid-resize-columns ui-grid-move-columns ui-grid-pinning 
			ui-grid="scripDetailsGrid" class="scripDetailsGrid"/>

			<button ng-click="save()">Save</button>

			<i ng-show="loader.scripDetailsModifying" class="btn btn-success">
				ScripDetails Modifying...
				<i class="fa fa-spinner fa-spin"></i>
			</i> 

			<button ng-click="getScripRating()">Rating</button>

			<h3>Scrip Ratings</h3>
			<div id="grid2"  ui-grid-edit  ui-grid-row-edit  ui-grid-save-state ui-grid-selection ui-grid-cellNav ui-grid-resize-columns ui-grid-move-columns ui-grid-pinning 
			ui-grid="scripRatingsGrid"/>			
			
		</div>
	</body>
</html>