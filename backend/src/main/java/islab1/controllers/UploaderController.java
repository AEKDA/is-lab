package islab1.controllers;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import islab1.auth.services.AuthenticationService;
import islab1.models.auth.User;
import islab1.services.UploaderService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/uploader")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UploaderController {

    @Autowired
    public final UploaderService uploaderService;

    @Autowired
    private final AuthenticationService authenticationService;

    @PostMapping("/import")
    public ResponseEntity<String> uploadFile(@RequestPart("file") @NotNull MultipartFile file) {
        User user = authenticationService.getCurrentUser();

        try {
            uploaderService.importData(file, user);
            return ResponseEntity.ok("Файл успешно импортирован");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при импорте: " + e.getMessage());
        }
    }
}
