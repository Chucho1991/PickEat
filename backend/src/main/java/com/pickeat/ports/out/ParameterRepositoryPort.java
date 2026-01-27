package com.pickeat.ports.out;

import com.pickeat.domain.AppParameter;

import java.util.Optional;

/**
 * Puerto de salida para parametros de configuracion.
 */
public interface ParameterRepositoryPort {
    Optional<AppParameter> findByKey(String key);

    /**
     * Guarda el parametro indicado.
     *
     * @param parameter parametro a persistir.
     * @return parametro guardado.
     */
    AppParameter save(AppParameter parameter);
}
