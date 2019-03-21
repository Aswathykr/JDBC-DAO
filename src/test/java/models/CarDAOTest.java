package models;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CarDAOTest {
    @Test
    public void testUpdate(){
        //Given
        CarDAO dao = new CarDAO();
        Car car = dao.findById(2);
        String oldMake = car.getMake();
        String newNake = oldMake + " New ";
        car.setMake(newNake);

        //When
        dao.update(car);

        //Then
        Car updatedCar = dao.findById(2);
        Assert.assertEquals(updatedCar.getMake(), newNake);
    }

    @Test
    public void findByIdTest(){
        //Given
        CarDAO dao = new CarDAO();
        Car newCar = new Car("testFindByIDMake","testFindByIDModel", 0, "blue", "testFindByIDVin");
        Car insertedCar = dao.create(newCar);
        //When
        Car retrievedCar = dao.findById(insertedCar.getId());

        //Then
        dao.delete(retrievedCar);

        Assert.assertEquals(retrievedCar, insertedCar);

    }

    @Test
    public void findAllTest(){
        //Given
        CarDAO dao = new CarDAO();
        //When
        List<Car> records = dao.findAll();

        //Then
        int count = dao.getTotalRowCount();
        Assert.assertEquals(records.size(), count);

        for (int i = 0; i < records.size(); i++) {
            Car retrievedCar = dao.findById(records.get(i).getId());
            Assert.assertEquals(retrievedCar, records.get(i));
        }

    }

    @Test
    public void create(){
        CarDAO dao = new CarDAO();
        Car newCar = new Car("testCreateMake","testCreateModel", 0, "red", "testCreateVin");
        //When
        Car insertedCar = dao.create(newCar);
        //Then
        Car retrievedCar = dao.findById(insertedCar.getId());
        dao.delete(retrievedCar);

        Assert.assertEquals(retrievedCar, insertedCar);
    }

    @Test
    public void delete(){

        //Given
        CarDAO dao = new CarDAO();
        Car newCar = new Car("testDeleteMake","testDeleteModel", 0, "red", "testDeleteVin");
        Car insertedCar = dao.create(newCar);
        Car retrievedCar = dao.findById(insertedCar.getId());
        Assert.assertEquals(retrievedCar, insertedCar);

        //When
        dao.delete(retrievedCar);

        //Then
        Car retrievedCarAfterDelete = dao.findById(insertedCar.getId());
        Assert.assertNull(retrievedCarAfterDelete);
    }

}
