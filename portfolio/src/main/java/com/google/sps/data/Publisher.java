//  Publisher.java
//  Sample Google API
//
//  Created by Mohammadreza on 10/7/18.
//  Copyright © 2019 Mohammadreza Mohades. All rights reserved.


package com.google.sps.data;

public class Publisher {

    private int ID;
    private String  name;

    public Publisher() {

        this.name = "Default name";
        this.ID = 0;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
    public int getID() { return  this.ID; }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

}