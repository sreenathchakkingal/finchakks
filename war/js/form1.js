var app = angular.module('app',['ui.grid','ui.grid.exporter',  'ui.grid.resizeColumns', 'ui.grid.grouping']);

app.controller('MainCtrl', function ($scope, $http, uiGridGroupingConstants ) {
  $scope.gridOptions = {
			  enableGridMenu: true,  
			  enableSorting: true,
			  exporterCsvFilename: 'unrealizedDetails.csv',
	    	    enableFiltering: true,
	    	    enableColumnResizing: true,
    columnDefs: [
      { name: 'name', width: '30%' },
      { name: 'age', treeAggregationType: uiGridGroupingConstants.aggregation.MAX, width: '20%'},
      { name: 'company', width: '25%' },
      { name: 'registered', width: '40%', cellFilter: 'date', type: 'date' },
      { name: 'state', grouping: { groupPriority: 0 },  width: '35%'},
      { name: 'balance', width: '25%', treeAggregationType: uiGridGroupingConstants.aggregation.AVG}
    ],
//    onRegisterApi: function( gridApi ) {
//      $scope.gridApi = gridApi;
//    }
  };

  $http.get('https://cdn.rawgit.com/angular-ui/ui-grid.info/gh-pages/data/500_complex.json')
    .success(function(data) {
      for ( var i = 0; i < data.length; i++ ){
        var registeredDate = new Date( data[i].registered );
        data[i].state = data[i].address.state;
        data[i].gender = data[i].gender === 'male' ? 1: 2;
        data[i].balance = Number( data[i].balance.slice(1).replace(/,/,'') );
        data[i].registered = new Date( registeredDate.getFullYear(), registeredDate.getMonth(), 1 )
      }
      $scope.gridOptions.data = data;
    });


});
