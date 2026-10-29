package repository.dao;

import domain.Reservation;
import exception.DataAccessException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ReservationExportDaoImpl implements ReservationExportDao {

  private static final String HEADER =
      "reservationId,date,ownerUsername,serviceCompany,calculatedPrice";

  private final File file;

  public ReservationExportDaoImpl() {
    this.file = new File("reservations-export.csv");
  }

  ReservationExportDaoImpl(File file) {
    this.file = file;
  }

  @Override
  public void exportAll(List<Reservation> reservations) throws DataAccessException {
    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {

      writer.write(HEADER);
      writer.newLine();

      for (Reservation reservation : reservations) {
        String line = String.format(Locale.US, "%s,%s,%s,%s,%.2f",
            reservation.getId(),                      
            reservation.getDate(),                   
            reservation.getOwner().getUsername(),     
            reservation.getService().getCompany(),    
            reservation.calculatePrice());            
        writer.write(line);
        writer.newLine();
      }

    } catch (IOException e) {
      throw new DataAccessException("Error al exportar reservas a " + file.getPath(), e);
    }
  }
}