package com.example.loginscreen;
import java.io.Serializable;

/**
 * This is the Admin user which is a person who has complete control of the system, probably a principal or dean.
 */
public class Admin extends User implements Serializable{


    /**
     * Creates an Admin user with the given information.
     * @param tmpName is the name of the user.
     * @param tmpEmail is the email of the user.
     * @param tmpPassword is the password of the user's account.
     */
    public Admin(String tmpName, String tmpEmail, String tmpPassword)
    {
        super(tmpName, tmpEmail, tmpPassword,null, 0, 0);
    }
}
