package uz.bek.telecommunication_company.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilialDto {

    private String name;

    private String DirectorUserName;

    private String DirectorFullName;

    private Integer DirectorRoleId;

    private Integer filialId;

    private String DirectorPosition;

    private String DirectorPassword;

    private List<String> StaffUsernames;
}
