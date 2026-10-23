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

// seguridad de la api
// las contraseñas van cifradas y para usar /api hay que mandar el token del login
// @EnableMethodSecurity es para que funcionen los @PreAuthorize
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    // los roles los cojo del enum para no escribirlos a mano
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

    // avisa cuando alguien hace login, bien o mal (para contar los fallos)
    @Bean
    public AuthenticationEventPublisher authenticationEventPublisher(ApplicationEventPublisher publicador) {
        return new DefaultAuthenticationEventPublisher(publicador);
    }

    // comprueba el email y la contraseña del login
    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService usuarios, PasswordEncoder passwordEncoder,
            AuthenticationEventPublisher publicador) {
        DaoAuthenticationProvider proveedor = new DaoAuthenticationProvider(usuarios);
        proveedor.setPasswordEncoder(passwordEncoder);
        ProviderManager manager = new ProviderManager(proveedor);
        manager.setAuthenticationEventPublisher(publicador);
        return manager;
    }

    // saca el rol del token
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter roles = new JwtGrantedAuthoritiesConverter();
        roles.setAuthoritiesClaimName("rol");
        roles.setAuthorityPrefix("");
        JwtAuthenticationConverter conversor = new JwtAuthenticationConverter();
        conversor.setJwtGrantedAuthoritiesConverter(roles);
        return conversor;
    }

    // quien puede entrar a cada sitio
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // si no hay token o no vale -> 401
        AuthenticationEntryPoint noAutenticado = (request, response, exception) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write("{\"message\":\"Tienes que iniciar sesion\"}");
        };

        // si el rol no tiene permiso -> 403
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
                        // primero quien puede ver (GET) y luego quien puede crear, modificar y borrar

                        // trabajadores y puestos: los ve el encargado y los cambia el admin
                        .requestMatchers(HttpMethod.GET, "/api/trabajador/**", "/api/puesto/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)
                        .requestMatchers("/api/trabajador/**", "/api/puesto/**").hasAuthority(ADMIN)

                        // clientes: los lleva el comercial
                        .requestMatchers(HttpMethod.GET, "/api/cliente/**", "/api/empresa/**",
                                "/api/particular/**", "/api/contacto-empresa/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, TRANSPORTISTA)
                        .requestMatchers("/api/cliente/**", "/api/empresa/**", "/api/particular/**",
                                "/api/contacto-empresa/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)

                        // catalogo: solo lo cambia el admin
                        .requestMatchers(HttpMethod.GET, "/api/catalogo/**").hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO)
                        .requestMatchers("/api/catalogo/**").hasAuthority(ADMIN)

                        // presupuestos: los hace el comercial
                        .requestMatchers(HttpMethod.GET, "/api/presupuesto/**", "/api/linea-presupuesto/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO)
                        .requestMatchers("/api/presupuesto/**", "/api/linea-presupuesto/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)

                        // ordenes de trabajo: las ven todos y las organiza el encargado
                        .requestMatchers(HttpMethod.GET, "/api/orden-trabajo/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, OPERARIO, TRANSPORTISTA)
                        .requestMatchers("/api/orden-trabajo/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // operarios de cada orden
                        .requestMatchers(HttpMethod.GET, "/api/orden-trabajador/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/orden-trabajador/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)

                        // maquinaria: la lleva el encargado, el operario solo la ve
                        .requestMatchers(HttpMethod.GET, "/api/maquinaria/**").hasAnyAuthority(ADMIN, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/maquinaria/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // almacen, pedidos y proveedores: los lleva el encargado
                        .requestMatchers(HttpMethod.GET, "/api/inventario/**", "/api/pedido/**", "/api/proveedor/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, OPERARIO)
                        .requestMatchers("/api/inventario/**", "/api/pedido/**", "/api/proveedor/**")
                            .hasAnyAuthority(ADMIN, ENCARGADO)

                        // albaranes: los hacen el admin y el encargado, el transportista los ve
                        .requestMatchers(HttpMethod.GET, "/api/albaran/**", "/api/linea-albaran/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL, ENCARGADO, TRANSPORTISTA)
                        .requestMatchers("/api/albaran/**", "/api/linea-albaran/**").hasAnyAuthority(ADMIN, ENCARGADO)

                        // facturas y pagos: solo el admin, el comercial puede verlas
                        .requestMatchers(HttpMethod.GET, "/api/factura/**", "/api/linea-factura/**", "/api/pago/**")
                            .hasAnyAuthority(ADMIN, COMERCIAL)
                        .requestMatchers("/api/factura/**", "/api/linea-factura/**", "/api/pago/**")
                            .hasAuthority(ADMIN)

                        // lo que no este en la lista esta prohibido
                        // (por si hago un controlador nuevo y se me olvida ponerlo)
                        .requestMatchers("/api/**").denyAll()
                        .anyRequest().denyAll())
                .oauth2ResourceServer(o -> o
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(noAutenticado))
                .exceptionHandling(e -> e.authenticationEntryPoint(noAutenticado).accessDeniedHandler(sinPermiso));

        // si en application.properties pongo https a true, todo va por https
        if (exigirHttps) {
            http.redirectToHttps(Customizer.withDefaults());
        }
        return http.build();
    }
}