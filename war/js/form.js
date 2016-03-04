var app = angular.module('myApp', ['ui.grid', 'ui.bootstrap']);
app.controller('MyCtrl', function($scope) {
    $scope.myData = [
             {name: "Moroni", age: 50},
  					 {name: "Tiancum", age: 43},
						 {name: "Jacob", age: 27},
						 {name: "Nephi", age: 29},
						 {name: "Enos", age: 34 }];
						 
		angular.forEach($scope.myData,function(row){
		  row.getNameAndAge = function(){
		    return this.name + '-' + this.age;
		  }
		});

		 $scope.gridOptions = { 
			        data: 'myData',
			        columnDefs: [{field: 'name', displayName: 'Name'},
			                     {field:'age', displayName:'Age'},
			                     {field: 'getNameAndAge()', displayName: 'Name and age'},
			                     ]
			        };
});