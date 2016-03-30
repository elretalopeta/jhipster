'use strict';

angular.module('ligabaloncestoApp')
    .factory('Estadio', function ($resource, DateUtils) {
        return $resource('api/estadios/:id', {}, {
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
