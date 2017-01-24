(function() {
    'use strict';

    angular
        .module('notifApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-ae', {
            parent: 'entity',
            url: '/user-ae?page&sort&search',
            data: {
                // authorities: ['ROLE_USER'],
                pageTitle: 'notifApp.userAE.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-ae/user-aes.html',
                    controller: 'UserAEController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userAE');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-ae-detail', {
            parent: 'entity',
            url: '/user-ae/{id}',
            data: {
                // authorities: ['ROLE_USER'],
                pageTitle: 'notifApp.userAE.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-ae/user-ae-detail.html',
                    controller: 'UserAEDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userAE');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserAE', function($stateParams, UserAE) {
                    return UserAE.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-ae',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-ae-detail.edit', {
            parent: 'user-ae-detail',
            url: '/detail/edit',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ae/user-ae-dialog.html',
                    controller: 'UserAEDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserAE', function(UserAE) {
                            return UserAE.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-ae.new', {
            parent: 'user-ae',
            url: '/new',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ae/user-ae-dialog.html',
                    controller: 'UserAEDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                region: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-ae', null, { reload: 'user-ae' });
                }, function() {
                    $state.go('user-ae');
                });
            }]
        })
        .state('user-ae.edit', {
            parent: 'user-ae',
            url: '/{id}/edit',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ae/user-ae-dialog.html',
                    controller: 'UserAEDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserAE', function(UserAE) {
                            return UserAE.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-ae', null, { reload: 'user-ae' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-ae.delete', {
            parent: 'user-ae',
            url: '/{id}/delete',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-ae/user-ae-delete-dialog.html',
                    controller: 'UserAEDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserAE', function(UserAE) {
                            return UserAE.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-ae', null, { reload: 'user-ae' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
