import java.awt.*;
import java.util.Random;

/**
 * Класс-родитель для отображения и поведения участников игры
 * у участника на поле есть координаты
 * есть цвет (иконка)
 * А зачем делать классы наследники. если у всех все одно и тоже, разница только в цвете(иконке)?
 *
 * @author  Artur Antonov
 * @version 2.0.050916
 */

public class Point {

    // Имя робота
    private String name;

    // Цвет для графической отрисовки
    private Color color;

    // Координаты на старте
    private int y = -1;
    private int x = -1;

    // Значок точки - символ(иконка) отображения на игровом поле
    private char symbol;

    // Связь с распорядителем игры
    RobotGame robotGame;

    //Желаемая цель
    Point wishTarget;

    // Исключаемая цель
    Point ignoreTarget;

    Random random = new Random();

    // Стандартные геттеры\сеттеры
    public Color getColor() {
        return color;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public char getSymbol() {
        return this.symbol;
    }

    public String getName() {
        return name;
    }

    // Сеттеры по типу паттерна builder для инициализации полей кучей
    public Point setColor(Color color) {
        this.color = color;
        return this;
    }

    public Point setSymbol(char symbol) {
        this.symbol = symbol;
        return this;
    }

    public Point setY(int y) {
        this.y = y;
        return this;
    }

    public Point setX(int x) {
        this.x = x;
        return this;
    }

    public Point setName(String name) {
        this.name = name;
        return this;
    }

    public Point setGame(RobotGame robotGame) {
        this.robotGame = robotGame;
        return this;
    }

    public Point setWishTarget(Point wishTarget) {
        this.wishTarget = wishTarget;
        return this;
    }

    public Point setIgnoreTarget(Point ignoreTarget) {
        this.ignoreTarget = ignoreTarget;
        return this;
    }

    // Метод для движения точки в своем раунде.
    // по итогам этого метода точка прописывает свое новое положение в поле игры
    // точка имеет свои координаты, точка уже находится на поле, точка можед двигаться в пределах поля и что-то замечать
    // Метод запускается игрой
    public void move () {
        robotGame.emptyMemberLastTurnPlace(this); // перед началом хода очищаем поле под участником
        while (true) {
            this.setX(random.nextInt(robotGame.FIELD_HEIGHT))
                .setY(random.nextInt(robotGame.FIELD_WIDTH));
            if (!this.isFindOther(ignoreTarget)) {
                // Размещение хода на поле
                robotGame.putMemberOnField(this);
                break;
            }
        }
        System.out.println(this.name + " сделал ход");
    }

    // Метод, который проверяет совпадение местоположения с другим роботом
    public boolean isFindOther(Point point) {
       return this.x == point.getX() && this.y == point.getY();
    }

    // Метод, который проверяет условие победы (совпали координаты с желаемой целью-роботом)
    // Метод общий для всех классов
    // Фишка в том, что "не те" совпадения исключены условием хода.
    // Метод проверяет условие и, в случае его выполнения, передает инфу распорядителю игры
    // Метод запускается игрой
    public boolean isWin() {
        if (isFindOther(wishTarget)) {
            robotGame.setWinner(this.getClass().getSimpleName()); // сообщили свое имя - как победителя
            robotGame.setGameOver(true); // сообщили о прекращении игры
            return true;
        }
        return false;
    }
}