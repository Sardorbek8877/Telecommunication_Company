package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import uz.bek.telecommunication_company.entity.EntertainingService;
import uz.bek.telecommunication_company.entity.ServiceCategory;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.enums.ActionType;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.DetailDto;
import uz.bek.telecommunication_company.payload.ServiceDto;
import uz.bek.telecommunication_company.payload.SimCardEntertainingDto;
import uz.bek.telecommunication_company.repository.ServiceCategoryRepository;
import uz.bek.telecommunication_company.repository.ServiceRepository;
import uz.bek.telecommunication_company.repository.SimCardRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceCategoryRepository serviceCategoryRepository;
    @Autowired
    DetailsService detailsService;
    @Autowired
    SimCardService simCardservice;
    @Autowired
    SimCardRepository simCardRepository;

    public List<EntertainingService> getServiceList(){
        return serviceRepository.findAll();
    }

    public ApiResponse getServiceById(UUID id){
        Optional<EntertainingService> serviceOptional = serviceRepository.findById(id);
        if (serviceOptional.isEmpty())
            return new ApiResponse("service with this id not found",false,null);
        EntertainingService entertainingService = serviceOptional.get();
        return new ApiResponse("Request successfully done",false,entertainingService);
    }

    public ApiResponse addEntertainingService(ServiceDto serviceDto){
        boolean exists = serviceRepository.existsByName(serviceDto.getName());
        if (exists)
            return new ApiResponse("Service not found",false,null);
        EntertainingService entertainingService = new EntertainingService();
        entertainingService.setName(serviceDto.getName());
        entertainingService.setExpiredDate(serviceDto.getExpiredDate());
        entertainingService.setPrice(serviceDto.getPrice());
        Optional<ServiceCategory> categoryOptional = serviceCategoryRepository.findById(serviceDto.getServiceCategoryID());

        if (categoryOptional.isEmpty()){
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setName(serviceDto.getCategoryName());
            serviceCategoryRepository.save(serviceCategory);
        }
        ServiceCategory serviceCategory = categoryOptional.get();
        entertainingService.setServiceCategory(serviceCategory);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Added!",true,entertainingService);
    }

    public ApiResponse editEntertainingService(UUID id,ServiceDto serviceDto){
        Optional<EntertainingService> serviceOptional = serviceRepository.findById(id);
        if (serviceOptional.isEmpty())
            return new ApiResponse("Service with this id not found",false,null);
        EntertainingService entertainingService = serviceOptional.get();
        boolean exist = serviceRepository.existsByName(serviceDto.getName());
        if (exist)
            return new ApiResponse("Service already exist",false,null);
        entertainingService.setName(serviceDto.getName());
        entertainingService.setPrice(serviceDto.getPrice());
        entertainingService.setExpiredDate(serviceDto.getExpiredDate());
        Optional<ServiceCategory> categoryOptional = serviceCategoryRepository.findById(serviceDto.getServiceCategoryID());
        if (categoryOptional.isEmpty()){
            ServiceCategory serviceCategory = new ServiceCategory();
            serviceCategory.setName(serviceDto.getCategoryName());
            serviceCategoryRepository.save(serviceCategory);
        }
        ServiceCategory serviceCategory = categoryOptional.get();
        entertainingService.setServiceCategory(serviceCategory);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Service edited!",false,entertainingService);
    }

    public ApiResponse addEntertainingServiceForClient(@RequestBody SimCardEntertainingDto simcardEntertainingDto){
        SimCard principal = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<EntertainingService> serviceOptional = serviceRepository.findByName(simcardEntertainingDto.getEntertainingName());
        if (serviceOptional.isEmpty())
            return new ApiResponse("service with this id not found",false,null);
        EntertainingService entertainingService = serviceOptional.get();
        if (entertainingService.getPrice()>principal.getBalance()){
            return new ApiResponse("Amount is not enough",false,null);
        }
        Set<EntertainingService> entertainingServices = principal.getEntertainingServices();
        entertainingServices.add(entertainingService);
        principal.setEntertainingServices(entertainingServices);
        simCardRepository.save(principal);
        detailsService.add(new DetailDto(ActionType.SERVICE,principal, (float) entertainingService.getPrice()));
        entertainingService.setCount(entertainingService.getCount()+1);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Connected",true,null);
    }
    public ApiResponse deleteEntertainingForClient(@RequestBody SimCardEntertainingDto simcardEntertainingDto){
        SimCard principal = (SimCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<EntertainingService> serviceOptional = serviceRepository.findByName(simcardEntertainingDto.getEntertainingName());
        if (serviceOptional.isEmpty())
            return new ApiResponse("Service not found",false,null);
        EntertainingService entertainingService = serviceOptional.get();
        principal.getEntertainingServices().remove(entertainingService);
        entertainingService.setCount(entertainingService.getCount()-1);
        simCardRepository.save(principal);
        serviceRepository.save(entertainingService);
        return new ApiResponse("Deleted!",true,null);

    }

    public ApiResponse getFamousServiceList(){
        List<EntertainingService> byCountOrderByCount = serviceRepository.findAllByOrderByCountAsc();
        return new ApiResponse("Success!",true,byCountOrderByCount);
    }
}
