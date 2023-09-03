package uz.bek.telecommunication_company.entity;

import jakarta.persistence.*;
import lombok.Data;
import uz.bek.telecommunication_company.entity.enums.PacketType;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

import java.util.List;

@Data
@Entity
public class Packet extends AbsEntity {

    @Enumerated(EnumType.STRING)
    private PacketType packetType;

    @Column(nullable = false,unique = true)
    private String name;

    private int amount;

    private int cost;

    private int duration;

    private boolean isTariff;

    @OneToMany
    private List<Tariff> availableTariffs;
}
