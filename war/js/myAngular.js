
function init() {
	window.init();
}

var app = angular.module('finchakksApp', ['ui.grid','ui.grid.exporter',  'ui.grid.resizeColumns']);
app.controller('initializeController', 
	function($scope, $window, uiGridConstants) {
	  
		$scope.blackListedGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'blackListed.csv',
	    	    showColumnFooter: true,
	    	    enableColumnResizing: true,
	    	    columnDefs: [
	    	      { field: 'stockName'},
	    	      { name : 'Return(%)', field: 'returnTillDate' ,  cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true, footerCellFilter:'number: 2' },
	    	      { name : 'Impact(%)',field: 'formattedImpactOnAverageReturn', cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 2'},
	    	      { field: 'quantity'},
	    	      { field: 'totalInvestment', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
	    	      { field: 'totalReturn', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
	    	      {  name : 'Bank Return', field: 'totalReturnIfBank', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  }
	    	    ],
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.grid1Api = gridApi;
	    	    }
	    	  };
	  
	  $scope.nDaysHistoryGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'nDaysHistory.csv',
	    	    showColumnFooter: true,
	    	    enableFiltering: true,
	    	    enableColumnResizing: true,
	    	    columnDefs: [
	    	      { name : 'Name', field: 'stockName', width:"125"},
	    	      { name : 'SMV (%)',field: 'simpleMovingAverageAndSellDeltaNormalized', cellFilter: 'number: 0'},
	    	      { name : 'NDays Gain(%)', field: 'formattedNetNDaysGain', cellFilter: 'number: 0'},
	    	      { name : 'Score', field: 'stockRatingValue.score'},
	    	      { name : 'Inv (%)', field: 'formattedInvestmentRatio', cellFilter: 'number: 0'},
	    	      { name : 'Ind Inv (%)',field: 'formattedIndustryInvestmentRatio', cellFilter: 'number: 0'},
	    	      { name : 'Return (%)', field: 'returnTillDate', cellFilter: 'number: 2'},
	    	      { name : 'Impact (%)',field: 'formattedImpactOnAverageReturn'},
	    	      { field: 'buyPrice'},
	    	      { name : 'Period', field: 'duration'},
	    	      { name : 'Ind', field : 'industry', cellTooltip: true},
	    	      { field: 'sellPrice'},
	    	      { name :'SMV', field : 'simpleMovingAverage', cellFilter: 'number: 0'},
	    	      { name :'Day 1' , field : 'formattedValues[0]',  cellFilter: 'number: 2'},
	    	      { name :'Day 2' , field : 'formattedValues[1]',  cellFilter: 'number: 2'},
	    	      { name :'Day 3' , field : 'formattedValues[2]',  cellFilter: 'number: 2'},
	    	      { name :'Day 4' , field : 'formattedValues[3]',  cellFilter: 'number: 2'},
	    	      { name :'Day 5' , field : 'formattedValues[4]',  cellFilter: 'number: 2'},
	    	      { name :'Day 6' , field : 'formattedValues[5]',  cellFilter: 'number: 2'}
	    	      ],
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.grid1Api = gridApi;
	    	    }
	    	  };

	  $scope.unrealizedDetailsGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'unrealizedDetails.csv',
	    	    showColumnFooter: true,
	    	    enableFiltering: true,
	    	    enableColumnResizing: true,
	    	    columnDefs: [
	    	      { field: 'stockName', width:"130"},
	    	      { name : 'Return (%)',field: 'returnTillDate', cellFilter: 'number: 2'},
	    	      { field: 'buyPrice', cellFilter: 'number: 2'},
	    	      { field: 'sellPrice', cellFilter: 'number: 2'},
	    	      { name : 'Period', field: 'duration'},
	    	      { name : 'Diff'},
	    	      { name : 'Qty', field: 'quantity'},
	    	      { field: 'buyDate'},
	    	      { name: 'total Investment'},
	    	      { field: 'bankSellPrice'},
	    	      { field: 'bankSellPrice'},
	    	      { name: 'totalSellPrice'},
	    	      { name: 'totalBankSellPrice'},
	    	      { name: 'PnL'},
	    	      { name: 'PnLIfBank'}
	    	      ],
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.grid1Api = gridApi;
	    	    }
	    	  };
	  
		$window.init = function() {
			$scope.$apply($scope.load_initialize_end_points);
	    };
	    
	    $scope.load_initialize_end_points = function() {
	    	gapi.client.load('initalizeControllerEndPoint', 'v1', function() {
				$scope.listBlackListedStocks();
				$scope.listNDaysHistoryStocks();
				$scope.listUnrealizedDetails();
			}, '/_ah/api');
		};
		
		$scope.listNDaysHistoryStocks = function() {
			gapi.client.initalizeControllerEndPoint.listNDaysHistoryStocks().execute(
					function(resp) {
						$scope.nDaysHistoryGrid.data = resp.items;
						$scope.$apply();
					}
			);
		};
				
		$scope.listBlackListedStocks = function() {
			gapi.client.initalizeControllerEndPoint.listBlackListedStocks().execute(
					function(resp) {
						$scope.blackListedGrid.data = resp.items;
						$scope.$apply();
					}
			);
		};
		
		$scope.listUnrealizedDetails = function() {
			gapi.client.initalizeControllerEndPoint.listUnrealizedDetails().execute(
					function(resp) {
						$scope.unrealizedDetailsGrid.data = resp.items;
						$scope.$apply();
					}
			);
		};

	}
);

