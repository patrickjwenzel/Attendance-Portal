package com.example.loginscreen;

import static junit.framework.TestCase.assertEquals;

import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import android.content.Intent;

/**
 * Unit tests for the {@link TeacherViewActivity} that mocks {@link Intent}.
 */
@RunWith(MockitoJUnitRunner.class)
public class TeacherViewActivityTest {

    String[] descriptions = {"Embedded C programming. Interrupt handling. Memory mapped I/O in the context of an application. Elementary embedded design flow/methodology. Timers, scheduling, resource allocation, optimization, state machine based controllers, real time constraints within the context of an application. Applications laboratory exercises with embedded devices.", "A practical introduction to methods for managing software development. Process models, requirements analysis, structured and object-oriented design, coding, testing, maintenance, cost and schedule estimation, metrics. Programming projects.", "Solution methods for ordinary differential equations. First order equations, linear equations, constant coefficient equations. Eigenvalue methods for systems of first order linear equations. Introduction to stability and phase plane analysis.", "Introduction to the theory, methods, techniques, and problems of translation. Consideration of material from business, literature, and the social sciences. Taught in Spanish. \n" + "Meets International Perspectives Requirement."};
    String[] cCourseInfo = {"MWF ! 5-6pm ! BuildingF ! 712", "MWF ! 4-5pm ! BuildingG ! 713", "MWF ! 3-4pm ! BuildingB ! 714"};
    String[] lCourseInfo = {"TWR ! 12-1pm ! BuildingL ! 802", "", "T ! 11-12pm ! BuildingA ! 804"};
    String[] rCourseInfo = {"F ! 10-11am ! BuildingA ! 894", "T ! 9-10am ! BuildingP ! 902", ""};
    String[] courseActive = {"true", "false", "false"};

    TeacherViewActivity tva;
    @Mock
    JSONObject jsonMock;
    @Mock
    Intent mockIntent;
    @Before
    public void initMocks() {
        // Create SharedPreferenceEntry to persist.
        String[] sampleCourseNames = {"COMS", "SPANISH", "CPRE"};
        String[] sampleAttendance = {"COMS"};
        Course[] sampleCourses = new Course[sampleCourseNames.length];
        for(int i =0; i<sampleCourses.length;i++) sampleCourses[i] = new Course(sampleCourseNames[i], 0, 0, null, null, descriptions[i],cCourseInfo[i], lCourseInfo[i], rCourseInfo[i],Boolean.parseBoolean(courseActive[i]));
        Teacher sampleTeacher = new Teacher("teachname", "email@iastate.edu", "ABCabc123!", sampleCourseNames, 0,0);
        Mockito.when(mockIntent.getSerializableExtra("user")).thenReturn(sampleTeacher);
        Mockito.when(mockIntent.getSerializableExtra("attendance")).thenReturn(sampleAttendance);
        Mockito.when(mockIntent.getSerializableExtra("courseList")).thenReturn(sampleCourses);
        tva = new TeacherViewActivity();
        tva.initTVA(mockIntent);
    }

    @Test
    public void correctStudInfo() {
        assertEquals("teachname", tva.teacher.getName());
        assertEquals("email@iastate.edu", tva.teacher.getEmail());
        assertEquals("ABCabc123!", tva.teacher.getPassword());
        String[] names = tva.teacher.getClassList();
        String[] sampleCourseNames = {"COMS", "SPANISH", "CPRE"};
        for(int i=0; i<names.length; i++) assertEquals(names[i], sampleCourseNames[i]);
    }
    @Test
    public void correctCourses() {
        Course[] courses = tva.courseList;
        for(int i=0; i<courses.length;i+=1) {
            assertEquals(courses[i].getCourseDescription(), descriptions[i]);
            assertEquals(courses[i].getRecitationInfo(), rCourseInfo[i]);
            assertEquals(courses[i].getCourseInfo(), cCourseInfo[i]);
            assertEquals(courses[i].getLabInfo(), lCourseInfo[i]);
        }
    }
}