package com.pickeat.domain;

/**
 * Representa una imagen cargada para un ítem del menú.
 */
public class MenuItemImage {
    private final byte[] content;
    private final String contentType;
    private final String originalFilename;

    /**
     * Construye una imagen para almacenamiento.
     *
     * @param content contenido binario.
     * @param contentType tipo de contenido.
     * @param originalFilename nombre original del archivo.
     */
    public MenuItemImage(byte[] content, String contentType, String originalFilename) {
        if (content == null || content.length == 0) {
            throw new IllegalArgumentException("La imagen es obligatoria.");
        }
        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("El tipo de imagen es obligatorio.");
        }
        this.content = content;
        this.contentType = contentType;
        this.originalFilename = originalFilename;
    }

    /**
     * Obtiene el contenido binario.
     *
     * @return bytes de la imagen.
     */
    public byte[] getContent() {
        return content;
    }

    /**
     * Obtiene el tipo de contenido.
     *
     * @return content type.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Obtiene el nombre original del archivo.
     *
     * @return nombre original.
     */
    public String getOriginalFilename() {
        return originalFilename;
    }
}
