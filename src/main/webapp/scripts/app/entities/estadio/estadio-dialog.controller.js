'use strict';

angular.module('ligabaloncestoApp').controller('EstadioDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Estadio', 'Equipo', 'Partido',
        function($scope, $stateParams, $modalInstance, entity, Estadio, Equipo, Partido) {

        $scope.estadio = entity;
        $scope.equipos = Equipo.query();
        $scope.partidos = Partido.query();
        $scope.load = function(id) {
            Estadio.get({id : id}, function(result) {
                $scope.estadio = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('ligabaloncestoApp:estadioUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.estadio.id != null) {
                Estadio.update($scope.estadio, onSaveFinished);
            } else {
                Estadio.save($scope.estadio, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
