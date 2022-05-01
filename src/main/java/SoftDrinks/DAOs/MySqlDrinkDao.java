package SoftDrinks.DAOs;

import com.google.gson.Gson;
import SoftDrinks.DTOs.Drink;
import SoftDrinks.Exceptions.DaoException;


import java.util.List;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.ArrayList;
import java.sql.SQLException;
import java.sql.PreparedStatement;


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

    @Override
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

    @Override
    public List<Drink> findAllDrinkSubXEuro(float x) throws DaoException
    {
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet resultSet = null;
        List<Drink> drinkList = new ArrayList<>();

        try
        {
            //Get connection object using the methods in the super class (MySqlDao.java)...
            connection = this.getConnection();

            String query = "SELECT * FROM drink where price < ?";
            ps = connection.prepareStatement(query);
            ps.setFloat(1, x);

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
            throw new DaoException("findAllDrinkSubXEuro() " + e.getMessage());
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
                throw new DaoException("findAllDrinkSubXEuro() " + e.getMessage());
            }
        }
        return drinkList;     // may be empty

    }


    @Override
    public Drink findDrinkByID(String id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Drink drink = null;
        try
        {
            connection = this.getConnection();

            String query = "SELECT * FROM drink WHERE _id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                int stockAvailable = resultSet.getInt("stockAvailable");

                drink = new Drink(_id, brand, name, size, price, stockAvailable);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findDrinkByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findDrinkByID() " + e.getMessage());
            }
        }
        return drink;     // reference to User object, or null value
    }


    @Override
    public String findDrinkByIDJSON(String id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Drink drink = null;
        Gson gsonParser = new Gson();
        try
        {
            connection = this.getConnection();

            String query = "SELECT * FROM drink WHERE _id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);


            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                int stockAvailable = resultSet.getInt("stockAvailable");

                drink = new Drink(_id, brand, name, size, price, stockAvailable);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findDrinkByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findDrinkByID() " + e.getMessage());
            }
        }
        String Result = gsonParser.toJson(drink);    // Serialize an object

        return Result;     // may be empty
    }


    @Override
    public Drink deleteDrinkByID(String id) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Drink drink = null;
        try
        {
            connection = this.getConnection();

            String query = "DELETE FROM drink WHERE _id = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, id);

            preparedStatement.executeUpdate();

        } catch (SQLException e)
        {
            throw new DaoException("deleteDrinkByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("deleteDrinkByID() " + e.getMessage());
            }
        }

        return drink;
    }

    @Override
    public void addDrink(String brand, String name, int size, float price,  int stockAvailable) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try
        {
            connection = this.getConnection();

            String query = "INSERT INTO drink VALUES (null, ?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, brand);
            preparedStatement.setString(2, name);
            preparedStatement.setInt(3, size);
            preparedStatement.setFloat(4, price);
            preparedStatement.setInt(5, stockAvailable);

            preparedStatement.executeUpdate();

        } catch (SQLException e)
        {
            throw new DaoException("addDrink() " + e.getMessage());
        } finally
        {
            try
            {
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("addDrink() " + e.getMessage());
            }
        }

    }
    @Override
    public String findDrinkByNameBrandSizeJSON(String recString) throws DaoException
    {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Drink drink = null;
        Gson gsonParser = new Gson();

        String[] tokens = recString.split(" ");
        String param1 = tokens[0];
        String param2 = tokens[1];
        String param3Temp = tokens[2];
        int param3 = Integer.parseInt(param3Temp);
        try
        {
            connection = this.getConnection();

            String query = "SELECT * FROM drink WHERE name = ? AND brand = ? AND size = ?";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, param1);
            preparedStatement.setString(2, param2);
            preparedStatement.setString(3, param3Temp);

            resultSet = preparedStatement.executeQuery();
            if (resultSet.next())
            {
                int _id = resultSet.getInt("_id");
                String brand = resultSet.getString("brand");
                String name = resultSet.getString("name");
                int size = resultSet.getInt("size");
                float price = resultSet.getFloat("price");
                int stockAvailable = resultSet.getInt("stockAvailable");

                drink = new Drink(_id, brand, name, size, price, stockAvailable);
            }
        } catch (SQLException e)
        {
            throw new DaoException("findDrinkByID() " + e.getMessage());
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (preparedStatement != null)
                {
                    preparedStatement.close();
                }
                if (connection != null)
                {
                    freeConnection(connection);
                }
            } catch (SQLException e)
            {
                throw new DaoException("findDrinkByID() " + e.getMessage());
            }
        }
        String Result = gsonParser.toJson(drink);    // Serialize an object

        return Result;     // may be empty
    }

}