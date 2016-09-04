import java.awt.*;

/**
 * Created by v-aranto on 02.09.2016.
 * Класс-родитель для отображения и поведения участников игры
 * у участника на поле есть координаты
 * есть цвет (иконка)
 * А зачем делать классы наследники. если у всех все одно и тоже, разница только в цвете(иконке)?
 * А для того, чтобы отслеживать победы и прочее (вроде бы как)
 */

public class Point {
    protected Color color;
    private int y;
    private int x;

    public Point() {

    }

    public Point(int y, int x) {
        this.y = y;
        this.x = x;
    }

    public Color getColor() {
        return color;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public int[] getPos() {
        int[] posCoord = new int[2];
        posCoord[0] = y;
        posCoord[1] = x;
        return posCoord;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setX(int x) {
        this.x = x;
    }

}