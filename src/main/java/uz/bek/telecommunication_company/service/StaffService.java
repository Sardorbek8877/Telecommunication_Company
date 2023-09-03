package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Filial;
import uz.bek.telecommunication_company.entity.Role;
import uz.bek.telecommunication_company.entity.Staff;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.StaffDto;
import uz.bek.telecommunication_company.repository.FilialRepository;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.StaffRepository;
import uz.bek.telecommunication_company.security.JwtProvider;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class StaffService {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    FilialRepository filialRepository;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    PasswordEncoder passwordEncoder;

    public ApiResponse addStaff(StaffDto staffDto){
        Optional<Role> optionalRole = roleRepository.findById(staffDto.getRoleId());
        if (optionalRole.isEmpty()){
            return new ApiResponse("Such Role doesnt exist",false);
        }
        Optional<Filial> optionalFilial = filialRepository.findById(staffDto.getFilialId());
        if (optionalFilial.isEmpty()){
            return new ApiResponse("Such Filial doesnt exist",false);
        }
        Staff staff=new Staff();
        staff.setFullName(staffDto.getFullName());
        staff.setUsername(staffDto.getUserName());
        Set<Role> roles=new HashSet<>();
        roles.add(optionalRole.get());
        staff.setRoles(roles);
        staff.setFilial(optionalFilial.get());
        staff.setPosition(staffDto.getPosition());
        staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        staffRepository.save(staff);
        return new ApiResponse("Staff added!",true);
    }

    public List<Staff> getStaffList(){
        List<Staff> staffList = staffRepository.findAll();
        return staffList;
    }

    public ApiResponse getStaff(){
        Staff staff = (Staff) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<Staff> optionalStaff = staffRepository.findById(staff.getId());
        if (!optionalStaff.isPresent()){
            return new ApiResponse("Such staff doesnt exist",false);
        }
        return new ApiResponse("=>",true,staff);
    }

    public ApiResponse editStaff(String username,StaffDto staffDto){
        Optional<Staff> optionalStaff = staffRepository.findByUsername(username);
        if (optionalStaff.isEmpty()){
            return new ApiResponse("Such Staff doesnt exist",false);
        }
        Optional<Filial> optionalFilial = filialRepository.findById(staffDto.getFilialId());
        if (optionalFilial.isEmpty()){
            return new ApiResponse("Such Filial doesnt exist",false);
        }
        Staff staff = optionalStaff.get();
        staff.setFullName(staffDto.getFullName());
        boolean existsByUserName = staffRepository.existsByUsername(staff.getUsername());
        if (existsByUserName){
            return new ApiResponse("Such staff already exist",false);
        }
        staff.setUsername(staffDto.getUserName());
        staff.setFilial(optionalFilial.get());
        staff.setPosition(staffDto.getPosition());
        staff.setPassword(passwordEncoder.encode(staffDto.getPassword()));
        staffRepository.save(staff);
        return new ApiResponse("Staff edited!",true);
    }


    public ApiResponse deleteStaff(String username){
        Optional<Staff> optionalStaff = staffRepository.findByUsername(username);
        if (optionalStaff.isEmpty()){
            return new ApiResponse("Such staff doesnt exist",false);
        }
        staffRepository.delete(optionalStaff.get());
        return new ApiResponse("Staff deleted",true);
    }

}
