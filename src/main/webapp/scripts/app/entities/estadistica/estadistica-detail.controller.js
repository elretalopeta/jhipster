'use strict';

angular.module('ligabaloncestoApp')
    .controller('EstadisticaDetailController', function ($scope, $rootScope, $stateParams, entity, Estadistica, Partido, Jugador) {
        $scope.estadistica = entity;
        $scope.load = function (id) {
            Estadistica.get({id: id}, function(result) {
                $scope.estadistica = result;
            });
        };
        $rootScope.$on('ligabaloncestoApp:estadisticaUpdate', function(event, result) {
            $scope.estadistica = result;
        });
    });
