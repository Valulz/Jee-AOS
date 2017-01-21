(function() {
    'use strict';

    angular
        .module('notifApp')
        .controller('UserTypeController', UserTypeController);

    UserTypeController.$inject = ['$scope', '$state', 'UserType'];

    function UserTypeController ($scope, $state, UserType) {
        var vm = this;

        vm.userTypes = [];

        loadAll();

        function loadAll() {
            UserType.query(function(result) {
                vm.userTypes = result;
                vm.searchQuery = null;
            });
        }
    }
})();
