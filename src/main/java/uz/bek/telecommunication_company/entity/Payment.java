package uz.bek.telecommunication_company.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uz.bek.telecommunication_company.entity.enums.PayType;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

@Data
@Entity
public class Payment extends AbsEntity {

    @OneToOne
    private SimCard simCard;

    @Enumerated(EnumType.STRING)
    private PayType payType;

    private double amount;

    private String payerName;

    private String payerId;
}
