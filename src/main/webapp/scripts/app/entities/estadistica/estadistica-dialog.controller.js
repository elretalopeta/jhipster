'use strict';

angular.module('ligabaloncestoApp').controller('EstadisticaDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Estadistica', 'Partido', 'Jugador',
        function($scope, $stateParams, $modalInstance, entity, Estadistica, Partido, Jugador) {

        $scope.estadistica = entity;
        $scope.partidos = Partido.query();
        $scope.jugadors = Jugador.query();
        $scope.load = function(id) {
            Estadistica.get({id : id}, function(result) {
                $scope.estadistica = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('ligabaloncestoApp:estadisticaUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.estadistica.id != null) {
                Estadistica.update($scope.estadistica, onSaveFinished);
            } else {
                Estadistica.save($scope.estadistica, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
