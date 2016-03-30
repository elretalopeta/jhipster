'use strict';

angular.module('ligabaloncestoApp')
    .controller('EstadioController', function ($scope, Estadio, ParseLinks) {
        $scope.estadios = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Estadio.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.estadios = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Estadio.get({id: id}, function(result) {
                $scope.estadio = result;
                $('#deleteEstadioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Estadio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteEstadioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.estadio = {nombre: null, id: null};
        };
    });
