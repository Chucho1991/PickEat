package com.pickeat.domain;

/**
 * Imagen asociada a un descuento.
 */
public class DiscountItemImage {
    private byte[] content;
    private String contentType;
    private String originalFilename;

    public DiscountItemImage(byte[] content, String contentType, String originalFilename) {
        this.content = content;
        this.contentType = contentType;
        this.originalFilename = originalFilename;
    }

    public byte[] getContent() {
        return content;
    }

    public String getContentType() {
        return contentType;
    }

    public String getOriginalFilename() {
        return originalFilename;
    }
}
