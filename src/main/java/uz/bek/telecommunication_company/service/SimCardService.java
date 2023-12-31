package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.component.NumberGenerator;
import uz.bek.telecommunication_company.component.SerialNumberGenerator;
import uz.bek.telecommunication_company.entity.Packet;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.Tariff;
import uz.bek.telecommunication_company.entity.enums.ActionType;
import uz.bek.telecommunication_company.entity.enums.PacketType;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.DetailDto;
import uz.bek.telecommunication_company.payload.SimCardDto;
import uz.bek.telecommunication_company.repository.*;

import java.util.*;

@Service
public class SimCardService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    NumberGenerator numberGenerator;
    @Autowired
    TariffRepository tariffRepository;
    @Autowired
    UssdCodeRepository ussdCodeRepository;
    @Autowired
    PacketRepository packetRepository;
    @Autowired
    DetailsService detailsService;

    public ApiResponse addSimCard(SimCardDto simCardDto) {

        for (int i = 1; i < 100; i++) {
            String randomNumber = numberGenerator.generateRandomPassword(7);
            boolean byNumberAndCode = simCardRepository.existsByNumberAndCode(randomNumber, simCardDto.getCode());
            if (byNumberAndCode) return new ApiResponse("Number already exist", false);
            SimCard simCard = new SimCard();
            simCard.setSimCardNumber(randomNumber);
            simCard.setBalance(0);
            simCard.setName(simCardDto.getName());
            simCard.setCode(simCardDto.getCode());

            String pinCode = numberGenerator.generateRandomPassword(4);
            simCard.setPinCode(pinCode);
            simCard.setSimCardNumber(SerialNumberGenerator.generateImei());
            simCardRepository.save(simCard);
        }
        return new ApiResponse("Successfully added", true);
    }


    public ApiResponse unableSimCard(String code, String number) {

        Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(code, number);
        if (optionalSimCard.isEmpty()) return new ApiResponse("Sim Card not found", false);
        SimCard simCard = optionalSimCard.get();
        simCard.setActive(false);
        simCardRepository.save(simCard);
        return new ApiResponse("Sim deactivated", true);
    }

    public ApiResponse getUssdCode(String ussdCode) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        boolean existsByCode = ussdCodeRepository.existsByCode(ussdCode);
        if (!existsByCode) return new ApiResponse("Invalid code", false);
        if (ussdCode.equals("*100#")) {
            double balance = simCard.getBalance();
            return new ApiResponse("Sizning balansinggiz", true, balance);
        } else if (ussdCode.equals("*101#")) {
            double amountMb = simCard.getAmountMb();
            return new ApiResponse("Sizning enternet trafik qoldig'inggiz", true, amountMb);
        } else if (ussdCode.equals("*102#")) {
            double amountSms = simCard.getAmountSms();
            return new ApiResponse("Sizning sms qoldig'inggiz", true, amountSms);

        } else if (ussdCode.equals("*103#")) {
            double amountMinute = simCard.getAmountMinute();
            return new ApiResponse("Sizning sms qoldig'inggiz", true, amountMinute);

        } else if (ussdCode.equals("*104#")) {
            Optional<Packet> byPacketTypeAndAmount = packetRepository.findByPacketTypeAndAmount(PacketType.MB, 10240);
            if (!byPacketTypeAndAmount.isPresent())
                return new ApiResponse("Afsuski hozir siz xohlagan paket bizda mavjud emas", false);

            Packet packet = byPacketTypeAndAmount.get();
            if (simCard.isActive() && simCard.isTariffIsActive() && simCard.getBalance() > packet.getCost()) {
                simCard.setBalance(simCard.getBalance() - packet.getCost());
                simCard.setAmountMb(simCard.getAmountMb() + packet.getAmount());
                detailsService.add(new DetailDto(ActionType.PAKET, simCard, (float) packet.getCost()));
                simCardRepository.save(simCard);
                return new ApiResponse("Siz 10GB paket sotib oldinggiz", true);
            }
            return new ApiResponse("Something went wrong", false);
        } else if (ussdCode.equals("*105#")) {
            Optional<Packet> optionalMinute = packetRepository.findByPacketTypeAndAmount(PacketType.MINUTE, 500);
            if (!optionalMinute.isPresent()) return new ApiResponse("Siz xohlagan paket afsuskki mavjud emas", false);
            Packet minute = optionalMinute.get();
            if (simCard.isActive() && simCard.isTariffIsActive() && simCard.getBalance() > minute.getCost()) {
                simCard.setBalance(simCard.getBalance() - minute.getCost());
                simCard.setAmountMinute(simCard.getAmountMinute() + minute.getAmount());
                detailsService.add(new DetailDto(ActionType.PAKET, simCard, (float) minute.getCost()));
                simCardRepository.save(simCard);
                return new ApiResponse("Siz 500MIN paket sotib oldinggiz", true);
            }
            return new ApiResponse("Something went wrong", false);

        }
        return new ApiResponse("Invalid USSD code", false);

    }

    public ApiResponse connectTariff(UUID tariffId) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Tariff> tariffRepositoryById = tariffRepository.findById(tariffId);
        if (!tariffRepositoryById.isPresent()) return new ApiResponse("Tariff not found", false);
        Tariff tariff = tariffRepositoryById.get();
        if (simCard.getBalance() > tariff.getSwitchPrice() + tariff.getPrice() && simCard.isActive()) {
            simCard.setTariff(tariff);
            simCard.setBalance(simCard.getBalance() - (tariff.getPrice() + tariff.getSwitchPrice()));
            simCard.setAmountMinute(tariff.getMin());
            simCard.setAmountMb(tariff.getMb());
            simCard.setAmountSms(tariff.getSms());
            simCardRepository.save(simCard);
            return new ApiResponse("Your tariff is " + tariff.getName(), true);
        }
        return new ApiResponse("Error", false);
    }

    public Set<SimCard> getSimCards() {
        List<SimCard> simCardList = simCardRepository.findAll();
        Set<SimCard> simCardSet = new HashSet<>();
        for (SimCard simCard : simCardList) {
            if (!simCard.isActive()) {
                simCardSet.add(simCard);
            }
        }
        return simCardSet;
    }
}
