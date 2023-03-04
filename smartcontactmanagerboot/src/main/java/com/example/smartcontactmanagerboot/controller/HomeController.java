package com.example.smartcontactmanagerboot.controller;


import java.util.Random;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.smartcontactmanagerboot.dao.UserRepository;
import com.example.smartcontactmanagerboot.pojo.User;
import com.example.smartcontactmanagerboot.util.Message;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@RequestMapping("/smartContactManager")
	public String home() {
		return "home";
	}
	
	@RequestMapping("/smartContactManager/about")
	public String about() {
		return "about";
	}
	
	@RequestMapping("/smartContactManager/signup")
	public String signUp(Model model) {
		model.addAttribute("user", new User());
		return "signup";
	}
	@RequestMapping("/smartContactManager/login")
	public String login(Model model) {
		model.addAttribute("user", new User());
		return "login";
	}
	@RequestMapping(value="/smartContactManager/register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result,
			@RequestParam(value = "agreement",defaultValue = "false")boolean agreement,Model model, HttpSession session) {
		try {
			if(!agreement) {
				System.out.println("Terms and Conditions not agreed.");
//				model.addAttribute("user", user);
//				session.setAttribute("message", new Message("Did not agree to terms and condition!","alert-danger"));
//				return "signup";
				throw new Exception("Did not agree to terms and condition!");
				
			}
			if(result.hasErrors()) {
				System.out.println("Error : "+result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageUrl("default.png");
			user.setPassword(passwordEncoder.encode(user.getPassword()));
			
			
			System.out.println(agreement);
			User save = this.userRepository.save(user);
			System.out.println(save);
			model.addAttribute("user", new User());
			session.setAttribute("message", new Message("Last User: Successfully Registered!","alert-success"));
			return "signup";
		}catch(Exception e) {
			e.printStackTrace();
			model.addAttribute("user",user);
			session.setAttribute("message", new Message("Last User: Something went Wrong!!!"+e.getMessage(),"alert-danger"));
			return "signup";
		}
		
	}
	
	
	@PostMapping("/smartContactManager/sendotp")
	public String sendOtp(@RequestParam("email") String email) {
		System.out.println(" Forgot Password : "+email);
		int otp1 = new Random().nextInt(10000);
		String otp = String.valueOf(otp1);
		if(otp.length()==1)
			otp="000"+otp;
		else if(otp.length()==2)
			otp="00"+otp;
		else if(otp.length()==3)
			otp="0"+otp;
		System.out.println(otp);
		
		//Send E-Mail
		
		
		return "varifyotp";
	}
}
