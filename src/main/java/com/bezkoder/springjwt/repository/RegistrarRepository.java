package com.bezkoder.springjwt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Registrar;
import com.bezkoder.springjwt.models.User;

@Repository
public interface RegistrarRepository extends JpaRepository<Registrar, Long> {
	
	Optional<Registrar> findById(long id);
	
	Optional<Registrar> findByUsername(String username);


 

}
