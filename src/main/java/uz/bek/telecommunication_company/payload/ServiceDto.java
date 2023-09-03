package uz.bek.telecommunication_company.payload;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ServiceDto {

    private String name;

    private double price;

    private Integer serviceCategoryID;

    private Timestamp expiredDate;

    private String categoryName;
}
