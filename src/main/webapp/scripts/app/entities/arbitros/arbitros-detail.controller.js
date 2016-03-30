'use strict';

angular.module('ligabaloncestoApp')
    .controller('ArbitrosDetailController', function ($scope, $rootScope, $stateParams, entity, Arbitros, Partido, Equipo) {
        $scope.arbitros = entity;
        $scope.load = function (id) {
            Arbitros.get({id: id}, function(result) {
                $scope.arbitros = result;
            });
        };
        $rootScope.$on('ligabaloncestoApp:arbitrosUpdate', function(event, result) {
            $scope.arbitros = result;
        });
    });
