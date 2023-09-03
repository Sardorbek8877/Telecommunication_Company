package uz.bek.telecommunication_company.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.service.DetailsService;

import java.io.IOException;

@RestController
@RequestMapping("/api/details")
public class DetailsController {

    @Autowired
    DetailsService detailsService;

    /**
     * GET ALL DATA AS PDF
     * @param response
     * @param id
     * @return API RESPONSE
     * @throws IOException
     */
    @GetMapping("/pdf/{id}")
    public HttpEntity<?> getAllPdf(HttpServletResponse response, @PathVariable String id) throws IOException {
        ApiResponse apiResponse = detailsService.getAllBySimCard(response, 0);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * GET ALL DATA AS EXCEL
     * @param response
     * @param id
     * @return API RESPONSE
     * @throws IOException
     */
    @GetMapping("/excel/{id}")
    public HttpEntity<?> getAllExcel(HttpServletResponse response, @PathVariable String id) throws IOException {
        ApiResponse apiResponse = detailsService.getAllBySimCard(response, 1);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * GET ALL DATA WITH ACTION AS PDF
     * @param response
     * @param id
     * @param action
     * @return API RESPONSE
     * @throws IOException
     */
    @GetMapping("/pdfWithAction/{id}")
    public HttpEntity<?> getAllPdfWithAction(HttpServletResponse response, @PathVariable String id, @RequestParam String action) throws IOException {
        ApiResponse apiResponse = detailsService.getAllBySimCardAndActionType(response, action,0);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * GET ALL DATA WITH ACTION AS EXCEL
     * @param response
     * @param id
     * @param action
     * @return  API RESPONSE
     * @throws IOException
     */
    @GetMapping("/excelWithAction/{id}")
    public HttpEntity<?> getAllExcelWithAction(HttpServletResponse response, @PathVariable String id, @RequestParam String action) throws IOException {
        ApiResponse apiResponse = detailsService.getAllBySimCardAndActionType(response,action,1);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * GET ALL
     * @param id
     * @param response
     * @return API RESPONSE
     * @throws IOException
     */
    @GetMapping("/{id}")
    public HttpEntity<?> getAll(@PathVariable String id, HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = detailsService.getAllBySimCard(response, 2);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    /**
     * GET ALL
     * @param id
     * @param action
     * @param response
     * @return API RESPONSE
     * @throws IOException
     */
    @GetMapping("/{id}/{action}")
    public HttpEntity<?> getAllWithAction(@PathVariable String id, @PathVariable String action, HttpServletResponse response) throws IOException {
        ApiResponse apiResponse = detailsService.getAllBySimCardAndActionType(response, action,2);
        return ResponseEntity.status(apiResponse.isSuccess()? HttpStatus.OK:HttpStatus.BAD_REQUEST).body(apiResponse);
    }
}
