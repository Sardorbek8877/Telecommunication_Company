package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.Staff;
import uz.bek.telecommunication_company.entity.UssdCode;
import uz.bek.telecommunication_company.entity.enums.RoleName;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.UssdCodeRepository;

import java.util.List;
import java.util.Optional;

@Service
public class UssdCodeService {

    @Autowired
    UssdCodeRepository ussdCodeRepository;
    @Autowired
    RoleRepository roleRepository;

    /**
     * GET ALL USSD CODES
     * @return List<UssdCode>
     */
    public List<UssdCode> getAllUssdCodes(){
        return ussdCodeRepository.findAll();
    }

    /**
     * GET USSD CODE BY ID
     * @param id
     * @return API RESPONSE
     */
    public ApiResponse getUssdCodeById(Integer id){
        Optional<UssdCode> optionalUssdCode = ussdCodeRepository.findById(id);
        return optionalUssdCode.map(ussdCode -> new ApiResponse("Your Ussd Code ", true, ussdCode))
                .orElseGet(() -> new ApiResponse("Ussd Code not found", false));
    }

    /**
     * ADD USSD CODE
     * @param ussdCode
     * @return API RESPONSE
     */
    public ApiResponse addUssdCode( UssdCode ussdCode ){

        Staff employee = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role managerRole = roleRepository.findByRoleName(RoleName.ROLE_MANAGER);
        Role staffRole = roleRepository.findByRoleName(RoleName.ROLE_STAFF);
        if ((employee.getRoles().contains(staffRole)) || (employee.getRoles().contains(managerRole) &&
                employee.getPosition().equalsIgnoreCase("USSD_MANAGER"))){
            return new ApiResponse("You can not add new Ussd Codes", false);
        }
        UssdCode ussdCodeNew = new UssdCode();
        ussdCodeNew.setCode(ussdCode.getCode());
        ussdCodeNew.setDescription(ussdCode.getDescription());
        ussdCodeRepository.save(ussdCodeNew);
        return new ApiResponse("USSD Code added!", true);
    }

    /**
     * EDIT USSD CODE
     * @param id
     * @param ussdCode
     * @return API RESPONSE
     */
    public ApiResponse editUssdCode(Integer id, UssdCode ussdCode){
        Staff employee = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role managerRole = roleRepository.findByRoleName(RoleName.ROLE_MANAGER);
        Role staffRole = roleRepository.findByRoleName(RoleName.ROLE_STAFF);
        if ((employee.getRoles().contains(staffRole)) || (employee.getRoles().contains(managerRole) &&
                employee.getPosition().equalsIgnoreCase("USSD_MANAGER"))){
            return new ApiResponse("You can not edit Ussd Codes", false);
        }

        Optional<UssdCode> optionalUssdCode = ussdCodeRepository.findById(id);
        if (optionalUssdCode.isEmpty())
            return new ApiResponse("USSD Code not found", false);

        UssdCode editingUssdCode = optionalUssdCode.get();
        editingUssdCode.setCode(ussdCode.getCode());
        editingUssdCode.setDescription(ussdCode.getDescription());
        ussdCodeRepository.save(editingUssdCode);
        return new ApiResponse("USSD Code edited!", true);
    }

    /**
     * DELETE USSD CODE
     * @param id
     * @return API RESPONSE
     */
    public ApiResponse deleteUssdCode(Integer id){

        Staff employee = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Role managerRole = roleRepository.findByRoleName(RoleName.ROLE_MANAGER);
        Role staffRole = roleRepository.findByRoleName(RoleName.ROLE_STAFF);
        if ((employee.getRoles().contains(staffRole)) || (employee.getRoles().contains(managerRole) &&
                employee.getPosition().equalsIgnoreCase("USSD_MANAGER"))){
            return new ApiResponse("You can not delete Ussd Codes", false);
        }

        Optional<UssdCode> optionalUssdCode = ussdCodeRepository.findById(id);
        if (optionalUssdCode.isEmpty())
            return new ApiResponse("USSD Code not found", false);
        ussdCodeRepository.deleteById(id);
        return new ApiResponse("USSD Code deleted", true);
    }
}


















