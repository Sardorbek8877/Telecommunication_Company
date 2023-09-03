package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.entity.Staff;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.StaffDto;
import uz.bek.telecommunication_company.service.StaffService;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {

    @Autowired
    StaffService staffService;

    /**
     * ADD STAFF
     * @param staffDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> addStaff(StaffDto staffDto){
        ApiResponse apiResponse = staffService.addStaff(staffDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**
     * EDIT STAFF
     * @param username
     * @param staffDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_STAFF')")
    @PutMapping("/{username}")
    public HttpEntity<?> editStaff(@PathVariable String username, StaffDto staffDto){
        ApiResponse apiResponse = staffService.editStaff(username, staffDto);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**
     * GET STAFF BY ID
     * @return STAFF
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER','ROLE_STAFF')")
    @GetMapping("/one")
    public HttpEntity<?> getStaff(){
        ApiResponse apiResponse = staffService.getStaff();
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }

    /**
     * GET STAFF LIST
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getStaffList(){
        List<Staff> staffList = staffService.getStaffList();
        return ResponseEntity.ok(staffList);
    }


    /**
     * DELETE STAFF
     * @param username
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR','ROLE_MANAGER')")
    @DeleteMapping("/{username}")
    public HttpEntity<?> deleteStaff(@PathVariable String username){
        ApiResponse apiResponse = staffService.deleteStaff(username);
        return ResponseEntity.status(apiResponse.isSuccess()?200:409).body(apiResponse);
    }
}
