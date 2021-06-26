package com.smartcontract.smartcontract.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.smartcontract.smartcontract.dao.RegisterRepo;
import com.smartcontract.smartcontract.entities.User;
import com.smartcontract.smartcontract.helper.Message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;




@Controller
public class MainController 
{
    @Autowired
    private RegisterRepo repo;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @GetMapping("/index")
    public String index(Model model)
    {
        model.addAttribute("title", "SmartContact");
        return "index";
    }
    @GetMapping("/signup")
    public String signup()
    {
        return "signup";
    }


    // form registration controller
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@Valid
        @ModelAttribute("user") User user, BindingResult result,
    @RequestParam(value = "checkbox", defaultValue = "false") boolean terms,
    Model model, HttpSession session)
    {
        try 
        {
            if(terms==false)
            {
                System.out.println("please agree with terms and conditions.");
                throw new Exception("please agree with terms and conditions.");
            
            }
            if(result.hasErrors())
            {
                System.out.println(result.toString());
                model.addAttribute("conditions", result);
                return "signup";
            }
            else
            {
                user.setRole("USER");
                user.setStatus("Allow");
                user.setImage("default.jpg");
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                User registeredUser = repo.save(user);
                System.out.println(registeredUser);
                model.addAttribute("user", user);
                session.setAttribute("message", 
                new Message("terms_success", "Successfully SignUp!"));

                
            }

            
        } 
        catch (Exception ae) 
        {
            ae.printStackTrace();
            session.setAttribute("message", 
            new Message("terms_error", "please agree with terms and conditons"));
            return "signup";
        }
        return "signup";

    }

    //login page controller

    @GetMapping("/signin")
    public String signin(Model model)
    {
        model.addAttribute("title", "SmartContact: SignIn");
        return "signin";
    }
}
