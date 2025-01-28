package hu.mikrum.backendbase.teszt.repository;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeCatalogRepository extends JpaRepository<CodeCatalogEntity, Integer> {
}
