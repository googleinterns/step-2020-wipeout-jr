package com.google.sps.data;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.sps.data.Book;
import com.google.sps.data.Review;
import com.google.sps.data.User;
import java.util.Set;
import java.util.HashSet;

public class ReviewDaoDatastore implements ReviewDao {

    private DatastoreService datastore;

    public ReviewDaoDatastore() {
        datastore = DatastoreServiceFactory.getDatastoreService();
    }

    @Override
    public void loadAll(Book book) {

        User defaultUser = User.create("unknown email", "GoodReads User");
        // loop through all the reviews of a book
        for (String review: book.reviews()) {
            Entity reviewEntity = new Entity("Review");
            reviewEntity.setProperty("Book", book);
            reviewEntity.setProperty("Content", review);
            reviewEntity.setProperty("User", defaultUser);
            datastore.put(reviewEntity);
        }
    }

    @Override
    public void loadNew(Review review) {
        Entity reviewEntity = new Entity("Review");
        reviewEntity.setProperty("Book", review.book());
        reviewEntity.setProperty("Content", review.fullText());
        reviewEntity.setProperty("User", review.user());
        datastore.put(reviewEntity);
    }

    @Override
    public Set<Review> getAll(Book book) {
        Set<Review> reviews = new HashSet<Review>();
        Entity bookEntity = new Entity("Review");

        Filter bookFilter =
            new FilterPredicate("Book", FilterOperator.EQUAL, book);
        Query query = new Query("Review").setFilter(bookFilter);
        PreparedQuery results = datastore.prepare(query);
        if (results == null) {
            return reviews;
        }
        for (Entity entity : results.asIterable()) {
            Review review = entityToReview(entity);
            reviews.add(review);
        }
        return reviews;
    }

    private static Review entityToReview(Entity reviewEntity) {
        return Review.builder().fullText((String) reviewEntity.getProperty("Content")).book(
            (Book) reviewEntity.getProperty("Book")).user(
            (User) reviewEntity.getProperty("User")).build();
  }
}