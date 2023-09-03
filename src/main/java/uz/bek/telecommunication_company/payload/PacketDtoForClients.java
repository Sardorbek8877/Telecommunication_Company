package uz.bek.telecommunication_company.payload;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import uz.bek.telecommunication_company.entity.enums.PacketType;

import java.util.List;

@Data
public class PacketDtoForClients {

    private PacketType packetType;
    private String name;
    private int amount;
    private int cost;
    private int duration;
    private boolean isTariff;
    private List<String> availableTariffs;

}
