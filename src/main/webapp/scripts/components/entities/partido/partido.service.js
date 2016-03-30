'use strict';

angular.module('ligabaloncestoApp')
    .factory('Partido', function ($resource, DateUtils) {
        return $resource('api/partidos/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.hora_inicio = DateUtils.convertDateTimeFromServer(data.hora_inicio);
                    data.hora_final = DateUtils.convertDateTimeFromServer(data.hora_final);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
