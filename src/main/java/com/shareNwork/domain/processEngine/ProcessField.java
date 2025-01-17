package com.shareNwork.domain.processEngine;

import com.shareNwork.domain.constants.FieldType;
import io.quarkus.hibernate.orm.panache.PanacheEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Data
@Entity
@EqualsAndHashCode(callSuper = false)
public class ProcessField extends PanacheEntity {
    @ManyToOne
    private Process process;
    private String  fieldName;
    private FieldType fieldType;
}
