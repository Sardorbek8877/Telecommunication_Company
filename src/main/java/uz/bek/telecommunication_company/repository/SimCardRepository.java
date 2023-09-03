package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.enums.RoleName;

import java.util.Optional;

public interface SimCardRepository extends JpaRepository<SimCard, Integer> {

    Optional<SimCard> findBySimCardNumber(String simCardNumber);

    Optional<SimCard> findByCodeAndNumber(String code, String number);

    boolean existsByNumberAndCode(String number, String code);

}
