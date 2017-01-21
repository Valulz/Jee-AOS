(function() {
    'use strict';
    angular
        .module('notifApp')
        .factory('AdverseEffect', AdverseEffect);

    AdverseEffect.$inject = ['$resource'];

    function AdverseEffect ($resource) {
        var resourceUrl =  'api/adverse-effects/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
