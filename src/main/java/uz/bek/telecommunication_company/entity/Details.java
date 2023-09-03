package uz.bek.telecommunication_company.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import uz.bek.telecommunication_company.entity.enums.ActionType;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

@Data
@Entity
public class Details extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @ManyToOne
    private SimCard simCard;

    private Float amount;
}
