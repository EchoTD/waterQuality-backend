package project.waterQuality.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import project.waterQuality.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	User findByUsername(String username);
}
