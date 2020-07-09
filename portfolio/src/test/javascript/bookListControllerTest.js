describe('The book list controller ', function() {
  // load the module containing the book list controller
  beforeEach(module('bookList'));  // if angular.mock.module, angular is not
                                   // defined error -- incorrect file structure?

  var scope;
  // inject $controller and $rootScope services
  beforeEach(inject(function($controller, $rootScope) {
    scope = $rootScope.$new();
    $controller('bookListCtrl', {$scope: scope});
  }));

  it('should have a view model', function() {
    expect(scope.vm).toBeDefined();
  });

  it('gets the correct book list data', function() {
    fetch('/book-data').then(response => response.json()).then((bookInfo) => {
      expectedBookList = bookInfo;
    });
    expect(scope.vm.bookList).toBe(expectedBookList);
  })
});
