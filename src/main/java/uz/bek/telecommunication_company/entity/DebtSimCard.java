package uz.bek.telecommunication_company.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class DebtSimCard extends AbsEntity {

    @ManyToOne
    private SimCard simCard;

    private Timestamp expireDate;

    private double amount;
}
