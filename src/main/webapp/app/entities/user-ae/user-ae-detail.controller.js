(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('UserAEDetailController', UserAEDetailController);

    UserAEDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'UserAE', 'User', 'UserType'];

    function UserAEDetailController($scope, $rootScope, $stateParams, previousState, entity, UserAE, User, UserType) {
        var vm = this;

        vm.userAE = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('notifApp:userAEUpdate', function(event, result) {
            vm.userAE = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
