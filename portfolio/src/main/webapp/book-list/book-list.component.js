angular.module('bookList').component('bookList', {
  // This name is what AngularJS uses to match to the `<book-list>` element.
  templateUrl: 'book-list/book-list.template.html',
  controller: function BookListController($http) {
    var vm = this;
    $http.get('books').then(function(response) {
      vm.bookList = response.data;
      for(i=0;i<vm.bookList.length;i++){
          vm.bookList[i].authors = formatList(vm.bookList[i].authors);
      }
    })

    function formatList(list) {
      var formattedList = '';
      var i;
      for (i = 0; i < list.length; i++) {
        if (i == list.length - 1) {
          var element = list[i];
        } else {
          var element = list[i] + ', ';
        }
        formattedList += element;
      }
      return formattedList;
    }
  },
  controllerAs: 'bookListCtrl'
});
