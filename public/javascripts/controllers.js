function BooksCtrl($scope, $http) {
    refreshBooks = function() {
        $http({method: 'GET', url: '/books', headers: {'Accept': 'application/json'}}).success(function(data) {
            $scope.books = data
        })
    }

    refreshBooks()

    $scope.addBook = function() {
        $http.put('/books', { title: $scope.bookTitle, author: $scope.bookAuthor, id: 0 }).success(function(data) {
            refreshBooks();
        })
        $scope.bookTitle = '';
        $scope.bookAuthor = '';
    };


};