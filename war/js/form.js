var app = angular.module('app', ['ngAnimate', 'ngTouch', 'ui.grid', 'ui.grid.saveState', 'ui.grid.selection', 'ui.grid.cellNav', 'ui.grid.resizeColumns', 'ui.grid.moveColumns', 'ui.grid.pinning', 'ui.bootstrap', 'ui.grid.autoResize' ]);

app.controller('MainCtrl', ['$scope', '$http', '$interval', function ($scope, $http, $interval) {
  $scope.gridOptions = {
    saveFocus: false,
    saveScroll: true,
    enableGridMenu: true,
    onRegisterApi: function(gridApi){ 
      $scope.gridApi = gridApi;
    }
  };
  $scope.gridOptions.enableFiltering = true;

  $http.get('https://cdn.rawgit.com/angular-ui/ui-grid.info/gh-pages/data/100.json')
    .success(function(data) {
      $scope.gridOptions.data = data;
    });
}]);
