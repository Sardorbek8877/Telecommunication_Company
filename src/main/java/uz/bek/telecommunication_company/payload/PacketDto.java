package uz.bek.telecommunication_company.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PacketDto {

    private Integer packetTypeId;
    @NotNull
    @Column(unique = true)
    private String name;
    private int amount;
    private int cost;
    private int duration;
    private boolean isTariff;
    private List<String> availableTariffs;

}
