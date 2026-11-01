package service;

import domain.Flight;
import domain.TouristService;
import exception.FlightNotFoundException;
import exception.ServiceNotFoundException;
import repository.dao.FlightDao;

import java.util.List;

public class TouristServiceService {

  // Ejercicio 2
  // Ejercicio 2
  public double getAveragePriceByCompany(List<TouristService> services, String company)
          throws ServiceNotFoundException {
    double total = 0;
    int cantidad = 0;

    for (TouristService service : services) {
      // Comparamos la compañía ignorando mayúsculas y minúsculas
      if (service.getCompany().equalsIgnoreCase(company)) {
        total += service.calculatePrice();
        cantidad++;
      }
    }

    // Si ningún servicio es de esa compañía no hay promedio que calcular
    if (cantidad == 0) {
      throw new ServiceNotFoundException(company);
    }

    return total / cantidad;
  }

  // Ejercicio 4
  public List<Flight> getFlightsSortedByPriceThenAirline(
      List<String> flightNumbers, FlightDao flightDao) throws FlightNotFoundException {
    throw new UnsupportedOperationException(
        "Ejercicio 4: implement getFlightsSortedByPriceThenAirline");
  }
}
