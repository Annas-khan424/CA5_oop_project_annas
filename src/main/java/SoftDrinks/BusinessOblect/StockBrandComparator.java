package SoftDrinks.BusinessOblect;
import SoftDrinks.DTOs.Drink;

import java.util.Comparator;

public class StockBrandComparator implements Comparator<Drink> {

    //compare integer stockAva withing   Brand
    @Override
    public int compare(Drink d1, Drink d2)
    {

        boolean brandSame =
                d1.getBrand().equalsIgnoreCase(d2.getBrand());


        if(brandSame)
        {

            return d1.getStockAvailable() - d2.getStockAvailable();
        }
        else
        {
            return d1.getBrand().compareToIgnoreCase(d2.getBrand());
        }
    }
}

