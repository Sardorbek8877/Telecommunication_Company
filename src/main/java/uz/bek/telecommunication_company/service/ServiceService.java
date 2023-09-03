package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.repository.ServiceCategoryRepository;
import uz.bek.telecommunication_company.repository.ServiceRepository;

@Service
public class ServiceService {

    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    ServiceCategoryRepository serviceCategoryRepository;
    @Autowired
    DetailsService detailsService;

}
