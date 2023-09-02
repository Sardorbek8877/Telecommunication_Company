package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.entity.Filial;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.FilialDto;
import uz.bek.telecommunication_company.service.FilialService;

import java.util.List;

@RestController
@RequestMapping("/api/filial")
public class FilialController {

    @Autowired
    FilialService filialService;
    /**
     * GET FILIAL LIST
     * @return List<Filial>
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping
    public HttpEntity<?> getFilialList(){
        List<Filial> filialList = filialService.getFilialList();
        return ResponseEntity.ok(filialList);
    }

    /**
     * GET FILIAL BY ID
     * @param id
     * @return Filial
     */
    @PreAuthorize(value = "hasAnyRole('ROLE_DIRECTOR', 'ROLE_MANAGER')")
    @GetMapping("/{id}")
    public HttpEntity<?> getFilialById(@PathVariable Integer id){
        ApiResponse filialById = filialService.getFilialById(id);
        return ResponseEntity.ok(filialById);
    }

    /**
     * ADD FILIAL
     * @param filialDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @PostMapping
    public HttpEntity<?> addFilial(@RequestBody FilialDto filialDto){
        ApiResponse apiResponse = filialService.addFilial(filialDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    /**
     * EDIT FILIAL
     * @param id
     * @param filialDto
     * @return API RESPONSE
     */
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @PutMapping("/{id}")
    public HttpEntity<?> editFilial(@PathVariable Integer id, @RequestBody FilialDto filialDto){
        ApiResponse apiResponse = filialService.editFilial(id, filialDto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }

    /**
     * DELETE FILIAL
     * @param id
     * @return
     */
    @PreAuthorize(value = "hasRole('ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> deleteFilial(@PathVariable Integer id){
        ApiResponse apiResponse = filialService.deleteFilial(id);
        return ResponseEntity.status(apiResponse.isSuccess() ? 200:409).body(apiResponse);
    }
}
