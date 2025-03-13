package com.bezkoder.springjwt.controllers;

import java.net.URI;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.Urls;
import com.bezkoder.springjwt.security.services.UrlService;


@RestController
@RequestMapping("/api/urls")
public class UrlController {

	
	
	 private final UrlService urlService;

	    public UrlController(UrlService urlService) {
	        this.urlService = urlService;
	    }
	    
	    
	    @PostMapping("/create")
	    public ResponseEntity<Map<String, String>> createUrl(@RequestBody Map<String, String> request) {
	        String longUrl = request.get("longUrl");
	        Long userId = Long.parseLong(request.get("userId"));
	        Urls url = urlService.createUrl(longUrl, userId);
	        return ResponseEntity.ok(Collections.singletonMap("shortCode", url.getShortCode()));
	    }

	    @GetMapping("/{shortCode}")
	    public ResponseEntity<Object> redirectToLongUrl(@PathVariable String shortCode) {
	        Optional<Urls> urlOptional = urlService.getUrlByShortCode(shortCode);
	        return urlOptional.map((Urls url) -> 
            ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getLong_url()))
                .build())
            .orElseGet(() -> ResponseEntity.notFound().build());
	    }
}
