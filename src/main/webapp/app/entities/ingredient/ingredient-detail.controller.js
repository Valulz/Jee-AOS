(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('IngredientDetailController', IngredientDetailController);

    IngredientDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ingredient', 'Product'];

    function IngredientDetailController($scope, $rootScope, $stateParams, previousState, entity, Ingredient, Product) {
        var vm = this;

        vm.ingredient = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('notifApp:ingredientUpdate', function(event, result) {
            vm.ingredient = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
