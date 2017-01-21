(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('IngredientController', IngredientController);

    IngredientController.$inject = ['$scope', '$state', 'Ingredient'];

    function IngredientController ($scope, $state, Ingredient) {
        var vm = this;

        vm.ingredients = [];

        loadAll();

        function loadAll() {
            Ingredient.query(function(result) {
                vm.ingredients = result;
                vm.searchQuery = null;
            });
        }
    }
})();
