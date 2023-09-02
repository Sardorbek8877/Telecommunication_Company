package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.UssdCode;

public interface UssdCodeRepository extends JpaRepository<UssdCode, Integer> {

    boolean existsByCode(String code);
}
