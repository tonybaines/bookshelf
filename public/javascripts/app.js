'use strict';

// Declare app level module which depends on filters, and services
angular.module('bookshelf',[]).
    config([
        '$routeProvider',
        '$locationProvider',
        function($routeProvider, $locationProvider) {
            $locationProvider.html5Mode(true);
        }
    ]);