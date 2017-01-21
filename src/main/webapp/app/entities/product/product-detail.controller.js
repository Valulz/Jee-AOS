(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('ProductDetailController', ProductDetailController);

    ProductDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Product', 'Ingredient', 'Notification'];

    function ProductDetailController($scope, $rootScope, $stateParams, previousState, entity, Product, Ingredient, Notification) {
        var vm = this;

        vm.product = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('notifApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
