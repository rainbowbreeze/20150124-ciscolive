angular.module("expo",[])
.controller("HeatmapController",function($scope, $interval, UserLocationService){
	$scope.heatmap = {};
	$scope.heatmap.ratio = {};
	$scope.heatmap.poi = {
 			max: 50,
 			min: 0, 
 			data: []
 	};

 	$scope.pois = [];
 	$scope.timeoutVal = 6;
 	$scope.intervalPromise = null;

 	var timeoutCall = function(fx){
 		return $interval(function(){
 				console.log("interval elapsed");
 				fx();
 		},$scope.timeoutVal*1000);
 	};
 	
 	$scope.getUserCoordinates = function(){
 		UserLocationService.getCoordinates($scope.heatmap.ratio.rx, $scope.heatmap.ratio.ry)
 		.then(function(data){
			$scope.heatmap.poi.data = data.data;
			},function(error){
				console.log(error);
			});
 	};
 	
 	$scope.realTimeUpdates = function(){
 		console.log($scope.intervalPromise);

 		if($scope.intervalPromise){
 			$interval.cancel($scope.intervalPromise);
 			$scope.intervalPromise = null;
 		}else{
 			$scope.intervalPromise = timeoutCall($scope.getUserCoordinates);
 		}	
 	};
 	/*$scope.togglePOIS = function(){
 		angular.forEach($scope.pois)
 	};*/

})
.service("UserLocationService",function($http){
	var basckendServiceHost = "http://10.10.30.215/explore/server/public/";

	this.getCoordinates = function(rx,ry){
		rx = rx ? rx : 1;
		ry = ry ? ry : 1;
		return $http({method: 'GET', url: basckendServiceHost+'api/users/coordinates?rx='+rx+'&ry='+ry});
	}
	this.getHeatmapConfig = function(){
		return $http({method: 'GET', url: basckendServiceHost+'api/floors'});
	}

})
.directive("heatmap",function(){
	return{
		restrict : "A",
		scope: '=',
		controller: function($scope, UserLocationService){
				var ratio = {
					rx : 1,
					ry : 1
				};
				this.configHeatmap = function(htmlElem){
					
					UserLocationService.getHeatmapConfig().then(function(data){
						$(htmlElem[0]).css("background-image","url("+data.data[0].image+")");
						$scope.heatmap.ratio.rx = htmlElem[0].clientWidth / data.data[0].width;
						$scope.heatmap.ratio.ry = htmlElem[0].clientHeight / data.data[0].length;
						$scope.pois = data.data[0].pois;
 					},function(error){
 						console.log(error);
 					});

				};
		},
		link: function(scope, element, attrs, controller){
				
				var heatmapInstance = h337.create({
						"element":document.getElementById("heatmapArea"), 
						"radius":7, 
						"visible":true
				}); 
				
				controller.configHeatmap(element);
				
				scope.$watch('heatmap.poi',function(newValue,oldValue){
					if(newValue != oldValue){
						heatmapInstance.store.setDataSet(angular.fromJson(newValue));
					}	
				},true);
		}
	}
})
.directive("expoPoi",function(){
	return{
		templateUrl : "poi-template.html"
	}
});
