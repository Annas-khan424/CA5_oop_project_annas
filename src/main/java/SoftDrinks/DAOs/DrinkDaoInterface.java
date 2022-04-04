package SoftDrinks.DAOs;

import SoftDrinks.DTOs.Drink;
import SoftDrinks.Exceptions.DaoException;

import java.util.List;

public interface DrinkDaoInterface {

    public List<Drink> findAllDrink() throws DaoException;

    public Drink findDrinkByID(String id) throws DaoException;


    public Drink deleteDrinkByID(String id) throws DaoException;

    public void addDrink(String brand, String name, int size, float price, int stockAvailable) throws DaoException;

    public List<Drink> findAllDrinkSubXEuro(float x) throws DaoException;

    public String findAllDrinkJSON() throws DaoException;

    public String findDrinkByIDJSON(String id) throws DaoException;

}
