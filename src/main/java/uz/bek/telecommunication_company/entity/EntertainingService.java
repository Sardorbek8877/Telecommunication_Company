package uz.bek.telecommunication_company.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.bek.telecommunication_company.entity.template.AbsEntity;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EntertainingService extends AbsEntity {
    private String name;

    private double price;

    @ManyToOne(optional = false,fetch = FetchType.EAGER)
    private ServiceCategory serviceCategory;

    private Timestamp expiredDate;
    @ManyToOne
    private SimCard simCard;
    private Integer count;
}
