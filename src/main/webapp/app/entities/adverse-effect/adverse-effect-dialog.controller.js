(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('AdverseEffectDialogController', AdverseEffectDialogController);

    AdverseEffectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AdverseEffect'];

    function AdverseEffectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AdverseEffect) {
        var vm = this;

        vm.adverseEffect = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.adverseEffect.id !== null) {
                AdverseEffect.update(vm.adverseEffect, onSaveSuccess, onSaveError);
            } else {
                AdverseEffect.save(vm.adverseEffect, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('notifApp:adverseEffectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
