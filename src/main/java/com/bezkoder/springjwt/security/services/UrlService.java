package com.bezkoder.springjwt.security.services;

import java.util.Optional;

import com.bezkoder.springjwt.models.Urls;



public interface UrlService {

	
    Urls createUrl(String longUrl, Long userId);
	
	Optional<Urls> getUrlByShortCode(String shortCode);
}
