package SoftDrinks;

import java.util.Comparator;

public class StockBrandComparator implements Comparator<Drink> {

    //compare integer stockLvl withing Brand
    @Override
    public int compare(Drink d1, Drink d2)
    {

        boolean brandSame =
                d1.getBrand().equalsIgnoreCase(d2.getBrand());


        if(brandSame)
        {
            //so, compare based on age
            return d1.getStockAvailable() - d2.getStockAvailable();
        }
        else
        {
            return d1.getBrand().compareToIgnoreCase(d2.getBrand());
        }
    }
}
