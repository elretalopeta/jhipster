'use strict';

angular.module('ligabaloncestoApp')
    .controller('EstadisticaController', function ($scope, Estadistica, ParseLinks) {
        $scope.estadisticas = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Estadistica.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.estadisticas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Estadistica.get({id: id}, function(result) {
                $scope.estadistica = result;
                $('#deleteEstadisticaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Estadistica.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEstadisticaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.estadistica = {asistencias: null, canastas: null, faltas: null, id: null};
        };
    });
