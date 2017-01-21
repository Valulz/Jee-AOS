(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('UserAEDeleteController',UserAEDeleteController);

    UserAEDeleteController.$inject = ['$uibModalInstance', 'entity', 'UserAE'];

    function UserAEDeleteController($uibModalInstance, entity, UserAE) {
        var vm = this;

        vm.userAE = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            UserAE.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
