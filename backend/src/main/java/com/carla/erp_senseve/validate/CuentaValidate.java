package com.carla.erp_senseve.validate;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Getter
@Setter
public class CuentaValidate {
    //Long or null id
    @Nullable
    private Long id;
    @NotBlank(message = "El nombre de la cuenta es requerido")
    private String nombre;
    @NotNull(message = "La empresa es requerida")
    private long empresa_id;
    @Nullable
    private long padre_id;

}
