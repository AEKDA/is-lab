package islab1.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import islab1.mappers.HistoryImportMapper;
import islab1.models.HistoryImport;
import islab1.models.DTO.HistoryImportDTO;
import islab1.models.auth.User;
import islab1.repos.HistoryImportRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HistoryImportService {
    @Autowired
    private final HistoryImportMapper historyImportMapper;
    @Autowired
    private final HistoryImportRepo historyImportRepo;

    public List<HistoryImport> getAllHistoryImport() {
        return historyImportRepo.findAll();
    }

    public List<HistoryImport> getHistoryImportByUser(User user) {
        return historyImportRepo.getHistoryImportByCreator(user);
    }

    public  List<HistoryImportDTO> convertHistoryImportToDTOs(List<HistoryImport> list) {
        return historyImportMapper.toDtos(list);
    }
}
