package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Client;
import uz.bek.telecommunication_company.entity.EntertainingService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<EntertainingService, UUID> {

    Optional<EntertainingService> findByName(String name);
    boolean existsByName(String name);
    List<EntertainingService> findAllByOrderByCountAsc();
}
