package SoftDrinks.DTOs;

public class DataInfo {
    private int totalStock;
    private int totalProducts;


    public DataInfo(int totalStock, int totalProducts) {
        this.totalStock = totalStock;
        this.totalProducts = totalProducts;
    }

    public DataInfo() {
        this.totalStock = -1;
        this.totalProducts = -1;
    }

    public int getTotalStock() {
        return totalStock;
    }

    public void setTotalStock(int totalStock) {
        this.totalStock = totalStock;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    @Override
    public String toString() {
        return "SummaryData{" +
                "totalStock=" + totalStock +
                ", totalProducts=" + totalProducts +
                '}';
    }

}
