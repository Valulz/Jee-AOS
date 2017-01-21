'use strict';

describe('Controller Tests', function() {

    describe('Notification Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockNotification, MockUserAE, MockProduct, MockAdverseEffect;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockNotification = jasmine.createSpy('MockNotification');
            MockUserAE = jasmine.createSpy('MockUserAE');
            MockProduct = jasmine.createSpy('MockProduct');
            MockAdverseEffect = jasmine.createSpy('MockAdverseEffect');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Notification': MockNotification,
                'UserAE': MockUserAE,
                'Product': MockProduct,
                'AdverseEffect': MockAdverseEffect
            };
            createController = function() {
                $injector.get('$controller')("NotificationDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'notifApp:notificationUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
