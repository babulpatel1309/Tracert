angular.module('main', [
    'ui.router',
    'ngMessages',
    'ngAnimate',
    'ui.bootstrap',
    'ui.bootstrap.showErrors',
    'ui.bootstrap.upload',
    'ui.bootstrap.contextMenu',
    'ui.bootstrap.shortcut',
    'ui.bootstrap.savvy',
    'ui.bootstrap.materialPicker',
    'timeago',
    'oc.lazyLoad'
]);

var files = function (fileList) {
    return {
        'main': ['$ocLazyLoad', function ($ocLazyLoad) {
            return $ocLazyLoad.load({
                name: "main",
                files: fileList,
                serie: true
            });
        }]
    }
};

angular.module('main')
.config([
    '$stateProvider', '$locationProvider', '$urlRouterProvider', '$ocLazyLoadProvider',
    function ($stateProvider, $locationProvider, $urlRouterProvider, $ocLazyLoadProvider) {
        $urlRouterProvider.otherwise("/");
        $stateProvider
        .state('app', {
            controller: 'RootController',
            template: '<div ui-view/>',
            resolve: files([
                '/app/root/root-service.js',
                '/app/root/root-controller.js'
            ])
        })
        .state('app.index', {
            url: '/',
            templateUrl: 'app/index/index.html',
            controller: 'IndexController',
            resolve: files([
                '/app/index/iconSearch-filter.js',
                '/app/directives/mdiIcon.js',
                '/app/directives/equals.js',
                '/app/directives/ngFocus.js',
                '/app/directives/svgViewbox.js',
                '/app/directives/ngRange.js',
                '/app/shared/icon-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/tag-service.js',
                '/app/contribute/contributor-service.js',
                '/app/index/index-controller.js',
                '/app/index/iconView-controller.js',
                '/app/shared/modal/codeViewer-controller.js'
            ])
        })
        .state('app.icon', {
            url: '/icon/:name',
            templateUrl: 'app/index/index.html',
            controller: 'IndexController',
            resolve: files([
                '/app/index/iconSearch-filter.js',
                '/app/directives/mdiIcon.js',
                '/app/directives/equals.js',
                '/app/directives/ngFocus.js',
                '/app/directives/svgViewbox.js',
                '/app/directives/ngRange.js',
                '/app/shared/icon-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/tag-service.js',
                '/app/contribute/contributor-service.js',
                '/app/index/index-controller.js',
                '/app/index/iconView-controller.js',
                '/app/shared/modal/codeViewer-controller.js'
            ])
        })
        .state('app.contributor', {
            url: '/contributor/:name',
            templateUrl: 'app/index/index.html',
            controller: 'IndexController',
            resolve: files([
                '/app/index/iconSearch-filter.js',
                '/app/directives/mdiIcon.js',
                '/app/directives/equals.js',
                '/app/directives/ngFocus.js',
                '/app/directives/svgViewbox.js',
                '/app/directives/ngRange.js',
                '/app/shared/icon-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/tag-service.js',
                '/app/contribute/contributor-service.js',
                '/app/index/index-controller.js',
                '/app/index/iconView-controller.js',
                '/app/shared/modal/codeViewer-controller.js'
            ])
        })
        .state('app.tag', {
            url: '/tag/:tag',
            templateUrl: 'app/index/index.html',
            controller: 'IndexController',
            resolve: files([
                '/app/index/iconSearch-filter.js',
                '/app/directives/mdiIcon.js',
                '/app/directives/equals.js',
                '/app/directives/ngFocus.js',
                '/app/directives/svgViewbox.js',
                '/app/directives/ngRange.js',
                '/app/shared/icon-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/tag-service.js',
                '/app/contribute/contributor-service.js',
                '/app/index/index-controller.js',
                '/app/index/iconView-controller.js',
                '/app/shared/modal/codeViewer-controller.js'
            ])
        })
        .state('app.search', {
            url: '/search/:search',
            templateUrl: 'app/index/index.html',
            controller: 'IndexController'
        })
        .state('app.contribute', {
            url: '/contribute',
            templateUrl: 'app/contribute/contribute.html',
            controller: 'ContributeController',
            resolve: files([
                '/app/contribute/contribute-controller.js'
            ])
        })
        .state('app.confirm', {
            url: '/confirm/:code',
            templateUrl: 'app/confirm/confirm.html',
            controller: 'ConfirmController',
            resolve: files([
                '/app/contribute/contribute-controller.js'
            ])
        })
        .state('app.community', {
            url: '/community',
            templateUrl: 'app/community/community.html',
            controller: 'CommunityController',
            resolve: files([
                '/app/community/community-controller.js'
            ])
        })
        .state('app.custom', {
            url: '/custom',
            templateUrl: 'app/custom/custom.html',
            controller: 'CustomController',
            resolve: files([
                '/app/custom/custom-controller.js'
            ])
        })
        .state('app.gettingStarted', {
            url: '/getting-started',
            templateUrl: 'app/getting-started/getting-started.html',
            controller: 'GettingStartedController',
            resolve: files([
                '/app/shared/github-service.js',
                '/app/getting-started/gettingStarted-controller.js'
            ])
        })
        .state('app.bootstrap', {
            url: '/bootstrap',
            templateUrl: 'app/bootstrap/bootstrap.html',
            controller: 'BootstrapController',
            resolve: files([
                '/app/bootstrap/bootstrap-controller.js'
            ])
        })
        .state('app.style', {
            url: '/style',
            templateUrl: 'app/style/style.html',
            controller: 'StyleController',
            resolve: files([
                '/app/style/style-controller.js'
            ])
        })
        .state('app.history', {
            url: '/history',
            templateUrl: 'app/history/history.html',
            controller: 'HistoryController',
            resolve: files([
                '/app/shared/icon-service.js',
                '/app/root/root-service.js',
                '/app/history/history-service.js',
                '/app/history/history-controller.js',
                '/app/history/modal/compareModal-controller.js'
            ])
        })
        .state('app.cheatsheet', {
            url: '/cheatsheet',
            templateUrl: 'app/cheatsheet/cheatsheet.html',
            controller: 'CheatsheetController',
            resolve: files([
                '/app/shared/util-service.js',
                '/app/shared/icon-service.js',
                '/app/shared/tag-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/user-service.js',
                '/app/cheatsheet/cheatsheet-controller.js'
            ])
        })
        .state('app.redeem', {
            url: '/redeem',
            templateUrl: 'app/redeem/redeem.html',
            controller: 'RedeemController',
            resolve: files([
                '/app/redeem/redeem-service.js',
                '/app/redeem/redeem-controller.js'
            ])
        })
        .state('app.coc', {
            url: '/code-of-conduct',
            templateUrl: 'app/codeofconduct/codeOfConduct.html',
            controller: 'CodeOfConductController',
            resolve: files([
                '/app/codeofconduct/codeOfConduct-service.js',
                '/app/codeofconduct/codeOfConduct-controller.js'
            ])
        })
        .state('app.design', {
            url: '/design',
            templateUrl: 'app/design/design.html',
            controller: 'DesignController',
            resolve: files([
                '/app/design/design-controller.js'
            ])
        })
        .state('app.login', {
            url: '/login',
            templateUrl: 'app/login/login.html',
            controller: 'LoginController',
            resolve: files([
                '/app/shared/auth-service.js',
                '/app/login/login-controller.js'
            ])
        })
        .state('app.admin', {
            url: '/admin',
            templateUrl: 'app/admin/admin.html',
            controller: 'AdminController',
            resolve: files([
                '/app/root/root-service.js',
                '/app/shared/icon-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/tag-service.js',
                '/app/shared/util-service.js',
                '/app/shared/user-service.js',
                '/app/admin/adminProfile-controller.js',
                '/app/admin/admin-controller.js'
            ])
        })
        .state('app.adminFont', {
            url: '/admin/font',
            templateUrl: 'app/admin/admin-font.html',
            controller: 'AdminFontController',
            resolve: files([
                '/app/root/root-service.js',
                '/app/shared/icon-service.js',
                '/app/shared/auth-service.js',
                '/app/shared/tag-service.js',
                '/app/shared/util-service.js',
                '/app/shared/user-service.js',
                '/app/admin/adminFont-controller.js'
            ])
        });
        $locationProvider.html5Mode(true);
    }
]);