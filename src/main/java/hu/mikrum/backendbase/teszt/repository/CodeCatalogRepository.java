package hu.mikrum.backendbase.teszt.repository;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CodeCatalogRepository extends JpaRepository<CodeCatalogEntity, Integer> {

    Optional<CodeCatalogEntity> findByAccessPath(String accessPath);

}
