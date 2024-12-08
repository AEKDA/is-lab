package islab1.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import islab1.auth.services.AuthenticationService;
import islab1.models.HistoryImport;
import islab1.models.DTO.HistoryImportDTO;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.services.HistoryImportService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/history")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class HistoryImportController {

    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final HistoryImportService historyImportService;

    @GetMapping
    public ResponseEntity<List<HistoryImportDTO>> getAllHistoryImport() {
        User user = authenticationService.getCurrentUser();
        List<HistoryImport> history = new ArrayList<>();
        if (user.getRole() == Role.ADMIN) {
            history = historyImportService.getAllHistoryImport();
        } else {
            history = historyImportService.getHistoryImportByUser(user);
        }
        List<HistoryImportDTO> HistoryImportDTOs = historyImportService.convertHistoryImportToDTOs(history);
        return ResponseEntity.ok(HistoryImportDTOs);
    }
}
