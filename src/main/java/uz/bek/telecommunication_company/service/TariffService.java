package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Tariff;
import uz.bek.telecommunication_company.entity.enums.ClientType;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.TariffDto;
import uz.bek.telecommunication_company.payload.TariffInfoForClient;
import uz.bek.telecommunication_company.repository.TariffRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TariffService {

    @Autowired
    TariffRepository tariffRepository;

    //GET ALL TARIFFS
    public List<Tariff> getAllTariffs(){
        return tariffRepository.findAll();
    }

    //GET TARIFF LIST FOR CLIENT
    public List<String> getAllForClient() {
        List<Tariff> tariffList = tariffRepository.findAll();
        List<String> list = new ArrayList<>();
        for (Tariff tariff : tariffList) {
            list.add(tariff.getName());
        }
        return list;
    }

    //ADD TARIFF
    public ApiResponse addTariff(TariffDto tariffDto){
        boolean existsByName = tariffRepository.existsByName(tariffDto.getName());
        if (existsByName)
            return new ApiResponse("Tariff with name " + tariffDto.getName() + " already exist!", false);

        Tariff tariff = new Tariff();
        if (tariffDto.getClientTypeId() == 1) { tariff.setClientType(ClientType.USER); }
        else if (tariffDto.getClientTypeId() == 2) { tariff.setClientType(ClientType.COMPANY); }
        else { return new ApiResponse("Client type id is wrong!", false); }
        tariff.setName(tariffDto.getName());
        tariff.setPrice(tariffDto.getPrice());
        tariff.setSwitchPrice(tariffDto.getSwitchPrice());
        tariff.setExpireDate(tariffDto.getExpireDate());
        tariff.setSms(tariffDto.getSms());
        tariff.setMin(tariffDto.getMin());
        tariff.setMb(tariffDto.getMb());
        tariff.setMbCost(tariffDto.getMbCost());
        tariff.setMinCost(tariffDto.getMinCost());
        tariff.setSmsCost(tariffDto.getSmsCost());
        tariffRepository.save(tariff);
        return new ApiResponse("Tariff added!", true);
    }

    //EDIT TARIFF
    public ApiResponse editTariff(UUID id, TariffDto tariffDto){
        Optional<Tariff> optionalTariff = tariffRepository.findById(id);
        if (optionalTariff.isEmpty())
            return new ApiResponse("Tariff not found!", false);

        Tariff tariff = new Tariff();
        if (tariffDto.getClientTypeId() == 1) { tariff.setClientType(ClientType.USER); }
        else if (tariffDto.getClientTypeId() == 2) { tariff.setClientType(ClientType.COMPANY); }
        else { return new ApiResponse("Client type id is wrong!", false); }
        tariff.setName(tariffDto.getName());
        tariff.setPrice(tariffDto.getPrice());
        tariff.setSwitchPrice(tariffDto.getSwitchPrice());
        tariff.setExpireDate(tariffDto.getExpireDate());
        tariff.setSms(tariffDto.getSms());
        tariff.setMin(tariffDto.getMin());
        tariff.setMb(tariffDto.getMb());
        tariff.setMbCost(tariffDto.getMbCost());
        tariff.setMinCost(tariffDto.getMinCost());
        tariff.setSmsCost(tariffDto.getSmsCost());
        tariffRepository.save(tariff);
        return new ApiResponse("Tariff edited!", true);
    }

    //DELETE TARIFF
    public ApiResponse deleteTariff(UUID id){
        Optional<Tariff> optionalTariff = tariffRepository.findById(id);
        if (optionalTariff.isEmpty())
            return new ApiResponse("Tariff not found!", false);
        tariffRepository.deleteById(id);
        return new ApiResponse("Tariff deleted!", true);
    }

    //GET INFORMATION ABOUT TARIFF FOR CLIENT
    public TariffInfoForClient getTariffInfo(String tariffName){
        Tariff tariff = tariffRepository.findByName(tariffName);
        TariffInfoForClient tariffInfoForClient = new TariffInfoForClient();
        tariffInfoForClient.setName(tariff.getName());
        tariffInfoForClient.setPrice(tariff.getPrice());
        tariffInfoForClient.setSwitchPrice(tariff.getSwitchPrice());
        tariffInfoForClient.setExpireDate(tariff.getExpireDate());
        tariffInfoForClient.setSms(tariff.getSms());
        tariffInfoForClient.setMin(tariff.getMin());
        tariffInfoForClient.setMb(tariff.getMb());
        tariffInfoForClient.setMbCost(tariff.getMbCost());
        tariffInfoForClient.setMinCost(tariff.getMinCost());
        tariffInfoForClient.setSmsCost(tariff.getSmsCost());
        return tariffInfoForClient;
    }
}
