(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('AdverseEffectDetailController', AdverseEffectDetailController);

    AdverseEffectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AdverseEffect'];

    function AdverseEffectDetailController($scope, $rootScope, $stateParams, previousState, entity, AdverseEffect) {
        var vm = this;

        vm.adverseEffect = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('notifApp:adverseEffectUpdate', function(event, result) {
            vm.adverseEffect = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
