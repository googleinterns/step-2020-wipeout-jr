// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

// AngularJS App Configuration
angular.module('betterReadsApp', ['ngRoute', 'bookList', 'bookDetail']);

/**
 * Adds a random greeting to the page.
 */
function addRandomGreeting() {
  const greetings =
      ['Hello world!', '¡Hola Mundo!', '你好，世界！', 'Bonjour le monde!'];

  // Pick a random greeting.
  const greeting = greetings[Math.floor(Math.random() * greetings.length)];

  // Add it to the page.
  const greetingContainer = document.getElementById('greeting-container');
  greetingContainer.innerText = greeting;
}

function getBookInfo() {
  const urlHead = 'https://www.googleapis.com/books/v1/volumes?q=';

  fetch('/book-data').then(response => response.json()).then((bookInfo) => {
    for (i = 0; i < bookInfo.length; i++) {
      var query = bookInfo[i].title.replace(/ /g, '+');
      var url = urlHead + query;
      searchBooks(bookInfo[i], url);
    }
  });
}

const bookFields =
    {
      TITLE: 'title',
      GENRE: 'genre',
      CATEGORIES: 'categories',
      AUTHOR: 'author',
      LANGUAGE: 'language',
      DESCRIPTION: 'description',
      INFO_LINK: 'infoLink',
      PAGE_COUNT: 'pageCount',
      PUBLISH_DATE: 'publishedDate',
      PUBLISHER: 'publisher',
      MATURITY_RATING: 'maturityRating'
    }

function searchBooks(book, url) {
  $.getJSON(url, function(json) {
     var info = json.items[0].volumeInfo;
     var title = book.title;
     var reviews = book.reviews;
     var genre = book.genre;            // List of genres from GoodReads
     var categories = info.categories;  // List of genres from Books API
     var author = info.authors;
     var language = info.language;
     var description = info.description;
     var infoLink = info.infoLink;
     var pageCount = info.pageCount;
     var publishedDate = info.publishedDate;
     var publisher = info.publisher;
     var maturityRating = info.maturityRating;

     var fieldContentArray = [
       title, genre, categories, author, language, description, infoLink,
       pageCount, publishedDate, publisher, maturityRating
     ];
     var fieldsString;
     var index = 0;

     // passing values through as URL parameters
     for (var field in bookFields) {
       if (index == 0) {
         fieldsString = '?' + field + '=' + fieldContentArray[index];
       } else if (index < bookFields.length - 1) {
         fieldsString += '&' + field + '=' + fieldContentArray[index]
       }
       index++;
     }

     fetch('/data-upload' + fieldsString, {method: 'POST'});
   }).fail(function(jqxhr, status, errorMessage) {
    $('#bookresult')
        .html('Status Code: ' + status + '<br>Error Message: ' + errorMessage);
  });
}

function
getUserStatus() {
  fetch('/user-status').then(response => response.json()).then((userStatus) => {
    if (userStatus == 'Logged In') {
      document.getElementById('userStatus').innerText = 'Log Out';
    } else {
      document.getElementById('userStatus').innerText = 'Log In';
    }
  });
}
