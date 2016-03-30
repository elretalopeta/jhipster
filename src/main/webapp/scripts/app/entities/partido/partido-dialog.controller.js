'use strict';

angular.module('ligabaloncestoApp').controller('PartidoDialogController',
    ['$scope', '$stateParams', '$modalInstance', '$q', 'entity', 'Partido', 'Estadio', 'Temporada', 'Estadistica',
        function($scope, $stateParams, $modalInstance, $q, entity, Partido, Estadio, Temporada, Estadistica) {

        $scope.partido = entity;
        $scope.estadios = Estadio.query({filter: 'partido-is-null'});
        $q.all([$scope.partido.$promise, $scope.estadios.$promise]).then(function() {
            if (!$scope.partido.estadio.id) {
                return $q.reject();
            }
            return Estadio.get({id : $scope.partido.estadio.id}).$promise;
        }).then(function(estadio) {
            $scope.estadios.push(estadio);
        });
        $scope.temporadas = Temporada.query();
        $scope.estadisticas = Estadistica.query();
        $scope.load = function(id) {
            Partido.get({id : id}, function(result) {
                $scope.partido = result;
            });
        };

        var onSaveFinished = function (result) {
            $scope.$emit('ligabaloncestoApp:partidoUpdate', result);
            $modalInstance.close(result);
        };

        $scope.save = function () {
            if ($scope.partido.id != null) {
                Partido.update($scope.partido, onSaveFinished);
            } else {
                Partido.save($scope.partido, onSaveFinished);
            }
        };

        $scope.clear = function() {
            $modalInstance.dismiss('cancel');
        };
}]);
