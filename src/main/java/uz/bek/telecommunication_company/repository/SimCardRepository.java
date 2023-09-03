package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.enums.RoleName;

public interface SimCardRepository extends JpaRepository<SimCard, Integer> {

}
