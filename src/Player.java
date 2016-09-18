import java.util.ArrayList;

/**
 * Класс для робота-игрока
 *
 * @author  Artur Antonov
 * @version 3.0.180916
 */
public class Player extends Point {

    private ArrayList<Point> ignoreTargets;

    // Робот-игрок взаимодействует с сокровищем и НЕ взаимодействует с роботом-противником
    // Переопределяем метод, т.к. должен не попасться куче целей. игнорируем список врагов, а не одного врага
    @Override
    public void move () {
        robotGame.emptyMemberLastTurnPlace(this); // перед началом хода очищаем поле под участником
        while (true) {
            this.setX(random.nextInt(robotGame.FIELD_HEIGHT))
                    .setY(random.nextInt(robotGame.FIELD_WIDTH));
            if (!this.isFindOther(ignoreTargets)) {
                // Размещение хода на поле
                robotGame.putMemberOnField(this);
                break;
            }
        }
//        System.out.println(this.name + " сделал ход");
    }

    // Метод по добавлению цели для игнорирования
    public void addIgnoreTarget(Point point) {
        ignoreTargets.add(point);
    }

    // Метод по первоначальной инициализации списка игнорируемых целей (врагов в данном случае)
    @Override
    public Point setIgnoreTarget(Point ignoreTarget) {
        ignoreTargets = new ArrayList<>();
        ignoreTargets.add(ignoreTarget);
        return this;
    }
}
