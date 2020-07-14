package com.google.sps.data;
 
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

/** 
* Access Datastore to manage Users
*/
public class UserDaoDatastore implements UserDao {
    
    private DatastoreService datastore;
    UserService userService;

    public UserDaoDatastore() {
        datastore = DatastoreServiceFactory.getDatastoreService();
        userService = UserServiceFactory.getUserService();

    }
    
    @Override
    public Entity get(String id) {
        Entity userEntity = new Entity("new");
        try{
            Key userKey = KeyFactory.stringToKey(id);
            userEntity = datastore.get(userKey);
        } catch(EntityNotFoundException ex) {
            ex.printStackTrace();
        } finally {
            return userEntity;
        }
    }

    @Override
    public void upload() {
        UserService userService = UserServiceFactory.getUserService();
        String userEmail = userService.getCurrentUser().getEmail();
        Entity userEntity = new Entity("User", userEmail);
        userEntity.setProperty("email",userEmail);
        datastore.put(userEntity);
    }
}