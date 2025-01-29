package hu.mikrum.backendbase.teszt.service;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import hu.mikrum.backendbase.teszt.repository.CodeCatalogRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static hu.mikrum.backendbase.teszt.util.Util.LANGUAGE_ACCESS_PATH;
import static hu.mikrum.backendbase.teszt.util.Util.PREFIX_FOR_MISSING_LANG_VALUE;

@Service
@RequiredArgsConstructor
public class CodeCatalogService {

    private final CodeCatalogRepository codeCatalogRepository;

    @Transactional
    public CodeCatalogEntity saveItem(CodeCatalogEntity codeCatalogEntity) {
        return codeCatalogRepository.save(codeCatalogEntity);
    }

    @Transactional
    public CodeCatalogEntity getItemById(Integer id) {
        CodeCatalogEntity codeCatalogEntity = codeCatalogRepository
                .findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Item not found"));
        if (codeCatalogEntity == null) {
            return null;
        }

        if (codeCatalogEntity.getAccessPath().startsWith(LANGUAGE_ACCESS_PATH)) {
            return codeCatalogEntity;
        }

        Set<String> entityLangKeys = codeCatalogEntity.getLang().keySet();
        List<CodeCatalogEntity> langsInDb = codeCatalogRepository
                .findAll()
                .stream()
                .filter(item -> item.getAccessPath().startsWith(LANGUAGE_ACCESS_PATH))
                .toList();

        for (CodeCatalogEntity langInDb : langsInDb) {
            if (!entityLangKeys.contains(langInDb.getAccessPath())) {
                codeCatalogEntity.getLang().put(langInDb.getAccessPath(), PREFIX_FOR_MISSING_LANG_VALUE + codeCatalogEntity.getKey());
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

    @Transactional
    public List<CodeCatalogEntity> getAllItems() {
        List<Integer> allIds = codeCatalogRepository.findAll().stream().map(CodeCatalogEntity::getId).toList();
        List<CodeCatalogEntity> all = new LinkedList<>();
        for (Integer id : allIds) {
            all.add(getItemById(id));
        }
        return all;
    }

}
