package com.B3B.farmbros.util;

import com.B3B.farmbros.domain.Mensaje;

import java.util.Comparator;

public class SortMessageByTimeStamp implements Comparator<Mensaje> {
    @Override
    public int compare(Mensaje m1, Mensaje m2) {
        if(m1.getHoraCreacion() < m2.getHoraCreacion()) {
            return -1;
        }
        else if (m1.getHoraCreacion() > m2.getHoraCreacion()){
            return 1;
        }
        else {
            return 0;
        }
    }
}
