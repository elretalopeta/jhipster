'use strict';

angular.module('ligabaloncestoApp')
    .controller('TemporadaDetailController', function ($scope, $rootScope, $stateParams, entity, Temporada, Liga, Equipo) {
        $scope.temporada = entity;
        $scope.load = function (id) {
            Temporada.get({id: id}, function(result) {
                $scope.temporada = result;
            });
        };
        $rootScope.$on('ligabaloncestoApp:temporadaUpdate', function(event, result) {
            $scope.temporada = result;
        });
    });
