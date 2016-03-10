<!doctype html>
<html ng-app="app">
  
  <head>
	<head>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular.min.js"></script>
		<script	src="http://ajax.googleapis.com/ajax/libs/angularjs/1.4.3/angular-touch.js"></script>
		<script src="http://ajax.googleapis.com/ajax/libs/angularjs/1.5.0/angular-animate.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/csv.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/pdfmake.js"></script>
		<script src="http://ui-grid.info/docs/grunt-scripts/vfs_fonts.js"></script>
		<script src="http://ui-grid.info/release/ui-grid.js"></script>
		
		<script src="//angular-ui.github.io/bootstrap/ui-bootstrap-tpls-0.13.0.js"></script>
		<script src="http://ui-grid.info/release/ui-grid-unstable.js"></script>
		<link href="//netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet">
		<link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.min.css" rel="stylesheet">
		<link rel="stylesheet" href="http://ui-grid.info/release/ui-grid.css" type="text/css">
				
		<script src="js/form.js"></script>
		<script src="https://apis.google.com/js/client.js?onload=init"></script>
		<link rel="stylesheet" href="js/formMain.css" type="text/css">
	</head>
    
  </head>
  <body>

<div ng-controller="MainCtrl">
  
  <tabset>
    <tab heading="Tab 1" >
        <h1>1st Content</h1>
        <div id="grid1" ui-grid="gridOptions" ></div>
    </tab>
    <tab heading="Tab 2" >
        <h1>2nd Content</h1>
        <div id="grid2" ui-grid="gridOptions" ui-grid-cellNav ui-grid-resize-columns ui-grid-move-columns ui-grid-pinning ui-grid-selection ui-grid-exporter ui-grid-auto-resize class="grid"></div>
    </tab>
    <tab heading="Tab 3">
        <h1>3rd Content</h1>
        <div id="grid2" ui-grid="gridOptions" ui-grid-cellNav ui-grid-resize-columns ui-grid-move-columns ui-grid-pinning ui-grid-selection ui-grid-exporter ui-grid-auto-resize class="grid"></div>        
    </tab>
  </tabset>
</div>

    <script src="js/form.js"></script>
  </body>
</html>
