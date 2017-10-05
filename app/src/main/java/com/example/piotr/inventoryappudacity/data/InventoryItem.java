package com.example.piotr.inventoryappudacity.data;

/**
 * Created by Piotr on 04.10.2017.
 */

public class InventoryItem {

    private final String itemName;
    private final String price;
    private final int quantity;
    private final String supplierName;
    private final String supplierPhone;
    private final String supplierEmail;
    private final String image;

    public InventoryItem(String itemName, String price, int quantity, String supplierName,
                         String supplierPhone, String supplierEmail, String image) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
        this.supplierName = supplierName;
        this.supplierPhone = supplierPhone;
        this.supplierEmail = supplierEmail;
        this.image = image;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public String getSupplierPhone() {
        return supplierPhone;
    }

    public String getSupplierEmail() {
        return supplierEmail;
    }

    public String getImage() {
        return image;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("InventoryItem{");
        sb.append("itemName='").append(itemName).append('\'');
        sb.append(", price='").append(price).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append(", supplierName='").append(supplierName).append('\'');
        sb.append(", supplierPhone='").append(supplierPhone).append('\'');
        sb.append(", supplierEmail='").append(supplierEmail).append('\'');
        sb.append(", image='").append(image).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
