package com.example.loginscreen;

import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;
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
 * Tests the password reset activity
 */
@RunWith(MockitoJUnitRunner.class)
public class PasswordResetTest {

    @Test
    public void passwordReset_CorrectPassword_ReturnsTrue(){
        PasswordReset activity = Mockito.mock(PasswordReset.class);
        String[] passwords = {"ABCabc123!", "md9ERBS!!", "sdfaALKS5432$"};
        for(int i=0; i< passwords.length; i+=1){
            assertTrue(PasswordReset.isPasswordValid(passwords[i], "pwenzel"));
        }
    }

    @Test
    public void passwordReset_IncorrectPassword_ReturnsFalse(){
        PasswordReset activity = Mockito.mock(PasswordReset.class);
        String[] passwords = {"abc123#$@", "aBadfb1234", "abc", "wenze98^^"};
        for(int i=0; i < passwords.length; i+=1){
            assertFalse(PasswordReset.isPasswordValid(passwords[i], "pwenzel"));
        }
    }

    String ValidNewPassword;
    String InvalidNewPassword;
    String InvalidEmail;
    String InvalidOldPassword;
    String ValidOldPassword;

    @Mock
    Student validStudent;

    @Before
    public void init_mocks() throws JSONException{
        ValidNewPassword = "ABCabc234!";
        InvalidNewPassword = "a";
        InvalidEmail = "pwenzel@iastate.com";
        InvalidOldPassword = "bbbbbbbbbbb";
        ValidOldPassword = "ABCabc123!";
        validStudent = createStudent(createMockStudent());
    }

    private JSONObject createMockStudent() throws JSONException {
        JSONObject jsonMock = Mockito.mock(JSONObject.class);
        Mockito.when(jsonMock.toString()).thenReturn("{\"id\":5,\"email\":\"pwenzel@iastate.edu\",\"password\":\"ABCabc123!\",\"name\":\"Patrick\",\"type\":\"student\",\"classes\":[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("class")).thenReturn("[{\"id\":1,\"classId\":99239624,\"name\":\"ComS\",\"description\":\"Embedded C programming. Interrupt handling. Memor\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":2,\"classId\":98085377,\"name\":\"Math\",\"description\":\"Solution methods for ordinary differential equati\",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":3,\"classId\":67213309,\"name\":\"SoftE\",\"description\":\"A practical introduction to methods for managing \",\"lon\":-122.084,\"lat\":37.422,\"active\":true,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}},{\"id\":4,\"classId\":38699741,\"name\":\"Spanish\",\"description\":\"Introduction to the theory, methods, techniques, \",\"lon\":0,\"lat\":0,\"active\":false,\"cInfo\":\"mwf \\/ 9 \\/ Buildingf \\/ 15\",\"rInfo\":\" \",\"lInfo\":\" \",\"school\":{\"id\":8,\"sName\":\"Iowa State\"}}],\"attendance\":[],\"messages\":[],\"activeC\":[]}\n");
        Mockito.when(jsonMock.getString("name")).thenReturn("Patrick");
        Mockito.when(jsonMock.getString("email")).thenReturn("pwenzel@iastate.edu");
        Mockito.when(jsonMock.getString("password")).thenReturn("ABCabc123!");
        return jsonMock;
    }

    @Test
    public void test_isValidPassword() {
        PasswordReset pr = new PasswordReset();
        assertTrue(pr.isPasswordValid(ValidNewPassword, pr.getUsername(validStudent.getEmail())));
        assertFalse(pr.isPasswordValid(InvalidNewPassword, pr.getUsername(validStudent.getEmail())));
    }

    private Student createStudent(JSONObject json) throws JSONException {
        LoginActivity login = new LoginActivity();
        String[] classArr = login.parseString(json.getString("class"), "name");
        return new Student(json.getString("name"), json.getString("email"), json.getString("password"), classArr, 0, 0);
    }
}
