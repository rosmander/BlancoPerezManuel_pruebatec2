<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.company.pruebatec2.logica.Turno" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Turnos</title>
    <link href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" rel="stylesheet">
    <script>
        // Función para validar el formulario de ciudadano
        function validarFormularioCiudadano() {
            const nombre = document.getElementById('nombre_ciudadano').value;
            const apellido = document.getElementById('apellido_ciudadano').value;

            // Validar que el nombre no esté vacío
            if (nombre.trim() === "") {
                alert("El nombre del ciudadano no puede estar vacío.");
                return false;
            }

            // Validar que el apellido no esté vacío
            if (apellido.trim() === "") {
                alert("El apellido del ciudadano no puede estar vacío.");
                return false;
            }

            return true;
        }

        // Función para validar el formulario de turno
        function validarFormularioTurno() {
            const numeroTurno = document.getElementById('numero_turno').value;
            const descripcionTramite = document.getElementById('descripcion_tramite').value;
            const ciudadanoId = document.getElementById('ciudadano_id').value;

            // Validar que el número de turno sea un valor numérico positivo
            if (isNaN(numeroTurno) || numeroTurno <= 0) {
                alert("El número de turno debe ser un valor numérico positivo.");
                return false;
            }

            // Validar que la descripción del trámite no esté vacía
            if (descripcionTramite.trim() === "") {
                alert("La descripción del trámite no puede estar vacía.");
                return false;
            }

            // Validar que el ID del ciudadano sea un valor numérico positivo
            if (isNaN(ciudadanoId) || ciudadanoId <= 0) {
                alert("El ID del ciudadano debe ser un valor numérico positivo.");
                return false;
            }

            return true;
        }
    </script>
</head>
<body>
<div class="container">
    <h1 class="mt-5">Gestión de Turnos</h1>
    <hr/>
    
    <!-- Mostrar mensajes de éxito o error si están presentes -->
    <%
        String mensajeExitoCiudadano = (String) request.getAttribute("mensajeExito");
        String mensajeExitoTurno = (String) request.getAttribute("mensajeExitoTurno");
        String errorMensaje = (String) request.getAttribute("errorMensaje");
        
        if (mensajeExitoCiudadano != null) {
    %>
    <div class="alert alert-success" role="alert">
        <%= mensajeExitoCiudadano %>
    </div>
    <%
        } else if (mensajeExitoTurno != null) {
    %>
    <div class="alert alert-success" role="alert">
        <%= mensajeExitoTurno %>
    </div>
    <%
        } else if (errorMensaje != null) {
    %>
    <div class="alert alert-danger" role="alert">
        <%= errorMensaje %>
    </div>
    <%
        }
    %>
    
    <!-- Formulario para agregar nuevo ciudadano -->
    <div class="card mt-3">
        <div class="card-header">
            Agregar Nuevo Ciudadano
        </div>
        <div class="card-body">
            <form action="SvCiudadano" method="post" onsubmit="return validarFormularioCiudadano()">
                <div class="form-group">
                    <label for="nombre_ciudadano">Nombre:</label>
                    <input type="text" class="form-control" id="nombre_ciudadano" name="nombre_ciudadano" required>
                </div>
                <div class="form-group">
                    <label for="apellido_ciudadano">Apellido:</label>
                    <input type="text" class="form-control" id="apellido_ciudadano" name="apellido_ciudadano" required>
                </div>
                <button type="submit" class="btn btn-primary">Agregar Ciudadano</button>
            </form>
        </div>
    </div>

    <!-- Formulario para agregar nuevo turno -->
    <div class="card mt-3">
        <div class="card-header">
            Agregar Nuevo Turno
        </div>
        <div class="card-body">
            <form action="SvTurno" method="post" onsubmit="return validarFormularioTurno()">
                <div class="form-group">
                    <label for="numero_turno">Número de Turno:</label>
                    <input type="number" class="form-control" id="numero_turno" name="numero_turno" required>
                </div>
                <div class="form-group">
                    <label for="fecha_turno">Fecha:</label>
                    <input type="date" class="form-control" id="fecha_turno" name="fecha_turno" required>
                </div>
                <div class="form-group">
                    <label for="descripcion_tramite">Descripción del Trámite:</label>
                    <input type="text" class="form-control" id="descripcion_tramite" name="descripcion_tramite" required>
                </div>
                <div class="form-group">
                    <label for="estado_turno">Estado:</label>
                    <select class="form-control" id="estado_turno" name="estado_turno" required>
                        <option value="En espera">En espera</option>
                        <option value="Ya atendido">Ya atendido</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="ciudadano_id">ID del Ciudadano:</label>
                    <input type="number" class="form-control" id="ciudadano_id" name="ciudadano_id" required>
                </div>
                <button type="submit" class="btn btn-primary">Agregar Turno</button>
            </form>
        </div>
    </div>

    <!-- Lista de turnos -->
    <div class="card mt-3" id="resultados">
        <div class="card-header">
            Listado de Turnos
        </div>
        <div class="card-body">
            <form action="SvTurno#resultados" method="get">
                <div class="form-group">
                    <label for="fecha_busqueda">Fecha:</label>
                    <input type="date" class="form-control" id="fecha_busqueda" name="fecha_busqueda" required>
                </div>
                <div class="form-group">
                    <label for="estado_busqueda">Estado:</label>
                    <select class="form-control" id="estado_busqueda" name="estado_busqueda" required>
                        <option value="En espera">En espera</option>
                        <option value="Ya atendido">Ya atendido</option>
                    </select>
                </div>
                <button type="submit" class="btn btn-primary">Buscar Turnos</button>
            </form>
            <hr/>
            
            <!-- Mostrar mensaje cuando no hay resultados -->
            <%
                String mensajeSinResultados = (String) request.getAttribute("mensajeSinResultados");
                if (mensajeSinResultados != null) {
            %>
            <div class="alert alert-info" role="alert">
                <%= mensajeSinResultados %>
            </div>
            <%
                } else {
                    List<Turno> turnos = (List<Turno>) request.getAttribute("turnos");
                    if (turnos != null && !turnos.isEmpty()) {
            %>
            <table class="table table-striped mt-3">
                <thead>
                    <tr>
                        <th>Número</th>
                        <th>Fecha</th>
                        <th>Descripción del Trámite</th>
                        <th>Estado</th>
                        <th>Ciudadano</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        for (Turno turno : turnos) {
                    %>
                    <tr>
                        <td><%= turno.getNumero() %></td>
                        <td><%= turno.getFechaFormateada() %></td>
                        <td><%= turno.getDescripcionDelTramite() %></td>
                        <td><%= turno.getEstado() %></td>
                        <td><%= turno.getCiudadano().getNombre() + " " + turno.getCiudadano().getApellido() %></td>
                    </tr>
                    <%
                        }
                    %>
                </tbody>
            </table>
            <%
                    }
                }
            %>
        </div>
    </div>
</div>
</body>
</html>
