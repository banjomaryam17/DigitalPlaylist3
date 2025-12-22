package SpringProject.entities;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Getter
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Playlists {
//    id,userId,name,description,isPublic,createdAt
    @EqualsAndHashCode.Include
    @NonNull
    @NotBlank(message= "Id is required")
    private int id;

    @EqualsAndHashCode.Include
    @NonNull
    @NotBlank(message = "userId id required")
    private int userId;

    @NonNull
    private String name;

    @NonNull
    private String description;

    @NonNull
    private boolean isPublic;

    @NonNull
    private Date createdAt;

}
