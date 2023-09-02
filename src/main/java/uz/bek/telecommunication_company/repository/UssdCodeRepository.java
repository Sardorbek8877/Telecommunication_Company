package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.UssdCode;
import uz.bek.telecommunication_company.entity.enums.RoleName;

public interface UssdCodeRepository extends JpaRepository<UssdCode, Integer> {

    boolean existsByCode(String code);
}
