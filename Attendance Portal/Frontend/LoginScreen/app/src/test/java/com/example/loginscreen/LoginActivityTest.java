package com.example.loginscreen;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests the login activity
 */
public class LoginActivityTest {

    @Test
    public void isEmailValid_CorrectEmailSimple_ReturnsTrue() {
        LoginActivity login = new LoginActivity();
        assertTrue(login.isEmailValid("name@iastate.edu"));
    }

    @Test
    public void isEmailValid_CorrectEmailNewDomain_ReturnsTrue() {
        LoginActivity login = new LoginActivity();
        assertTrue(login.isEmailValid("name@isu.edu"));
    }

    @Test
    public void isEmailValid_CorrectEmailComplex_ReturnsTrue() {
        LoginActivity login = new LoginActivity();
        assertTrue(login.isEmailValid("name123ABC&&@iastate.edu"));
    }

    @Test
    public void isEmailValid_IncorrectEmailDomain_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        assertFalse(login.isEmailValid("name@gmail.com"));
    }

    @Test
    public void isEmailValid_IncorrectEmailDomainError_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        assertFalse(login.isEmailValid("name@iastate"));
    }

    @Test
    public void isEmailValid_IncorrectEmailMissingDomain_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        assertFalse(login.isEmailValid("name"));
    }

    @Test
    public void isEmailValid_IncorrectEmailEmpty_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        assertFalse(login.isEmailValid("@iastate.edu"));
    }


    @Test
    public void isPasswordValid_CorrectPasswordSimple_ReturnsTrue() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertTrue(login.isPasswordValid("ABCabc123!", "pwenzel"));
    }

    @Test
    public void isPasswordValid_CorrectPasswordUniqueChar_ReturnsTrue() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertTrue(login.isPasswordValid("Ab1!@$%^&*", "pwenzel"));
    }

    @Test
    public void isPasswordValid_CorrectPasswordLong_ReturnsTrue() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertTrue(login.isPasswordValid("AdhEILMmdniop.23**&", "pwenzel"));
    }

    @Test
    public void isPasswordValid_IncorrectPasswordNoNumbers_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertFalse(login.isPasswordValid("ABCabcwasd!@$", "pwenzel"));
    }

    @Test
    public void isPasswordValid_IncorrectPasswordNoSpecial_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertFalse(login.isPasswordValid("ABCabc123", "pwenzel"));
    }

    @Test
    public void isPasswordValid_IncorrectPasswordNoCaps_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertFalse(login.isPasswordValid("ABCASD123!", "pwenzel"));
    }
    @Test
    public void isPasswordValid_IncorrectPasswordContainsName_ReturnsFalse() {
        LoginActivity login = new LoginActivity();
        login.userName = "Hello";
        login.passErr = new StringBuilder();
        assertFalse(login.isPasswordValid("Aa123!pwenzel", "pwenzel"));
    }




    //Mocking instances of the server.
    @Mock
    JSONObject receivedValidStudent;
    @Mock
    JSONObject receivedValidTeacher;
    @Mock
    JSONObject receivedInvalidStudent;
    @Mock
    JSONObject receivedInvalidTeacher;

    @Before
    public void initMocks() throws JSONException {
        // Create SharedPreferenceEntry to persist.
        receivedValidStudent = createMockStudent();
        receivedValidTeacher = createMockTeacher();
        receivedInvalidStudent = createMockInvalidStudent();
        receivedInvalidTeacher = createMockInvalidTeacher();
    }


    @Test
    public void studentToString()
    {
        assertEquals("{\"id\":5,\"email\":\"aholman@iastate.edu\",\"password\":\"ABCabc123!\",\"name\":\"Andrew\",\"type\":\"student\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n", receivedValidStudent.toString());
    }
    @Test
    public void teacherToString()
    {
        assertEquals("{\"id\":6,\"email\":\"bholman@iastate.edu\",\"password\":\"ABCabc123!\",\"name\":\"Andrew\",\"type\":\"teacher\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n", receivedValidTeacher.toString());
    }
    @Test
    public void studentParseClassNames() throws JSONException {
        LoginActivity login = new LoginActivity();
        String[] classArr = login.parseString(receivedValidStudent.getString("class"), "name");
        String[] expectedClassArr = {"ComS", "Math", "SoftE", "Spanish"};
        assertEquals(expectedClassArr.length, classArr.length);
        for(int i=0; i<classArr.length && i<expectedClassArr.length; i++)
            assertEquals(expectedClassArr[i], classArr[i]);
    }
    @Test
    public void teacherParseClassNames() throws JSONException {
        LoginActivity login = new LoginActivity();
        String[] classArr = login.parseString(receivedValidTeacher.getString("class"), "name");
        String[] expectedClassArr = {"ComS", "Math", "SoftE", "Spanish"};
        assertEquals(expectedClassArr.length, classArr.length);
        for(int i=0; i<classArr.length && i<expectedClassArr.length; i++)
            assertEquals(expectedClassArr[i], classArr[i]);
    }
    @Test
    public void isValidStudent() throws JSONException {
        LoginActivity login = new LoginActivity();
        assertTrue(login.isValidUser(createStudent(receivedValidStudent)));
        assertFalse(login.isValidUser(createStudent(receivedInvalidStudent)));
    }
    @Test
    public void isValidTeacher() throws JSONException {
        LoginActivity login = new LoginActivity();
        assertTrue(login.isValidUser(createTeacher(receivedValidTeacher)));
        assertFalse(login.isValidUser(createTeacher(receivedInvalidTeacher)));
    }

    //TODO
    private JSONObject createMockStudent() throws JSONException {
        JSONObject jsonMock = Mockito.mock(JSONObject.class);
        Mockito.when(jsonMock.toString()).thenReturn("{\"id\":5,\"email\":\"aholman@iastate.edu\",\"password\":\"ABCabc123!\",\"name\":\"Andrew\",\"type\":\"student\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("class")).thenReturn("[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("name")).thenReturn("Andrew");
        Mockito.when(jsonMock.getString("email")).thenReturn("aholman@iastate.edu");
        Mockito.when(jsonMock.getString("password")).thenReturn("ABCabc123!");

        return jsonMock;
    }
    //TODO
    private JSONObject createMockTeacher() throws JSONException {
        JSONObject jsonMock = Mockito.mock(JSONObject.class);
        Mockito.when(jsonMock.toString()).thenReturn("{\"id\":6,\"email\":\"bholman@iastate.edu\",\"password\":\"ABCabc123!\",\"name\":\"Andrew\",\"type\":\"teacher\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("class")).thenReturn("[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("name")).thenReturn("Andrew");
        Mockito.when(jsonMock.getString("email")).thenReturn("bholman@iastate.edu");
        Mockito.when(jsonMock.getString("password")).thenReturn("ABCabc123!");

        return jsonMock;
    }
    //TODO
    private JSONObject createMockInvalidStudent() throws JSONException {
        JSONObject jsonMock = Mockito.mock(JSONObject.class);
        Mockito.when(jsonMock.toString()).thenReturn("{\"id\":7,\"email\":\"fail\",\"password\":\"null\",\"name\":\"fail\",\"type\":\"student\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("class")).thenReturn("[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("name")).thenReturn("fail");
        Mockito.when(jsonMock.getString("email")).thenReturn("fail");
        Mockito.when(jsonMock.getString("password")).thenReturn("null");

        return jsonMock;
    }
    //TODO
    private JSONObject createMockInvalidTeacher() throws JSONException {
        JSONObject jsonMock = Mockito.mock(JSONObject.class);
        Mockito.when(jsonMock.toString()).thenReturn("{\"id\":8,\"email\":\"fail\",\"password\":\"null\",\"name\":\"fail\",\"type\":\"student\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("class")).thenReturn("[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("name")).thenReturn("fail");
        Mockito.when(jsonMock.getString("email")).thenReturn("fail");
        Mockito.when(jsonMock.getString("password")).thenReturn("null");

        return jsonMock;
    }
    private Student createStudent(JSONObject json) throws JSONException {
        LoginActivity login = new LoginActivity();
        String[] classArr = login.parseString(json.getString("class"), "name");
        return new Student(json.getString("name"), json.getString("email"), json.getString("password"), classArr, 0, 0);
    }
    private Teacher createTeacher(JSONObject json) throws JSONException {
        LoginActivity login = new LoginActivity();
        String[] classArr = login.parseString(json.getString("class"), "name");
        return new Teacher(json.getString("name"), json.getString("email"), json.getString("password"), classArr, 0, 0);
    }


}