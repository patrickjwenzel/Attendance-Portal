package com.example.loginscreen;
import java.io.Serializable;

/**
 * Student user
 */
public class Student extends User implements Serializable{

    /**
     * Creates a new student user
     * @param tmpName Given name
     * @param tmpEmail Given email
     * @param tmpPassword Given password
     * @param tmpClassList Given class list
     * @param latitude Given latitude coordinates
     * @param longitude Given longitude coordinates
     */
    public Student(String tmpName, String tmpEmail, String tmpPassword, String[] tmpClassList, float latitude, float longitude) {
        super(tmpName, tmpEmail, tmpPassword,tmpClassList, latitude, longitude);
    }
}
