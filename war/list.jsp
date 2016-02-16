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
    
<script src="js/form1.js"></script>
<script src="https://apis.google.com/js/client.js?onload=init"></script>
</head>
<body>
	
<div ng-app="myApp" ng-controller="myCtrl">

<div id="grid1" ui-grid-exporter ui-grid="gridOptions1" class="grid"></div>

<!-- 
First Name: <input type="text" ng-model="firstName"><br>
Last Name: <input type="text" ng-model="lastName"><br>
<br>
Full Name: {{firstName + " " + lastName}}
 
messages
 	<ul>
			<li ng-repeat="message in messages">
				{{message}}
				</li>
		</ul> 
	 	  -->
</div>

</html>