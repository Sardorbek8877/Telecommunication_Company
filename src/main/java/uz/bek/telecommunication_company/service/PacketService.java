package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Packet;
import uz.bek.telecommunication_company.entity.Tariff;
import uz.bek.telecommunication_company.entity.enums.PacketType;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.PacketDto;
import uz.bek.telecommunication_company.payload.PacketDtoForClients;
import uz.bek.telecommunication_company.repository.PacketRepository;
import uz.bek.telecommunication_company.repository.TariffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PacketService {

    @Autowired
    PacketRepository packetRepository;
    @Autowired
    TariffRepository tariffRepository;

    public ApiResponse addPacket(PacketDto packetDto) {
        boolean exists = packetRepository.existsByName(packetDto.getName());
        if (exists)
            return new ApiResponse("Packet already exist", false);
        Packet packet = new Packet();
        packet.setName(packetDto.getName());
        if (packetDto.getPacketTypeId() == 1) {
            packet.setPacketType(PacketType.MB);
        } else if (packetDto.getPacketTypeId() == 2) {
            packet.setPacketType(PacketType.SMS);
        } else if (packetDto.getPacketTypeId() == 3) {
            packet.setPacketType(PacketType.MINUTE);
        } else {
            return new ApiResponse("Wrong packet type id!", false);
        }
        packet.setAmount(packetDto.getAmount());
        packet.setCost(packetDto.getCost());
        packet.setDuration(packetDto.getDuration());
        packet.setTariff(packetDto.isTariff());
        List<Tariff> tariffList = new ArrayList<>();
        List<String> availableTariffs = packetDto.getAvailableTariffs();
        for (String availableTariff : availableTariffs) {
            Tariff tariff = tariffRepository.findByName(availableTariff);
            tariffList.add(tariff);
        }
        packet.setAvailableTariffs(tariffList);
        packetRepository.save(packet);
        return new ApiResponse("Packet saved", true);

    }

    public List<Packet> getAllPackets() {
        return packetRepository.findAll();
    }

    public List<String> getAllPacketsForClients() {
        List<Packet> packetList = packetRepository.findAll();
        List<String> packetsName = new ArrayList<>();
        for (Packet packet : packetList) {
            packetsName.add(packet.getName());
        }
        return packetsName;
    }

    public ApiResponse editPacket(PacketDtoForClients packetDto, UUID id) {
        Optional<Packet> byId = packetRepository.findById(id);
        if (byId.isEmpty())
            return new ApiResponse("Wrong packet id!", false);
        Packet packet = byId.get();
        packet.setName(packetDto.getName());
        packet.setAmount(packetDto.getAmount());
        packet.setCost(packetDto.getCost());
        packet.setDuration(packetDto.getDuration());
        packetRepository.save(packet);
        return new ApiResponse("Packet edited", true);
    }

    public ApiResponse deletePacket(UUID id) {
        Optional<Packet> optionalPacket = packetRepository.findById(id);
        if (optionalPacket.isEmpty())
            return new ApiResponse("Id is wrong!", false);
        Packet packet = optionalPacket.get();
        packetRepository.delete(packet);
        return new ApiResponse("Packet deleted!", true);
    }

    public PacketDtoForClients getPacketInfo(String packetName) {
        Packet packet = packetRepository.findByName(packetName);
        PacketDtoForClients packetDto = new PacketDtoForClients();
        packetDto.setName(packet.getName());
        packetDto.setPacketType(packet.getPacketType());
        packetDto.setTariff(packet.isTariff());
        packetDto.setDuration(packet.getDuration());
        packetDto.setAmount(packet.getAmount());
        packetDto.setCost(packet.getCost());
        List<Tariff> availableTariffs = packet.getAvailableTariffs();
        List<String> tariffsList = new ArrayList<>();
        for (Tariff availableTariff : availableTariffs) {
            tariffsList.add(availableTariff.getName());
        }
        packetDto.setAvailableTariffs(tariffsList);
        return packetDto;
    }
}
