package com.example.smartcontactmanagerboot.controller;


import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.smartcontactmanagerboot.dao.ContactRepository;
import com.example.smartcontactmanagerboot.dao.UserRepository;
import com.example.smartcontactmanagerboot.pojo.Contact;
import com.example.smartcontactmanagerboot.pojo.User;
import com.example.smartcontactmanagerboot.util.Message;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/smartContactManager/user")
public class UserController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@Autowired
	private BCryptPasswordEncoder bPasswordEncoder;
	
	@ModelAttribute
	public void addCommonData(Model model,Principal principal) {
		String userEmail = principal.getName();
		System.out.println(userEmail);
		User user = userRepository.getUserByUserName(userEmail);
		System.out.println(user);
		model.addAttribute("user", user);
	}
	
	@RequestMapping("/index")
	public String dashBoard(Model model, Principal principal) {
		return "normal/user_dashboard";
	}
	
	@RequestMapping("/add-contact")
	public String openAddContactForm(Model  model, Principal principal) {
		model.addAttribute("contact", new Contact());
		return "normal/add_contact";
	}
	
	@PostMapping("/process-contact")
	public String processContact(@ModelAttribute Contact contact,
			Principal principal, HttpSession session) {
		
		try {
			String email = principal.getName();
			User user = this.userRepository.getUserByUserName(email);
//			if(file.isEmpty()) {
//				System.out.println("Empty file");
//			}
//			else {
//				System.out.println("Uploading Image");
//				contact.setImage(file.getOriginalFilename());
//				File saveFile = new ClassPathResource("static/img").getFile();
//				Path path = Paths.get(saveFile.getAbsolutePath()+File.separator
//						+file.getOriginalFilename());
//				Files.copy(file.getInputStream(), path , StandardCopyOption.REPLACE_EXISTING);
//				System.out.println("Image Uploaded");
//			}
			
			contact.setUser(user);
			user.getContacts().add(contact);
			this.userRepository.save(user);
			System.out.println(contact);
			session.setAttribute("message", new Message("Last Contact added successfully!", "success"));
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
			session.setAttribute("message", new Message("Something went wrong", "danger"));
		}
		return "normal/add_contact";
	}
	
	@GetMapping("/show-contacts/{page}")	
	public String showContacts(@PathVariable("page")Integer page ,Model m ,Principal principal) {
		String email = principal.getName();
		User user = this.userRepository.getUserByUserName(email);
//		List<Contact> contacts = user.getContacts();
		Pageable pageable = PageRequest.of(page,5);
		Page<Contact> contacts = this.contactRepository.findContactByUser(user.getId(),pageable);
		m.addAttribute("contacts", contacts);
		m.addAttribute("currentPage", page);
		m.addAttribute("totalPages", contacts.getTotalPages());
		return "normal/contacts";
	}
	
	@GetMapping("/show-contacts/details/{id}")
	public String showDetails(@PathVariable("id") Integer id, Model m,Principal principal) {
		System.out.println("details");
		Optional<Contact> contact = this.contactRepository.findById(id);
		Contact contactObj = contact.get();
		String email = principal.getName();
		User user = this.userRepository.getUserByUserName(email);
		if(user.getId()==contactObj.getUser().getId())
			m.addAttribute("contact", contactObj);
		return "normal/contact_detail";
		
	}
	
	@GetMapping("/delete/{id}")
	public String deleteContact(@PathVariable("id") Integer id,Model m,Principal principal,HttpSession session)  {
		Optional<Contact> optional = this.contactRepository.findById(id);
		Contact contact = optional.get();
		if(this.userRepository.getUserByUserName(principal.getName()).getId()==contact.getUser().getId()){
			//contact.setUser(null);
			//this.contactRepository.delete(contact);
			User user = this.userRepository.getUserByUserName(principal.getName());
			user.getContacts().remove(contact);
			this.userRepository.save(user);
			System.out.println("DELETED");
			session.setAttribute("message", new Message("Conatct deleted successfully.","success"));
			return "redirect:/smartContactManager/user/show-contacts/0";
		}		
		return "normal/delete";
	}
	
	@PostMapping("/update/{id}")
	public String update_Form(@PathVariable("id") Integer id,Model m) {
		Optional<Contact> optional = this.contactRepository.findById(id);
		Contact contact = optional.get();
		m.addAttribute("contact", contact);
		return "normal/update_form";
	}
	
	@PostMapping("/process-update")
	public String updateDetails(@ModelAttribute Contact contact, Model m, HttpSession session, Principal principal) {
		System.out.println(contact);
		try {
			String email = principal.getName();
			User user = this.userRepository.getUserByUserName(email);
			contact.setUser(user);
			this.contactRepository.save(contact);
			session.setAttribute("message", new Message("Your contact is updated","success"));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/smartContactManager/user/show-contacts/details/"+contact.getId();
	}
	
	@GetMapping("/profile")
	public String yourProfile() {
		return "normal/profile";
	}
	
	@GetMapping("/change-password")
	public String changePassword() {
		return "normal/changepassword";
	}
	
	@PostMapping("/pass-change")
	public String verifyAndChangePassword(@RequestParam("oldPassword")String oldPassword,@RequestParam("newPassword")String newPassword,
			Principal principal, HttpSession session) {
		System.out.println(oldPassword);
		System.out.println(newPassword);
		String email = principal.getName();
		User user = this.userRepository.getUserByUserName(email);
		if(this.bPasswordEncoder.matches(oldPassword, user.getPassword())) {
			user.setPassword(this.bPasswordEncoder.encode(newPassword));
			this.userRepository.save(user);
			session.setAttribute("message", new Message("Your Password has been changed successfully", "success"));
		}
		else {
			session.setAttribute("message", new Message("Your Old Password did not match", "danger"));
			return "normal/changepassword";
		}
			
		return "redirect:/smartContactManager/user/index";
	}
	
	
	
}
