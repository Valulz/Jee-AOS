(function() {
    'use strict';

    angular
        .module('notifApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('adverse-effect', {
            parent: 'entity',
            url: '/adverse-effect',
            data: {
                // authorities: ['ROLE_USER'],
                pageTitle: 'notifApp.adverseEffect.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adverse-effect/adverse-effects.html',
                    controller: 'AdverseEffectController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adverseEffect');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('adverse-effect-detail', {
            parent: 'entity',
            url: '/adverse-effect/{id}',
            data: {
                // authorities: ['ROLE_USER'],
                pageTitle: 'notifApp.adverseEffect.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/adverse-effect/adverse-effect-detail.html',
                    controller: 'AdverseEffectDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('adverseEffect');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AdverseEffect', function($stateParams, AdverseEffect) {
                    return AdverseEffect.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'adverse-effect',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('adverse-effect-detail.edit', {
            parent: 'adverse-effect-detail',
            url: '/detail/edit',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adverse-effect/adverse-effect-dialog.html',
                    controller: 'AdverseEffectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdverseEffect', function(AdverseEffect) {
                            return AdverseEffect.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adverse-effect.new', {
            parent: 'adverse-effect',
            url: '/new',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adverse-effect/adverse-effect-dialog.html',
                    controller: 'AdverseEffectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('adverse-effect', null, { reload: 'adverse-effect' });
                }, function() {
                    $state.go('adverse-effect');
                });
            }]
        })
        .state('adverse-effect.edit', {
            parent: 'adverse-effect',
            url: '/{id}/edit',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adverse-effect/adverse-effect-dialog.html',
                    controller: 'AdverseEffectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AdverseEffect', function(AdverseEffect) {
                            return AdverseEffect.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('adverse-effect', null, { reload: 'adverse-effect' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('adverse-effect.delete', {
            parent: 'adverse-effect',
            url: '/{id}/delete',
            data: {
                // authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/adverse-effect/adverse-effect-delete-dialog.html',
                    controller: 'AdverseEffectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AdverseEffect', function(AdverseEffect) {
                            return AdverseEffect.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('adverse-effect', null, { reload: 'adverse-effect' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
