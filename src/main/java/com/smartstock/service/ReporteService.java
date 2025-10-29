package com.smartstock.service;

import com.smartstock.dto.ReporteInventarioDTO;
import net.sf.jasperreports.engine.JRException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

public interface ReporteService {
    void generarReporteInventario(HttpServletResponse response) throws JRException, IOException;
    void generarReporteVentas(LocalDate fechaInicio, LocalDate fechaFin, HttpServletResponse response) throws JRException, IOException;
    List<ReporteInventarioDTO> obtenerDatosReporteInventario();
}