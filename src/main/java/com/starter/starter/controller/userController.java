package com.starter.starter.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
// import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.starter.starter.dao.ContactRepo;
import com.starter.starter.dao.userRepo;
import com.starter.starter.entites.contact;
import com.starter.starter.entites.user;
import com.starter.starter.helper.helpermsg;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/user")
public class userController {


     @Autowired   // we need to get data of the currently logged in user so we autowire userRepo here
     private userRepo userrepo;

     @Autowired // we need to show the contacts to the user so autowiring with the contactrepo
     private ContactRepo contactRepo;

     @ModelAttribute
     public void addCommondata(Model m , Principal principle) {  // we have to send the user each time so made it as a common object
  String pname = principle.getName();
     m.addAttribute("name" , pname);

// get the user using email
        user user = userrepo.findByEmail(pname);
     System.out.println("User " + user);

     // now we have to send this user object to the dashboard page therefore we need the model object
        m.addAttribute("user" , user);
     }


    @RequestMapping("/index")
    public String dashboard(Model model , Principal principle) {  // this principle object contains the unique id of the user b which it is logged in in this case it is email
    // so we are trying to get the user using email from the database
         model.addAttribute("title" , "User Dashboard");

        return "normal/user_dashboard";
    }
    // add form handler
    @GetMapping("/add-contact")
    public String addContact(Model model) {
    model.addAttribute("title" , "Add Contact");
        model.addAttribute("contact" , new contact());
        return "normal/addContact";
    }
    @PostMapping("/process-contact") 
    public String processContact(@ModelAttribute contact contact ,  @RequestParam("profileImage") MultipartFile file,
        Principal principal , HttpSession session) {
// this contact has to save all the attributes of contstarterApplication
// requestparam as we know used when there is a attribute that is not in the entity that is profileimage
// principal as we know used when we have to find the object by unique id(email)



// we are saving the data of the contact in the databse useing the user same as we did above with the help of user by using the unique id in this case the email
 try{

     if (file.isEmpty()) {
System.out.println("File is empty");
contact.setImage("smartimage.jpg");
        } else {
            contact.setImage(file.getOriginalFilename());

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path  = Paths.get(saveFile.getAbsolutePath()+File.separator+file.getOriginalFilename());

            Files.copy(file.getInputStream() , path , StandardCopyOption.REPLACE_EXISTING);

            System.out.println("Image is uploaded");
        } 
        // the images we choose only show ass name in the database but the original images are stored in the target

    String name = principal.getName();
 user user = this.userrepo.findByEmail(name);
 contact.setUser(user); // this is for the user id (contact ko user de dia)
 user.getContacts().add(contact); // user ko contact de dia
 this.userrepo.save(user);
     System.out.println("Data" + contact);
System.out.println("Added to the database");
// success message
session.setAttribute("message", new helpermsg("Your content is added successfully", "success"));

 } catch(Exception e) {
    System.out.println("Error"+ e.getMessage());
    e.printStackTrace();
// danger message
session.setAttribute("message", new helpermsg("There is an Error", "danger"));

 }
        return "normal/addContact";
    }

    //show contact handler
    //per page = 4[n]
    //current page = 0[page]
    @GetMapping("/show-contact/{page}")
    public String showContact(@PathVariable("page") Integer page, Model m , Principal principal) {
     m.addAttribute("title","Show Contacts");
    //contact ki list return krni hai ab
    
    String Uname = principal.getName();
    user user = this.userrepo.findByEmail(Uname);

    Pageable pageable = PageRequest.of(page, 4);


    Page<contact> contacts = this.contactRepo.findContactsByUser(user.getId(),pageable);

    m.addAttribute("contacts" , contacts);
    m.addAttribute("CurrentPage" , page);
    m.addAttribute("TotalPages" , contacts.getTotalPages());



    // here first we are fetching the user like we did earlier by find by email and then for that specific user we use the method to get the contacts and getting the contacts by the method w create in the contactrepo
        return "normal/showContact";

    }

    @GetMapping("/show-contact")
public String showContactDefault() {
    return "redirect:/user/show-contact/0";
}

        // showing contact details

        @RequestMapping("/{cId}/contact")
        public String ContactDetails(@PathVariable("cId") Integer cId , Model model , Principal principal) {
            
            System.out.println("CID" + cId);

            Optional<contact> contactOp = this.contactRepo.findById(cId);
            contact contact = contactOp.get();

            // passing only the contacts of the login user
            
            String useString = principal.getName();
            user user = this.userrepo.findByEmail(useString);

            if (user.getId() == contact.getUser().getId()) {
                
                            model.addAttribute("contact" , contact);
                                                        model.addAttribute("title" , contact.getName());
            }
            return "normal/contact_details";
        }

        // deleting a contact

       @GetMapping("/delete/{cid}")
public String deleteContact(
        @PathVariable("cid") Integer cid,
        Principal principal,
        HttpSession session) {

    contact contact = this.contactRepo.findById(cid).orElse(null);

    if (contact == null) {
        session.setAttribute("message",
                new helpermsg("Contact not found", "danger"));
        return "redirect:/user/show-contact/0";
    }

    user user = this.userrepo.findByEmail(principal.getName());
    user.getContacts().remove(contact);
    this.userrepo.save(user);

    // phele humne contact fetch kia then user fetch kia kyuki humne id match krni hai agr id match ho tbhi delete krna hai

    // ✅ ownership check
     if (!(user.getId() == contact.getUser().getId())) {
        session.setAttribute("message",
                new helpermsg("Unauthorized delete", "danger"));
        return "redirect:/user/show-contact/0";
    }

    // ✅ VERY IMPORTANT PART
    user.getContacts().remove(contact);
    contact.setUser(null);
    this.userrepo.save(user);

    session.setAttribute("message",
            new helpermsg("Contact deleted successfully", "success"));

    return "redirect:/user/show-contact/0";
}


// Durgesh sir used POST for both operations to simplify the flow and avoid exposing data in the URL, but ideally GET should be used to render the update form and POST should be used to submit the updated data.”

// update form handler
@PostMapping("/update-contact/{cid}")
public String updateform(Model m , @PathVariable("cid") Integer cid) {

    m.addAttribute("title" , "Update Contact");


    contact contact =this.contactRepo.findById(cid).get();
    m.addAttribute("contact", contact);
    return "normal/update_form";
}

// now we have to show the updated contact details therefore that page 
 
@PostMapping("/process-update")
public String updatehandler(@ModelAttribute contact contact , @RequestParam("profileImage") MultipartFile file , Principal principal , HttpSession session  ) {
    try {
        contact oldContact = this.contactRepo.findById(contact.getcId()).orElse(null);

        if (oldContact == null) {
            session.setAttribute("message", new helpermsg("Contact not found", "danger"));
            return "redirect:/user/show-contact/0";
        }

        if (!file.isEmpty()) {

            // delete old image
            if (!oldContact.getImage().equals("smartimage.jpg")) {
                File oldFile = new ClassPathResource("static/img").getFile();
                Path oldPath = Paths.get(oldFile.getAbsolutePath() + File.separator + oldContact.getImage());
                Files.deleteIfExists(oldPath);
            }

            // update
            contact.setImage(file.getOriginalFilename());

            File saveFile = new ClassPathResource("static/img").getFile();

            Path path  = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } else {
            contact.setImage(oldContact.getImage());
        }

        user user = this.userrepo.findByEmail(principal.getName());
        contact.setUser(user);
        this.contactRepo.save(contact);

        session.setAttribute("message", new helpermsg("Contact updated successfully", "success"));
    } catch (Exception e) {
        System.out.println("Error: " + e.getMessage());
        e.printStackTrace();
        session.setAttribute("message", new helpermsg("There is an error while updating the contact", "danger"));
    }

  System.out.println("Contact " + contact.getcId());
  System.out.println("Contact " + contact.getName());
        return "redirect:/user/show-contact/0";
}

// for the profile handler
@GetMapping("/profile")
public String yourprofile(Model m , Principal principal) {
    m.addAttribute("title" , "Your Profile");
    String name = principal.getName();
    user user = this.userrepo.findByEmail(name);
    m.addAttribute("user" , user);
    return "normal/profile";
}

}