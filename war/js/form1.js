var app = angular.module('app', ['ui.grid']);

app.controller('MainCtrl', ['$scope', '$http', 'uiGridConstants', function ($scope, $http, uiGridConstants) {
  $scope.gridOptions1 = {
    enableSorting: true,
    enableFiltering:true,
    columnDefs: [
      { field: 'credit_amount',                
  displayName: 'Credit Amount',           
  type: 'number', 
  enableFocusedCellEdit: true, 
  //This 'filters' is the sort box to do the search.
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
        
      }]
  };

 $scope.gridOptions1.data = [{
   credit_amount:1000.02
 },{
   credit_amount:1001.0
 },{
   credit_amount:100.0
 },{
   credit_amount:500.0
 },{
   credit_amount:-500.0
 },{
   credit_amount:11
 },{
   credit_amount:-10
 }
 ,{
	   credit_amount:7
	 },{
		   credit_amount:-5
	 },{
		   credit_amount:1058
	 }
 ]
}]);
