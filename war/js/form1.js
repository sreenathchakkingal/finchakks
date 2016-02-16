
function init() {
	window.init();
}

var app = angular.module('myApp', ['ui.grid','ui.grid.exporter']);
app.controller('myCtrl', 
	function($scope, $window, uiGridConstants) {
	  $scope.gridOptions1 = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'myFile.csv',
	    	    showColumnFooter: true,
	    	    enableFiltering: true,
	    	    columnDefs: [
	    	      { field: 'stockName'},
	    	      { name : 'Return(%)', field: 'returnTillDate' ,  cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true },
	    	      { field: 'quantity'},
	    	      
	    	      { field: 'totalInvestment', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true  },
	    	      { field: 'totalReturn', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true  },
	    	      {  name : 'Bank Return', field: 'totalReturnIfBank', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true  },
	    	      { name : 'Impact(%)',field: 'formattedImpactOnAverageReturn', cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true}
	    	    ],
//	    	  
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.grid1Api = gridApi;
	    	    }
	    	  };
	  
		$window.init = function() {
			$scope.$apply($scope.load_guestbook_lib);
	    };
	    
	    $scope.load_guestbook_lib = function() {
	    	gapi.client.load('initalizeControllerEndPoint', 'v1', function() {
				$scope.is_backend_ready = true;
				$scope.list();
			}, '/_ah/api');
		};
		
				
		$scope.list = function() {
			gapi.client.initalizeControllerEndPoint.listBlackListedStocks().execute(
					function(resp) {
						$scope.messages = resp.items;
						$scope.gridOptions1.data = resp.items;
						$scope.$apply();
					}
			);
		};

		$scope.firstName= "John";
		$scope.lastName= "Doe111";
	}
);

