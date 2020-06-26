angular.
  module('bookList').
  component('bookList', {  // This name is what AngularJS uses to match to the `<book-list>` element.
    templateUrl: 'book-list/book-list.template.html',
    controller: function BookListController() {
      // TODO(danya): change this to call the book servlet to get book data (we don't want phones lol)
      this.phones = [
        {
          id: 1,
          name: 'Nexus S',
          snippet: 'Fast just got faster with Nexus S.'
        }, {
          id: 2,
          name: 'Motorola XOOM™ with Wi-Fi',
          snippet: 'The Next, Next Generation tablet.'
        }, {
          id: 3,
          name: 'MOTOROLA XOOM™',
          snippet: 'The Next, Next Generation tablet.'
        }
      ];
    }
  });
  