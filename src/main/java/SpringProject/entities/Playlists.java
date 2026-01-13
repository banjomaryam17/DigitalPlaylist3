package SpringProject.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Playlists {
//    id,userId,name,description,isPublic,createdAt
    @EqualsAndHashCode.Include
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @NotBlank(message= "Id is required")
    private Integer id;

    @EqualsAndHashCode.Include
    @NonNull
    private Integer userId;
    @NotBlank(message = "name is required")
    private String name;
    private String description;
    private Boolean isPublic;
    private Date createdAt;

}
