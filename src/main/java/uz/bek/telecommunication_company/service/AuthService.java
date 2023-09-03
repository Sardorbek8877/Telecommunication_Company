package uz.bek.telecommunication_company.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.bek.telecommunication_company.entity.Client;
import uz.bek.telecommunication_company.entity.SimCard;
import uz.bek.telecommunication_company.entity.Staff;
import uz.bek.telecommunication_company.payload.ApiResponse;
import uz.bek.telecommunication_company.payload.LoginDto;
import uz.bek.telecommunication_company.repository.ClientRepository;
import uz.bek.telecommunication_company.repository.RoleRepository;
import uz.bek.telecommunication_company.repository.SimCardRepository;
import uz.bek.telecommunication_company.repository.StaffRepository;
import uz.bek.telecommunication_company.security.JwtProvider;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    StaffRepository staffRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SimCardRepository simCardRepository;

    /**
     * LOGIN FOR STAFF
     * @param dto
     * @return API RESPONSE
     */
    public ApiResponse loginForStaff(LoginDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (dto.getUsername(), dto.getPassword()));
            Staff employee = (Staff) authentication.getPrincipal();
            String token = jwtProvider.generateToken(dto.getUsername(), employee.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Username or password wrong", false);
        }
    }

    /**
     * LOGIN FOR CLIENT
     * @param dto
     * @return API RESPONSE
     */
    public ApiResponse loginForClient(LoginDto dto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken
                    (dto.getUsername(), dto.getPassword()));
            Client client = (Client) authentication.getPrincipal();
            String token = jwtProvider.generateToken(dto.getUsername(), client.getRoles());
            return new ApiResponse("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new ApiResponse("Username or password wrong", false);
        }

    }

    /**
     * LOAD USER BY USENAME
     * @param username
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Staff> optionalUser = staffRepository.findByUsername(username);
        Optional<SimCard> optionalSimCard = simCardRepository.findByCodeAndNumber(username.substring(0, 2), username.substring(3));
        if (optionalUser.isPresent()) return optionalUser.get();
        if (optionalSimCard.isPresent()) return optionalSimCard.get();
        throw new UsernameNotFoundException(username + "not found");
    }

    public UserDetails loadClientByUsernameFromSimCard(String simCardNumber) {
        SimCard simCard = simCardRepository.findBySimCardNumber(simCardNumber).orElseThrow(() -> new UsernameNotFoundException(simCardNumber));
        return simCard;
    }
}
