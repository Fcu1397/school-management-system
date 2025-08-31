package com.example.schoolmanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import java.util.Arrays;

/**
 * Spring Security 配置類別
 * 設定安全策略、密碼加密、CORS 等
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * 密碼加密器
     * 使用 BCrypt 演算法加密密碼
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全過濾鏈配置
     * 定義哪些路徑需要認證、哪些可以公開訪問
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 關閉 CSRF（因為我們使用 REST API）
                .csrf(csrf -> csrf.disable())

                // 設定 CORS
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // 設定 Session 管理（無狀態）
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // 設定路徑權限
                .authorizeHttpRequests(auth -> auth
                        // 公開路徑（不需要認證）
                        .requestMatchers(
                                "/api/auth/**",        // 認證相關
                                "/api/test/**",        // 測試端點
                                "/swagger-ui/**",      // Swagger UI
                                "/swagger-ui.html",
                                "/api-docs/**",        // API 文件
                                "/v3/api-docs/**",
                                "/error",              // 錯誤頁面
                                "/favicon.ico"         // 網站圖標
                        ).permitAll()

                        // 學生專用路徑
                        .requestMatchers("/api/student/**")
                        .hasAnyRole("STUDENT", "ADMIN")

                        // 教師專用路徑
                        .requestMatchers("/api/teacher/**")
                        .hasAnyRole("TEACHER", "ADMIN")

                        // 管理員專用路徑
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // 其他所有路徑都需要認證
                        .anyRequest().authenticated()
                )

                // 設定例外處理
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(401);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"error\":\"未授權\",\"message\":\"請先登入\"}");
                        })
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(403);
                            response.setContentType("application/json;charset=UTF-8");
                            response.getWriter().write("{\"error\":\"禁止訪問\",\"message\":\"您沒有權限訪問此資源\"}");
                        })
                );

        return http.build();
    }

    /**
     * CORS 配置
     * 允許前端跨域請求
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // 允許的來源
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:3000",  // React 開發伺服器
                "http://localhost:4200",  // Angular 開發伺服器
                "http://localhost:8081"   // Vue 開發伺服器
        ));

        // 允許的方法
        configuration.setAllowedMethods(Arrays.asList(
                "GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"
        ));

        // 允許的標頭
        configuration.setAllowedHeaders(Arrays.asList("*"));

        // 允許認證資訊
        configuration.setAllowCredentials(true);

        // 快取時間
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}