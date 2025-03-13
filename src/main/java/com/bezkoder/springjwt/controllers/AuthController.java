package com.bezkoder.springjwt.controllers;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.springjwt.models.ERole;
import com.bezkoder.springjwt.models.Registrar;
import com.bezkoder.springjwt.models.Role;
import com.bezkoder.springjwt.models.User;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.request.SignupRequest;
import com.bezkoder.springjwt.payload.response.JwtResponse;
import com.bezkoder.springjwt.payload.response.MessageResponse;
import com.bezkoder.springjwt.repository.RegistrarRepository;
import com.bezkoder.springjwt.repository.RoleRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.jwt.JwtUtils;
import com.bezkoder.springjwt.security.services.UserDetailsImpl;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;
  
  @Autowired
  RegistrarRepository  registrarservice;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
  
  @Autowired
  private JavaMailSender javaMailSender;

  @PostMapping("/login")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();    
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail()));
  }

  @PostMapping("/register")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }


    
    Registrar regg = new Registrar();
    
    regg.setUsername(signUpRequest.getUsername());
    regg.setPassword(signUpRequest.getPassword());
    regg.setEmail(signUpRequest.getEmail());
    
    registrarservice.save(regg);
    
    User user = new User();
    
    user.setUsername(signUpRequest.getUsername());
    user.setEmail(signUpRequest.getEmail());
    user.setCreatedAt(LocalDateTime.now());
    

    
 // Ensure password is not null or empty, then hash it
    if (signUpRequest.getPassword() != null && !signUpRequest.getPassword().isEmpty()) {
        // Hash the password before saving it
        final String hashedPassword = BCrypt.hashpw(signUpRequest.getPassword(), BCrypt.gensalt());
        user.setPassword(hashedPassword);
    } else {
        // Handle the case where password is null or empty
        return ResponseEntity
            .badRequest()
            .body(new MessageResponse("Error: Password cannot be null or empty!"));
    }

    user.setReg(regg);
    
    userRepository.save(user);


    
   
    return ResponseEntity.ok(user);
    //return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  
  @PostMapping("/reset")
  public ResponseEntity<?> restpassword(@RequestBody Registrar user) {
    
	  Optional<Registrar> existuser = registrarservice.findByUsername(user.getUsername());
	  
	  Registrar existinguser = existuser.get();
	  
	  if(existinguser!=null) {
		  
		  SimpleMailMessage mailMessage = new SimpleMailMessage();
          mailMessage.setTo(existinguser.getEmail());
          mailMessage.setSubject("Complete Password Reset!");
          mailMessage.setFrom("sethfils2016@gmail.com");
          mailMessage.setText("To complete the password reset process is: "
            + " "+existinguser.getPassword());
          
          javaMailSender.send(mailMessage);
	  }


   
    //return ResponseEntity.ok(user);
    return ResponseEntity.ok(new MessageResponse("Complete Password Reset!"));
  }
  
  @PostMapping("/changepassowrd")
  public ResponseEntity<?> forgotPassword(@RequestBody SignupRequest user) {
    
	  Optional<User> existuser = userRepository.findByUsername(user.getUsername());
	  Optional<Registrar> reg = registrarservice.findByUsername(user.getUsername());
	  
	  User existinguser = existuser.get();
	  Registrar regg = reg.get();
	  
	  if(regg!=null) {
		  
		  regg.setPassword(user.getPassword());
		  existinguser.setPassword(encoder.encode(user.getPassword()));
		  existinguser.setReg(regg);
		  
		  userRepository.save(existinguser);
	  }


   
    //return ResponseEntity.ok(user);
    return ResponseEntity.ok(new MessageResponse("Complete Changed Successfully!"));
  }
}
