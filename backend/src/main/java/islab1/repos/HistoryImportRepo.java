package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import islab1.models.HistoryImport;
import islab1.models.auth.User;

public interface HistoryImportRepo extends JpaRepository<HistoryImport,Long> {
    List<HistoryImport> getHistoryImportByCreator(User user);
}
