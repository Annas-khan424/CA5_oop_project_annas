package SoftDrinks.DTOs;

import java.util.Objects;

public class  Drink implements Comparable<Drink>{

    private int _id;
    private String brand;
    private String name;
    private int size;
    private double price;
    private int stockAvailable;

    public Drink(int _id, String brand, String name, int size, double price, int stockAvailable)
    {
        this._id = _id;
        this.brand = brand;
        this.name = name;
        this.size = size;
        this.price = price;
        this.stockAvailable = stockAvailable;
    }

    public Drink(int _id, String brand, String name)
    {
        this._id = _id;
        this.brand = brand;
        this.name = name;
        this.size = -1;
        this.price = -1;
        this.stockAvailable = -1;
    }



    // If 2 drinks are the same price/size ratio, the larger sized bottle has priority
    @Override
    public int compareTo(Drink d)
    {
        double currentD = (this.getPrice() / (double)this.getSize());
        double paramD = (d.getPrice() / (double)d.getSize());

        boolean PriceSizeComp =
                currentD == paramD;

        if (PriceSizeComp)
        {
            return 0;
        }
        else
        {
            if (currentD - paramD > 0)
            {
                return -1;
            }
            else
            {
                return 1;
            }
        }
    }

    public int get_id() {return _id;}

    public void set_id(int _id) {this._id = _id;}

    public String getBrand() {return brand;}

    public void setBrand(String brand) {this.brand = brand;}

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public int getSize() {return size;}

    public void setSize(int size) {this.size = size;}

    public double getPrice() {return price;}

    public void setPrice(double price) {this.price = price;}

    public int getStockAvailable() {return stockAvailable;}

    public void setStockAvailable(int stockAvailable) {this.stockAvailable = stockAvailable;}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Drink)) return false;
        Drink drink = (Drink) o;
        return get_id() == drink.get_id() && getBrand().equals(drink.getBrand()) && getName().equals(drink.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_id(), getBrand(), getName());
    }

    @Override
    public String toString() {
        return "Drink{" +
                "brand='" + brand + '\'' +
                ", name='" + name + '\'' +
                ", size=" + size +
                ", price=" + price +
                ", stockAvailable=" + stockAvailable +
                '}';
    }
}
