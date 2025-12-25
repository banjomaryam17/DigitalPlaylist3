package SpringProject.entities;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@NonNull
@AllArgsConstructor

public class PlaylistsSongs {
// id, playlistId, songId,addedDate, positionInPlaylist

    @EqualsAndHashCode.Include
    @NonNull
    private int id;

    @NonNull
    @NotBlank(message = "playlistId is required")
    private int playlistId;

    @NonNull
    private int songId;

    @NonNull
    private Date addedDate;

    @NonNull
    private int positionInPlaylist;
}
