
function init() {
	window.init();
}

var app = angular.module('finchakksApp', ['ui.grid','ui.grid.exporter', 'ui.grid.resizeColumns', 'ui.grid.grouping', 'ui.bootstrap']);
app.controller('initializeController', 
	function($scope, $window, uiGridConstants, uiGridGroupingConstants) {
	
    $scope.loader = {
    				initialLoading: true,
    				nDaysHistoryLoading : false,
    				unrealizedDetailsLoading : false
    	    		};
    
	  var calculateWeightedAverageReturn = function( columns, rows ) {
		    columns.forEach( function( column ) {
		      if ( column.grouping && column.grouping.groupPriority > -1 ){
		        
		        column.treeAggregationFn = function( aggregation, fieldValue, numValue, row ) {
			      	var date1 = new Date(row.entity.buyDate);
			    	var date2 = new Date();
			    	var timeDiff = Math.abs(date2.getTime() - date1.getTime());
			    	var diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)); 
			    	
		        if ( typeof(aggregation.weightedReturnBuyPrice) === 'undefined') {
		            aggregation.weightedReturnBuyPrice = 0;
		            aggregation.totalInvestment = 0;
		          }

		        if(diffDays>90)
		        	{
		        	aggregation.weightedReturnBuyPrice = aggregation.weightedReturnBuyPrice + row.entity.getTotalInvestment()*row.entity.returnTillDate;
			          aggregation.totalInvestment = aggregation.totalInvestment + row.entity.buyPrice*row.entity.quantity;
		        	}
		        };
		        
		        column.customTreeAggregationFinalizerFn = function( aggregation ) {
		        	if(aggregation.totalInvestment>0)
		        		{
			        	aggregation.weightedAvgReturn = aggregation.weightedReturnBuyPrice/aggregation.totalInvestment;
//			        	aggregation.weightedAvgReturn = aggregation.weightedAvgReturn.toFixed();
		        		}
		        	else
		        		{
		        		aggregation.weightedAvgReturn=0;
		        		}
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

	//grid definitions
	$scope.blackListedGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'blackListed.csv',
	    	    showColumnFooter: true,
	    	    enableColumnResizing: true,
	    	    columnDefs: [
	    	      { field: 'stockName'},
	    	      { name : 'Return(%)', field: 'returnTillDate' ,type: 'number',  cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true, footerCellFilter:'number: 2', sort: { priority: 0, direction: 'desc' }},
	    	      { name : 'Impact(%)',field: 'getImpactOnAverageReturnAsPercent()', type: 'number', cellFilter: 'number: 2', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 2'},
	    	      { field: 'quantity', type: 'number'},
	    	      { field: 'totalInvestment', type: 'number', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
	    	      { field: 'totalReturn', type: 'number', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  },
	    	      {  name : 'Bank Return', type: 'number', field: 'totalReturnIfBank', cellFilter: 'number: 0', aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'  }
	    	    ]
	    	  };
	  
	  $scope.nDaysHistoryGrid = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'nDaysHistory.csv',
	    	    enableFiltering: true,
	    	    enableColumnResizing: true,
	    	    columnDefs: [
	    	      { name : 'Name', field: 'stockName', width:"125"},
	    	      { name : 'SMV (%)',field: 'simpleMovingAverageAndSellDeltaNormalized', type: 'number',cellFilter: 'number: 0', 
		    	      filters: [
	    	                       {
	    	                           condition: uiGridConstants.filter.GREATER_THAN,
	    	                           placeholder: 'greater than'
	    	                         },
	    	                         {
	    	                           condition: uiGridConstants.filter.LESS_THAN,
	    	                           placeholder: 'less than'
	    	                         }
		    	               ],
	    	      },
	    	      { name : 'NDays (%)', field: 'getNetNDaysGainAsPercent()',type: 'number', cellFilter: 'number: 0',
		    	      filters: [
	    	                       {
	    	                           condition: uiGridConstants.filter.GREATER_THAN,
	    	                           placeholder: 'greater than'
	    	                         },
	    	                         {
	    	                           condition: uiGridConstants.filter.LESS_THAN,
	    	                           placeholder: 'less than'
	    	                         }
		    	               ]
	    	      },
	    	      { name : 'Score', field: 'stockRatingValue.score'},
	    	      { name : 'Inv (%)', field: 'getInvestmentRatioAsPercent()',type: 'number', cellFilter: 'number: 0'},
	    	      { name : 'Ind Inv (%)',field: 'getIndustryInvestmentRatioAsPercent()',type: 'number', cellFilter: 'number: 0'},
	    	      { name : 'Return (%)', field: 'returnTillDate',type: 'number', cellFilter: 'number: 0'},
	    	      { name : 'Impact (%)',field: 'getImpactOnAverageReturnAsPercent()',type: 'number', cellFilter: 'number: 2'},
	    	      { name : 'Buy Price', field: 'buyPrice',type: 'number',cellFilter: "number:0"},
	    	      { name : 'Period', field: 'duration'},
	    	      { name : 'Ind', field : 'industry', cellTooltip: true},
	    	      { name : 'Closed Price', field: 'sellPrice', width:"75",type: 'number', cellFilter: 'number: 0'},
	    	      { name :'SMV', field : 'simpleMovingAverage',type: 'number', cellFilter: 'number: 0'},
	    	      { name :'Day 1' , field : 'formattedValues[0]',type: 'number', cellFilter: 'number: 2'},
	    	      { name :'Day 2' , field : 'formattedValues[1]',type: 'number', cellFilter: 'number: 2'},
	    	      { name :'Day 3' , field : 'formattedValues[2]',type: 'number', cellFilter: 'number: 2'},
	    	      { name :'Day 4' , field : 'formattedValues[3]',type: 'number', cellFilter: 'number: 2'},
	    	      { name :'Day 5' , field : 'formattedValues[4]',type: 'number', cellFilter: 'number: 2'},
	    	      { name :'Day 6' , field : 'formattedValues[5]',type: 'number', cellFilter: 'number: 2'}
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
	    	      { name : 'Return (%)',field: 'returnTillDate', type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.AVG,
	    	    	  //this ensures that "avg" is not printed in the summary volumn
	    	    	customTreeAggregationFinalizerFn: function( aggregation) 
	    	    	{
    	            	  aggregation.rendered = aggregation.value.toFixed(0);  
	    	        },
		    	      filters: [
	    	                       {
	    	                           condition: uiGridConstants.filter.GREATER_THAN,
	    	                           placeholder: 'greater than'
	    	                         },
	    	                         {
	    	                           condition: uiGridConstants.filter.LESS_THAN,
	    	                           placeholder: 'less than'
	    	                         }
		    	               ],
	    	        //for footer
	    	        aggregationType: uiGridConstants.aggregationTypes.avg, aggregationHideLabel: true, footerCellFilter:'number: 2'
	    	      },
	    	      
	    	      { name: 'Buy Price', field: 'buyPrice', type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.AVG,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	      },
	    	      { name: 'Sell Price',field: 'sellPrice', type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.MAX,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          }
    	          },
    	          { name : 'Period', field: 'duration', treeAggregationType: uiGridGroupingConstants.aggregation.MIN,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value;
	    	          },
		    	      filters: [
	    	                       {
	    	                           condition: uiGridConstants.filter.GREATER_THAN,
	    	                           placeholder: 'greater than'
	    	                         },
	    	                         {
	    	                           condition: uiGridConstants.filter.LESS_THAN,
	    	                           placeholder: 'less than'
	    	                         }
		    	               ]
    	          },
    	          
	    	      { name : 'Diff', field: 'getTotalSellPriceDiffTotalBankSell()',type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      
	    	      { name : 'Qty', field: 'quantity', type: 'number', cellFilter: 'number: 0',treeAggregationType: uiGridGroupingConstants.aggregation.SUM, type:'number',
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          }
	    	      },
	    	      { field: 'buyDate',  treeAggregationType: uiGridGroupingConstants.aggregation.MAX,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value;
	    	          }
	    	      },
	    	      { name: 'T. Inv', field: 'getTotalInvestment()',type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0',
		    	      filters: [
	    	                       {
	    	                           condition: uiGridConstants.filter.GREATER_THAN,
	    	                           placeholder: 'greater than'
	    	                         },
	    	                         {
	    	                           condition: uiGridConstants.filter.LESS_THAN,
	    	                           placeholder: 'less than'
	    	                         }
		    	               ]
	    	      },
	    	      { name: 'T. Sell Price',field: 'getTotalSellPrice()', type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { name: 'T. Bank Sell Price', field: 'getTotalBankSellPrice()', type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { name: 'PnL', field:'getProfitAndLoss()', type: 'number', cellFilter: 'number: 0',treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
	    	    	  customTreeAggregationFinalizerFn: function( aggregation ) 
	    	    	  {
	    	              aggregation.rendered = aggregation.value.toFixed(0);
	    	          },
	    	          aggregationType: uiGridConstants.aggregationTypes.sum, aggregationHideLabel: true, footerCellFilter:'number: 0'
	    	      },
	    	      { name: 'PnL (Bank)', field:'getBankProfit()', type: 'number', cellFilter: 'number: 0', treeAggregationType: uiGridGroupingConstants.aggregation.SUM,
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
	    		$scope.listExceptionStocks(); 
	    		$scope.listBlackListedStocks();
				$scope.listNDaysHistoryStocks();
				$scope.listUnrealizedDetails();
				$scope.getProfitAndLoss();
				
			}, ROOT);
	    	
	    	gapi.client.load('nDaysHistoryControllerEndPoint', 'v1', function() {}, ROOT);
	    	gapi.client.load('unrealizedDetailsControllerEndPoint', 'v1', function() {}, ROOT);
    		
		};

		$scope.listExceptionStocks = function() {
			gapi.client.initalizeControllerEndPoint.listExceptionStocks().execute( 
					function(resp) {
						$scope.exceptionStocks = resp.items;
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
							  			return (this.sellPrice - this.bankSellPrice)*this.quantity;
							  		};
							  		
							  		row.getTotalInvestment= function()
							  		{
							  			return this.buyPrice * this.quantity;
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
						$scope.listExceptionStocks();
						$scope.profitAndLoss();
					}
			);
		};

		$scope.getProfitAndLoss = function() {
			gapi.client.initalizeControllerEndPoint.getProfitAndLoss().execute( 
					function(resp) {
						$scope.profitAndLoss = resp;
						$scope.$apply();
						$scope.loader.initialLoading= false;
						
					}
			);
		};
		
		//default values
		$scope.ndaysHistoryInput= {numOfDays:"6" ,simpleMovingAverage :"50" };
		
		$scope.refreshNdaysHistory=function(ndaysHistoryInput)
		{
			$scope.loader.nDaysHistoryLoading = true;
			var endPoint = gapi.client.nDaysHistoryControllerEndPoint;
			var	request = endPoint.refreshNDaysHistoryStocks({numOfDays:ndaysHistoryInput.numOfDays, simpleMovingAverage:ndaysHistoryInput.simpleMovingAverage});
			request.execute(
					function(resp) {
						$scope.listNDaysHistoryStocks();
						$scope.loader.nDaysHistoryLoading = false;
					}
			);
		};//refreshNdaysHistory
			
			$scope.refreshUnrealizedDetails=function(unrealizedDetails)
			{
				$scope.loader.unrealizedDetailsLoading = true;
				var endPoint = gapi.client.unrealizedDetailsControllerEndPoint;
				var request;
				if ( typeof(unrealizedDetails) === 'undefined') 
				{
					request = endPoint.refreshUnrealizedDetails({unrealizedDetailsContent:" "});
				}
				else
				{
					request = endPoint.refreshUnrealizedDetails({unrealizedDetailsContent:unrealizedDetails.content});	
				}
				
				request.execute(
						function(resp) {
							$scope.$apply();
							$scope.listUnrealizedDetails();
							$scope.loader.unrealizedDetailsLoading = false;
						}
				);
			};//refreshUnrealizedDetails
			
			$scope.refreshAll=function()
			{
				$scope.refreshUnrealizedDetails();
				$scope.refreshNdaysHistory($scope.ndaysHistoryInput);
			};//refreshAll	
			
	});

