package org.api.rest_api_grupo2.repository;


import org.api.rest_api_grupo2.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {


    
}
