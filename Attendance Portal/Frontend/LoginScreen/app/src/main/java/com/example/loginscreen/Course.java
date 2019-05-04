package com.example.loginscreen;

import java.io.Serializable;
import java.util.Random;

/**
 * Class for a Course object
 */
public class Course implements Serializable {

    /**
     * Course's name
     */
    private String courseName;

    /**
     * Course's credit load
     */
    private int numCredit;

    /**
     * List of Students in course
     */
    private Student[] studentList;

    /**
     * Teacher for the course
     */
    private Teacher teacher;

    /**
     * imageID
     */
    private int image;

    /**
     * Description of the course
     */
    private String courseDescription;

    /**
     * Course's ID
     */
    private int courseID;

    /**
     * Tells if course is active or not
     */
    private boolean isActive;

    /**
     * Info about the course
     */
    private String courseInfo;

    /**
     * Info about the lab
     */
    private String labInfo;

    /**
     * Info about the recitation
     */
    private String recitationInfo;

    /**
     * Constructs a new course
     * @param name Name of course
     * @param imageId imageID of course
     * @param credit Number of credits that the course is
     * @param registry List of students in course
     * @param tmpTeacher Teacher of the course
     * @param description Description of the course
     * @param cInfo Info about the class
     * @param lInfo Info about the lab
     * @param rInfo Info about the recitation
     * @param active Whether or not the class is active or not
     */
    public Course(String name, int imageId, int credit, Student[] registry, Teacher tmpTeacher, String description, String cInfo, String lInfo
            ,String rInfo, boolean active)
    {
        courseName = name;
        numCredit = credit;
        studentList = registry;
        teacher = tmpTeacher;
        image = imageId;
        courseDescription = description;
        courseInfo = cInfo;
        labInfo = lInfo;
        recitationInfo = rInfo;
        courseID = generateID();
        isActive = active;

    }

    /**
     * Gets the course name
     * @return Returns the course name
     */
    public String getCourseName() { return courseName; }

    /**
     * Gets the number of credits the course is
     * @return Returns the number of credits the course is
     */
    public int getNumCredit() { return numCredit; }

    /**
     * Gets the students in the class
     * @return Returns the students in the class
     */
    public Student[] getStudentList() { return studentList; }

    /**
     * Gets the teacher for the course
     * @return Returns the teacher for the course
     */
    public Teacher getTeacher() { return teacher; }

    /**
     * Checks if the class is active
     * @return True if the class is active, false otherwise
     */
    public Boolean isActive(){return isActive;}

    /**
     * Gets the course's info
     * @return Returns the course's info
     */
    public String getCourseInfo(){return courseInfo;}

    /**
     * Gets the course's lab info
     * @return Returns the course's lab info
     */
    public String getLabInfo(){return labInfo;}

    /**
     * Gets the course's recitation info
     * @return Returns the course's recitation info
     */
    public String getRecitationInfo(){return recitationInfo;}

    /**
     * Gets the course's image id
     * @return Returns the course's image id
     */
    public int getImage() { return image; }

    /**
     * Gets the course's description
     * @return Returns the course's description
     */
    public String getCourseDescription(){return courseDescription;}

    /**
     * Generates an id for the course
     * @return The course's new id
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
    public void setActive(boolean bool)
    {
        isActive = bool;
    }
}
