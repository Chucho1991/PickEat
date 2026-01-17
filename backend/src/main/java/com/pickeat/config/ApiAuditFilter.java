package com.pickeat.config;

import com.pickeat.ports.out.AuditRepositoryPort;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiAuditFilter extends OncePerRequestFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(ApiAuditFilter.class);
    private final AuditRepositoryPort auditRepository;
    private final int maxPayloadSize;

    public ApiAuditFilter(AuditRepositoryPort auditRepository,
                          @Value("${app.audit.max-payload-size}") int maxPayloadSize) {
        this.auditRepository = auditRepository;
        this.maxPayloadSize = maxPayloadSize;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);
        long startTimeMs = System.currentTimeMillis();
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            String endpoint = request.getMethod() + " " + request.getRequestURI();
            String requestBody = toPayload(requestWrapper.getContentAsByteArray(), requestWrapper.getContentType());
            String responseBody = toPayload(responseWrapper.getContentAsByteArray(), responseWrapper.getContentType());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication != null ? authentication.getName() : "anon";
            String role = authentication != null && !authentication.getAuthorities().isEmpty()
                    ? authentication.getAuthorities().iterator().next().getAuthority() : "";
            try {
                auditRepository.saveApiAudit(endpoint, requestBody, responseBody, user, role);
            } catch (RuntimeException ex) {
                LOGGER.warn("API audit save failed for {}", endpoint, ex);
            }
            long elapsedMs = System.currentTimeMillis() - startTimeMs;
            LOGGER.info("API {} status={} user={} role={} elapsedMs={} request={} response={}",
                    endpoint,
                    responseWrapper.getStatus(),
                    user,
                    role,
                    elapsedMs,
                    requestBody,
                    responseBody);
            responseWrapper.copyBodyToResponse();
        }
    }

    private String toPayload(byte[] payload, String contentType) {
        if (payload == null || payload.length == 0) {
            return "";
        }
        if (!isTextContentType(contentType)) {
            return "[binary]";
        }
        String body = new String(payload, StandardCharsets.UTF_8);
        if (body.length() > maxPayloadSize) {
            return body.substring(0, maxPayloadSize) + "...";
        }
        return body;
    }

    private boolean isTextContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        String normalized = contentType.toLowerCase();
        return normalized.startsWith("text/")
                || normalized.startsWith("application/json")
                || normalized.contains("+json")
                || normalized.contains("application/xml")
                || normalized.contains("+xml")
                || normalized.startsWith("application/x-www-form-urlencoded");
    }
}
