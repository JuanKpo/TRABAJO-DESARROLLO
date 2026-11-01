package repository.dao;

import domain.User;
import exception.DataAccessException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FileUserDaoImpl implements FileUserDao {
  private final File file;

  public FileUserDaoImpl() {
    this.file = new File("users.txt");
  }

  // visible for testing, so tests can point at a temp file instead of the real one
  FileUserDaoImpl(File file) {
    this.file = file;
  }

  // Ejercicio 9 (Parte A)
  @Override
  public List<User> findAll() throws DataAccessException {
    List<User> users = new ArrayList<>();

    try {
      // Leemos todas las líneas del archivo
      List<String> lines = Files.readAllLines(file.toPath());

      for (String line : lines) {
        // Separamos la línea por espacios: "uuid username" → ["uuid", "username"]
        String[] parts = line.trim().split("\\s+");

        // Línea mal formada (no tiene exactamente 2 partes) → la salteamos
        if (parts.length != 2) {
          continue;
        }

        try {
          UUID id = UUID.fromString(parts[0]);
          users.add(User.builder()
              .id(id)
              .username(parts[1])
              .build());
        } catch (IllegalArgumentException e) {
          // El primer dato no es un UUID válido → también es mal formada, la salteamos
        }
      }

    } catch (IOException e) {
      // No se pudo leer el archivo (no existe, sin permisos, etc.)
      throw new DataAccessException("Error al leer usuarios de " + file.getPath(), e);
    }

    return users;
  }
}