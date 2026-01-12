package SpringProject.entities;
import lombok.*;
import java.util.Objects;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode

public class Song {
    private int id;
    private String title;
    private int artistId;
    private int albumId;
    private int genreId;
    private double durationSeconds;
    private int releaseYear;
}
