package SpringProject.entities;
import lombok.*;
import jakarta.validation.constraints.*;

import java.util.*;

@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString
@AllArgsConstructor

public class Playlists_songs {
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
