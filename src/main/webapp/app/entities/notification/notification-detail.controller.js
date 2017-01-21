(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('NotificationDetailController', NotificationDetailController);

    NotificationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Notification', 'UserAE', 'Product', 'AdverseEffect'];

    function NotificationDetailController($scope, $rootScope, $stateParams, previousState, entity, Notification, UserAE, Product, AdverseEffect) {
        var vm = this;

        vm.notification = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('notifApp:notificationUpdate', function(event, result) {
            vm.notification = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
