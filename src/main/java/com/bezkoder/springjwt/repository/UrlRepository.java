package com.bezkoder.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.models.Urls;


@Repository
public interface UrlRepository extends JpaRepository<Urls, Long> {

	Optional<Urls> findByShortCode(String shortCode); 

}
