package com.B3B.farmbros.util;

import com.B3B.farmbros.domain.Consulta;

import java.util.Comparator;

public class SortConsultaByTimeStampDescendent implements Comparator<Consulta> {
    @Override
    public int compare(Consulta c1, Consulta c2) {
        if(c1.getFechaConsulta() < c2.getFechaConsulta()) {
            return -1;
        }
        else if (c1.getFechaConsulta() > c2.getFechaConsulta()){
            return 1;
        }
        else {
            return 0;
        }
    }
}
