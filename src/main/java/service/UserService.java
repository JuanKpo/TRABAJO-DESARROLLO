package service;

import domain.User;
import dto.user.UserResponse;
import exception.DataAccessException;
import exception.IncompleteUserException;
import exception.UserNotFoundException;
import repository.dao.FileUserDao;
import repository.dao.UserDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserService {
  private final UserDao userDao;

  public UserService(UserDao userDao) {
    this.userDao = userDao;
  }

  public UserResponse getByUsername(String username)
      throws UserNotFoundException, DataAccessException {
    User user =
        userDao.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
    return new UserResponse(user.getId(), user.getUsername());
  }

  // Ejercicio 6
  public User getValidatedUser(String username, UserDao userDao)
      throws UserNotFoundException, IncompleteUserException, DataAccessException {
    throw new UnsupportedOperationException("Ejercicio 6: implement getValidatedUser");
  }

  // Ejercicio 9
  public List<User> getUsersPresentInFileAndDao(FileUserDao fileUserDao, UserDao userDao)
      throws DataAccessException {
    // Leemos los usuarios del archivo.
    // Si esto falla, la DataAccessException sigue de largo (se propaga).
    List<User> fileUsers = fileUserDao.findAll();

    List<User> result = new ArrayList<>();

    for (User fileUser : fileUsers) {
      try {
        // Le preguntamos al UserDao si existe un usuario con ese username
        Optional<User> daoUser = userDao.findByUsername(fileUser.getUsername());

        // Si existe, agregamos el usuario del DAO (no el del archivo)
        if (daoUser.isPresent()) {
          result.add(daoUser.get());
        }
      } catch (DataAccessException e) {
        // Falló la búsqueda de ESTE usuario: lo ignoramos y seguimos con el próximo
      }
    }

    return result;
  }
}