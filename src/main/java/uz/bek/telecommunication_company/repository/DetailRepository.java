package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Details;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.enums.ActionType;

import java.util.List;
import java.util.UUID;

public interface DetailRepository extends JpaRepository<Details, UUID> {

    List<Details> findAllByActionTypeAndSimCard(ActionType actionType, SimCard simCard);

    List<Details> findAllBySimCard(SimCard simCard);
}
