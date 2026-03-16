package api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserResponse {

    // ─── Single User Fields ───────────────────────────
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private Integer age;
    private String gender;
    private Boolean isDeleted;  // returned only on DELETE
}