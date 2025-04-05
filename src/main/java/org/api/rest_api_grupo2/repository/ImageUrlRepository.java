package org.api.rest_api_grupo2.repository;

import org.api.rest_api_grupo2.model.ImageUrl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
}
