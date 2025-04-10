package org.api.rest_api_grupo2.repository;

import java.util.Optional;

import org.api.rest_api_grupo2.model.Cart;
import org.api.rest_api_grupo2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUserId(User user);
}
