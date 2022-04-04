package SoftDrinks.DAOs;

import SoftDrinks.DTOs.Drink;
import SoftDrinks.Exceptions.DaoException;

import java.util.List;

public interface DrinkDaoInterface {

    public List<Drink> findAllDrink() throws DaoException;

}
