package com.campuscompanion.cc.server;

import com.campuscompanion.cc.server.controllers.bathroomManagement.BathroomManagement;
import com.campuscompanion.cc.server.controllers.eventManagement.EventManagement;
import com.campuscompanion.cc.server.controllers.userManagement.UserManagement;

import java.net.URL;
import java.sql.*;

public class RequestManager {

    private final UserManagement userManagement;
    private final BathroomManagement bathroomManagement;
    private final EventManagement eventManagement;

    public RequestManager() {
        this.userManagement = new UserManagement();
        this.bathroomManagement = new BathroomManagement();
        this.eventManagement = new EventManagement();
    }

    public UserManagement getUserManagement(){
        return userManagement;
    }

    public BathroomManagement getBathroomManagement(){
        return bathroomManagement;
    }

    public EventManagement getEventManagement(){
        return eventManagement;
    }
}
