package SpringProject.entities;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscription {


    @EqualsAndHashCode.Include
    private String username;
    @EqualsAndHashCode.Exclude
    private LocalDateTime sub_startDate;
    private LocalDateTime sub_endDate;




}
