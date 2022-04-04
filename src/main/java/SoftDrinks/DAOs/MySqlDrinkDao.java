package SoftDrinks.DAOs;

import com.google.gson.Gson;
import SoftDrinks.DTOs.Drink;
import SoftDrinks.Exceptions.DaoException;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MySqlDrinkDao extends MySqlDao implements DrinkDaoInterface {


    @Override
    public List<Drink> findAllDrink() throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Drink> drinkList = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM drink";
            ps = connection.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                int stockAvailable = resultSet.getInt("stockAvailable");
                Drink d = new Drink(_id, brand, name, size, price,  stockAvailable);
                drinkList.add(d);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllDrinkResultSet() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }
        return drinkList;     // may be empty

    }
    public String findAllDrinkJSON() throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Drink> drinkList = new ArrayList<>();
        Gson gsonParser = new Gson();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM drink";
            ps = connection.prepareStatement(query);

            //Using a PreparedStatement to execute SQL...
            resultSet = ps.executeQuery();
            while (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                int stockAvailable = resultSet.getInt("stockAvailable");
                Drink d = new Drink(_id, brand, name, size, price, stockAvailable);
                drinkList.add(d);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findAllDrinkResultSet() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (ps != null)
                {
                    ps.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findAllUsers() " + e.getMessage());
            }
        }

        String Result = gsonParser.toJson(drinkList);    // Serialize an object

        return Result;     // may be empty

    }


}