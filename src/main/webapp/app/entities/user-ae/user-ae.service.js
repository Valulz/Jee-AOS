(function() {
    'use strict';
    angular
        .module('notifApp')
        .factory('UserAE', UserAE);

    UserAE.$inject = ['$resource'];

    function UserAE ($resource) {
        var resourceUrl =  'api/user-aes/:id';

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
