package com.example.loginscreen;

import android.content.Intent;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

/**
 * Unit tests for the {@link ClassInfoActivity} that mocks {@link Intent}.
 */
@RunWith(MockitoJUnitRunner.class)
public class ClassInfoActivityTest {
    private static final String TEST_NAME = "Test name";
    private static final String TEST_EMAIL = "test@email.com";

        String [] descriptions = {"Embedded C programming. Interrupt handling. Memory mapped I/O in the context of an application. Elementary embedded design flow/methodology. Timers, scheduling, resource allocation, optimization, state machine based controllers, real time constraints within the context of an application. Applications laboratory exercises with embedded devices.",
            "A practical introduction to methods for managing software development. Process models, requirements analysis, structured and object-oriented design, coding, testing, maintenance, cost and schedule estimation, metrics. Programming projects.",
            "Solution methods for ordinary differential equations. First order equations, linear equations, constant coefficient equations. Eigenvalue methods for systems of first order linear equations. Introduction to stability and phase plane analysis.",
            "Introduction to the theory, methods, techniques, and problems of translation. Consideration of material from business, literature, and the social sciences. Taught in Spanish. \n" +
                    "Meets International Perspectives Requirement."};

    String[] cCourseInfo = {"MWF ! 5-6pm ! BuildingF ! 712", "MWF ! 4-5pm ! BuildingG ! 713", "MWF ! 3-4pm ! BuildingB ! 714"};
    String[] lCourseInfo = {"TWR ! 12-1pm ! BuildingL ! 802", "", "T ! 11-12pm ! BuildingA ! 804"};
    String[] rCourseInfo = {"F ! 10-11am ! BuildingA ! 894", "T ! 9-10am ! BuildingP ! 902", ""};
    String[] courseActive = {"true", "false", "false"};

    ClassInfoActivity cia;
    @Mock
    JSONObject jsonMock;
    @Mock
    Intent mockIntent;
    @Before
    public void initMocks() throws JSONException {
        String[] sampleCourseNames = {"COMS", "SPANISH", "CPRE"};
        String[] sampleAttendance = {"COMS"};
        Course[] sampleCourses = new Course[sampleCourseNames.length];
        for(int i =0; i<sampleCourses.length;i++)
        {
            sampleCourses[i] = new Course(sampleCourseNames[i], 0, 0, null, null, descriptions[i],cCourseInfo[i]
                    , lCourseInfo[i], rCourseInfo[i],Boolean.parseBoolean(courseActive[i]));
        }
        Course sample = sampleCourses[0];
        Student sampleStudent = new Student("studname", "email@iastate.edu", "ABCabc123!", sampleCourseNames, 0,0);
        Mockito.when(mockIntent.getSerializableExtra("user")).thenReturn(sampleStudent);
        Mockito.when(mockIntent.getSerializableExtra("attendance")).thenReturn(sampleAttendance);
        Mockito.when(mockIntent.getSerializableExtra("courseList")).thenReturn(sampleCourses);

        cia = new ClassInfoActivity();
        cia.initClassInfoActivity(mockIntent);
    }
    @Test
    public void correctUserInfo()
    {
        assertEquals("studname", cia.user.getName());
        assertEquals("email@iastate.edu", cia.user.getEmail());
        assertEquals("ABCabc123!", cia.user.getPassword());
        String[] names = cia.user.getClassList();
        String[] sampleCourseNames = {"COMS", "SPANISH", "CPRE"};
        for(int i=0; i<names.length; i++)
        {
            assertEquals(names[i], sampleCourseNames[i]);
        }
    }
    @Test
    public void correctCourses()
    {
        Course course = cia.course;
        assertEquals(course.getCourseDescription(), descriptions[0]);
        assertEquals(course.getCourseInfo(), cCourseInfo[0]);
        assertEquals(course.getLabInfo(), lCourseInfo[0]);
        assertEquals(course.getRecitationInfo(), rCourseInfo[0]);
    }
    @Test
    public void findNumSingleDigit()
    {
        int[] expected = {1,2};
        String input = "1/2";
        int[] actual = AttendanceViewer.findNum(input);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }
    @Test
    public void findNumDoubleDigit()
    {
        int[] expected = {35,75};
        String input = "35/75";
        int[] actual = AttendanceViewer.findNum(input);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }
    @Test
    public void findNumTripleDigit()
    {
        int[] expected = {125,974};
        String input = "125/974";
        int[] actual = AttendanceViewer.findNum(input);
        assertEquals(expected[0], actual[0]);
        assertEquals(expected[1], actual[1]);
    }
}