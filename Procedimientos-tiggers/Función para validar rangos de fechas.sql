-- Función para validar rangos de fechas
CREATE OR REPLACE FUNCTION fn_validar_rango_fechas(
    p_fecha_inicio IN DATE,
    p_fecha_fin IN DATE,
    p_max_dias IN NUMBER DEFAULT 365
) RETURN VARCHAR2 AS
    v_dias_diferencia NUMBER;
BEGIN
    -- Calcular diferencia en días
    v_dias_diferencia := p_fecha_fin - p_fecha_inicio + 1;
    
    -- Validaciones
    IF p_fecha_inicio IS NULL OR p_fecha_fin IS NULL THEN
        RETURN 'ERROR: Las fechas no pueden ser nulas';
    END IF;
    
    IF p_fecha_inicio > p_fecha_fin THEN
        RETURN 'ERROR: La fecha de inicio no puede ser mayor a la fecha fin';
    END IF;
    
    IF v_dias_diferencia > p_max_dias THEN
        RETURN 'ERROR: El rango no puede ser mayor a ' || p_max_dias || ' días';
    END IF;
    
    IF p_fecha_fin > SYSDATE THEN
        RETURN 'ADVERTENCIA: La fecha fin es futura';
    END IF;
    
    RETURN 'OK';
    
EXCEPTION
    WHEN OTHERS THEN
        RETURN 'ERROR: ' || SQLERRM;
END;
/