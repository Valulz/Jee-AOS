(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('UserTypeDetailController', UserTypeDetailController);

    UserTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserType'];

    function UserTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, UserType) {
        var vm = this;

        vm.userType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('notifApp:userTypeUpdate', function(event, result) {
            vm.userType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
