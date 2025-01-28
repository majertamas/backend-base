package hu.mikrum.backendbase;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import hu.mikrum.backendbase.teszt.repository.CodeCatalogRepository;
import hu.mikrum.backendbase.teszt.service.SqlExecutorService;
import jakarta.persistence.EntityManager;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.testng.annotations.Test;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


public class PGCodeCatalogControllerTest extends PGBackendBaseApplicationTests {
    public static final String API_CODE_CATALOG = "/api/code-catalog";
    public static final String TEST_KEY_1 = "test-key";
    public static final String TEST_VALUE_1_1 = "Test Value 1";
    public static final String TEST_VALUE_2_1 = "Test Value 2";
    public static final String ADDITIONAL_TEST_INFO_1 = "Additional test info";
    public static final String TEST_DESCRIPTION_1 = "Test description";
    public static final String TEST_ACCESS_PATH_1 = "$.TESZT";
    public static final String EN = "En";
    public static final String HU = "Hu";
    public static final String ENGLISH_NAME = "English Name";
    public static final String HUNGARIAN_NAME = "Magyar Név";
    public static final String LANGUAGE_ACCESS_PATH = "$.Language.";
    public static final String ENGLISH = "English";
    public static final String HUNGARIAN = "Hungarian";
    public static final Map<String, String> LANG_MAP = Map.of(
            LANGUAGE_ACCESS_PATH + EN, ENGLISH_NAME,
            LANGUAGE_ACCESS_PATH + HU, HUNGARIAN_NAME
    );

    @Autowired
    private CodeCatalogRepository codeCatalogRepository;

    @Autowired
    private SqlExecutorService sqlExecutorService;

    @Autowired
    private EntityManager entityManager;

    @Test
    public void test_save() throws Exception {
        CodeCatalogEntity createdEntity = init();
        assert createdEntity.getId() != null;
        assert createdEntity.getKey().equals(TEST_KEY_1);
        assert createdEntity.getValue1().equals(TEST_VALUE_1_1);
        assert createdEntity.getValue2().equals(TEST_VALUE_2_1);
        assert createdEntity.getAdditionalInfo().equals(ADDITIONAL_TEST_INFO_1);
        assert createdEntity.getDescription().equals(TEST_DESCRIPTION_1);
        assert createdEntity.getAccessPath().equals(TEST_ACCESS_PATH_1);
        assert createdEntity.getLang().size() == 2;
        assert createdEntity.getLang().get(LANGUAGE_ACCESS_PATH + EN).equals(ENGLISH_NAME);
        assert createdEntity.getLang().get(LANGUAGE_ACCESS_PATH + HU).equals(HUNGARIAN_NAME);
    }

    @Test
    public void testDeleteCodeCatalogCascadesToLangTable() throws Exception {
        CodeCatalogEntity createdEntity = init();
        int countCodeCatalog = sqlExecutorService.countCodeCatalog();
        assert countCodeCatalog == 3;
        codeCatalogRepository.deleteById(createdEntity.getId());
        int countCodeCatalogLang = sqlExecutorService.countCodeCatalogLang();
        assert countCodeCatalogLang == 0;
        assert codeCatalogRepository.findAll().size() == 2;
    }

    @Test
    public void aaa() throws Exception {
        CodeCatalogEntity createdEntity = init();

        Set<String> langKeys = new HashSet<>(createdEntity.getLang().keySet());
        assert langKeys.size() == 2;

        Set<Integer> langEntityIds = codeCatalogRepository
                .findAll()
                .stream()
                .filter(item -> langKeys.contains(item.getAccessPath()))
                .map(CodeCatalogEntity::getId)
                .collect(Collectors.toSet());
        assert langEntityIds.size() == 2;

        assert codeCatalogRepository.findAll().size() == 3;
        assert codeCatalogRepository.findAllById(langEntityIds).size() == 2;


        try {
            codeCatalogRepository.deleteAllById(langEntityIds);
            entityManager.flush();
        } catch (Exception e) {
            assert e instanceof ConstraintViolationException;
        }
    }

    private CodeCatalogEntity init() throws Exception {
        CodeCatalogEntity langEn = createLangEn();
        String langEnJson = objectMapper.writeValueAsString(langEn);
        doPost(API_CODE_CATALOG, langEnJson);

        CodeCatalogEntity langHu = createLangHu();
        String langHuJson = objectMapper.writeValueAsString(langHu);
        doPost(API_CODE_CATALOG, langHuJson);

        CodeCatalogEntity codeCatalogEntity = createCodeCatalogEntity();
        String codeCatalogEntityJson = objectMapper.writeValueAsString(codeCatalogEntity);
        MvcResult saveResult = doPost(API_CODE_CATALOG, codeCatalogEntityJson);
        String contentAsString = saveResult.getResponse().getContentAsString();
        return objectMapper.readValue(contentAsString, CodeCatalogEntity.class);
    }

    private CodeCatalogEntity createLangEn() {
        return CodeCatalogEntity.builder()
                .key(EN)
                .value1(ENGLISH)
                .accessPath(LANGUAGE_ACCESS_PATH + EN)
                .validFrom(LocalDateTime.now())
                .build();
    }

    private CodeCatalogEntity createLangHu() {
        return CodeCatalogEntity.builder()
                .key(HU)
                .value1(HUNGARIAN)
                .accessPath(LANGUAGE_ACCESS_PATH + HU)
                .validFrom(LocalDateTime.now())
                .build();
    }

    private CodeCatalogEntity createCodeCatalogEntity() {
        Map<String, String> lang = new HashMap<>();

        codeCatalogRepository
                .findAll()
                .stream()
                .filter(item -> item.getAccessPath().startsWith(LANGUAGE_ACCESS_PATH))
                .forEach(codeCatalogEntity -> {
                    String langCode = codeCatalogEntity.getAccessPath();
                    lang.put(langCode, LANG_MAP.get(langCode));
                });


        return CodeCatalogEntity.builder()
                .key(TEST_KEY_1)
                .value1(TEST_VALUE_1_1)
                .value2(TEST_VALUE_2_1)
                .additionalInfo(ADDITIONAL_TEST_INFO_1)
                .description(TEST_DESCRIPTION_1)
                .accessPath(TEST_ACCESS_PATH_1)
                .validFrom(LocalDateTime.now())
                .lang(lang)
                .build();
    }
}