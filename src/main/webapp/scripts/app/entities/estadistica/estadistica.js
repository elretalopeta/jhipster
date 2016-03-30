'use strict';

angular.module('ligabaloncestoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('estadistica', {
                parent: 'entity',
                url: '/estadisticas',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.estadistica.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estadistica/estadisticas.html',
                        controller: 'EstadisticaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estadistica');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('estadistica.detail', {
                parent: 'entity',
                url: '/estadistica/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.estadistica.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/estadistica/estadistica-detail.html',
                        controller: 'EstadisticaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('estadistica');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Estadistica', function($stateParams, Estadistica) {
                        return Estadistica.get({id : $stateParams.id});
                    }]
                }
            })
            .state('estadistica.new', {
                parent: 'estadistica',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/estadistica/estadistica-dialog.html',
                        controller: 'EstadisticaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {asistencias: null, canastas: null, faltas: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('estadistica', null, { reload: true });
                    }, function() {
                        $state.go('estadistica');
                    })
                }]
            })
            .state('estadistica.edit', {
                parent: 'estadistica',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/estadistica/estadistica-dialog.html',
                        controller: 'EstadisticaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Estadistica', function(Estadistica) {
                                return Estadistica.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('estadistica', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
