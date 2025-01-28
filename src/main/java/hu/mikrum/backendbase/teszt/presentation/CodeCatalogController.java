package hu.mikrum.backendbase.teszt.presentation;

import hu.mikrum.backendbase.teszt.model.CodeCatalogEntity;
import hu.mikrum.backendbase.teszt.service.CodeCatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/code-catalog")
@RequiredArgsConstructor
public class CodeCatalogController {

    private final CodeCatalogService codeCatalogService;

    @PostMapping
    public ResponseEntity<CodeCatalogEntity> saveItem(@RequestBody CodeCatalogEntity codeCatalogEntity) {
        return ResponseEntity.ok(codeCatalogService.saveItem(codeCatalogEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CodeCatalogEntity> getItemById(@PathVariable Integer id) {
        return ResponseEntity.ok(codeCatalogService.getItemById(id));
    }

    @GetMapping
    public ResponseEntity<List<CodeCatalogEntity>> getAllItems() {
        return ResponseEntity.ok(codeCatalogService.getAllItems());
    }
}
