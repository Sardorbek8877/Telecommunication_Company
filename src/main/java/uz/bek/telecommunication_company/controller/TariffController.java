package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.entity.Tariff;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.TariffDto;
import uz.bek.telecommunication_company.payload.TariffInfoForClient;
import uz.bek.telecommunication_company.service.TariffService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tariff")
public class TariffController {

    @Autowired
    TariffService tariffService;

    //GET ALL TARIFFS
    @PreAuthorize("hasAnyRole('ROLE_MANAGER', 'ROLE_DIRECTOR', 'ROLE_STAFF')")
    @GetMapping("/getAllTariffs")
    public HttpEntity<?> getAllTariffs(){
        List<Tariff> allTariffs = tariffService.getAllTariffs();
        return ResponseEntity.ok(allTariffs);
    }

    //GET TARIFF LIST FOR CLIENT
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    @GetMapping("/getAllForClient")
    private HttpEntity<?> getAllForClients(){
        List<String> allForClient = tariffService.getAllForClient();
        return ResponseEntity.ok(allForClient);
    }

    //ADD TARIFF
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/addTariff")
    public HttpEntity<?> addTariff(@RequestBody TariffDto tariffDto){
        ApiResponse apiResponse = tariffService.addTariff(tariffDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    //EDIT TARIFF
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/editTariff/{id}")
    public HttpEntity<?> editTariff(@PathVariable UUID id, @RequestBody TariffDto tariffDto){
        ApiResponse apiResponse = tariffService.editTariff(id, tariffDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    //DELETE TARIFF
    @PreAuthorize("hasRole('ROLE_MANAGER')")
    @GetMapping("/deleteTariff")
    public HttpEntity<?> deleteTariff(@PathVariable UUID id){
        ApiResponse apiResponse = tariffService.deleteTariff(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    //INFORMATION ABOUT TARIFF FOR CLIENT
    @PreAuthorize(value = "hasRole('ROLE_CLIENT')")
    @GetMapping("/getTariff/{tariffName}")
    public HttpEntity<?> getTariffInfo(@PathVariable String tariffName){
        TariffInfoForClient tariffInfo = tariffService.getTariffInfo(tariffName);
        return ResponseEntity.ok(tariffInfo);
    }
}