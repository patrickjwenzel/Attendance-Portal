package com.example.loginscreen;

import java.io.Serializable;

/**
 * Class for the teacher user
 */
public class Teacher extends User implements Serializable {

    /**
     * Creates a new teacher user
     * @param tmpName Given name
     * @param tmpEmail Given email
     * @param tmpPassword Given password
     * @param tmpClassList Given class list
     * @param latitude Given latitude coordinates
     * @param longitude Given longitude coordinates
     */
    public Teacher(String tmpName, String tmpEmail, String tmpPassword, String[] tmpClassList, float latitude, float longitude) {
        super(tmpName, tmpEmail, tmpPassword,tmpClassList, latitude, longitude);
    }
}
