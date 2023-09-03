package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.component.NumberGenerator;
import uz.bek.telecommunication_company.repository.ClientRepository;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.SimCardRepository;
import uz.bek.telecommunication_company.repository.TariffRepository;

@Service
public class ClientService {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SimCardRepository simCardRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    NumberGenerator numberGenerator;
    @Autowired
    TariffRepository tariffRepository;
    @Autowired
    DetailsService detailsService;
}
