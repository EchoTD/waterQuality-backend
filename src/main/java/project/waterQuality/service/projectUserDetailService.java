package project.waterQuality.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.waterQuality.repository.UserRepository;

@Service
public class projectUserDetailService implements UserDetailsService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		project.waterQuality.model.User user = userRepository.findByUsername(username);
		if (user == null)
			throw new UsernameNotFoundException(username);
		return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
	}
}
