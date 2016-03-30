'use strict';

angular.module('ligabaloncestoApp').controller('TemporadaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Temporada', 'Liga', 'Equipo',
        function($scope, $stateParams, $modalInstance, entity, Temporada, Liga, Equipo) {

        $scope.temporada = entity;
        $scope.ligas = Liga.query();
        $scope.equipos = Equipo.query();
        $scope.load = function(id) {
            Temporada.get({id : id}, function(result) {
                $scope.temporada = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('ligabaloncestoApp:temporadaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.temporada.id != null) {
                Temporada.update($scope.temporada, onSaveFinished);
            } else {
                Temporada.save($scope.temporada, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
