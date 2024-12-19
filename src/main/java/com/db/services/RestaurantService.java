package com.db.services;
import com.db.entities.Restaurant;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface RestaurantService {

    Restaurant save(Restaurant restaurant);
    ResponseEntity<Iterable<Restaurant>> findAll();
    Iterable<Restaurant> getAllRestaurants();
    public Optional<Restaurant> findById(Long id);
    public void deleteRestaurantById(Long id);
    public Restaurant updateRestaurant(Long id,Restaurant updatedRestaurant);
}
