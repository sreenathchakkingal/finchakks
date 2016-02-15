function GuestbookCtrl($scope) {
	$scope.list = function() {
		gapi.client.unrealizedsummarydbobjectendpoint.listUnrealizedSummaryDbObject
				.execute(function(resp) {
					$scope.messages = resp.items;
					$scope.$apply();
				});
	}

	$window.init = function() {
		$scope.$apply($scope.load_guestbook_lib);
	};

	$scope.load_guestbook_lib = function() {
		gapi.client.load('unrealizedsummarydbobjectendpoint', 'v1', function() {
			$scope.is_backend_ready = true;
			$scope.list();
		}, '/_ah/api');
	};
}