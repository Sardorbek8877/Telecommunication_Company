package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.EntertainingService;
import uz.bek.telecommunication_company.entity.ServiceCategory;

import java.util.List;
import java.util.Optional;

public interface ServiceCategoryRepository extends JpaRepository<ServiceCategory, Integer> {

    boolean existsByName(String name);
}
