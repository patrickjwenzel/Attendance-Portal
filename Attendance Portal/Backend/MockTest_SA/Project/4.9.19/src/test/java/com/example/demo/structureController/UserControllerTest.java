
package com.example.demo.structureController;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest {
    
    private MockMvc mockMvc;

   

    @InjectMocks
    private UserController userResource;

    @Mock
    private UserRepository userRepository;
    
    @Before
    public void setUp() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(userResource)
                .build();
    }
    /**
     * Test of u1 method, of class UserController.
     * @throws java.lang.Exception
     */
    @Test
    public void testU1()  throws Exception {
        
       List<User> list = new ArrayList<User>();
		list.add(new User("test@hello","23452345345","Student"));
		
            list.add(new User("test@hello","23452345345","Teacher"));
      when(userRepository.findAll()).thenReturn(list);
             mockMvc.perform(get("/user/get/all"))
                .andExpect(status().isOk())
                 .andExpect(jsonPath("$.*", Matchers.hasSize(2)));
             
          verify(userRepository ,atLeastOnce()).findAll();
          
    }

    /**
     * Test of u2 method, of class UserController.
     */
      @Test
    public void testU2()  throws Exception {
        
      User user = new User("test@hello","23452345345","Student");
		
        
      when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
             mockMvc.perform(get("/user/get/id/{id}" ,"3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("test@hello")));
             
     //     verify(userRepository ,atLeastOnce()).findById();
          
    }
    /**
     * Test of u3 method, of class UserController.
     */
    @Test
    public void testU3()  throws Exception {
        
      User user = new User("test@hello","23452345345","Student");
	user.name = "Hello name";
        
      when(userRepository.findByName(anyString())).thenReturn(Optional.of(user));
             mockMvc.perform(get("/user/get/name/{name}" ,"Hello name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", Matchers.is("Hello name")));
             
    }

    /**
     * Test of u4 method, of class UserController.
     */
    @Test
    public void testU4()  throws Exception  {
        User user = new User("test@hello","23452345345","Student");
	user.name = "Hello name";
        
      when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
             mockMvc.perform(get("/user/get/email/{email}" ,"Hello name"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("test@hello")));
    }

    /**
     * Test of u5 method, of class UserController.
     */
    @Test
    public void testU5()  throws Exception  {
        User user = new User("test@hello","23452345345","Student");
	user.name = "Hello name";
        
      when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
      when(userRepository.save(user)).thenReturn(user);
      
             mockMvc.perform(post("/user/post/create/user/{email}/{password}/{type}" ,"hello123@test.com","testest123123","Teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("test@hello")));
    }

    /**
     * Test of u6 method, of class UserController.
     */
        @Test
    public void testU6()  throws Exception  {
        User user = new User("test@hello","23452345345","Student");
	user.name = "Hello name";
        
      when(userRepository.findById(anyInt())).thenReturn(Optional.of(user));
      doNothing().when(userRepository).delete(user);
      
             mockMvc.perform(post("/user/post/delete/{id}" ,"3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("test@hello")));
    }


    /**
     * Test of u7 method, of class UserController.
     */
    @Test
    public void testU7() throws Exception {
        
      doNothing().when(userRepository).deleteAll();
      
             mockMvc.perform(post("/user/post/delete/all"))
                .andExpect(status().isOk())
                .andExpect(content().string("All Users Were Deleted!"));
    }

    /**
     * Test of changeType method, of class UserController.
     */
    @Test
    public void testChangeType() throws Exception {
        
        User user = new User("hello123@test.com","23452345345","Teacher");
	user.name = "Hello name";
        
      when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));
      when(userRepository.save(user)).thenReturn(user);
      
             mockMvc.perform(post("/user/post/change/type/{email}/{type}" ,"hello123@test.com","Teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email", Matchers.is("hello123@test.com")));
    }
    
}
