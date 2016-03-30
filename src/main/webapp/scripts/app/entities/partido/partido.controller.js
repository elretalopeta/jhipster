'use strict';

angular.module('ligabaloncestoApp')
    .controller('PartidoController', function ($scope, Partido, ParseLinks) {
        $scope.partidos = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Partido.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.partidos = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Partido.get({id: id}, function(result) {
                $scope.partido = result;
                $('#deletePartidoConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Partido.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deletePartidoConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.partido = {hora_inicio: null, hora_final: null, equipo_local: null, equipo_visitante: null, arbitro: null, resultado: null, id: null};
        };
    });
