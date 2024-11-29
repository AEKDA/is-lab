package islab1.repos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import islab1.models.Worker;
import islab1.models.auth.User;

@Repository
public interface  WorkerRepo extends JpaRepository<Worker, Long>{
    List<Worker> getWorkerByCreator(User user);
}
