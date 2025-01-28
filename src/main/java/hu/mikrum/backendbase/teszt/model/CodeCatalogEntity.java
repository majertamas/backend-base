package hu.mikrum.backendbase.teszt.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapKeyColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "code_catalog")
@Data
@EqualsAndHashCode
@AllArgsConstructor
@ToString
@Builder
public class CodeCatalogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code_catalog_id")
    private Integer id;

    @NotBlank
    @Column(name = "code_catalog_key")
    private String key;

    @Column(name = "value1")
    private String value1;

    @Column(name = "value2")
    private String value2;

    @Column(name = "additional_info")
    private String additionalInfo;

    @Column(name = "description")
    private String description;

    @Column(name = "order_of_display")
    private Integer orderOfDisplay;

    @Column(name = "access_path")
    private String accessPath;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent")
    private CodeCatalogEntity parent;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "parent")
    private Set<CodeCatalogEntity> children;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "code_catalog_tags", joinColumns = @JoinColumn(name = "code_catalog_id"))
    @Column(name = "tags")
    private Set<String> tags;

    @Column(name = "code_catalog_constraint")
    private String constraint;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "code_catalog_related_items", joinColumns = @JoinColumn(name = "code_catalog_id"))
    @Column(name = "related_items")
    private Set<String> relatedItems;

    @Column(name = "valid_from")
    private LocalDateTime validFrom;

    @Column(name = "valid_to")
    private LocalDateTime validTo;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "code_catalog_access_right_read", joinColumns = @JoinColumn(name = "code_catalog_id"))
    @Column(name = "access_right_read")
    private List<String> accessRightRead;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "code_catalog_access_right_write", joinColumns = @JoinColumn(name = "code_catalog_id"))
    @Column(name = "access_right_write")
    private List<String> accessRightWrite;

    @ElementCollection
    @CollectionTable(
            name = "code_catalog_lang",
            joinColumns = @JoinColumn(name = "code_catalog_id")
    )
    @MapKeyColumn(name = "lang_code")
    @Column(name = "lang_value")
    private Map<String, String> lang = new HashMap<>();

    public CodeCatalogEntity() {
        this.children = new HashSet<>();
        this.tags = new HashSet<>();
        this.relatedItems = new HashSet<>();
        this.accessRightRead = new ArrayList<>();
        this.accessRightWrite = new ArrayList<>();
    }
}
