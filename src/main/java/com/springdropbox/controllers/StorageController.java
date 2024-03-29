package com.springdropbox.controllers;

import com.springdropbox.entities.File;
import com.springdropbox.entities.User;
import com.springdropbox.exceptions.StorageFileNotFoundException;
import com.springdropbox.services.FileService;
import com.springdropbox.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class StorageController {
    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public String showUserStorage(@PathVariable String username,
                                  @ModelAttribute("file") File file,
                                  Model model) {
        String authUserName = getAuthenticatedUserName();
        if (authUserName.equals(username)) {
            User user = userService.findByUserName(username);
            List<File> files = fileService.findAllFilesByUserId(user.getId());
            model.addAttribute("username", username);
            model.addAttribute("files", files);
            return "user-storage";
        } else {
            throw new SecurityException("You don't have permissions to access on this page MFC");
        }
    }

    private String getAuthenticatedUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            return currentUserName;
        }
        return null;
    }

    @PostMapping("/{username}")
    public String handleFileUpload(@PathVariable String username,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        fileService.store(file);
        redirectAttributes.addFlashAttribute(
                "message",
                "You successfully uploaded " + file.getOriginalFilename() + "!"
        );

        return "redirect:/" + username;
    }

    @GetMapping("/{username}/{filename:.+}/delete")
    public String deleteFile(@PathVariable String filename,
                             @PathVariable String username,
                             RedirectAttributes redirectAttributes) {

        fileService.deleteFilesByPath(filename);
        redirectAttributes.addFlashAttribute(
                "message",
                "You successfully delete " + filename + "!"
        );

        return "redirect:/" + username;
    }

    @GetMapping("/{username}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename,
                                              @PathVariable String username) {
        java.io.File fileLocation = new java.io.File("./storage/" + username + "/" + filename);

        Resource resource = fileService.loadAsResource(filename);
        return ResponseEntity.ok().header(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + resource.getFilename() + "\""
        ).body(resource);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }
}
