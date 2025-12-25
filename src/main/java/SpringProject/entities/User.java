package SpringProject.entities;
import lombok.*;



@Getter
@Setter
@ToString(exclude = {"email"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @EqualsAndHashCode.Include
    private String username;

    private String email;
    private String password;
    private int userType;
}


