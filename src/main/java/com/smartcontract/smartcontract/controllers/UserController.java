package com.smartcontract.smartcontract.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smartcontract.smartcontract.dao.RegisterRepo;
import com.smartcontract.smartcontract.dao.contatctRepo;
import com.smartcontract.smartcontract.entities.Contact;
import com.smartcontract.smartcontract.entities.User;
import com.smartcontract.smartcontract.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private RegisterRepo repo;
    @Autowired
    private contatctRepo contactRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;

    @ModelAttribute
    public void commonData(Model model, Principal principal) {
        String email = principal.getName();
        User user = repo.getUserByName(email);
        model.addAttribute("user", user);
    }

    @RequestMapping("/home")
    public String home(Model model, Principal principal) {
        model.addAttribute("title", "Home");
        return "normal/home";
    }

    @GetMapping("/addcontact")
    public String addContact() {
        return "normal/addContact";
    }

    @PostMapping("/processContact")
    public String processContact(@Valid @ModelAttribute Contact contact, BindingResult result,
            @RequestPart("profileImage") MultipartFile file, Principal principal, Model model, HttpSession session) {
        try {
            if (result.hasErrors()) {
                System.out.println(result.toString());
                model.addAttribute("fieldErrors", result);
            }
            // model.addAttribute("contact", contact);
            String email = principal.getName();
            User currentUser = this.repo.getUserByName(email);
            if (file.isEmpty()) {
                System.out.println("File is emptye select a valid image");
                contact.setImage("default.jpg");

            } else {
                contact.setImage(file.getOriginalFilename());
                String upload_dir = "C:\\Users\\User\\SpringProjects\\smartcontract\\src\\main\\resources\\static\\profile_images";
                Path path = Paths.get(upload_dir + File.separator + file.getOriginalFilename());
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("file uploaded successfully....");
            }
            currentUser.getContacts().add(contact);
            contact.setUser(currentUser);
            this.repo.save(currentUser);
            session.setAttribute("message", new Message("successContact", "Contact Added Successfullly..."));
        } catch (Exception ae) {
            ae.printStackTrace();
        }
        return "normal/addcontact";
    }

    // show contacts
    @GetMapping("/showcontacts/{page}")
    public String showContact(Model model, Principal principal, @PathVariable("page") Integer page) {
        String email = principal.getName();
        User user = this.repo.getUserByName(email);

        Pageable pageable = PageRequest.of(page, 3);
        Page<Contact> contacts = this.contactRepository.getContactById(user.getId(), pageable);
        model.addAttribute("contacts", contacts);
        model.addAttribute("totalPage", contacts.getTotalPages());
        model.addAttribute("currentPage", page);
        model.addAttribute("title", "show-contact");
        return "normal/showContact";
    }

    // profile controller
    @GetMapping("/profile")
    public String profile(Model model, Principal principal) {
        String email = principal.getName();
        User user = this.repo.getUserByName(email);
        model.addAttribute("user", user);
        model.addAttribute("title", "profile");
        return "normal/profile";
    }

    // user update controller
    @GetMapping("/updateuser")
    public String updateUser(Model model, Principal principal) {
        String email = principal.getName();
        User user = this.repo.getUserByName(email);
        model.addAttribute("user", user);
        return "normal/updateUser";
    }

    // user update controller
    @PostMapping("/processUpdate")
    public String processUpdate(@Valid @ModelAttribute() User user, BindingResult result,
            @RequestPart("image") MultipartFile file, Model model, Principal principal) 
    {
        

        if (result.hasErrors()) 
        {
            System.out.println(result.toString());
            System.out.println("something went wrong");
            System.out.println(file.getOriginalFilename());    
        }
        if (file.isEmpty()) 
        {
            System.out.println("Please select a valid email");
            String oldEmail = principal.getName();
            User oldUser = this.repo.getUserByName(oldEmail);
            user.setImage(oldUser.getImage());

        } 
        else 
        {
            user.setImage(file.getOriginalFilename());
            String upload_dir = "C:\\Users\\User\\SpringProjects\\smartcontract\\src\\main\\resources\\static\\profile_images";
            Path path = Paths.get(upload_dir + File.separator + file.getOriginalFilename());
            try 
            {
                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } 
            catch (IOException e) 
            {
                e.printStackTrace();
            }
            user.setPassword(encoder.encode(user.getPassword()));
            this.repo.save(user);
            System.out.println("profile updated successfully....");
        }
        return "normal/profile";
    }
    //deleting contact
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cid, Principal principal)
    {
        String email = principal.getName();
        User user = this.repo.getUserByName(email);
        Optional<Contact> op = this.contactRepository.findById(cid);
        Contact contact = op.get();
        contact.setUser(user);
        if(contact.getUser().getId()==user.getId())
        { 
            List<Contact> allContacts = user.getContacts();
            for(int i =0; i<allContacts.size(); i++)
            {
                if(allContacts.get(i)==contact)
                {
                    user.getContacts().remove(allContacts.get(i));
                    this.repo.save(user);
                }
            }
        }
        else
        {
            System.out.println("this is not your contact...");
        }
        System.out.println(cid);
        return "redirect:/user/showContact/0";

    }
    
}
