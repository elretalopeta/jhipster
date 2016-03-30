'use strict';

angular.module('ligabaloncestoApp')
    .controller('LigaDetailController', function ($scope, $rootScope, $stateParams, entity, Liga) {
        $scope.liga = entity;
        $scope.load = function (id) {
            Liga.get({id: id}, function(result) {
                $scope.liga = result;
            });
        };
        $rootScope.$on('ligabaloncestoApp:ligaUpdate', function(event, result) {
            $scope.liga = result;
        });
    });
