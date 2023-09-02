package uz.bek.telecommunication_company.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Filial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne
    private Staff director;

    @OneToMany(mappedBy = "filial", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Staff> staffs;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp createdAt;

    @CreationTimestamp
    private Timestamp updatedAt;

    @CreatedBy
    @Column(updatable = false)
    private UUID createdBy;

    @LastModifiedBy
    private UUID updatedBy;
}
