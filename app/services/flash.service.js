(function () {
	'use strict';

	angular
		.module('app')
		//create object with fields in the form simple variables and functions
		.factory('FlashService', FlashService);

	FlashService.$inject = ['$rootScope'];
	function FlashService($rootScope) {
		var service = {};

		service.Success = Success;
		service.Error = Error;

		initService();
		return service;

		function initService() {
			//listens on events of given type
			$rootScope.$on('$locationChangeStart', function() {
				clearFlashMessage();
			});

			function clearFlashMessage() {
				//gloabl $rootScope
				var flash = $rootScope.flash;
				if(flash){
					if(!flash.keepAfterLocationChange){
						delete $rootScope.flash;
					} else {
						//only keep for a single location change
						flash.keepAfterLocationChange = false;
					}
				}
			}
		}

		function Success(message, keepAfterLocationChange) {
			$rootScope.flash = {
				message: message,
				type: 'success',
				keepAfterLocationChange; keepAfterLocationChange
			};
		}

		function Error(message, keepAfterLocationChange) {
			$rootScope.flash = {
				message: message,
				type: 'error',
				keepAfterLocationChange: keepAfterLocationChange
			}
		};
	}

})();