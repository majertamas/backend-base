package hu.mikrum.backendbase.teszt.service;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import hu.mikrum.backendbase.teszt.repository.CodeCatalogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CodeCatalogService {

    private final CodeCatalogRepository codeCatalogRepository;

    public CodeCatalogEntity saveItem(CodeCatalogEntity codeCatalogEntity) {
        return codeCatalogRepository.save(codeCatalogEntity);
    }

    public CodeCatalogEntity getItemById(Integer id) {
        CodeCatalogEntity codeCatalogEntity = codeCatalogRepository.findById(id).orElse(null);
        if (codeCatalogEntity == null) {
            return null;
        }

        if (codeCatalogEntity.getAccessPath().startsWith("$.Language.")) {
            return codeCatalogEntity;
        }

        Set<String> entityLangKeys = codeCatalogEntity.getLang().keySet();
        List<CodeCatalogEntity> langsInDb = codeCatalogRepository
                .findAll()
                .stream()
                .filter(item -> item.getAccessPath().startsWith("$.Language."))
                .toList();

        for (CodeCatalogEntity langInDb : langsInDb) {
            if (!entityLangKeys.contains(langInDb.getAccessPath())) {
                codeCatalogEntity.getLang().put(langInDb.getAccessPath(), codeCatalogEntity.getKey());
            }
        }

        return codeCatalogEntity;
    }

    public void deleteItem(Integer id) {
        codeCatalogRepository.deleteById(id);
    }

    public CodeCatalogEntity updateItem(CodeCatalogEntity codeCatalogEntity) {
        return codeCatalogRepository.save(codeCatalogEntity);
    }

    public List<CodeCatalogEntity> getAllItems() {
        List<Integer> allIds = codeCatalogRepository.findAll().stream().map(CodeCatalogEntity::getId).toList();
        List<CodeCatalogEntity> all = new LinkedList<>();
        for (Integer id : allIds) {
            all.add(getItemById(id));
        }
        return all;
    }

}
