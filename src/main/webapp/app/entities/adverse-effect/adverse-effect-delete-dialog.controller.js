(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('AdverseEffectDeleteController',AdverseEffectDeleteController);

    AdverseEffectDeleteController.$inject = ['$uibModalInstance', 'entity', 'AdverseEffect'];

    function AdverseEffectDeleteController($uibModalInstance, entity, AdverseEffect) {
        var vm = this;

        vm.adverseEffect = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AdverseEffect.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
