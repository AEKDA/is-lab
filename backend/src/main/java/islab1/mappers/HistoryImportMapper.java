package islab1.mappers;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import islab1.models.HistoryImport;
import islab1.models.DTO.HistoryImportDTO;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class HistoryImportMapper {
    public List<HistoryImportDTO> toDtos(List<HistoryImport> historyImports) {
        List<HistoryImportDTO> list = new LinkedList<>();

        for (HistoryImport h : historyImports) {
            HistoryImportDTO dto = new HistoryImportDTO();
            dto.setId(h.getId());
            dto.setCreatorId(h.getCreator().getId());
            dto.setCount(h.getCount());
            dto.setCreatedAt(h.getCreatedAt());
            dto.setStatus(h.getStatus());

            list.add(dto);
        }
        return list;
    }
}
