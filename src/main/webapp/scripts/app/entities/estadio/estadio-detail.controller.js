'use strict';

angular.module('ligabaloncestoApp')
    .controller('EstadioDetailController', function ($scope, $rootScope, $stateParams, entity, Estadio, Equipo, Partido) {
        $scope.estadio = entity;
        $scope.load = function (id) {
            Estadio.get({id: id}, function(result) {
                $scope.estadio = result;
            });
        };
        $rootScope.$on('ligabaloncestoApp:estadioUpdate', function(event, result) {
            $scope.estadio = result;
        });
    });
