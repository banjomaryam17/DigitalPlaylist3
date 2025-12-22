package SpringProject.entities;
import lombok.*;
import jakarta.validation.constraints.*;

@Getter
@Builder
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Genre {
//    id,name,description

    @NonNull
    private int id;

    @NonNull
    @NotBlank(message = "Genre name is required")
    private String name;

    @NonNull
    @NotBlank(message = "Genre description is required")
    private String description;

}
