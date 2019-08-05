package resources;

import dao.UserDAO;
import data.User;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * User resource exposed at "/user" path
 */
@Path("user")
public class UserResource {

    /**
     * Method handling HTTP GET requests
     * @return List of users as JSON response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getUsers(){
        UserDAO userDAO = new UserDAO();

        new Thread(){
            public void run(){
                Date date = new Date();
                int number = 0;
                boolean run = true;

                while (run) {
                    number++;
                    if (number == Integer.MAX_VALUE) {
                        Date newDate = new Date();
                        if(newDate.getTime()-date.getTime() > 25000) run = false;
                        number = 0;
                    }
                }
            }
        }.start();

        return userDAO.getUsers();
    }

    /**
     * Method handling HTTP POST requests
     * @param user userInformation as String
     * @return the new User if not registered, or the User that matches the username as JSON object
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public User newUser(User user){
        UserDAO userDAO = new UserDAO();
        return userDAO.addUser(user);
    }

}
