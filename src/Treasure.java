
/**
 * Класс для сокровища
 *
 * @author  Artur Antonov
 * @version 3.0.180916
 */
public class Treasure extends Point {

    // Стоит на месте и ничего не делает
    @Override
    public void move() {
//        System.out.println(this.getName() + " сделал ход.");
        // do nothing
    }

    // Сам не побеждает
    @Override
    public boolean isWin() {
        return false;
    }
}
