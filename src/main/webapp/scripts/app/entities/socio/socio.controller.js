'use strict';

angular.module('ligabaloncestoApp')
    .controller('SocioController', function ($scope, Socio, ParseLinks) {
        $scope.socios = [];
        $scope.page = 1;
        $scope.loadAll = function() {
            Socio.query({page: $scope.page, per_page: 20}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.socios = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();

        $scope.delete = function (id) {
            Socio.get({id: id}, function(result) {
                $scope.socio = result;
                $('#deleteSocioConfirmation').modal('show');
            });
        };

        $scope.confirmDelete = function (id) {
            Socio.delete({id: id},
                function () {
                    $scope.loadAll();
                    $('#deleteSocioConfirmation').modal('hide');
                    $scope.clear();
                });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.socio = {nombre: null, id: null};
        };
    });
