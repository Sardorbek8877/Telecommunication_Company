package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.component.NumberGenerator;
import uz.bek.telecommunication_company.entity.*;
import uz.bek.telecommunication_company.entity.enums.ActionType;
import uz.bek.telecommunication_company.entity.enums.ClientType;
import uz.bek.telecommunication_company.entity.enums.RoleName;
import uz.bek.telecommunication_company.payload.*;
import uz.bek.telecommunication_company.repository.ClientRepository;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.SimCardRepository;
import uz.bek.telecommunication_company.repository.TariffRepository;

import java.sql.Timestamp;
import java.util.*;

@Service
public class ClientService {

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
    DetailsService detailsService;

    /**
     * BUY SIMCARD
     * @param clientDto
     * @return API RESPONSE
     */
    public ApiResponse buySimCard(ClientDto clientDto){
        boolean existsedByPassportNumber = clientRepository.existsByPassportNumber(clientDto.getPassportNumber());
        if (existsedByPassportNumber){
            Optional<Client> optionalClient = clientRepository.findByPassportNumber(clientDto.getPassportNumber());
            Client client = optionalClient.get();

            List<BuyingSimCardDto> buyingSimCardDto = clientDto.getBuyingSimCardDto();

            List<SimCard> simCardList = new ArrayList<>();

            for (BuyingSimCardDto buyingSimCardDto1 : buyingSimCardDto){
                Optional<Tariff> optionalTariff = tariffRepository.findById(buyingSimCardDto1.getTariffId());
                if (!optionalTariff.isPresent()) return new ApiResponse("Tariff not found", false);
                Tariff tariff = optionalTariff.get();

                Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(buyingSimCardDto1.getCode(), buyingSimCardDto1.getNumber());
                if (!optionalSimCard.isPresent()) return new ApiResponse("SimCard not found", false);
                SimCard simCard = optionalSimCard.get();

                if (simCard.isActive()) return new ApiResponse("SimCard already bought", false);

                simCard.setActive(true);
                simCard.setTariff(tariff);
                simCard.setClient(client);
                simCard.setBalance(buyingSimCardDto1.getSum());
                if (buyingSimCardDto1.getSum() >= tariff.getPrice()) {
                    simCard.setBalance(simCard.getBalance() - tariff.getPrice());
                    simCard.setAmountMb(tariff.getMb());
                    simCard.setAmountMinute(tariff.getMin());
                    simCard.setAmountSms(tariff.getSms());
                    simCard.setTariffIsActive(true);
                } else {
                    simCard.setAmountMb(0);
                    simCard.setAmountMinute(0);
                    simCard.setAmountSms(0);
                    simCard.setTariffIsActive(false);
                }
                simCardList.add(simCard);
            }
            client.setSimCardList(simCardList);
            clientRepository.save(client);

            return new ApiResponse("SimCard activated", true);
            }
        else {
            Client client = new Client();

            client.setPassportNumber(clientDto.getPassportNumber());
            if (clientDto.getClientTypeOrdinal() == 1) {
                client.setClientType(ClientType.USER);
            } else if (clientDto.getClientTypeOrdinal() == 2) {
                client.setClientType(ClientType.COMPANY);
            } else {
                return new ApiResponse("Client Type is wrong", false);
            }
            client.setFullName(clientDto.getFullName());
            Role byRoleName = roleRepository.findByRoleName(RoleName.ROLE_CLIENT);
            client.setRoles(Collections.singleton(byRoleName));

            //ALL SIMCARDS
            List<SimCard> simCardList = new ArrayList<>();

            for (BuyingSimCardDto buyingSimCardDto : clientDto.getBuyingSimCardDto()) {
                Optional<Tariff> optionalTariff = tariffRepository.findById(buyingSimCardDto.getTariffId());
                if (!optionalTariff.isPresent()) return new ApiResponse("Tariff not found", false);
                Tariff tariff = optionalTariff.get();


                Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(buyingSimCardDto.getCode(), buyingSimCardDto.getNumber());
                if (!optionalSimCard.isPresent()) return new ApiResponse("SimCard not found", false);
                SimCard simCard = optionalSimCard.get();

                //ADD SIMCARD DATA
                simCard.setActive(true);
                simCard.setTariff(tariff);
                simCard.setBalance(buyingSimCardDto.getSum());
                simCard.setClient(client);

                if (buyingSimCardDto.getSum() >= tariff.getPrice()) {
                    simCard.setBalance(simCard.getBalance() - tariff.getPrice());
                    simCard.setAmountMb(tariff.getMb());
                    simCard.setAmountMinute(tariff.getMin());
                    simCard.setAmountSms(tariff.getSms());
                    simCard.setTariffIsActive(true);

                } else {
                    simCard.setAmountMb(0);
                    simCard.setAmountMinute(0);
                    simCard.setAmountSms(0);
                    simCard.setTariffIsActive(false);
                }
                simCardList.add(simCard);
            }
            client.setSimCardList(simCardList);
            clientRepository.save(client);
            return new ApiResponse("SimCard successfully authorized", true);
        }
    }

    public ApiResponse requestCredit(DebtSimCard debtDto) {
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (simCard.isCredit() || simCard.getBalance() > 5000)
            return new ApiResponse("You can not get cresit", false);
        if (debtDto.getAmount() > 25000) return new ApiResponse("You can get cresit only until 25000 ", false);

        simCard.setCredit(true);
        simCard.setBalance(simCard.getBalance() + debtDto.getAmount());


        DebtSimCard debtSimCard = new DebtSimCard();
        debtSimCard.setSimCard(simCard);
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_YEAR, 3);
        Date expireTime = calendar.getTime();
        debtSimCard.setExpireDate(new Timestamp(expireTime.getTime()));
        debtSimCard.setAmount(debtDto.getAmount());

        simCard.setDebtSimCards(Collections.singletonList(debtSimCard));

        simCardRepository.save(simCard);
        return new ApiResponse("You got credit successfully", true);
    }

    public ApiResponse callSmb(CallDto callDto) {

        Optional<SimCard> byCodeAndNumber = simCardRepository.findByCodeAndNumber(callDto.getCode(), callDto.getNumber());
        if (!byCodeAndNumber.isPresent()) return new ApiResponse("Your Number not found", false);

        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tariff tariff = simCard.getTariff();
        if (simCard.isActive() && !simCard.isCredit() && simCard.getBalance() > tariff.getMinCost()) {
            int minCost = tariff.getMinCost();
            float outcome = 0;

            for (int i = 0; i < callDto.getSeconds() / 60; i++) {
                if (simCard.getAmountMinute() > 1 && simCard.getBalance() > minCost) {
                    simCard.setAmountMinute(simCard.getAmountMinute() - 1);
                } else if (simCard.getAmountMinute() == 0 && simCard.getBalance() > 0) {
                    simCard.setBalance(simCard.getBalance() - minCost);
                    outcome += minCost;
                } else if (simCard.getBalance() <= 0 && simCard.getAmountMinute() == 0) {
                    simCard.setCredit(true);
                    return new ApiResponse("Call canceled, please fill the balance", false);
                }
            }
            detailsService.add(new DetailDto(ActionType.MINUTE, simCard, outcome));
            simCardRepository.save(simCard);
            return new ApiResponse("Cal canceled", true);
        }
        return new ApiResponse("Your balance empty", false);
    }

    public ApiResponse sendSms(SmsDto smsDto) {

        Optional<SimCard> byCodeAndNumber = simCardRepository.findByCodeAndNumber(smsDto.getCode(), smsDto.getNumber());
        if (!byCodeAndNumber.isPresent()) return new ApiResponse("This number not found", false);

        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Tariff tariff = simCard.getTariff();

        float outcome = 0;

        if (simCard.getAmountSms() > 1 && !simCard.isCredit() && simCard.isActive()) {
            simCard.setAmountMinute(simCard.getAmountMinute() - 1);
        } else if (simCard.getAmountSms() == 0 && simCard.getBalance() >= tariff.getSmsCost()) {
            simCard.setBalance(simCard.getBalance() - tariff.getSmsCost());
            outcome = outcome + tariff.getSmsCost();
        } else if (simCard.getBalance() <= 0 && simCard.getAmountSms() == 0) {
            return new ApiResponse("Your balance is empty", false);
        }
        detailsService.add(new DetailDto(ActionType.SMS, simCard, outcome));
        simCardRepository.save(simCard);
        return new ApiResponse("Success", true);
    }
}
