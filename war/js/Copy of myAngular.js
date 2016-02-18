
function init() {
	window.init();
}

var app = angular.module('finchakksApp', ['ui.grid','ui.grid.exporter']);
app.controller('initializeController', 
	function($scope, $window, uiGridConstants) {
	  $scope.blackListedGrid = {
			  	enableGridMenu: true,  
			  	enableSorting: true,
			  	exporterCsvFilename: 'blackListed.csv',
	    	    showColumnFooter: true,
//	    	    columnDefs: [
//	    	      { field: 'stockName'},
//	    	      { name : 'Return(%)', field: 'returnTillDate' ,  cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true, footerCellFilter:'number: 2' },
//	    	      { name : 'Impact(%)',field: 'formattedImpactOnAverageReturn', cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 2'},
//	    	      { field: 'quantity'},
//	    	      { field: 'totalInvestment', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
//	    	      { field: 'totalReturn', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
//	    	      {  name : 'Bank Return', field: 'totalReturnIfBank', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  }
//	    	    ],
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.grid1Api = gridApi;
	    	    }
	    	  };
/*	  
	  $scope.nDaysHistoryGrid = {
			  	enableGridMenu: true,  
			  	enableSorting: true,
			  	exporterCsvFilename: 'nDaysHistory.csv',
	    	    showColumnFooter: true,
	    	    columnDefs: [
	    	      { field: 'stockName'},
	    	      { field: 'simpleMovingAverageAndSellDeltaNormalized'},
	    	      { field: 'netNDaysGain'},
	    	      { field: 'simpleMovingAverageAndSellDeltaNormalized'},
	    	      { name : 'Rating', field: 'stockRatingValue.score'},
	    	      { field: 'investmentRatio'},
	    	      { field: 'industryInvestmentRatio'},
	    	      { name : 'Return', field: 'returnTillDate'},
	    	      { name : 'Impact(%)',field: 'impactOnAverageReturn'},
	    	      { field: 'buyPrice'},
	    	      { field: 'duration'},
	    	      { field : 'industry'},
	    	      { field: 'sellPrice'},
	    	      { field : 'simpleMovingAverage'}
	    	    ],
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.grid1Api = gridApi;
	    	    }
	    	  };
	  */
		$window.init = function() {
			$scope.$apply($scope.load_initialize_end_points);
	    };
	    
	    $scope.load_initialize_end_points = function() {
	    	gapi.client.load('initalizeControllerEndPoint', 'v1', function() {
				$scope.is_backend_ready = true;
//				$scope.listNdaysHistoryStocks();
				$scope.listBlackListedStocks();
			}, '/_ah/api');
		};
		
//		$scope.listNdaysHistoryStocks = function() {
//			gapi.client.initalizeControllerEndPoint.listNDaysHistoryStocks().execute(
//					function(resp) {
//						$scope.nDaysHistoryGrid.data = resp.items;
//						$scope.$apply();
//					}
//			);
//		};
		
		$scope.listBlackListedStocks = function() {
			gapi.client.initalizeControllerEndPoint.listBlackListedStocks().execute(
					function(resp) {
						$scope.blackListedGrid.data = resp.items;
						$scope.$apply();
					}
			);
		};
	}
);

