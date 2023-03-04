package com.example.smartcontactmanagerboot.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.smartcontactmanagerboot.dao.UserRepository;
import com.example.smartcontactmanagerboot.pojo.User;

public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = userRepository.getUserByUserName(username);
		if(user==null) {
			throw new UsernameNotFoundException("Could not find user!");
		}
		
		CustomUserDetails cUserDetails = new CustomUserDetails(user);
		
		return cUserDetails;
	}
	
}
