package com.consumer.ponto.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Calendar;

@Data
public class InfoPontoDTO {

    private Long id;
    @JsonFormat(pattern="dd/MM/yyyy HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private Calendar dataInclusao;
    private Long codigoFuncionario;
    private Long codigoEmpresa;
    private EStatusPonto statusPonto;


}
