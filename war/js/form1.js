
function init() {
	window.init();
}
var app = angular.module('myApp', []);
app.controller('myCtrl', 
	function($scope, $window) {
	
		$window.init = function() {
			$scope.$apply($scope.load_guestbook_lib);
	    	 
	    };
		
	    $scope.load_guestbook_lib = function() {
	    	gapi.client.load('ndaysHistoryEndPoint', 'v1', function() {
				$scope.is_backend_ready = true;
				$scope.list();
			}, '/_ah/api');
		};
		
				
		$scope.list = function() {
			gapi.client.ndaysHistoryEndPoint.listUnrealizedSummaryDbObject().execute(
					function(resp) {
						$scope.messages = resp.items;
						$scope.$apply();
					}
			);
		};

		$scope.firstName= "John";
		    $scope.lastName= "Doe111";
	}
);
