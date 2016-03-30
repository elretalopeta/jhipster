'use strict';

angular.module('ligabaloncestoApp')
    .controller('TemporadaController', function ($scope, Temporada, ParseLinks) {
        $scope.temporadas = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Temporada.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.temporadas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Temporada.get({id: id}, function(result) {
                $scope.temporada = result;
                $('#deleteTemporadaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Temporada.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteTemporadaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.temporada = {nombre: null, fecha: null, id: null};
        };
    });
