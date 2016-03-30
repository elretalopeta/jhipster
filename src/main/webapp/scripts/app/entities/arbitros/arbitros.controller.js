'use strict';

angular.module('ligabaloncestoApp')
    .controller('ArbitrosController', function ($scope, Arbitros, ParseLinks) {
        $scope.arbitross = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Arbitros.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.arbitross = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Arbitros.get({id: id}, function(result) {
                $scope.arbitros = result;
                $('#deleteArbitrosConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Arbitros.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteArbitrosConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.arbitros = {nombre: null, id: null};
        };
    });
