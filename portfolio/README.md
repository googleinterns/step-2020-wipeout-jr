# BetterReads


BetterReads is a review-focused one-stop shop for exploring books online.  
  
It currently displays a list of books with their genres, and clicking on each individual book gives more details such as the author(s), publisher, page count, etc, along with the user reviews for the book. 

**Note:** The app requires the user to log in to be able to leave reviews for a book. 


### Technologies Used

* **Frontend**
  * [AngularJS](https://angularjs.org/)
  * [jQuery](https://jquery.com/)
  * [Bootstrap](https://getbootstrap.com/)
* **Backend**
  * APIs
      - [Cloud Natural Language API](https://cloud.google.com/natural-language)
      - [Books API](https://developers.google.com/books)
      - [Cloud App Engine API](https://cloud.google.com/appengine/docs/admin-api)
      - [User Service API](https://cloud.google.com/appengine/docs/standard/java/javadoc/com/google/appengine/api/users/UserService)
  * [Java Servlets](https://docs.oracle.com/javaee/7/api/javax/servlet/http/HttpServlet.html)
  * [AutoValue](https://github.com/google/auto/blob/master/value/userguide/index.md)

### To run a local server:

```
mvn package appengine:run
```

### About Us:
BetterReads is an intern project created by STEP Interns amanhey@, mitraan@, and danyagao@.