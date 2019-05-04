package com.example.loginscreen;

import java.io.Serializable;
import java.util.Random;

/**
 * User class, used to create teachers and students
 */
public abstract class User implements Serializable {

    /**
     * User's name
     */
    private String name;

    /**
     * User's email
     */
    private String email;

    /**
     * User's password
     */
    private String password;

    /**
     * User's class list
     */
    private String[] classList;

    /**
     * User's latitude coordinates
     */
    private float latitude;

    /**
     * User's longitude coordinates
     */
    private float longitude;

    /**
     * User's id
     */
    private int userID;

    /**
     * Creates a new user
     * @param tmpName Given name for the user
     * @param tmpEmail Given email for the user
     * @param tmpPassword Given password for the user
     * @param tmpClassList Given class list for the user
     * @param latitude Given latitude coordinates for the user
     * @param longitude Given longitude coordinates for the user
     */
    public User(String tmpName, String tmpEmail, String tmpPassword, String[] tmpClassList, float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        name = tmpName;
        email = tmpEmail;
        password = tmpPassword;
        classList = tmpClassList;
        userID = generateID();
    }

    /**
     * Gets the user's name
     * @return User's name
     */
    public String getName() { return name; }

    /**
     * Gets the user's email
     * @return User's email
     */
    public String getEmail() { return email; }

    /**
     * Gets the user's password
     * @return User's password
     */
    public String getPassword() {return password;}

    /**
     * Gets the user's class list
     * @return User's class list
     */
    public String[] getClassList() { return classList; }

    /**
     * Updates the user's latitude and longitude locations
     * @param latitude Given latitude coordinates
     * @param longitude Given longitude coordinates
     */
    public void updateLatLon(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    /**
     * Gets the user's latitude coordinates
     * @return User's latitude coordinates
     */
    public float getLatitude(){ return latitude; }

    /**
     * Gets the user's longitude coordinates
     * @return User's longitude coordinates
     */
    public float getLongitude(){ return longitude; }

    /**
     * Generates id for user
     * @return Randomly generated id
     */
    public int generateID(){
        Random r = new Random();
        StringBuilder s = new StringBuilder();
        for(int i=0; i<7; i+=1) {
            s.append(r.nextInt(10));
        }

        //TODO Add function that checks if the ID is not being used
        return Integer.parseInt(s.toString());
    }

    public int getID(){return userID;}

}
