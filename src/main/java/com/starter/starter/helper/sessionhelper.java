package com.starter.starter.helper;

import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Component
public class sessionhelper {
    
    public void removemessage() {
        try{
            System.out.println("removing message from session");       
         HttpSession session = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest().getSession();
          session.removeAttribute("message");    
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
}
