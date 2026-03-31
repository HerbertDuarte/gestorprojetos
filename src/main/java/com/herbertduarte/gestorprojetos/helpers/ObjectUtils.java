package com.herbertduarte.gestorprojetos.helpers;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ObjectUtils {

    public static <K, V> Map<K, V> mapearResultados(
            List<Object[]> resultados,
            Function<Object[], K> keyMapper,
            Function<Object[], V> valueMapper) {
        return resultados.stream()
                .collect(Collectors.toMap(keyMapper, valueMapper));
    }
}
