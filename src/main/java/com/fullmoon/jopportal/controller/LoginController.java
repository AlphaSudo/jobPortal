package com.fullmoon.jopportal.controller;

import com.fullmoon.jopportal.entity.JobSeeker;
import com.fullmoon.jopportal.entity.Recruiter;
import com.fullmoon.jopportal.entity.User;
import com.fullmoon.jopportal.repository.JobSeekerRepository;
import com.fullmoon.jopportal.repository.RecruiterRepository;
import com.fullmoon.jopportal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.logging.Logger;

@Controller
public class LoginController {
    private final UserRepository userRepository;
//    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JobSeekerRepository jobSeekerRepository;
    private final RecruiterRepository recruiterRepository;

    private static final Logger logger = Logger.getLogger(LoginController.class.getName());

    @Autowired
    public LoginController(UserRepository userRepository /*,AuthenticationManager authenticationManager*/, PasswordEncoder passwordEncoder, JobSeekerRepository jobSeekerRepository, RecruiterRepository recruiterRepository) {
        this.userRepository = userRepository;
//        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.jobSeekerRepository = jobSeekerRepository;
        this.recruiterRepository = recruiterRepository;
    }

    @GetMapping("/home")
    public String home() {
        return "home";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register/new")
    public String registerNew(@Valid @ModelAttribute("user") User user, RedirectAttributes redirectAttributes, BindingResult result) {
        logger.info("User: " + user);
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Registration failed! Please correct the errors and try again.");
            return "redirect:/register";
        }
        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Email already in use! Please try a different email.");
            return "redirect:/register";
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(true);
        user.setRegDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        user.setProfiled(0);
        userRepository.save(user);
        redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please log in.");
        return "redirect:/login";
    }

    @GetMapping("/home2")
    public String home2() {
        return "home2";
    }

    @GetMapping("/home3")
    public String home3() {
        return "home3";
    }


//    @GetMapping("/explore")
//    public String explore(Model model, Authentication authentication) {
//        org.springframework.security.core.userdetails.User springUser =
//                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
//        String email = springUser.getUsername();
//        User authenticatedUser = userRepository.findByEmail(email);
//
//        if (authenticatedUser == null) {
//            logger.severe("Authenticated user not found in the database.");
//            return "redirect:/login";
//        }
//
//        if (authenticatedUser.getProfiled() == 0) {
//            return authenticatedUser.getUserType().equals("ROLE_RECRUITER") ? "redirect:/recruiter-profile" : "redirect:/job-seeker-profile";
//        }
//
//        model.addAttribute("authenticatedUser", authenticatedUser);
//
//        if (authenticatedUser.getUserType().equals("ROLE_SEEKER")) {
//            handleJobSeekerProfile(model, authenticatedUser);
//        } else if (authenticatedUser.getUserType().equals("ROLE_RECRUITER")) {
//            handleRecruiterProfile(model, authenticatedUser);
//        }
//
//        return "explore";
//    }
//
//    private void handleJobSeekerProfile(Model model, User authenticatedUser) {
//        JobSeeker jobSeeker = jobSeekerRepository.findByUser(authenticatedUser);
//        model.addAttribute("jobSeeker", jobSeeker);
//
//        addCommonAttributes(model);
//
//        if (jobSeeker.getFinished() == 0) {
//            processProfileCompletion(model, jobSeeker.getStartDate(), jobSeeker.getPhoto(), "base64");
//            jobSeeker.setFinished(1);
//            jobSeekerRepository.save(jobSeeker);
//        }
//    }
//
//    private void handleRecruiterProfile(Model model, User authenticatedUser) {
//        Recruiter recruiter = recruiterRepository.findByUser(authenticatedUser);
//        model.addAttribute("recruiter", recruiter);
//
//        addCommonAttributes(model);
//
//        if (recruiter.getFinished() == 0) {
//            processProfileCompletion(model, recruiter.getStartDate(), recruiter.getProfilePic(), "base64q");
//            recruiter.setFinished(1);
//            recruiterRepository.save(recruiter);
//        }
//    }
//
//    private void addCommonAttributes(Model model) {
//        model.addAttribute("isFirstTimeProfileSetup", model.asMap().get("isFirstTimeProfileSetup"));
//        model.addAttribute("uploadedImageBase64", model.asMap().get("uploadedImageBase64"));
//        model.addAttribute("successMessage", model.asMap().get("successMessage"));
//    }
//
//    private void processProfileCompletion(Model model, String startDate, String photoPath, String base64AttributeName) {
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//        LocalDateTime profileCompletionDateTime = LocalDateTime.parse(startDate, formatter);
//        LocalDateTime now = LocalDateTime.now();
//        long minutes = java.time.Duration.between(profileCompletionDateTime, now).toMinutes();
//
//        if (minutes < 3) {
//            String base64 = convertPhotoToBase64(photoPath);
//            model.addAttribute(base64AttributeName, base64);
//            model.addAttribute("notDoneYet", 1);
//        }
//        model.addAttribute("notDoneYet", 0);
//    }
//
//    private String convertPhotoToBase64(String photoPath) {
//        if (photoPath == null) {
//            return "";
//        }
//
//        File file = new File(photoPath);
//        if (!file.exists()) {
//            String absolutePath = "C:\\Users\\CM\\IdeaProjects\\JopPortal\\src\\main\\resources\\static\\uploads\\" + new File(photoPath).getName();
//            file = new File(absolutePath);
//        }
//
//        if (file.exists()) {
//            try {
//                byte[] fileContent = Files.readAllBytes(file.toPath());
//                return Base64.getEncoder().encodeToString(fileContent);
//            } catch (IOException e) {
//                logger.severe("IOException occurred while converting photo to base64: " + e);
//            }
//        }
//
//        return "";
//    }
    @GetMapping("/explore")
    public String explore(Model model, Authentication authentication) {
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String email = springUser.getUsername();
        User authenticatedUser = userRepository.findByEmail(email);

        if (authenticatedUser == null) {
            logger.severe("Authenticated user not found in the database.");
            return "redirect:/login";
        }

        if (authenticatedUser.getProfiled() == 0) {
            if (authenticatedUser.getUserType().equals("ROLE_RECRUITER")) {
                return "redirect:/recruiter-profile";
            } else if (authenticatedUser.getUserType().equals("ROLE_SEEKER")) {
                return "redirect:/job-seeker-profile";
            }
        } else {
            System.out.println(authenticatedUser.getUserType());
            model.addAttribute("authenticatedUser", authenticatedUser);
            Object successMessage = model.asMap().get("successMessage");





            model.addAttribute("successMessage", successMessage);
            System.out.println("Step 1");
            if (authenticatedUser.getUserType().equals("ROLE_SEEKER")) {
                JobSeeker jobSeeker = jobSeekerRepository.findByUser(authenticatedUser);

                Recruiter recruiter = new Recruiter();
                recruiter.setProfilePic("profilePic");
                model.addAttribute("recruiter", recruiter);

                model.addAttribute("jobSeeker", jobSeeker );
                Object isFirstTimeProfileSetup = model.asMap().get("isFirstTimeProfileSetup");
                model.addAttribute("isFirstTimeProfileSetup", isFirstTimeProfileSetup);
                System.out.println("Step 2");
                Object uploadedImageBase64 = model.asMap().get("uploadedImageBase64");
                model.addAttribute("uploadedImageBase64", uploadedImageBase64);


                System.out.println("Step 3");
                // Retrieve the session attributes


                System.out.println("Step 4");

                if (jobSeeker.getFinished() == 0) {
                    System.out.println("Step 5");

                    System.out.println("base 64 may be produced");
                // Check if 2 minutes have passed since the user first created their profile
                String profileCompletionTime = jobSeeker.getStartDate();
                    System.out.println("Step 6");

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime profileCompletionDateTime = LocalDateTime.parse(profileCompletionTime, formatter);
                LocalDateTime now = LocalDateTime.now();
                long minutes = java.time.Duration.between(profileCompletionDateTime, now).toMinutes();
                    System.out.println("Step 7");

                    if (minutes < 3) {
                    System.out.println("2 minutes have not passed yet");
                        System.out.println("Step 8");

                        String originalPhoto = jobSeeker.getPhoto();
                        System.out.println("Step 9");

                        // Convert the photo to base64
                    String base64 = "";
                    if (originalPhoto != null) {
                        File file = new File(originalPhoto); // Try with relative path first
                        System.out.println("Trying relative path: " + file.getAbsolutePath());
                        System.out.println("Step 10");


                        if (!file.exists()) {
                            // Log failure and attempt with an absolute path
                            logger.warning("File not found at relative path: " + file.getAbsolutePath());
                            System.out.println("Step 11");


                            // Replace with the correct absolute path to your file or directory
                            String absolutePath = "C:\\Users\\CM\\IdeaProjects\\JopPortal\\src\\main\\resources\\static\\uploads\\" + new File(originalPhoto).getName();
                            file = new File(absolutePath);
                            System.out.println("Trying absolute path: " + file.getAbsolutePath());
                            System.out.println("Step 12");


                            if (!file.exists()) {
                                System.out.println("Step 13");

                                // Log failure if the absolute path also fails
                                logger.severe("File not found at absolute path: " + file.getAbsolutePath());
                            }
                        }

                        if (file.exists()) {
                            try {
                                // Read and encode file if it exists
                                System.out.println("Step 14");

                                byte[] fileContent = Files.readAllBytes(file.toPath());
                                base64 = Base64.getEncoder().encodeToString(fileContent);
                                System.out.println("Base64 has been produced");
                            } catch (IOException e) {
                                logger.severe("IOException occurred while converting photo to base64: " + e);
                            }
                        }
                    }


                    int notDoneYet = 1;
                        System.out.println("Step 15");

                        model.addAttribute("base64", base64);
                    model.addAttribute("notDoneYet", notDoneYet);
                        System.out.println("Step 16");

                    } else {
                        System.out.println("Step 17");
                        int notDoneYet = 0;
                        model.addAttribute("notDoneYet", notDoneYet);



                        jobSeeker.setFinished(1);
                    jobSeekerRepository.save(jobSeeker);
                        System.out.println("Step 18");

                    }
            }
        } else if (authenticatedUser.getUserType().equals("ROLE_RECRUITER")) {
                System.out.println("#Step 2");
                JobSeeker jobSeeker= new JobSeeker();
                jobSeeker.setPhoto("photo");
                model.addAttribute("jobSeeker", jobSeeker);

                Recruiter recruiter = recruiterRepository.findByUser(authenticatedUser);
                model.addAttribute("recruiter", recruiter);


                System.out.println("#Step 3");

                if (recruiter.getFinished() == 0) {
                    System.out.println("base 64 may be produced");
                    System.out.println("#Step 4");
                    // Check if 2 minutes have passed since the user first created their profile
                    String profileCompletionTime = recruiter.getStartDate();
                    System.out.println("#Step 5");
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime profileCompletionDateTime = LocalDateTime.parse(profileCompletionTime, formatter);
                    LocalDateTime now = LocalDateTime.now();
                    long minutes = java.time.Duration.between(profileCompletionDateTime, now).toMinutes();
                    System.out.println("#Step 6");
                    if (minutes < 3) {
                        System.out.println("2 minutes have not passed yet");
                        String originalPhoto = recruiter.getProfilePic();
                        System.out.println("#Step 7 ");
                        // Convert the photo to base64
                        String base64q = "";
                        if (originalPhoto != null) {
                            System.out.println("#Step 8");
                            File file = new File(originalPhoto); // Try with relative path first
                            System.out.println("Trying relative path: " + file.getAbsolutePath());
                            System.out.println("#Step 9");

                            if (!file.exists()) {
                                // Log failure and attempt with an absolute path
                                logger.warning("File not found at relative path: " + file.getAbsolutePath());
                                System.out.println("#Step 10");

                                // Replace with the correct absolute path to your file or directory
                                String absolutePath = "C:\\Users\\CM\\IdeaProjects\\JopPortal\\src\\main\\resources\\static\\uploads\\" + new File(originalPhoto).getName();
                                file = new File(absolutePath);
                                System.out.println("Trying absolute path: " + file.getAbsolutePath());
                                System.out.println("#Step 11");

                                if (!file.exists()) {
                                    // Log failure if the absolute path also fails
                                    logger.severe("File not found at absolute path: " + file.getAbsolutePath());
                                }
                            }

                            if (file.exists()) {
                                try {
                                    System.out.println("#Step 12");
                                    // Read and encode file if it exists
                                    byte[] fileContent = Files.readAllBytes(file.toPath());
                                    base64q = Base64.getEncoder().encodeToString(fileContent);
                                    System.out.println("Base64 has been produced");
                                    System.out.println("#Step 13");
                                } catch (IOException e) {
                                    logger.severe("IOException occurred while converting photo to base64: " + e);
                                }
                            }
                        }
                        System.out.println("#Step 14");
                        int notDoneYet = 1;
                        model.addAttribute("base64q", base64q);
                        model.addAttribute("notDoneYet", notDoneYet);
                        System.out.println("#Step 15");
                    }
                } else {
                    int notDoneYet = 0;
                    model.addAttribute("notDoneYet", notDoneYet);
                    System.out.println("#Step 16");
                    recruiter.setFinished(1);
                    recruiterRepository.save(recruiter);
                    System.out.println("#Step 17");
                }
                System.out.println("#Step f1");
            }
            System.out.println("#Step f2");
        } System.out.println("#Step f3");
        return "explore";
    }

}