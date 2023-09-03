package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Filial;
import uz.bek.telecommunication_company.entity.Packet;
import uz.bek.telecommunication_company.entity.enums.PacketType;

import java.util.Optional;

public interface PacketRepository extends JpaRepository<Packet, Integer> {

    boolean existsByName(String name);

    Packet findByName(String name);

    Optional<Packet> findByPacketTypeAndAmount(PacketType packetType, int amount);
}
