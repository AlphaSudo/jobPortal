package com.fullmoon.jopportal.controller;

import com.fullmoon.jopportal.entity.JobSeeker;
import com.fullmoon.jopportal.entity.Recruiter;
import com.fullmoon.jopportal.entity.Skills;
import com.fullmoon.jopportal.entity.User;
import com.fullmoon.jopportal.repository.JobSeekerRepository;
import com.fullmoon.jopportal.repository.RecruiterRepository;
import com.fullmoon.jopportal.repository.SkillsRepository;
import com.fullmoon.jopportal.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import net.coobird.thumbnailator.Thumbnails;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class ProfileController {
    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;
    private final SkillsRepository skillsRepository;
    private final RecruiterRepository recruiterRepository;

    public ProfileController(JobSeekerRepository jobSeekerRepository, UserRepository userRepository,SkillsRepository skillsRepository,RecruiterRepository recruiterRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.userRepository = userRepository;
        this.skillsRepository=skillsRepository;
        this.recruiterRepository=recruiterRepository;
        System.out.println("ProfileController initialized");

    }



    @GetMapping("/job-seeker-profile")
    public String showProfileForm(Model model) {
        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setSkills(new ArrayList<>()); // Initialize the skills list
        model.addAttribute("jobSeeker", jobSeeker);
        System.out.println("Job Seeker profile form displayed");
        return "job-seeker-profile";
    }

    @PostMapping("/job-seeker-profile/addNew")
    public String addNewJobSeeker(
            @ModelAttribute("jobSeeker") JobSeeker jobSeeker,
            @RequestParam("uploadedImageBase64") String uploadedImageBase64,
            RedirectAttributes redirectAttributes,
            BindingResult result,
            org.springframework.security.core.Authentication authentication
          , HttpSession session, Model model
    ) {

        System.out.println("Entering addNewJobSeeker method");

        if (result.hasErrors()) {
            System.out.println("Binding result has errors: " + result.getAllErrors());
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid data.");
            return "job-seeker-profile";
        }

        try {
            // Handle profile picture upload
            if (!jobSeeker.getProfilePicture().isEmpty()) {
                System.out.println("Profile picture file is not empty");
                String[] paths = saveFile(jobSeeker.getProfilePicture());
                jobSeeker.setPhoto(paths[0]);

//                jobSeeker.setThumbnail(paths[1]);
            }

            // Handle resume upload
            if (!jobSeeker.getResume().isEmpty()) {
                System.out.println("Resume file is not empty");
                String resumePath = saveFile(jobSeeker.getResume())[0];
                jobSeeker.setResumePath(resumePath);
            }
        } catch (IOException e) {
            System.out.println("IOException occurred while uploading files " + e);
            redirectAttributes.addFlashAttribute("errorMessage", "File upload failed.");
            return "job-seeker-profile";
        }

        // Get authenticated user
        org.springframework.security.core.userdetails.User springUser =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String email = springUser.getUsername();
        System.out.println("Authenticated user email: " + email);
        User authenticatedUser = userRepository.findByEmail(email);
        int isFirstTimeProfileSetup = 1;
        authenticatedUser.setProfiled(1);
        jobSeeker.setFinished(0);
        // Set the start date for the jobSeeker
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        jobSeeker.setStartDate(formatter.format(new Date()));

        userRepository.save(authenticatedUser);

        jobSeeker.setUser(authenticatedUser);
        // Ensure skills list is initialized and add a default skill if null
        if (jobSeeker.getSkills() == null) {
            jobSeeker.setSkills(new ArrayList<>());
            jobSeeker.getSkills().add(new Skills("Default Skill", "Description", "Level", jobSeeker));
        }
        // Save skills to this jobSeeker using an iterator
        List<Skills> skills = new ArrayList<>(jobSeeker.getSkills());
        try {
            for (Skills skill : skills) {
                skill.setJobSeeker(jobSeeker);
                skillsRepository.save(skill);
            }
        } catch (Exception e) {
            System.out.println("Exception occurred while saving skills: " + e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to save skills.");
            return "job-seeker-profile";
        }
        jobSeekerRepository.save(jobSeeker);

//        // Reset session timeout to default (e.g., 30 minutes) after the first session
//        session.setMaxInactiveInterval(30 * 60);

        redirectAttributes.addFlashAttribute("successMessage", "Job Seeker profile created successfully.");
        redirectAttributes.addFlashAttribute("uploadedImageBase64", uploadedImageBase64);
        redirectAttributes.addFlashAttribute("isFirstTimeProfileSetup", isFirstTimeProfileSetup);
        // Store base64 image and timestamp in session
        session.setAttribute("uploadedImageBase64", uploadedImageBase64);
        session.setAttribute("profileCompletionTime", System.currentTimeMillis());

//        model.addAttribute("uploadedImageBase64", uploadedImageBase64);
//        model.addAttribute("isFirstTimeProfileSetup", isFirstTimeProfileSetup);
//        System.out.println("Exiting addNewJobSeeker method");
        model.addAttribute("jobSeeker", jobSeeker);
        return "redirect:/explore";
    }

    private String[] saveFile(MultipartFile file) throws IOException {
        logger.info("Entering saveFile method with file: {}", file.getOriginalFilename());
        String folder = "src/main/resources/static/uploads/";

        // Ensure the folder exists
        Path uploadDir = Paths.get(folder);
        if (!Files.exists(uploadDir)) {
            logger.info("Upload directory does not exist, creating it");
            Files.createDirectories(uploadDir);
        }

        // Sanitize file name
        String originalFilename = file.getOriginalFilename();
        String sanitizedFileName = (originalFilename != null) ? Paths.get(originalFilename).getFileName().toString() : "default_filename";
        // Save the original file
        Path originalPath = uploadDir.resolve(sanitizedFileName);
        if (Files.exists(originalPath)) {
            logger.info("File already exists at path: {}", originalPath);
            // Convert file to Base64
            byte[] fileContent = Files.readAllBytes(originalPath);
            String base64String = Base64.getEncoder().encodeToString(fileContent);
            return new String[]{"/uploads/" + sanitizedFileName,base64String};
        }
        //Files.copy is blocking, so it's better to use it in a separate thread
        try {
            Files.copy(file.getInputStream(), originalPath);
            logger.info("Original file saved at path: {}", originalPath);
        } catch (IOException e) {
            logger.error("Failed to save file at path: {}", originalPath, e);
        }
        // Save the thumbnail
//        String thumbnailFileName = "thumb_" + sanitizedFileName;
//        Path thumbnailPath = uploadDir.resolve(thumbnailFileName);
//        Thumbnails.of(file.getInputStream()).size(150, 150).toFile(thumbnailPath.toFile());
//        logger.info("Thumbnail saved at path: {}", thumbnailPath.toString());

        // Convert file to Base64
        byte[] fileContent = Files.readAllBytes(originalPath);
        String base64String = Base64.getEncoder().encodeToString(fileContent);

        logger.info("Exiting saveFile method");
//        return new String[]{"/uploads/" + sanitizedFileName, "/uploads/" + thumbnailFileName};
//        return new String[]{"/uploads/" + sanitizedFileName};
        return new String[]{"/uploads/" + sanitizedFileName, base64String};

    }

    private boolean isValidResumeFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && contentType.equals("application/pdf");
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "File size exceeds the maximum limit!");
        return "redirect:/job-seeker-profile";
    }

    @PostMapping("/upload")
    @ResponseBody
    public Map<String, String> handleFileUpload(@RequestParam("profilePicture") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            String[] paths = saveFile(file);
            response.put("filePath", paths[0]);
            response.put("base64", paths[1]);
//            response.put("thumbnailPath", paths[1]);
        } catch (IOException e) {
            response.put("error", "File upload failed: " + e.getMessage());
        }
        return response;
    }

//    @PostMapping("/upload2")
//    @ResponseBody
//    public Map<String, String> handleFileUpload2(@RequestParam("profilePicture") MultipartFile file) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            String[] paths = saveFile(file);
//            response.put("filePath", paths[0]);
//            response.put("base64", paths[1]);
//        } catch (IOException e) {
//            response.put("error", "File upload failed: " + e.getMessage());
//        }
//        return response;
//    }

    @GetMapping("/recruiter-profile")
    public String showRecruiterProfileForm(Model model) {
        Recruiter recruiter = new Recruiter();
        model.addAttribute("recruiter", recruiter);
        return "set-recruiter-profile";
    }

    @PostMapping("/recruiter-profile/addNew")
    public String addNewRecruiter(@ModelAttribute("recruiter") Recruiter recruiter, RedirectAttributes redirectAttributes, BindingResult result, org.springframework.security.core.Authentication authentication,Model model) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid data.");
            return "set-recruiter-profile";
        }
        System.out.println("Entering addNewRecruiter method");
try{
        // Handle profile picture upload
        if (recruiter.getProfilePicture() != null && !recruiter.getProfilePicture().isEmpty()) {
            System.out.println("Profile picture file is not empty");
            try {
                String[] paths = saveFile(recruiter.getProfilePicture());
                recruiter.setProfilePic(paths[0]);
            } catch (IOException e) {
                System.out.println("IOException occurred while uploading files " + e);
                redirectAttributes.addFlashAttribute("errorMessage", "File upload failed.");
                return "set-recruiter-profile";
            }
        }}
        catch (Exception e){
            System.out.println("Exception occurred while uploading files " + e);
            redirectAttributes.addFlashAttribute("errorMessage", "File upload failed.");
            return "set-recruiter-profile";
        }

        // Get authenticated user
        org.springframework.security.core.userdetails.User springUser = (org.springframework.security.core.userdetails.User) authentication.getPrincipal();
        String email = springUser.getUsername();
        System.out.println("Authenticated user email: " + email);

        User authenticatedUser = userRepository.findByEmail(email);

        authenticatedUser.setProfiled(1);
        recruiter.setFinished(0);
        // Set the start date for the jobSeeker
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        recruiter.setStartDate(formatter.format(new Date()));
        userRepository.save(authenticatedUser);
        recruiter.setUser(authenticatedUser);
        recruiterRepository.save(recruiter);

        redirectAttributes.addFlashAttribute("successMessage", "Job Seeker profile created successfully.");
        model.addAttribute("recruiter", recruiter);
        return "redirect:/explore";

    }
}