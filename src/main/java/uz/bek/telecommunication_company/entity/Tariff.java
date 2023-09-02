package uz.bek.telecommunication_company.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.bek.telecommunication_company.entity.enums.ClientType;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Tariff extends AbsEntity {

    @Column(nullable = false, unique = true)
    private String name;

    private double price;

    private double switchPrice;

    private int expireDate;

    private int mb;
    private int sms;
    private int min;
    private int mbCost;
    private int smsCost;
    private int minCost;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;
}











