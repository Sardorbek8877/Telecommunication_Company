package uz.bek.telecommunication_company.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.PaymentDto;
import uz.bek.telecommunication_company.service.PaymentService;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    /**
     * ADD PAYMENT
     * @param dto
     * @return API RESPONSE
     */
    @PostMapping
    public HttpEntity<?> addPayment(@RequestBody PaymentDto dto) {
        ApiResponse apiResponse = paymentService.addPayment(dto);
        return ResponseEntity.status(apiResponse.isSuccess() ? 201 : 409).body(apiResponse);
    }


    /**
     * GET PAYMENT HISTORY FOR ONE CLIENT
     * @param simCardNumber
     * @return PAYMENT
     */
    @GetMapping("/{simCardNumber}")
    public ResponseEntity<?> getOneClientsPaymentHistory(@PathVariable String simCardNumber){
        return ResponseEntity.ok(paymentService.getOneClientsPaymentHistory(simCardNumber));
    }

    /**
     * GET PAYMENT HISTORY
     * @return PAYMENT
     */
    @GetMapping("/byClient")
    public ResponseEntity<?>getPaymentHistoryByClient(){
        return ResponseEntity.ok(paymentService.getPaymentHistoryByClient());
    }

    /**
     * GET ALL PAYMENT HISTORY
     * @return List<Payment>
     */
    @GetMapping
    public ResponseEntity<?>getAllPaymentHistory(){
        return ResponseEntity.ok(paymentService.getAll());
    }
}
