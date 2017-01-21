(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('AdverseEffectController', AdverseEffectController);

    AdverseEffectController.$inject = ['$scope', '$state', 'AdverseEffect'];

    function AdverseEffectController ($scope, $state, AdverseEffect) {
        var vm = this;

        vm.adverseEffects = [];

        loadAll();

        function loadAll() {
            AdverseEffect.query(function(result) {
                vm.adverseEffects = result;
                vm.searchQuery = null;
            });
        }
    }
})();
