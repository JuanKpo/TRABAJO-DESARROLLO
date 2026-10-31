package repository.dao;

import domain.Reservation;
import domain.TouristService;
import domain.User;
import exception.DataAccessException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReservationDaoFileImpl implements ReservationDao {
  private static ReservationDaoFileImpl instance;
  private final File file;

  private ReservationDaoFileImpl() {
    this.file = new File("reservations.csv");
  }

  // visible for testing, so tests can point at a temp file instead of the real one
  ReservationDaoFileImpl(File file) {
    this.file = file;
  }

  // singleton
  public static synchronized ReservationDaoFileImpl getInstance() {
    if (instance == null) {
      instance = new ReservationDaoFileImpl();
    }

    return instance;
  }

  @Override
  public void save(Reservation reservation) throws DataAccessException {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
      String line =
          String.format(
              "%s,%s,%s,%s\n",
              reservation.getId(),
              reservation.getService().getId(),
              reservation.getOwner().getId(),
              reservation.getDate());
      bw.write(line);
    } catch (IOException e) {
      throw new DataAccessException("Error al guardar la reserva", e);
    }
  }

  // Ejercicio 3 (Parte A)
 @Override
  public List<Reservation> findByDateRange(Instant from, Instant to) throws DataAccessException {
    List<Reservation> resultado = new ArrayList<>();

    if (!file.exists()) {
      return resultado;
    }

    try (BufferedReader rd = new BufferedReader(new FileReader(file))) {
      String linea = null;

      while ((linea = rd.readLine()) != null) {
        String[] datos = linea.split(",");
        if (datos.length != 4) {
          continue; // línea mal formada
        }

        Long id = Long.parseLong(datos[0]);
        UUID serviceId = UUID.fromString(datos[1]);
        UUID ownerId = UUID.fromString(datos[2]);
        Instant fecha = Instant.parse(datos[3]);

        // inclusive en ambos extremos: fecha >= from y fecha <= to
        if (!fecha.isBefore(from) && !fecha.isAfter(to)) {
          // el archivo solo guarda el id del servicio: clase anónima que extiende la abstracta
          TouristService servicio = new TouristService(serviceId, null) {
            public double calculatePrice() {
              throw new IllegalStateException("El archivo solo guarda el id del servicio");
            }
          };
          User owner = User.builder().id(ownerId).build();

          Reservation reserva = new Reservation.Builder()
              .id(id)
              .date(fecha)
              .service(servicio)
              .owner(owner)
              .build();
          resultado.add(reserva);
        }
      }
    } catch (IOException e) {
      throw new DataAccessException("Error al leer las reservas", e);
    }

    return resultado;
  }
}
