
function init() {
	window.initGapi();
}

var app = angular.module('maintainanceApp', ['ui.grid', 'ui.grid.grouping', 'ui.grid.exporter', 'ui.grid.rowEdit', 
                                             'ui.grid.edit', 'ui.grid.resizeColumns', 'ui.grid.saveState',
                                             'ui.grid.selection', 'ui.grid.cellNav', 'ui.bootstrap']);
app.controller('maintainanceController', 
	function($scope, $window, uiGridConstants,uiGridGroupingConstants,$q) {
	
	$window.initGapi = function() {
		$scope.$apply($scope.load_initialize_end_points);
    };
    
    $scope.load_initialize_end_points = function() {
    	var ROOT = 'https://finchakks.appspot.com/_ah/api';
    	//var ROOT = 'https://3-dot-finchakks.appspot.com/_ah/api';
//    	var ROOT = 'http://localhost:8888/_ah/api';

    	gapi.client.load('allScripsControllerEndPoint', 'v1', function() {
    		$scope.retriveInterestingScrips(); 
		}, ROOT);
	};
	
    $scope.loader = {
			scripDetailsLoading : false,
			scripDetailsModifying : false,
			retrivingInterestingScrips : false,
			scripRatings: false,
			scripRatingModifying: false
    		};
	
	$scope.scripInput= {};
	
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
		var rowEntity = $scope.selectedScripDetail[0];
		var nseId = rowEntity.nseId;
		var moneycontrolName = rowEntity.moneycontrolName;
		var isWatchListed = rowEntity.isWatchListed;
		var isBlackListed = rowEntity.isBlackListed;
		
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
	};//save
	
	$scope.saveRating=function()
	{
		$scope.loader.scripRatingModifying = true;	
		var rowEntity = $scope.selectedScripDetail[0];
		var nseId = rowEntity.nseId;
		var moneycontrolName = rowEntity.moneycontrolName;
		var isWatchListed = rowEntity.isWatchListed;
		var isBlackListed = rowEntity.isBlackListed;
		
		$scope.allScripsObject={"nseId":nseId, "moneycontrolName":moneycontrolName, "isWatchListed":isWatchListed, "isBlackListed":isBlackListed};
		var endPoint = gapi.client.allScripsControllerEndPoint;
		var	request = endPoint.modifyScripDetails($scope.allScripsObject);
		
		request.execute(
				function(resp) {
					$scope.scripDetailsGrid.data = resp.items;
					$scope.loader.scripRatingModifying = false;
					$scope.$apply();
				}
		);
	};//save

	$scope.scripRatingInput={"nseId":"ITC"};
	
	$scope.getScripRating = function() {
		$scope.loader.scripRatings = true;	
		var rowEntity = $scope.selectedScripDetail[0];
		var nseId = rowEntity.nseId;
		var endPoint = gapi.client.allScripsControllerEndPoint;
		var	request = endPoint.getScripRatings({nseId:nseId});
		
		request.execute(
				function(resp) {
					$scope.scripRatingsGrid.data = resp.items;
					$scope.loader.scripRatings = false;
					$scope.$apply();
				}
		);
	}; //getScripRatings
	
	$scope.retriveInterestingScrips=function()
	{
		$scope.loader.retrivingInterestingScrips = true;
		var endPoint = gapi.client.allScripsControllerEndPoint;
		var	request = endPoint.retriveInterestingScrips();
		
		request.execute(
				function(resp) {
					$scope.scripDetailsGrid.data = resp.items;
					$scope.loader.retrivingInterestingScrips = false;
					$scope.$apply();
				}
		);
	};//retriveInterestingScrips

	$scope.selectedScripDetail = [];
	
	//grid definitions
	$scope.scripDetailsGrid = {
			rowEditWaitInterval: -1, 
			enableRowSelection: true,
			enableGridMenu: true,  
	    	enableColumnResizing: true,
	    	multiSelect: false,
	    	enableFiltering: true,
    	    columnDefs: [
    	      { field: 'nseId'},
    	      { field : 'bseId'},
    	      { field: 'moneycontrolName'},
    	      { field: 'isWatchListed', type: 'boolean'},
    	      { field: 'isBlackListed', type: 'boolean'}
    	    ],
    	    onRegisterApi: function(gridApi){
    	      $scope.gridApi = gridApi;
    	      
    	      gridApi.selection.on.rowSelectionChanged($scope,function(rows){
    	          $scope.selectedScripDetail = gridApi.selection.getSelectedRows();
    	        });
    	    }
	  };
	
	$scope.scripRatingsGrid = {
			rowEditWaitInterval: -1, 
			enableRowSelection: true,
			enableGridMenu: true,  
	    	enableColumnResizing: true,
    	    columnDefs: [
    	      { field: 'ratingName'},
    	      { field : 'ratingValue',
    	    	editDropdownValueLabel: 'name',
	            editableCellTemplate: 'ui-grid/dropdownEditor',
	            editDropdownOptionsArray: [
	    					{id:1, name:'Good'},
	    					{id:2, name:'Average'},
	    					{id:3, name:'Bad'},
	    					{id:4, name:'Not Rated'}  ]
    	      }
    	    ],
    	    onRegisterApi: function(gridApi){
    	      $scope.gridApi = gridApi;
    	    }
	  };	
});

