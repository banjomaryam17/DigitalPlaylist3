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

    @EqualsAndHashCode.Include
    private String username;
    private int songID;
    private double userRating;

    @Override
    public int compareTo(Rating o) {

         int y= 0;
          if (this.userRating > o.userRating) {
               return y=1;
          } else if (this.userRating < o.userRating) {
              y = 0;
          }
        return 0;
    }
}

