package SpringProject.entities;
import jakarta.validation.constraints.NotNull;
import lombok.*;
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Artist {
    @EqualsAndHashCode.Exclude
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String bio;
    private String country;
    @NotNull
    private int formedYear;



}
