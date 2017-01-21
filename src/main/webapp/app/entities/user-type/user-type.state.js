(function() {
    'use strict';

    angular
        .module('notifApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-type', {
            parent: 'entity',
            url: '/user-type',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'notifApp.userType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-type/user-types.html',
                    controller: 'UserTypeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-type-detail', {
            parent: 'entity',
            url: '/user-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'notifApp.userType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-type/user-type-detail.html',
                    controller: 'UserTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserType', function($stateParams, UserType) {
                    return UserType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-type-detail.edit', {
            parent: 'user-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-type/user-type-dialog.html',
                    controller: 'UserTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserType', function(UserType) {
                            return UserType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-type.new', {
            parent: 'user-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-type/user-type-dialog.html',
                    controller: 'UserTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                weight: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-type', null, { reload: 'user-type' });
                }, function() {
                    $state.go('user-type');
                });
            }]
        })
        .state('user-type.edit', {
            parent: 'user-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-type/user-type-dialog.html',
                    controller: 'UserTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserType', function(UserType) {
                            return UserType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-type', null, { reload: 'user-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-type.delete', {
            parent: 'user-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-type/user-type-delete-dialog.html',
                    controller: 'UserTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserType', function(UserType) {
                            return UserType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-type', null, { reload: 'user-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
