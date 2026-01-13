# Convenciones de código
- Mantener arquitectura hexagonal en backend: domain / application / ports / adapters.
- No usar entidades JPA en domain, siempre mapear.
- DTOs solo en adapters/in.
- Validar entrada con `jakarta.validation`.
- Documentar cada método con Javadoc en Java y con Compodoc en Angular.

# Cómo agregar un nuevo módulo (plantilla hexagonal)
1. **Domain**: crear entidad/enum en `backend/src/main/java/com/pickeat/domain`.
2. **Ports**:
   - `ports/in`: interfaz de caso de uso.
   - `ports/out`: interfaz de repositorio/servicios externos.
3. **Application**: servicio en `application/usecase` implementando el puerto.
4. **Adapters**:
   - `adapters/in/rest`: controller + DTOs + mapper.
   - `adapters/out/persistence`: repositorios JPA y mappers.
5. **Migraciones**: agregar script en `resources/db/migration`.
6. **Tests**: agregar al menos 1 test unitario por caso de uso.

# Checklist para PR
- [ ] Migraciones Flyway actualizadas.
- [ ] DTOs y validaciones creadas.
- [ ] Use cases con puertos in/out.
- [ ] Tests mínimos añadidos.
- [ ] README actualizado si aplica.
