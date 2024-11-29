package islab1.services;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;

import org.springframework.stereotype.Service;

import islab1.exceptions.ConvertionException;
import islab1.mappers.WorkerMapper;
import islab1.models.DTO.WorkerDTO;
import islab1.models.Worker;
import islab1.models.auth.Role;
import islab1.models.auth.User;
import islab1.repos.WorkerRepo;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkerService {

    private final WorkerMapper workerMapper;
    private final WorkerRepo workerRepo;

    public List<Worker> getAllWorkers() {
        return workerRepo.findAll();
    }

    public List<Worker> getWorkersByUser(User user) {
        return workerRepo.getWorkerByCreator(user);
    }

    public Worker getWorkerById(Long id) {
        return workerRepo.getById(id);
    }

    public boolean existById(Long id) {
        return workerRepo.existsById(id);
    }

    public Worker createWorker(WorkerDTO workerDTO) throws ConvertionException {
        Worker worker = workerMapper.toEntity(workerDTO);
        return workerRepo.save(worker);
    }

    public Worker updateWorker(Long id, WorkerDTO workerDTO) throws ConvertionException, EntityNotFoundException {
        Worker newWorker = workerMapper.toEntity(workerDTO);
        if (!existById(id)) {
            throw new EntityNotFoundException("There is no worker with id " + id);
        }
        Worker worker = getWorkerById(id);
        
        newWorker.setId(id);
        newWorker.setCreator(worker.getCreator());
        workerRepo.save(newWorker);
        return newWorker;
    }

    public void deleteWorker(Long id) {
        workerRepo.deleteById(id);
    }

    public boolean checkAccess(User user, Long workerId) {
        if (user.getRole().equals(Role.ADMIN)) {
            return true;
        }
        Worker worker = getWorkerById(workerId);
        return worker != null && worker.getCreator().equals(user);
    }

    public List<WorkerDTO> convertWorkersToDTOs(List<Worker> workers) {
        return workers.stream()
            .map(workerMapper::toDto)
            .collect(Collectors.toList());
    }

    public WorkerDTO convertWorkerToDTO(Worker worker) {
        return workerMapper.toDto(worker);
    }
}
