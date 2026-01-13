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

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
public class ApiAuditFilter extends OncePerRequestFilter {
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
        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            String endpoint = request.getMethod() + " " + request.getRequestURI();
            String requestBody = toPayload(requestWrapper.getContentAsByteArray());
            String responseBody = toPayload(responseWrapper.getContentAsByteArray());
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String user = authentication != null ? authentication.getName() : "anon";
            String role = authentication != null && !authentication.getAuthorities().isEmpty()
                    ? authentication.getAuthorities().iterator().next().getAuthority() : "";
            auditRepository.saveApiAudit(endpoint, requestBody, responseBody, user, role);
            responseWrapper.copyBodyToResponse();
        }
    }

    private String toPayload(byte[] payload) {
        if (payload == null || payload.length == 0) {
            return "";
        }
        String body = new String(payload, StandardCharsets.UTF_8);
        if (body.length() > maxPayloadSize) {
            return body.substring(0, maxPayloadSize) + "...";
        }
        return body;
    }
}
