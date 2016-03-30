'use strict';

angular.module('ligabaloncestoApp')
    .controller('SocioDetailController', function ($scope, $rootScope, $stateParams, entity, Socio, Equipo) {
        $scope.socio = entity;
        $scope.load = function (id) {
            Socio.get({id: id}, function(result) {
                $scope.socio = result;
            });
        };
        $rootScope.$on('ligabaloncestoApp:socioUpdate', function(event, result) {
            $scope.socio = result;
        });
    });
