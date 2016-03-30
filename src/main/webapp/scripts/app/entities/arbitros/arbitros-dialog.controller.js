'use strict';

angular.module('ligabaloncestoApp').controller('ArbitrosDialogController',
    ['$scope', '$stateParams', '$modalInstance', 'entity', 'Arbitros', 'Partido', 'Equipo',
        function($scope, $stateParams, $modalInstance, entity, Arbitros, Partido, Equipo) {

        $scope.arbitros = entity;
        $scope.partidos = Partido.query();
        $scope.equipos = Equipo.query();
        $scope.load = function(id) {
            Arbitros.get({id : id}, function(result) {
                $scope.arbitros = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('ligabaloncestoApp:arbitrosUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.arbitros.id != null) {
                Arbitros.update($scope.arbitros, onSaveFinished);
            } else {
                Arbitros.save($scope.arbitros, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
