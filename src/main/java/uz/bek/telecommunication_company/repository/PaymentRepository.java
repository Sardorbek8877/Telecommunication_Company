package uz.bek.telecommunication_company.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.bek.telecommunication_company.entity.Packet;
import uz.bek.telecommunication_company.entity.Payment;
import uz.bek.telecommunication_company.entity.enums.PacketType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
    List<Payment> findAllBySimCardNumber(String simCardNumber);
}
