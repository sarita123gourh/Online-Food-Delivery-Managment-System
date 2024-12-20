package com.db.services;

import com.db.entities.Restaurant;
import com.db.repo.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RestaurantServiceImpl implements RestaurantService{
    @Autowired
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @Override
    public Restaurant save(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

    @Override
    public ResponseEntity<Iterable<Restaurant>> findAll() {
        return (ResponseEntity<Iterable<Restaurant>>) restaurantRepository.findAll();
    }

    @Override
    public Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }


    @Override
    public Optional<Restaurant> findById(Long id) {
        return restaurantRepository.findById(id);
    }

    @Override
    public void deleteRestaurantById(Long id) {
        restaurantRepository.deleteById(id);
    }

    @Override
    public Restaurant updateRestaurant(Long id, Restaurant updatedRestaurant) {
        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Restaurant not found with id: " + id));

        existingRestaurant.setRestaurantName(updatedRestaurant.getRestaurantName());
        existingRestaurant.setRestaurantAddress(updatedRestaurant.getRestaurantAddress());
        existingRestaurant.setRestaurantPhone(updatedRestaurant.getRestaurantPhone());
        existingRestaurant.setApproved(updatedRestaurant.isApproved());

        return restaurantRepository.save(existingRestaurant);

    }
}
