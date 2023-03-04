package com.example.smartcontactmanagerboot.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.smartcontactmanagerboot.dao.ContactRepository;
import com.example.smartcontactmanagerboot.dao.UserRepository;
import com.example.smartcontactmanagerboot.pojo.Contact;

@RestController
public class SearchController {
		
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ContactRepository contactRepository;
	
	@GetMapping("/search/{query}")
	public ResponseEntity<?> search(@PathVariable("query")String query , Principal principal){
		System.out.println(query);
		List<Contact> contacts = this.contactRepository.findByNameContainingAndUser(query, this.userRepository.getUserByUserName(principal.getName()));
		return ResponseEntity.ok(contacts);
	}
}
