package com.campuscompanion.cc.server.controllers.userManagement;

import com.campuscompanion.cc.client.utility.Credentials;
import com.campuscompanion.cc.dao.UserAuthenticationDAO;
import com.campuscompanion.cc.dao.UserAuthorizationDAO;
import com.campuscompanion.cc.client.utility.Review;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class UserManagement {

    private final UserAuthenticationDAO userAuthenticationDAO;
    private final UserAuthorizationDAO userAuthorizationDAO;

    public UserManagement() {
        userAuthenticationDAO = new UserAuthenticationDAO();
        userAuthorizationDAO = new UserAuthorizationDAO();
    }

    public boolean authenticateUser(Credentials credentials) {
        return userAuthenticationDAO.checkCredentials(credentials);
    }

    public boolean authorizeUser(String email, String role) {
        return userAuthorizationDAO.checkAuthorization(email, role);
    }

    public void resetPassword(String email, String password) {
        userAuthenticationDAO.resetPW(email, password);
    }

    public String getUserRole(String email) {
        return userAuthorizationDAO.getRole(email);
    }

    public List<Review> getReviews(String email) {
        return userAuthorizationDAO.getUserReviews(email);
    }

    public void registerUser(String email, String fullName, String password, String role) {
        userAuthenticationDAO.registerUser(email, fullName, password, role);
    }

    public boolean isEmailRegistered(String email) {
        return userAuthenticationDAO.isEmailRegistered(email);
    }
}
