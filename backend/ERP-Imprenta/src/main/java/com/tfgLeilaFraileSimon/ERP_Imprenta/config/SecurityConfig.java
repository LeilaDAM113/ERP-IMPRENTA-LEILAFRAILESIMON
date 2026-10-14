package com.tfgLeilaFraileSimon.ERP_Imprenta.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.tfgLeilaFraileSimon.ERP_Imprenta.model.RolTrabajador;
import jakarta.servlet.http.HttpServletResponse;

// Configuracion de la seguridad de la API:
// - las contraseñas se guardan cifradas con BCrypt
// - para entrar se hace login en /auth/login y te devuelve un token
// - para usar /api/... hay que mandar ese token en cada peticion
// - el servidor no guarda sesiones, cada peticion lleva su token
// - @EnableMethodSecurity es para que funcionen los @PreAuthorize
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    // Los roles los saco del enum RolTrabajador, asi no los escribo a mano
    // y si algun dia cambio un rol, el compilador me avisa aqui.
    // .name() da el nombre del rol como texto ("ADMIN", "COMERCIAL"...)
    private static final String ADMIN = RolTrabajador.ADMIN.name();
    private static final String COMERCIAL = RolTrabajador.COMERCIAL.name();
    private static final String ENCARGADO = RolTrabajador.ENCARGADO_TALLER.name();
    private static final String OPERARIO = RolTrabajador.OPERARIO.name();
    private static final String TRANSPORTISTA = RolTrabajador.TRANSPORTISTA.name();
    @Value("${app.security.require-https:false}")
    private boolean exigirHttps;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // avisa cada vez que alguien hace login (bien o mal).
    // LoginAttemptListener lo escucha para contar los fallos y bloquear a los 5
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publicador) {
        return new DefaultAuthenticationEventPublisher(publicador);
    }

    // lo que comprueba el email y la contraseña en el login (lo usa AuthController).
    // busca al trabajador con TrabajadorService y compara la contraseña con BCrypt
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService usuarios, PasswordEncoder passwordEncoder,
            AuthenticationEventPublisher publicador) {
        DaoAuthenticationProvider proveedor = new DaoAuthenticationProvider(usuarios);
        proveedor.setPasswordEncoder(passwordEncoder);
        ProviderManager manager = new ProviderManager(proveedor);
        manager.setAuthenticationEventPublisher(publicador);
        return manager;
    }

    // saca el rol que va dentro del token para saber que permisos tiene
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("rol");
        roles.setAuthorityPrefix("");
        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(roles);
        return conversor;
    }

    // Quien puede entrar a cada sitio:
    // - el login y Swagger: cualquiera
    // - /api/...: hace falta el token
    // - todo lo demas: prohibido
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // lo que se responde si no hay token o el token no vale (401)
        AuthenticationEntryPoint noAutenticado = (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"Tienes que iniciar sesion\"}");
        };

        // lo que se responde si el rol no tiene permiso (403)
        AccessDeniedHandler sinPermiso = (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"No tienes permiso para hacer esta operacion\"}");
        };

        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/error").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/api/me").authenticated()

                        // ---- PERMISOS POR ROL ----
                        // Primero digo quien puede CONSULTAR (GET) y luego
                        // quien puede crear, modificar y borrar (todo lo demas).

                        // Trabajadores y puestos: los ve el encargado, los gestiona el admin
                        .requestMatchers(HttpMethod.GET, "/api/trabajador/**", "/api/puesto/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)
                        .requestMatchers("/api/trabajador/**", "/api/puesto/**").hasAuthority(ADMIN)

                        // Clientes: los gestiona el comercial
                        .requestMatchers(HttpMethod.GET, "/api/cliente/**", "/api/empresa/**",
                                "/api/particular/**", "/api/contacto-empresa/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, TRANSPORTISTA)
                        .requestMatchers("/api/cliente/**", "/api/empresa/**", "/api/particular/**",
                                "/api/contacto-empresa/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)

                        // Catalogo de productos: lo cambia solo el admin
                        .requestMatchers(HttpMethod.GET, "/api/catalogo/**").hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO)
                        .requestMatchers("/api/catalogo/**").hasAuthority(ADMIN)

                        // Presupuestos: los hace el comercial
                        .requestMatchers(HttpMethod.GET, "/api/presupuesto/**", "/api/linea-presupuesto/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO)
                        .requestMatchers("/api/presupuesto/**", "/api/linea-presupuesto/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)

                        // Ordenes de trabajo: las organiza el encargado, todos las pueden ver
                        .requestMatchers(HttpMethod.GET, "/api/orden-trabajo/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, OPERARIO, TRANSPORTISTA)
                        .requestMatchers("/api/orden-trabajo/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // Operarios asignados a cada orden
                        .requestMatchers(HttpMethod.GET, "/api/orden-trabajador/**", "/api/orden-empleado/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/orden-trabajador/**", "/api/orden-empleado/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)

                        // Maquinaria: la gestiona el encargado, el operario solo la ve
                        .requestMatchers(HttpMethod.GET, "/api/maquinaria/**").hasAnyAuthority(ADMIN, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/maquinaria/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // Almacen, compras y proveedores: los gestiona el encargado
                        .requestMatchers(HttpMethod.GET, "/api/inventario/**", "/api/pedido/**", "/api/proveedor/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/inventario/**", "/api/pedido/**", "/api/proveedor/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)

                        // Albaranes: los ve el transportista, los hacen el admin y el encargado
                        .requestMatchers(HttpMethod.GET, "/api/albaran/**", "/api/linea-albaran/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, TRANSPORTISTA)
                        .requestMatchers("/api/albaran/**", "/api/linea-albaran/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // Facturas y pagos: solo el admin (el comercial puede consultarlas)
                        .requestMatchers(HttpMethod.GET, "/api/factura/**", "/api/linea-factura/**", "/api/pago/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)
                        .requestMatchers("/api/factura/**", "/api/linea-factura/**", "/api/pago/**")
                            .hasAuthority(ADMIN)

                        // Cualquier otra direccion de la API que no este en la lista: prohibida.
                        // Asi, si creo un controlador nuevo y se me olvida ponerlo aqui,
                        // no queda abierto para todo el mundo.
                        .requestMatchers("/api/**").denyAll()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(o -> o
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(noAutenticado))
                .exceptionHandling(e -> e.authenticationEntryPoint(noAutenticado).accessDeniedHandler(sinPermiso));

        // si en application.properties pongo que hay que usar HTTPS,
        // las peticiones que lleguen por http:// se mandan a https://
        if (exigirHttps) {
            http.redirectToHttps(Customizer.withDefaults());
        }
        return http.build();
    }
}