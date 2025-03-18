package com.qt.springjwt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.qt.springjwt.models.Registrar;
import com.qt.springjwt.models.User;

@Repository
public interface RegistrarRepository extends JpaRepository<Registrar, Long> {
	
	Optional<Registrar> findById(long id);
	
	Optional<Registrar> findByUsername(String username);


 

}
