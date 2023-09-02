package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.entity.UssdCode;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.service.UssdCodeService;

@RestController
@RequestMapping("/api/auht/ussdCode")
public class UssdCodeController {

    @Autowired
    UssdCodeService ussdCodeService;

    /**
     * GET ALL USSD CODES
     * @return List<UssdCode>
     */
    @GetMapping
    public ResponseEntity<?> getAllUssdCodes(){
        return ResponseEntity.ok(ussdCodeService.getAllUssdCodes());
    }

    /**
     * GET USSD CODE BY ID
     * @param id
     * @return API RESPONSE
     */
    @GetMapping("/{id}")
    public HttpEntity<?> getUssdCodeById( @PathVariable Integer id){
        ApiResponse ussdCodeById = ussdCodeService.getUssdCodeById(id);
        return ResponseEntity.status(ussdCodeById.isSuccess() ? 202:409).body(ussdCodeById);
    }

    /**
     * ADD USSD CODE
     * @param ussdCode
     * @return API RESPONSE
     */
    @PostMapping
    public HttpEntity<?> addUssdCode(@RequestBody UssdCode ussdCode ){
        ApiResponse apiResponse = ussdCodeService.addUssdCode(ussdCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201:409).body(apiResponse);
    }

    /**
     * EDIT USSD CODE
     * @param id
     * @param ussdCode
     * @return API RESPONSE
     */
    @PutMapping("/{id}")
    public HttpEntity<?> editUssdCode(@PathVariable Integer id, @RequestBody UssdCode ussdCode){
        ApiResponse apiResponse = ussdCodeService.editUssdCode(id, ussdCode);
        return ResponseEntity.status(apiResponse.isSuccess() ? 202:409).body(apiResponse);
    }

    /**
     * DELETE USSD CODE
     * @param id
     * @return API RESPONSE
     */
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteUssdCode(@PathVariable Integer id){
        ApiResponse apiResponse = ussdCodeService.deleteUssdCode(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }
}
