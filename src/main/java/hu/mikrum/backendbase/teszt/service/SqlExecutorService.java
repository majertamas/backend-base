package hu.mikrum.backendbase.teszt.service;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SqlExecutorService {

    private final JdbcTemplate jdbcTemplate;

    public int countCodeCatalog() {
        String sql = "SELECT COUNT(*) FROM code_catalog";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public int countCodeCatalogLang() {
        String sql = "SELECT COUNT(*) FROM code_catalog_lang";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }

    public void deleteAllFromCodeCatalog() {
        String sql = "DELETE FROM code_catalog";
        jdbcTemplate.update(sql);
    }

    public void deleteAllFromCodeCatalogLang() {
        String sql = "DELETE FROM code_catalog_lang";
        jdbcTemplate.update(sql);
    }
}