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
var betterReadsApp = angular.module('betterReadsApp', ['ngRoute', 'bookList']);
 
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
 
    const urlHead = "https://www.googleapis.com/books/v1/volumes?q=";
 
    fetch('/book-data').then(response => response.json()).then((bookInfo) => {
        for (i = 0; i < bookInfo.length; i++) {
            var query = bookInfo[i].title.replace(/ /g, "+");
            var url = urlHead + query;
            searchBooks(bookInfo[i], url);
        }
    });
}
 
function searchBooks(book, url)
{

   $.getJSON(url, function (json)
   {
        var info = json.items[0].volumeInfo;
        var title = book.title;
        var reviews = book.reviews;
        var genre = book.genre; // List of genres from GoodReads
        var categories = info.categories; // List of genres from Books API
        var author = info.authors;
        var language = info.language;
        var description = info.description; 
        var infoLink = info.infoLink;
        var pageCount = info.pageCount;
        var publishedDate = info.publishedDate;
        var publisher = info.publisher;
        var maturityRating = info.maturityRating;
 
   })
   .fail(function (jqxhr, status, errorMessage)
   {
       $("#bookresult").html("Status Code: " + status+"<br>Error Message: "+errorMessage);
   }); 
}
