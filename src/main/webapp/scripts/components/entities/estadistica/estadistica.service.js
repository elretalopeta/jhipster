'use strict';

angular.module('ligabaloncestoApp')
    .factory('Estadistica', function ($resource, DateUtils) {
        return $resource('api/estadisticas/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
