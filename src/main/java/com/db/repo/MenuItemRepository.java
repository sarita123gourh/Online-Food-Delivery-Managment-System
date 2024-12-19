package com.db.repo;

import com.db.entities.MenuItem;
import com.db.entities.Restaurant;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface MenuItemRepository extends CrudRepository<MenuItem,Long> {

    List<MenuItem> findByRestaurant(Restaurant restaurant);
    Optional<MenuItem> findByMenuItemIdAndRestaurant(Long menuItemId, Restaurant restaurant);


}
