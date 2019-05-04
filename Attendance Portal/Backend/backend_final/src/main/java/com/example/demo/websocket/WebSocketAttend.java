package com.example.demo.websocket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.repository.UserRepository;

@ServerEndpoint("/attend/{username}/{class}") 

@Component
public class WebSocketAttend {

	
	@Autowired
	UserRepository userRepository;
	
	
	
	
    private static Map<Session, String> sessionUsernameMap = new HashMap<>();
    private static Map<String, Session> usernameSessionMap = new HashMap<>();
    private static Map<String, String> usertoGroupMap = new HashMap<String, String>();
    private static Map<String, List<String>> grouptoOnlineUsers = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    
    @OnOpen
    public void onOpen(Session session,@PathParam("username") String username,@PathParam("class") String room) throws IOException 
    {	
    	
    	//1. when user joins , get the group and insert into userstogroup and grouptoonlineusers
    	
    	usertoGroupMap.put(username, room);
//    	usertoGroupMap.put("b", "1");
//    	usertoGroupMap.put("c", "2");
//    	usertoGroupMap.put("d", "2");
    	if(!grouptoOnlineUsers.containsKey(usertoGroupMap.get(username))) {
    		List<String> al1 = new ArrayList<>();
    		al1.add(username);
    		grouptoOnlineUsers.put(usertoGroupMap.get(username), al1 );
    	}
    	else {
    		List<String> al2 = grouptoOnlineUsers.get(usertoGroupMap.get(username));
    		al2.add(username);
    		grouptoOnlineUsers.put(usertoGroupMap.get(username), al2 );
    	}
    	
    	
    	
        logger.info("Entered into Open");
        
        sessionUsernameMap.put(session, username);
        usernameSessionMap.put(username, session);
        
        String message=" has Joined the Chat";
        String group = usertoGroupMap.get(sessionUsernameMap.get(session));
		List<String> online = grouptoOnlineUsers.get(group);
		for(String user : online) {
			sendMessageToPArticularUser(user,username + message);
       // sendMessageToPArticularUser(username, message);
		}
        
    }
 
    @OnMessage
    public void onMessage(Session session, String message) throws IOException 
    {
        // Handle new messages
    	logger.info("Entered into Message: Got Message:"+message);
    	String username = sessionUsernameMap.get(session);
    	
    	if (message.startsWith("@"))
    	{
    		String name = message.substring(1, message.indexOf(' '));
    		System.out.println("particular name=" + name);
    		sendMessageToPArticularUser(name, "[Direct Message] " + username + ": " + message);
    	}
    	else {
    		
    		String group = usertoGroupMap.get(sessionUsernameMap.get(session));
    		List<String> online = grouptoOnlineUsers.get(group);
    		for(String user : online) {
    			sendMessageToPArticularUser(user, message);
    		}
    		//broadcast(message);
    	}
   
    }
    @SuppressWarnings("unused")
	@OnClose
    public void onClose(Session session) throws IOException
    {
    	logger.info("Entered into Close");
    	
    	
    	String username = sessionUsernameMap.get(session);
    	sessionUsernameMap.remove(session);
    	usernameSessionMap.remove(username);
    	String group = usertoGroupMap.get(username);
    	usertoGroupMap.remove(username);
    	List<String> al = grouptoOnlineUsers.get(group);
    	al.remove(username);
        
    	String message = username + " disconnected";
    	
    }
 
    @OnError
    public void onError(Session session, Throwable throwable) 
    {
        
    	logger.info("Entered into Error");
    }
    
	private void sendMessageToPArticularUser(String username, String message) 
    {	
    	try {
    		usernameSessionMap.get(username).getBasicRemote().sendText(message);
        } catch (IOException e) {
        	logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("unused")
	private static void broadcast(String message) 
    	      throws IOException 
    {	  
    	sessionUsernameMap.forEach((session, username) -> {
    		synchronized (session) {
	            try {
	                session.getBasicRemote().sendText(message);
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	@SuppressWarnings("unused")
	private void broadcast2(String message)
			throws IOException
	{

	}
}
