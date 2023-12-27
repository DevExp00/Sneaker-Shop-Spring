package com.marketplace.demo.controllers;

import com.marketplace.demo.entities.Users;
import com.marketplace.demo.services.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Value("${file.photo.viewPath}")
    private String viewPath;

    @Value("${file.photo.uploadPath}")
    private String uploadPath;

    @Value("${file.photo.defaultPicture}")
    private String defaultPicture;

    @GetMapping(value = "/403")
    public String accessDenied(Model model){
        model.addAttribute("currentUser",getUserData());
        return "403";
    }

    @GetMapping(value = "/login")
    public String login(Model model){
        model.addAttribute("currentUser",getUserData());
        return "login";
    }

    @GetMapping(value = "/login-error")
    public String loginError(Model model){
        model.addAttribute("loginError", true);
        return "login";
    }

    @GetMapping(value = "/profile")
    @PreAuthorize("isAuthenticated()")
    public String profile(Model model){
        model.addAttribute("currentUser",getUserData());
        return "profile";
    }

    @GetMapping(value = "/register")
    public String register(Model model){
        model.addAttribute("currentUser",getUserData());
        return "register";
    }

    @PostMapping(value = "/register")
    public String regin(@RequestParam(name = "user_fullName") String fullName,
                        @RequestParam(name = "user_email") String email,
                        @RequestParam(name = "user_password") String password,
                        @RequestParam(name = "user_repassword") String repassword
                        ){
        if(password.equals(repassword)){
            Users newUser = new Users();
            newUser.setFullname(fullName);
            newUser.setEmail(email);
            newUser.setPassword(password);
            if(userService.createUser(newUser)!=null){
                return "redirect:/register?success";
            }
        }
        return "redirect:/register?error";
    }

    @PostMapping(value = "/uploadimg")
    @PreAuthorize("isAuthenticated()")
    public String upload(@RequestParam(name = "photo") MultipartFile file){

        if(file.getContentType().equals("image/jpeg") || file.getContentType().equals("image/png")){
            try {

                Users currentUser = getUserData();
                String picName = DigestUtils.sha1Hex("avatar_" + currentUser.getId() + "_!Picture");
                byte[] bytes = file.getBytes();
                Path path = Paths.get(uploadPath + picName + ".jpg");
                Files.write(path, bytes);

                currentUser.setUseravatar(picName);
                userService.saveUser(currentUser);

                return "redirect:/profile?success";

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return "redirect:/profile?errorUpload";
    }

    @GetMapping(value = "/viewphoto/{url}", produces = {MediaType.IMAGE_JPEG_VALUE})
    public @ResponseBody byte[] viewProfilePhoto(@PathVariable(name = "url") String url) throws IOException {

        String pictureURL = viewPath+defaultPicture;

        if(url != null){
            pictureURL = viewPath+ url +".jpg";
        }

        InputStream in;

        try {
            ClassPathResource resource = new ClassPathResource(pictureURL);
            in = resource.getInputStream();
        }catch (Exception e){
            ClassPathResource resource = new ClassPathResource(viewPath+defaultPicture);
            in = resource.getInputStream();
            e.printStackTrace();
        }

        return IOUtils.toByteArray(in);

    }

    private Users getUserData(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(!(authentication instanceof AnonymousAuthenticationToken)){
            User secUser = (User) authentication.getPrincipal();
            Users myUser = userService.getUserByEmail(secUser.getUsername());
            return myUser;
        }
        return null;
    }


}
