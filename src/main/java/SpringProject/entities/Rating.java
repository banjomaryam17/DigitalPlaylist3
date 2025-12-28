package SpringProject.entities;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Rating implements Comparable<Rating> {

    // unique rating is  it will be  username + songID
    @EqualsAndHashCode.Include
    private String username;

    @EqualsAndHashCode.Include
    private int songID;

    private double userRating;

    // Sort by rating low to high
    @Override
    public int compareTo(Rating o) {
        return Double.compare(this.userRating, o.userRating);
    }
}
