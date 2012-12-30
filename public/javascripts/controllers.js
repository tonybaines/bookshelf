function BooksCtrl($scope, $http) {
    refreshBooks = function() {
        $http.get('/books').success(function(data) {
            $scope.books = data
        })
    }

    refreshBooks()

    $scope.addBook = function() {
        $http.put('/books', { title: $scope.bookTitle, id: 0 }).success(function(data) {
            refreshBooks();
        })
        $scope.bookTitle = '';
    };


};