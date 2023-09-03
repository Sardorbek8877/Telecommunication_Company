package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.entity.EntertainingService;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.ServiceDto;
import uz.bek.telecommunication_company.payload.SimCardEntertainingDto;
import uz.bek.telecommunication_company.service.ServiceService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/entertainingService")
public class EntertainingServiceController {

    @Autowired
    ServiceService service;

    /**
     * GET ENTERTAINING LIST
     * @return List<EntertainingService>
     */
    @GetMapping("/list")
    @Secured({ "ROLE_MANAGER"})
    public HttpEntity<?> getEntertainingList(){
        List<EntertainingService> list = service.getServiceList();
        return ResponseEntity.ok(list);
    }

    /**
     * GET ENTERTAINING SERVICE BY ID
     * @param id
     * @return ENTERTAINING SERVICE
     */
    @GetMapping("/list/{id}")
    @Secured({ "ROLE_MANAGER"})
    public HttpEntity<?> getById(@PathVariable UUID id){
        ApiResponse serviceById = service.getServiceById(id);
        return ResponseEntity.status(201).body(serviceById);
    }

    /**
     * ADD ENTERTAINING SERVICE
     * @param serviceDto
     * @return API RESPONSE
     */
    @PostMapping("/add")
    @Secured({ "ROLE_MANAGER"})
    public HttpEntity<?> addEntertaining(@RequestBody ServiceDto serviceDto){
        ApiResponse entertainingService = service.addEntertainingService(serviceDto);
        return ResponseEntity.status(201).body(entertainingService);
    }

    /**
     * EDIT ENTERTAINING SERVICE
     * @param id
     * @param serviceDto
     * @return API RESPONSE
     */
    @PutMapping("/edit/{id}")
    @Secured({ "ROLE_MANAGER"})
    public HttpEntity<?> editEntertaining(@PathVariable UUID id,@RequestBody ServiceDto serviceDto){
        ApiResponse apiResponse = service.editEntertainingService(id, serviceDto);
        return ResponseEntity.status(201).body(apiResponse);
    }

    /**
     * GET FAMOUS ENTERTAINING LIST
     * @return List<EntertainingService>
     */
    @Secured({ "ROLE_MANAGER"})
    @GetMapping("/famous")
    public HttpEntity<?> getFamousEntertainingList(){
        List<EntertainingService> serviceList = service.getServiceList();
        return ResponseEntity.ok(serviceList);
    }

    /**
     * ADD ENTERTAINING SERVICE FOR CLIENT
     * @param simcardEntertainingDto
     * @return API RESPONSE
     */
    @PostMapping("/addEntertaining")
    public HttpEntity<?> addEntertainingForClient(@RequestBody SimCardEntertainingDto simcardEntertainingDto){
        ApiResponse apiResponse = service.addEntertainingServiceForClient(simcardEntertainingDto);
        return ResponseEntity.ok(apiResponse);
    }

    /**
     * DELETE ENTERTAINING SERVICE
     * @param simcardEntertainingDto
     * @return API RESPONSE
     */
    @DeleteMapping("/delete")
    public HttpEntity<?> deleteEntertainingforClient(@RequestBody SimCardEntertainingDto simcardEntertainingDto) {
        ApiResponse apiResponse = service.addEntertainingServiceForClient(simcardEntertainingDto);
        return ResponseEntity.ok(apiResponse);
    }
}
