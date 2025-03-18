package com.qt.springjwt.security.services;

import java.util.Optional;

import com.qt.springjwt.models.Urls;



public interface UrlService {

	
    Urls createUrl(String longUrl, Long userId);
	
	Optional<Urls> getUrlByShortCode(String shortCode);
}
