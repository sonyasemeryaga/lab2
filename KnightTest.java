import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.InputMismatchException;
import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {
    private Knight knight;

    @BeforeEach
    public void setUp() {
        knight = new Knight();
    }

    @Test
    public void testAddAmmunition() {
        Ammunition spear = new Weapon("spear", 349.05, 2.1, 28.5);
        Ammunition helmet = new Helmet("helmet", 1377.0, 15.4, 23.0);
        Ammunition boots = new Armor("boots", 864.2, 7.4, 12.5);
        knight.addAmmunition(spear);
        knight.addAmmunition(helmet);
        knight.addAmmunition(boots);
        assertEquals(3, knight.ammunitions.size());
        assertEquals(helmet, knight.ammunitions.get(1));
    }

    @Test
    public void testAddAmmunitionWithNegativeValues() {
        assertThrows(IllegalArgumentException.class, () -> {
            knight.addAmmunition(new Shield("shield", -100.00, 2.1, 28.5));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            knight.addAmmunition(new Helmet("helmet", 1500.0, -5.0, 20.0));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            knight.addAmmunition(new Armor("boots", -250.0, 10.0, -5.0));
        });
        assertEquals(0, knight.ammunitions.size());
    }

    @Test
    public void testGetAllInfo() {
        knight.addAmmunition(new Armor("chest plate", 1500.00, 32.04, 123.55));
        knight.addAmmunition(new Helmet("helmet", 209.85, 7.45, 5.05));
        knight.addAmmunition(new Shield("shield", 98.30, 15.67, 3.33));
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));
        knight.getAllInfo();
        System.setOut(originalOut);
        String output = testOut.toString();
        assertTrue(output.contains("The weight of chest plate is 32.04 kgs. This ammunition costs $1500.0. It provides 123.55 units of defense."));
        assertTrue(output.contains("The weight of helmet is 7.45 kgs. This ammunition costs $209.85. It provides 5.05 units of defense."));
        assertTrue(output.contains("The weight of shield is 15.67 kgs. This ammunition costs $98.3. It provides 3.33 units of defense."));
    }

    @Test
    public void testCalculatePrice() {
        Ammunition spear = new Weapon("spear", 567.05, 2.1, 28.5);
        Ammunition helmet = new Helmet("helmet", 2439.0, 15.4, 23.0);
        Ammunition boots = new Armor("boots", 864.2, 7.4, 12.5);
        knight.addAmmunition(spear);
        knight.addAmmunition(helmet);
        knight.addAmmunition(boots);
        double totalCost = knight.calculatePrice();
        assertEquals(3870.25, totalCost);
    }

    @Test
    public void testWeightSort() {
        knight.addAmmunition(new Armor("cuirass", 1435.00, 25.56, 100.00));
        knight.addAmmunition(new Helmet("helmet", 209.85, 7.45, 35.05));
        knight.addAmmunition(new Shield("shield", 56.90, 11.05, 3.33));
        knight.addAmmunition(new Armor("gauntlets", 345.67, 4.32, 45.50));
        knight.addAmmunition(new Helmet("large helmet", 122.50, 3.76, 12.78));
        knight.weightSort();
        assertEquals("large helmet", knight.ammunitions.get(0).getName());
        assertEquals("gauntlets", knight.ammunitions.get(1).getName());
        assertEquals("helmet", knight.ammunitions.get(2).getName());
        assertEquals("shield", knight.ammunitions.get(3).getName());
        assertEquals("cuirass", knight.ammunitions.get(4).getName());
    }

    @Test
    public void testFindByPrice() {
        knight.addAmmunition(new Armor("cuirass", 1200.50, 20.0, 90.0));
        knight.addAmmunition(new Helmet("helmet", 300.75, 5.0, 4.5));
        knight.addAmmunition(new Shield("shield", 75.30, 10.5, 2.8));
        knight.addAmmunition(new Weapon("knife", 2900.54, 1.1, 23.9));
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));
        knight.findByPrice(100.0, 1000.0);
        String actualOutput = testOut.toString();
        assertFalse(actualOutput.contains("shield: $75.3"));
        assertTrue(actualOutput.contains("helmet: $300.75"));
        assertFalse(actualOutput.contains("cuirass: $1200.5"));
        assertFalse(actualOutput.contains("knife: $2900.54"));
        System.setOut(originalOut);
    }


    @Test
    public void testFindAmmunitionByPrice() {
        knight.addAmmunition(new Weapon("knife", 2900.54, 1.1, 23.9));
        knight.addAmmunition(new Helmet("helmet", 1263.85, 7.45, 5.05));
        knight.addAmmunition(new Shield("shield", 177.77, 11.05, 3.33));
        knight.addAmmunition(new Armor("chest plate", 1435.00, 25.56, 100.00));
        String simulatedInput = "200,0\n2000,0\n";
        InputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(testIn);
        ByteArrayOutputStream testOut = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(testOut));
        knight.findAmmunitionByPrice(knight);
        String output = testOut.toString();
        System.out.println(output);
        assertTrue(output.contains("The ammunition in this price range:\n"));
        assertTrue(output.contains("helmet: $1263.85"));
        assertTrue(output.contains("chest plate: $1435.0"));
        assertFalse(output.contains("knife: $2900.54"));
        assertFalse(output.contains("shield: $1435.00"));
        System.setIn(System.in);
        System.setOut(originalOut);
    }

    @Test
    public void testFindAmmunitionByPrice_NegativeValue() {
        knight.addAmmunition(new Weapon("knife", 2900.54, 1.1, 23.9));
        knight.addAmmunition(new Helmet("helmet", 1263.85, 7.45, 5.05));
        knight.addAmmunition(new Shield("shield", 177.77, 11.05, 3.33));
        knight.addAmmunition(new Armor("chest plate", 1435.00, 25.56, 100.00));
        String simulatedInput = "-1100,0\n2556,0\n";
        InputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(testIn);
        assertThrows(InputMismatchException.class, () -> {
            knight.findAmmunitionByPrice(knight);
        });
    }

    @Test
    public void testFindAmmunitionByPrice_NonNumericValue() {
        knight.addAmmunition(new Weapon("knife", 2900.54, 1.1, 23.9));
        knight.addAmmunition(new Helmet("helmet", 1263.85, 7.45, 5.05));
        knight.addAmmunition(new Shield("shield", 177.77, 11.05, 3.33));
        knight.addAmmunition(new Armor("chest plate", 1435.00, 25.56, 100.00));
        String simulatedInput = "asdvawv\n2556,0\n";
        InputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(testIn);
        assertThrows(InputMismatchException.class, () -> {
            knight.findAmmunitionByPrice(knight);
        });
    }

    @Test
    public void testFindAmmunitionByPrice_MinPriceGreaterThanMax() {
        knight.addAmmunition(new Weapon("knife", 2900.54, 1.1, 23.9));
        knight.addAmmunition(new Helmet("helmet", 1263.85, 7.45, 5.05));
        knight.addAmmunition(new Shield("shield", 177.77, 11.05, 3.33));
        knight.addAmmunition(new Armor("chest plate", 1435.00, 25.56, 100.00));
        String simulatedInput = "3556,0\n2556,0\n";
        InputStream testIn = new ByteArrayInputStream(simulatedInput.getBytes());
        System.setIn(testIn);
        assertThrows(InputMismatchException.class, () -> {
            knight.findAmmunitionByPrice(knight);
        });
    }
}
