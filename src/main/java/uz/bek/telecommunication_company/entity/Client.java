package uz.bek.telecommunication_company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.bek.telecommunication_company.entity.enums.ClientType;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Client extends AbsEntity {

    private String passportNumber;

    private String fullName;

    @Enumerated(EnumType.STRING)
    private ClientType clientType;

    @ManyToMany
    private Set<Role> roles;

    @OneToMany(mappedBy = "client", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<SimCard> simCardList;
}
