
function init() {
	window.init();
}

var app = angular.module('maintainanceApp', ['ui.grid', 'ui.grid.grouping', 'ui.grid.exporter', 'ui.grid.rowEdit', 'ui.grid.edit', 'ui.grid.resizeColumns', , 'ui.bootstrap']);
app.controller('maintainanceController', 
	function($scope, $window, uiGridConstants,$q, uiGridGroupingConstants) {
	
	$window.init = function() {
		$scope.$apply($scope.load_initialize_end_points);
    };
    
    $scope.load_initialize_end_points = function() {
    	var ROOT = 'https://2-dot-finchakks.appspot.com/_ah/api';
    	//var ROOT = 'http://localhost:8888/_ah/api';
    	
    	gapi.client.load('allScripsControllerEndPoint', 'v1', function() {}, ROOT);
		
	};
	
    $scope.loader = {
			scripDetailsLoading : false,
			scripDetailsModifying : false
    		};
	
	$scope.scripInput= {};
//	$scope.allScripsObject = {};
	
	
	$scope.retrieveScripDetails=function(scripInput)
	{
		$scope.loader.scripDetailsLoading = true;
		var nseId = $scope.scripInput.nseId;
		var endPoint = gapi.client.allScripsControllerEndPoint;
		var	request = endPoint.getScripDetails({nseId:nseId});
		
		request.execute(
				function(resp) {
					$scope.scripDetailsGrid.data = resp.items;
					$scope.loader.scripDetailsLoading = false;
					$scope.$apply();
				}
		);
	};//retrieveAll	
	
	$scope.save=function()
	{
		$scope.loader.scripDetailsModifying = true;
		var dirtyRows = $scope.gridApi.rowEdit.getDirtyRows($scope.gridApi.grid);
		var nseId = dirtyRows[0].entity.nseId;
		var moneycontrolName = dirtyRows[0].entity.moneycontrolName;
		var isWatchListed = dirtyRows[0].entity.isWatchListed;
		var isBlackListed = dirtyRows[0].entity.isBlackListed;
		
		$scope.allScripsObject={"nseId":nseId, "moneycontrolName":moneycontrolName, "isWatchListed":isWatchListed, "isBlackListed":isBlackListed};
		var endPoint = gapi.client.allScripsControllerEndPoint;
		var	request = endPoint.modifyScripDetails($scope.allScripsObject);
		
		request.execute(
				function(resp) {
					$scope.scripDetailsGrid.data = resp.items;
					$scope.loader.scripDetailsModifying = false;
					$scope.$apply();
				}
		);
	};//retrieveAll	
	
	//grid definitions
	$scope.scripDetailsGrid = {
			rowEditWaitInterval: -1,  
			enableGridMenu: true,  
	    	  enableColumnResizing: true,
	    	    columnDefs: [
	    	      { field: 'nseId'},
	    	      { name : 'bseId'},
	    	      { field: 'moneycontrolName'},
	    	      { field: 'isWatchListed', type: 'boolean'},//,cellTemplate: '<input type="checkbox" ng-model="row.entity.isWatchListed">'
	    	      { field: 'isBlackListed', type: 'boolean'}
	    	    ]
	    	  };
	
	//gets invoked when the grid is edited and Enter pressed
	 $scope.scripDetailsGrid.onRegisterApi = function(gridApi){
         //set gridApi on scope
         $scope.gridApi = gridApi;
         gridApi.rowEdit.on.saveRow($scope, $scope.saveRow);
         
//         gridApi.edit.on.afterCellEdit($scope,function(rowEntity, colDef, newValue, oldValue){
//           $scope.modification.lastCellEdited = 'edited row id:' + rowEntity.nseId + ' Column:' + colDef.name + ' newValue:' + newValue + ' oldValue:' + oldValue ;
//           alert($scope.modification.lastCellEdited);
//           $scope.$apply();
//         });
       };
       
       $scope.saveRow = function( rowEntity ) {
    	    // create a fake promise - normally you'd use the promise returned by $http or $resource
    	   alert("nseId "+rowEntity.nseId);
    	   alert("bseId "+rowEntity.bseId);
    	   alert("moneycontrolName "+rowEntity.moneycontrolName);
    	   alert("isWatchListed "+rowEntity.isWatchListed);
    	   alert("isBlackListed "+rowEntity.isBlackListed);
    	   
    	   var promise = $q.defer();
    	   
    	    $scope.gridApi.rowEdit.setSavePromise( rowEntity, promise.promise );
    	 
    	  };
			
});

