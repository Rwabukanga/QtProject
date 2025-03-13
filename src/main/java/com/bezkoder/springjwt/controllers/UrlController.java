package com.bezkoder.springjwt.controllers;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.UrlRequestDto;
import com.bezkoder.springjwt.models.Urls;
import com.bezkoder.springjwt.repository.UrlRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.services.UrlService;


@RestController
@RequestMapping("/url")
@CrossOrigin
public class UrlController {

	private static final Logger logger = LoggerFactory.getLogger(UrlController.class);
	
	@Autowired
	UrlRepository urlRepository;
	
	 private final UrlService urlService;

	    public UrlController(UrlService urlService) {
	        this.urlService = urlService;
	    }
	    
	    
//	    @PostMapping("/shorten")
//	    public ResponseEntity<Map<String, String>> createUrl(@RequestBody Map<String, String> request) {
//	        String longUrl = request.get("longUrl");
//	        Long userId = Long.parseLong(request.get("userId"));
//	        Urls url = urlService.createUrl(longUrl, userId);
//	        return ResponseEntity.ok(Collections.singletonMap("shortCode", url.getShortCode()));
//	    }
	    
	    @PostMapping("/shorten")
	    public ResponseEntity<Map<String, String>> createUrl(@RequestBody UrlRequestDto request) {


	        try {
	            // Create shortened URL
	            Urls url = urlService.createUrl(request.getLongUrl(), request.getUserId());
	            return ResponseEntity.ok(Collections.singletonMap("shortCode", url.getShortCode()));
	        } catch (IllegalArgumentException ex) {
	            logger.error("Error occurred while creating URL: {}", ex.getMessage());
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.singletonMap("error", ex.getMessage()));
	        } catch (Exception ex) {
	            logger.error("Unexpected error occurred: {}", ex.getMessage());
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "Internal server error"));
	        }
	    }

	    // Exception handler for NumberFormatException
	    @ExceptionHandler(NumberFormatException.class)
	    public ResponseEntity<Map<String, String>> handleNumberFormatException(NumberFormatException ex) {
	        logger.error("Number format error: {}", ex.getMessage());
	        return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid user ID format"));
	    }
	

	    @GetMapping("/{shortCode}")
	    public ResponseEntity<Object> redirectToLongUrl(@PathVariable String shortCode) {
	        Optional<Urls> urlOptional = urlService.getUrlByShortCode(shortCode);
	        return urlOptional.map((Urls url) -> 
            ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(url.getLongUrl()))
                .build())
            .orElseGet(() -> ResponseEntity.notFound().build());
	    }
	    
	    @GetMapping("/all")
		public List<Urls> getUrls() {
			return urlRepository.findAll();
		}

}
