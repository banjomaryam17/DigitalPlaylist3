package SpringProject.entities;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)

public class Genre implements Comparable<Genre> {
//    id,name,description

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private int id;

    @NonNull
    @NotBlank(message = "Genre name is required")
    private String name;

    @NonNull
    @NotBlank(message = "Genre description is required")
    private String description;

    public static boolean deepEquals(Genre g1, Genre g2) {
        return Objects.equals(g1.id, g2.id)
                && Objects.equals(g1.name, g2.name)
                && Objects.equals(g1.description, g2.description);
    }

    @Override
    public int compareTo(Genre g) {
        if (id < g.id) {
            return -1;
        } else if (id > g.id) {
            return 1;
        }
        return 0;
    }
}


