package com.pickeat.ports.in;

import com.pickeat.domain.Mesa;
import com.pickeat.domain.MesaId;

/**
 * Caso de uso para restaurar mesas eliminadas lógicamente.
 */
public interface RestoreMesaUseCase {
    /**
     * Restaura la mesa indicada y limpia el estado eliminado.
     *
     * @param id identificador de la mesa.
     * @return mesa restaurada.
     */
    Mesa restore(MesaId id);
}
