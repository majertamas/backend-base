package hu.mikrum.backendbase.teszt.service;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import hu.mikrum.backendbase.teszt.repository.CodeCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CodeCatalogService {

    private final CodeCatalogRepository codeCatalogRepository;

    public CodeCatalogEntity saveItem(CodeCatalogEntity codeCatalogEntity) {
        return codeCatalogRepository.save(codeCatalogEntity);
    }

    public CodeCatalogEntity getItemById(Integer id) {
        return codeCatalogRepository.findById(id).orElse(null);
    }

    public void deleteItem(Integer id) {
        codeCatalogRepository.deleteById(id);
    }

    public CodeCatalogEntity updateItem(CodeCatalogEntity codeCatalogEntity) {
        return codeCatalogRepository.save(codeCatalogEntity);
    }

    public List<CodeCatalogEntity> getAllItems(String key) {
        return codeCatalogRepository.findAll().stream()
                .filter(codeCatalogEntity -> codeCatalogEntity.getKey().equals(key))
                .toList();
    }


}
