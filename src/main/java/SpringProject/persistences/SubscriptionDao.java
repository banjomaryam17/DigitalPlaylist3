package SpringProject.persistences;

import SpringProject.entities.Subscription;

import java.time.LocalDateTime;
import java.util.ArrayList;

public interface SubscriptionDao {

    public int addSubscription(Subscription subscription);

    public Subscription getSubscriptionFromUsername(String username);

    public boolean updateUserSubscriptionEndDate(LocalDateTime endDate, String username) throws RuntimeException;

    public ArrayList<Subscription> getAllSubscriptions();

}
