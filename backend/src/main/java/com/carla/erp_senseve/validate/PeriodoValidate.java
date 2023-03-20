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
public class PeriodoValidate {
    //Long or null id
    @Nullable
    private Long id;
    @NotBlank(message = "El nombre de la gestión es requerido")
    private String nombre;
    @NotNull(message = "La fecha inicio de la gestión es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd") //Examaple 2021-01-01
    private Date fecha_inicio;
    @NotNull(message = "La fecha fin de la gestión es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd") //Examaple 2021-01-01
    private Date fecha_fin;
    @NotNull(message = "La empresa es requerida")
    private long gestion_id;

}
