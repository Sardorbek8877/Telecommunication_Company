package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.bek.telecommunication_company.entity.DebtSimCard;
import uz.bek.telecommunication_company.payload.*;
import uz.bek.telecommunication_company.service.ClientService;

@RestController
@RequestMapping("/api/client")
public class ClientController {

    @Autowired
    ClientService clientService;

    /**
     * BUY SIMCARD
     * @param clientDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasAnyRole(ROLE_STAFF,ROLE_CLIENT)")
    @PostMapping("/buySimCard")
    public ResponseEntity<?> buySimCard(@RequestBody ClientDto clientDto) {
        ApiResponse apiResponse = clientService.buySimCard(clientDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    /**
     * REQUEST DEBT
     * @param debtDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole(ROLE_CLIENT)")
    @PostMapping("/requestDebt")
    public ResponseEntity<?> requestDebt(@RequestBody DebtSimCard debtDto) {
        ApiResponse apiResponse = clientService.requestCredit(debtDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    /**
     * CALL
     * @param callDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole(ROLE_CLIENT)")
    @PostMapping("/call")
    public ResponseEntity<?> call(@RequestBody CallDto callDto) {
        ApiResponse apiResponse = clientService.callSmb(callDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

    /**
     * SEND SMS
     * @param smsDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole(ROLE_CLIENT)")
    @PostMapping("/sendSms")
    public ResponseEntity<?> sendSms(@RequestBody SmsDto smsDto) {
        ApiResponse apiResponse = clientService.sendSms(smsDto);
        if (apiResponse.isSuccess()) return ResponseEntity.ok(apiResponse);
        return ResponseEntity.status(409).body(apiResponse);
    }

}
