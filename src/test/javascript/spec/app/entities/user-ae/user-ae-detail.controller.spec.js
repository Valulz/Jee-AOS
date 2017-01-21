'use strict';

describe('Controller Tests', function() {

    describe('UserAE Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockUserAE, MockUser, MockUserType;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockUserAE = jasmine.createSpy('MockUserAE');
            MockUser = jasmine.createSpy('MockUser');
            MockUserType = jasmine.createSpy('MockUserType');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'UserAE': MockUserAE,
                'User': MockUser,
                'UserType': MockUserType
            };
            createController = function() {
                $injector.get('$controller')("UserAEDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notifApp:userAEUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
