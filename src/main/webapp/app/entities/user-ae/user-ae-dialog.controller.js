(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('UserAEDialogController', UserAEDialogController);

    UserAEDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'UserAE', 'User', 'UserType'];

    function UserAEDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, UserAE, User, UserType) {
        var vm = this;

        vm.userAE = entity;
        vm.clear = clear;
        vm.save = save;
        vm.users = User.query();
        vm.usertypes = UserType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.userAE.id !== null) {
                UserAE.update(vm.userAE, onSaveSuccess, onSaveError);
            } else {
                UserAE.save(vm.userAE, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('notifApp:userAEUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
