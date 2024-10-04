import java.util.ArrayList;
import java.util.Comparator;
import java.util.Scanner;
import java.util.InputMismatchException;

// Визначити ієрархію амуніції лицаря. Екіпірувати лицаря. //
// Порахувати вартість амуніції. Провести сортування амуніції за вагою. //
// Знайти елементи амуніції, що відповідає заданому діапазону цін. //

abstract class Ammunition {
    protected String name;
    protected double price;
    protected double weight;

    public Ammunition(String name, double price, double weight) {
        this.name = name;
        setPrice(price);
        setWeight(weight);
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        if (weight <= 0) {
            throw new IllegalArgumentException("Weight must be greater than zero.");
        }
        this.weight = weight;
    }

    public String getInfo() {
        return "The weight of " + name + " is " + weight + " kgs." + " This ammunition costs $" + price + ".";
    }
}


class Armor extends Ammunition {
    protected double defense;
    public Armor(String name, double price, double weight, double defense) {
    super(name, price, weight);
    setDefense(defense);
}

    public double getDefense() {
        return defense;
    }

    public void setDefense(double defense) {
        if (defense < 0) {
            throw new IllegalArgumentException("Defense must be non-negative.");
        }
        this.defense = defense;
    }

    @Override
    public String getInfo() {
         return super.getInfo() + " It provides " + defense + " units of defense.";
    }
}

class Weapon extends Ammunition {
    private double damage;
    public Weapon(String name, double price, double weight, double damage) {
        super(name, price, weight);
        setDamage(damage);
}

    public double getDamage() {
        return damage;
    }

    public void setDamage(double damage) {
        if (damage < 0) {
            throw new IllegalArgumentException("Damage must be non-negative.");
        }
        this.damage = damage;
    }

    @Override
    public String getInfo() {
        return super.getInfo() + " It can cause " + damage + " units of damage.";
    }
}

class Helmet extends Armor {
    public Helmet(String name, double price, double weight, double defense) {
        super(name, price, weight, defense);
    }
}

class Shield extends Armor {
    public Shield(String name, double price, double weight, double defense) {
        super(name, price, weight, defense);
    }
}

class Knight {
    protected ArrayList<Ammunition> ammunitions = new ArrayList<>();
    public void addAmmunition(Ammunition ammunition) {
        ammunitions.add(ammunition);
    }

    public void getAllInfo () {
        for (Ammunition ammunition : ammunitions) {
            System.out.println(ammunition.getInfo());
        }
    }

    public double calculatePrice() {
        double totalCost = 0;
        for (Ammunition ammunition : ammunitions) {
            totalCost += ammunition.getPrice();
        }
        return totalCost;
    }

    public void weightSort() {
        ammunitions.sort(Comparator.comparing(Ammunition::getWeight));
        for (Ammunition ammunition : ammunitions) {
            System.out.println(ammunition.getName() + ": " + ammunition.getWeight() + " kgs");
        }
    }

    public void findByPrice(double price1, double price2) {
        boolean found = false;
        for (Ammunition ammunition : ammunitions) {
            if (ammunition.getPrice() >= price1 && ammunition.getPrice() <= price2) {
                found = true;
                System.out.println(ammunition.getName() + ": $" + ammunition.getPrice());
            }
        }
        if (!found) {
            System.out.println("There are no items of ammunition in this range of price!");
        }
    }

    private double getPrice(Scanner scanner, String priceType) {
        System.out.print("\nEnter " + priceType + " price of the ammunition: ");
        double price = scanner.nextDouble();
        validatePrice(price, priceType);
        return price;
    }

    private static void validatePrice(double price, String priceType) {
        if (price <= 0) {
            throw new InputMismatchException("The" + priceType + " price can't be less than zero!");
        }
    }

    private static void isValidPriceRange(double minPrice, double maxPrice) {
        if (minPrice > maxPrice) {
            throw new InputMismatchException("Entered range is invalid!");
        }
    }


    public void findAmmunitionByPrice(Knight knight) {
        Scanner scanner = new Scanner(System.in);
        double minPrice = getPrice(scanner, "minimum");
        double maxPrice = getPrice(scanner, "maximum");
        isValidPriceRange(minPrice, maxPrice);
        System.out.print("\nThe ammunition in this price range:\n");
        knight.findByPrice(minPrice, maxPrice);
    }
}

public class Main {
    public static void main(String[] args) {
        Knight knight = new Knight();
        knight.addAmmunition(new Armor("cuirass", 1435.00, 25.56, 100.00));
        knight.addAmmunition(new Armor("bracers", 371.50, 2.00, 23.45));
        knight.addAmmunition(new Helmet("helmet", 209.85, 7.45, 5.05));
        knight.addAmmunition(new Weapon("sword", 2397.00, 2.50, 35.60));
        knight.addAmmunition(new Shield("shield", 56.90, 11.05, 3.33));
        System.out.println("Information about the ammunition of the knight:");
        knight.getAllInfo();
        System.out.println("\nTotal cost of the ammunition is $" + knight.calculatePrice() + ".");
        System.out.println("\nAmmunition sorted by weight:");
        knight.weightSort();
        knight.findAmmunitionByPrice(knight);
    }
}
