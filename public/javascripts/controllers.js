function BooksCtrl($scope, $http) {
    $http.get('/books').success(function(data) {
        $scope.books = data
    })
}