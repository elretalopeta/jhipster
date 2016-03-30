'use strict';

angular.module('ligabaloncestoApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('liga', {
                parent: 'entity',
                url: '/ligas',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.liga.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/liga/ligas.html',
                        controller: 'LigaController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('liga');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('liga.detail', {
                parent: 'entity',
                url: '/liga/{id}',
                data: {
                    roles: ['ROLE_USER'],
                    pageTitle: 'ligabaloncestoApp.liga.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/liga/liga-detail.html',
                        controller: 'LigaDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('liga');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Liga', function($stateParams, Liga) {
                        return Liga.get({id : $stateParams.id});
                    }]
                }
            })
            .state('liga.new', {
                parent: 'liga',
                url: '/new',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/liga/liga-dialog.html',
                        controller: 'LigaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {nombre: null, id: null};
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('liga', null, { reload: true });
                    }, function() {
                        $state.go('liga');
                    })
                }]
            })
            .state('liga.edit', {
                parent: 'liga',
                url: '/{id}/edit',
                data: {
                    roles: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$modal', function($stateParams, $state, $modal) {
                    $modal.open({
                        templateUrl: 'scripts/app/entities/liga/liga-dialog.html',
                        controller: 'LigaDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Liga', function(Liga) {
                                return Liga.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('liga', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
