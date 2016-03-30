'use strict';

angular.module('ligabaloncestoApp')
    .controller('LigaController', function ($scope, Liga, ParseLinks) {
        $scope.ligas = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Liga.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.ligas = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Liga.get({id: id}, function(result) {
                $scope.liga = result;
                $('#deleteLigaConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Liga.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteLigaConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.liga = {nombre: null, id: null};
        };
    });
