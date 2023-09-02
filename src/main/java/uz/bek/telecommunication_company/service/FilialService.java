package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Filial;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.Staff;
import uz.bek.telecommunication_company.entity.enums.RoleName;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.FilialDto;
import uz.bek.telecommunication_company.repository.FilialRepository;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.StaffRepository;

import java.util.*;

@Service
public class FilialService {

    @Autowired
    FilialRepository filialRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    StaffRepository staffRepository;


    /**
     * GET FILIAL LIST
     * @return List<Filial>
     */
    public List<Filial> getFilialList(){
        return filialRepository.findAll();
    }

    /**
     * GET FILIAL BY ID
     * @param id
     * @return API RESPONSE
     */
    public ApiResponse getFilialById(Integer id){
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        return optionalFilial.map(filial -> new ApiResponse("Filial with Id " + id + " is ", true, filial))
                .orElseGet(() -> new ApiResponse("Filial not found", false));
    }

    /**
     * ADD FILIAL
     * @param filialDto
     * @return API RESPONSE
     */
    public ApiResponse addFilial(FilialDto filialDto){

        Filial filial = new Filial();
        filial.setName(filialDto.getName());

        //CREATE DIRECTOR AND SET INFOS
        Staff staff = new Staff();
        staff.setUsername(filialDto.getDirectorUserName());
        staff.setFullName(filialDto.getDirectorFullName());

        //CREATE ROLE FOR DIRECTOR
        Set<Role> roleSet = new HashSet<>();
        Role role = new Role(1, RoleName.ROLE_DIRECTOR);
        roleSet.add(role);

        staff.setRoles(roleSet);
        staff.setPosition(filialDto.getDirectorPosition());
        staff.setFilial(filial);
        staff.setPassword(passwordEncoder.encode(filialDto.getDirectorPassword()));
        filial.setDirector(staff);

        List<Staff> staffList = new ArrayList<>();
        for (String staffUsername : filialDto.getStaffUsernames()){
            Optional<Staff> optionalStaff = staffRepository.findByUsername(staffUsername);
            if (optionalStaff.isEmpty())
                return new ApiResponse("Staff does not exist", false);
            staffList.add(optionalStaff.get());
        }
        filial.setStaffs(staffList);

        filialRepository.save(filial);
        return new ApiResponse("Filial added", true);
    }

    /**
     * EDIT FILIAL
     * @param id
     * @param filialDto
     * @return API RESPONSE
     */
    public ApiResponse editFilial(Integer id, FilialDto filialDto){
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (optionalFilial.isEmpty())
            return new ApiResponse("Filial not found", false);
        Filial filial = optionalFilial.get();
        filial.setName(filialDto.getName());

        //CREATE DIRECTOR AND SET INFOS
        Staff staff = new Staff();
        staff.setUsername(filialDto.getDirectorUserName());
        staff.setFullName(filialDto.getDirectorFullName());

        //CREATE ROLE FOR DIRECTOR
        Set<Role> roleSet = new HashSet<>();
        Role role = new Role(1, RoleName.ROLE_DIRECTOR);
        roleSet.add(role);

        staff.setRoles(roleSet);
        staff.setPosition(filialDto.getDirectorPosition());
        staff.setFilial(filial);
        staff.setPassword(passwordEncoder.encode(filialDto.getDirectorPassword()));
        filial.setDirector(staff);

        List<Staff> staffList = new ArrayList<>();
        for (String staffUsername : filialDto.getStaffUsernames()){
            Optional<Staff> optionalStaff = staffRepository.findByUsername(staffUsername);
            if (optionalStaff.isEmpty())
                return new ApiResponse("Staff does not exist", false);
            staffList.add(optionalStaff.get());
        }
        filial.setStaffs(staffList);

        filialRepository.save(filial);
        return new ApiResponse("Filial edited", true);
    }

    /**
     * DELETE FILIAL
     * @param id
     * @return
     */
    public ApiResponse deleteFilial(Integer id){
        Optional<Filial> optionalFilial = filialRepository.findById(id);
        if (optionalFilial.isEmpty())
            return new ApiResponse("Filial not ofund", false);

        filialRepository.deleteById(id);
        return new ApiResponse("Filial deleted", true);
    }
}









