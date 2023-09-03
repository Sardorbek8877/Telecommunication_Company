package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Payment;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.enums.PayType;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.PaymentDto;
import uz.bek.telecommunication_company.repository.PaymentRepository;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.SimCardRepository;

import java.util.List;

@Service
public class PaymentService {

    @Autowired
    PaymentRepository paymentRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    RoleRepository roleRepository;

    public ApiResponse addPayment(PaymentDto dto){
        Payment payment = new Payment();
        payment.setPayerId(dto.getPayerId());
        payment.setPayerName(dto.getPayerName());
        payment.setAmount(dto.getAmount());
        SimCard simCard = simCardRepository.findBySimCardNumber(dto.getSimCardNumber()).get();
        simCard.setBalance(simCard.getBalance()+ dto.getAmount());
        payment.setSimCard(simCard);
        if (dto.getPayType().equalsIgnoreCase("cash")) payment.setPayType(PayType.CASH);
        if (dto.getPayType().equalsIgnoreCase("humo")) payment.setPayType(PayType.HUMO);
        if (dto.getPayType().equalsIgnoreCase("click")) payment.setPayType(PayType.CLICK);
        if (dto.getPayType().equalsIgnoreCase("payme")) payment.setPayType(PayType.PAYME);
        paymentRepository.save(payment);
        return new ApiResponse("Payment success!", true);
    }


    public ApiResponse getAll(){
        List<Payment> paymentList = paymentRepository.findAll();
        return new ApiResponse("All Payments history", true, paymentList);
    }

    public ApiResponse getOneClientsPaymentHistory(String simCardNumber){
        List<Payment> allBySimCardNumber = paymentRepository.findAllBySimCardNumber(simCardNumber);
        SimCard simCard = simCardRepository.findBySimCardNumber(simCardNumber).get();
        return new ApiResponse(simCard.getCode()+simCard.getNumber()+
                " payment history for client", true, allBySimCardNumber);
    }

    public ApiResponse getPaymentHistoryByClient(){
        SimCard simCard = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Payment> allBySimCardNumber = paymentRepository.findAllBySimCardNumber(simCard.getSimCardNumber());
        return new ApiResponse(simCard.getCode()+simCard.getNumber()+" " +
                " payment history", true, allBySimCardNumber);
    }
}
