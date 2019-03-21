package models;

import daos.*;

import java.util.Objects;

public class Car implements DTO {
    private int id;
    private String make;
    private String model;
    private int year;
    private String color;
    private String vin;


    public Car(int id) {
        this.id = id;
    }

    public Car(String make, String model, int year, String color, String vin) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.color = color;
        this.vin = vin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Car)) return false;
        Car car = (Car) o;
        return id == car.id &&
                year == car.year &&
                Objects.equals(make, car.make) &&
                Objects.equals(model, car.model) &&
                Objects.equals(color, car.color) &&
                Objects.equals(vin, car.vin);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }
}