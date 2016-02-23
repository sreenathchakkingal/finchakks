
function init() {
	window.init();
}

var app = angular.module('finchakksApp', ['ui.grid','ui.grid.exporter',  'ui.grid.resizeColumns', 'ui.grid.grouping']);
app.controller('initializeController', 
	function($scope, $window, uiGridConstants, uiGridGroupingConstants) {

	  var calculateWeightedAverageReturn = function( columns, rows ) {
		    columns.forEach( function( column ) {
		      if ( column.grouping && column.grouping.groupPriority > -1 ){
		        
		        column.treeAggregationFn = function( aggregation, fieldValue, numValue, row ) {
		          if ( typeof(aggregation.weightedReturnBuyPrice) === 'undefined') {
		            aggregation.weightedReturnBuyPrice = 0;
		            aggregation.sumOfBuyPrice = 0;
		          }
		          aggregation.weightedReturnBuyPrice = aggregation.weightedReturnBuyPrice + row.entity.buyPrice*row.entity.returnTillDate;
		          aggregation.sumOfBuyPrice = aggregation.sumOfBuyPrice + row.entity.buyPrice;
		        };
		        
		        column.customTreeAggregationFinalizerFn = function( aggregation ) {
		        	aggregation.weightedAvgReturn = aggregation.weightedReturnBuyPrice/aggregation.sumOfBuyPrice;
		        	aggregation.weightedAvgReturn = aggregation.weightedAvgReturn.toFixed();
		        	if ( typeof(aggregation.groupVal) !== 'undefined') {
		        	  aggregation.rendered = aggregation.groupVal + ' (' + (aggregation.weightedAvgReturn) + ')';
		          } else {
		            aggregation.rendered = null;
		          }
		        };
		      }
		    });
		    return columns;
		  };

		  $scope.firstName = "John";
		    $scope.lastName = "Doe";
	//grid definitions
	
	$scope.blackListedGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'blackListed.csv',
	    	    showColumnFooter: true,
	    	    enableColumnResizing: true,
	    	    columnDefs: [
	    	      { field: 'stockName'},
	    	      { name : 'Return(%)', field: 'returnTillDate' ,  cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true, footerCellFilter:'number: 2' },
	    	      { name : 'Impact(%)',field: 'getImpactOnAverageReturnAsPercent()', cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 2'},
	    	      { field: 'quantity'},
	    	      { field: 'totalInvestment', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
	    	      { field: 'totalReturn', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
	    	      {  name : 'Bank Return', field: 'totalReturnIfBank', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  }
	    	    ]
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
	    	      { name : 'SMV (%)',field: 'simpleMovingAverageAndSellDeltaNormalized.toFixed()'},
	    	      { name : 'NDays Gain(%)', field: 'getNetNDaysGainAsPercent().toFixed()'},
	    	      { name : 'Score', field: 'stockRatingValue.score'},
	    	      { name : 'Inv (%)', field: 'getInvestmentRatioAsPercent().toFixed()'},
	    	      { name : 'Ind Inv (%)',field: 'getIndustryInvestmentRatioAsPercent().toFixed()'},
	    	      { name : 'Return (%)', field: 'returnTillDate.toFixed()'},
	    	      { name : 'Impact (%)',field: 'getImpactOnAverageReturnAsPercent()'},
	    	      { field: 'buyPrice'},
	    	      { name : 'Period', field: 'duration'},
	    	      { name : 'Ind', field : 'industry', cellTooltip: true},
	    	      { name : 'Closed Price', field: 'sellPrice', width:"75"},
	    	      { name :'SMV', field : 'simpleMovingAverage.toFixed()'},
	    	      { name :'Day 1' , field : 'formattedValues[0].toFixed(2)'},
	    	      { name :'Day 2' , field : 'formattedValues[1].toFixed(2)'},
	    	      { name :'Day 3' , field : 'formattedValues[2].toFixed(2)'},
	    	      { name :'Day 4' , field : 'formattedValues[3].toFixed(2)'},
	    	      { name :'Day 5' , field : 'formattedValues[4].toFixed(2)'},
	    	      { name :'Day 6' , field : 'formattedValues[5].toFixed(2)'}
	    	      ]
	    	  };
	  
	  $scope.unrealizedDetailsGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'unrealizedDetails.csv',
	    	    enableFiltering: true,
	    	    enableColumnResizing: true,
	    	    showColumnFooter: true,
	    	    columnDefs: [
	    	      { field: 'stockName', grouping: { groupPriority: 0 }, width:"130"},
	    	      { name : 'Return (%)',field: 'returnTillDate.toFixed(0)', treeAggregationType: uiGridGroupingConstants.aggregation.AVG,
	    	    	  //this ensures that "avg" is not printed in the summary volumn
	    	    	customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	{
    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	        },
	    	        //for footer
	    	        aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true, footerCellFilter:'number: 2'
	    	      },
	    	      
	    	      { name: 'Buy Price', field: 'buyPrice.toFixed()', treeAggregationType: uiGridGroupingConstants.aggregation.AVG,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	      },
	    	      { name: 'Sell Price',field: 'sellPrice', treeAggregationType: uiGridGroupingConstants.aggregation.MAX,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value;
	    	          }
    	          },
    	          { name : 'Period', field: 'duration', treeAggregationType: uiGridGroupingConstants.aggregation.MIN,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value;
	    	          }
    	          },
    	          
	    	      { name : 'Diff', field: 'getTotalSellPriceDiffTotalBankSell()', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      
	    	      { name : 'Qty', field: 'quantity', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	      },
	    	      { field: 'buyDate',  treeAggregationType: uiGridGroupingConstants.aggregation.MAX,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value;
	    	          },

	    	      },
	    	      { name: 'Total Inv', field: 'getTotalInvestment()', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { field: 'bankSellPrice', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	      },
	    	      { name: 'Total Sell Price',field: 'getTotalSellPrice().toFixed(0)', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { name: 'Total Bank Sell Price', field: 'getTotalBankSellPrice().toFixed(0)', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { name: 'Return', field:'getProfitAndLoss().toFixed(0)', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { name: 'Return (Bank)', field:'getBankProfit()', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      }
	    	      ],
	    	    onRegisterApi: function( gridApi ) {
	    	      $scope.gridApi = gridApi;
	    	      $scope.gridApi.grid.registerColumnsProcessor(calculateWeightedAverageReturn);
	    	    }
	    	  };
	  
		$window.init = function() {
			$scope.$apply($scope.load_initialize_end_points);
	    };
	    
	    $scope.load_initialize_end_points = function() {
	    	var ROOT = 'https://2-dot-finchakks.appspot.com/_ah/api';
//	    	var ROOT = 'http://localhost:8888/_ah/api';
	    	gapi.client.load('initalizeControllerEndPoint', 'v1', function() {
				$scope.listBlackListedStocks();
				$scope.listNDaysHistoryStocks();
				$scope.listUnrealizedDetails();
				$scope.getProfitAndLoss();
			}, ROOT);
		};
		
		$scope.getProfitAndLoss = function() {
			gapi.client.initalizeControllerEndPoint.getProfitAndLoss().execute( 
					function(resp) {
						$scope.profitAndLoss = resp;
						$scope.$apply();
					}
			);
			
		};
		
		$scope.listNDaysHistoryStocks = function() {
			gapi.client.initalizeControllerEndPoint.listNDaysHistoryStocks().execute(
					function(resp) {
						$scope.nDaysHistoryGrid.data = resp.items;
						
						//additional fields 
						angular.forEach($scope.nDaysHistoryGrid.data, function(row)
						{
							row.getInvestmentRatioAsPercent= function()
					  		{
								return this.investmentRatio*100;
					  		};
					  		row.getIndustryInvestmentRatioAsPercent= function()
					  		{
								return this.industryInvestmentRatio*100;
					  		};
					  		row.getImpactOnAverageReturnAsPercent= function()
					  		{
								return this.impactOnAverageReturn*100;
					  		};
					  		row.getNetNDaysGainAsPercent= function()
					  		{
								return this.netNDaysGain*100;
					  		};
					  		
						});				
						
						$scope.$apply();
					}
			);
		};
				
		$scope.listBlackListedStocks = function() {
			
			gapi.client.initalizeControllerEndPoint.listBlackListedStocks().execute( 
					function(resp) {
						$scope.blackListedGrid.data = resp.items;

						//additional fields 
						angular.forEach($scope.blackListedGrid.data, function(row)
						{
							row.getImpactOnAverageReturnAsPercent= function()
					  		{
								return this.impactOnAverageReturn*100;
					  		};
						});
						
						$scope.$apply();
					}
			);
			
		};
		
		$scope.listUnrealizedDetails = function() {
			gapi.client.initalizeControllerEndPoint.listUnrealizedDetails().execute(
					function(resp) 
					{
						$scope.unrealizedDetailsGrid.data = resp.items;
					
						//additional fields 
						angular.forEach($scope.unrealizedDetailsGrid.data, function(row)
								{
							  		row.getTotalSellPriceDiffTotalBankSell= function()
							  		{
							  			var result =(this.sellPrice - this.bankSellPrice)*this.quantity
							  			return result.toFixed();
							  		};
							  		
							  		row.getTotalInvestment= function()
							  		{
							  			return this.buyPrice *this.quantity;
							  		};
							  		row.getTotalSellPrice= function()
							  		{
							  			return this.sellPrice *this.quantity;
							  		};
							  		row.getTotalBankSellPrice= function()
							  		{
							  			return this.bankSellPrice *this.quantity;
							  		};
							  		row.getProfitAndLoss= function()
							  		{
							  			return (this.sellPrice - this.buyPrice)*this.quantity;
							  		};
							  		
							  		row.getBankProfit= function()
							  		{
							  			return (this.bankSellPrice - this.buyPrice)*this.quantity;
							  		};
								});
						
						
						$scope.$apply();
					}
			);
		};

	}
);

