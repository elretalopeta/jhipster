'use strict';

angular.module('ligabaloncestoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('arbitros', {
                parent: 'entity',
                url: '/arbitross',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.arbitros.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/arbitros/arbitross.html',
                        controller: 'ArbitrosController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('arbitros');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('arbitros.detail', {
                parent: 'entity',
                url: '/arbitros/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.arbitros.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/arbitros/arbitros-detail.html',
                        controller: 'ArbitrosDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('arbitros');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Arbitros', function($stateParams, Arbitros) {
                        return Arbitros.get({id : $stateParams.id});
                    }]
                }
            })
            .state('arbitros.new', {
                parent: 'arbitros',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/arbitros/arbitros-dialog.html',
                        controller: 'ArbitrosDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nombre: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('arbitros', null, { reload: true });
                    }, function() {
                        $state.go('arbitros');
                    })
                }]
            })
            .state('arbitros.edit', {
                parent: 'arbitros',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/arbitros/arbitros-dialog.html',
                        controller: 'ArbitrosDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Arbitros', function(Arbitros) {
                                return Arbitros.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('arbitros', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
