package SpringProject.entities;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Album {
    @NotNull
    private int id;
    @NotNull
    private String title;
    @NotNull
    private int artistId;
    @NotNull
    private int genreId;
    @NotNull
    private LocalDate releaseDate;
}