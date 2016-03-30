
angular.module('ligaBaloncestoApp',['ui.router'])
    .config(function ($stateProvider,$urlRouterProvider) {
        $urlRouterProvider.otherwise("/");
        $stateProvider
            .state('ej14', {
                url: '/ej14',
                data: {
                    pageTitle: 'Ej14'
                },
                views: {
                    'content@': {
                        templateUrl: 'ejercicio14-extra.html',
                        controller: 'jugadorCtrl'
                    }
                }
            })
            .state('ej15', {
                url: '/ej15',
                data: {
                    pageTitle: 'Ej15'
                },
                views: {
                    'content@': {
                        templateUrl: 'ejercicio15-extra.html',
                        controller: 'creacionJugadorCtrl'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Equipo', function($stateParams, Equipo) {
                        return Equipo.query();
                    }]
                }
            })
            .state('ej16', {
                url: '/ej16',
                data: {
                    pageTitle: 'Ej16'
                },
                views: {
                    'content@': {
                        templateUrl: 'ejercicio16-extra.html',
                        controller: 'creacionEquipoCtrl'
                    }
                }
            })
            .state('ej17', {
                url: '/ej17',
                data: {
                    pageTitle: 'Ej17'
                },
                views: {
                    'content@': {
                        templateUrl: 'ejercicio17-extra.html',
                        controller: 'modificacionCtrl'
                    }
                },
                resolve: {
                    entity: ['$stateParams', 'Jugador', function($stateParams, Jugador) {
                        return Jugador.query();
                    }]
                }
            })
    });
