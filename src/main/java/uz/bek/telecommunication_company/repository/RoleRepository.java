package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.Tariff;
import uz.bek.telecommunication_company.entity.enums.RoleName;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, Integer> {

    Role findByRoleName(RoleName roleName);
}
