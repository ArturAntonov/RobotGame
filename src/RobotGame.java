import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Treasure Adventure
 * Robot Game
 * Этот класс также выполняет роль менеджера игры
 * Это основной класс процесса игры. Тут все и происходит
 *
 * @author  Artur Antonov
 * @version 3.0.180916
 */
public class RobotGame {

    // Константы
    final int FIELD_HEIGHT = 10;
    final int FIELD_WIDTH = 10;
    final char SYMBOL_EMPTY = '.';
    final char SYMBOL_ENEMY = 'X';
    final char SYMBOL_TREASURE = '!';
    final char SYMBOL_PLAYER = 'o';
    final long TURN_DELAY = 500;


    // Участники игры
    private Player player;
    private Enemy enemy;
    private Treasure treasure;
    // Список со всеми участниками игры
    private ArrayList<Point> members;

    Random random = new Random();

    // Системные флаги
    volatile boolean isRestarted = true;
    private boolean isGameOver = true;
    private String winner = "";
    int round = 1;

    // Статусы игры
    final int PLAYER_WIN_STATE = 0;
    final int COMPUTER_WIN_STATE = 1;
    final int NOT_OVER_STATE = 99;
    int winner_state = NOT_OVER_STATE;

    // Поле для игры
    private char[][] field = new char[FIELD_HEIGHT][FIELD_WIDTH];


    // Основной метод действия игры
    public void go() {

        // Графическое окно игры
        MainWindow mainWindow = new MainWindow(FIELD_WIDTH, FIELD_HEIGHT);
        mainWindow.setGame(this);
        MainWindow.Canvas canvas = mainWindow.getCanvas();


        while (true) { // Завершение цикла будет вместе с закрытием окна игры
            if (isRestarted) {
                //инициализация игрового поля призом, врагом, игроком
                initField();
                // Начальная отрисовка игрового поля
                canvas.repaint();
                isRestarted = false;
                isGameOver = false;
                mainWindow.setTitle("Robot Game Started. Count Enemy : " + round);
                winner_state = NOT_OVER_STATE;
            }
            // Основной цикл игры
            while (!isGameOver) {
                for (Point member : members) {
                    member.move();
                    canvas.repaint(); // новое место ( так больеш нравится)
                    if (member.isWin()) break;
                }
//                canvas.repaint();   // старое место
                try {
                    Thread.sleep(TURN_DELAY);
                } catch (InterruptedException ex) {
                    ex.getStackTrace();
                }
            }
//            System.out.println("--------ПОБЕДИТЕЛЬ ОПРЕДЕЛЕН--------");
//            drawField();

            // Чествование победителя
            if (!winner.isEmpty()) {
                switch (winner) {
                    case "Player":
//                    System.out.println("Победитель - Робот-Игрок!");
                        mainWindow.setTitle("Победитель - Робот-Игрок!");
                        winner_state = PLAYER_WIN_STATE;
                        break;
                    case "Enemy":
//                    System.out.println("Победитель - Робот-Противник!");
                        mainWindow.setTitle("Победитель - Робот-Противник!");
                        winner_state = COMPUTER_WIN_STATE;
                        break;
                    default:
//                    System.out.println("Ничья!");
                }
                canvas.repaint();
                winner = ""; // обнулили победителя
//                System.out.println("ROUND END");
            }
        }
    }


    // Метод для стартовых приготовлений перед игрой.
    // Разметить поле
    // Обозначит участников, объяснить правила игры, расставить по полю
    public void initField() {
        // Вот поле
        // Добавление "пустых" ячеек в матрицу для инициализации поля
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for( int j = 0; j < FIELD_WIDTH; j++) {
                field[i][j] = SYMBOL_EMPTY;
            }
        }

        // Кто участники, какие у вас Логотипы, какие у вас цели?
        player = new Player();
        enemy = new Enemy();
        treasure = new Treasure();
        player.setName("Робот-Искатель")
                .setSymbol(SYMBOL_PLAYER)
                .setWishTarget(treasure)
                .setIgnoreTarget(enemy)
                .setColor(Color.GREEN);
        enemy.setName("Робот-Противник")
                .setSymbol(SYMBOL_ENEMY)
                .setWishTarget(player)
                .setIgnoreTarget(treasure)
                .setColor(Color.RED);
        treasure.setName("Сокровище").setSymbol(SYMBOL_TREASURE).setColor(Color.YELLOW);
        //Участникам указали на их цели
        //Участники, записывайтесь!
        members = new ArrayList<>();
        // Порядок важен
        members.add(treasure);
        members.add(player);
        members.add(enemy);
        //В новом раунде (кроме первого) добавляем еще одного противника
        // Нового противника добавляем в список Нежелательных целей для игрока
        for (int i = 1; i < round; i++) {
            Enemy newEnemy = new Enemy();
            newEnemy.setName("Робот-Противник")
                    .setSymbol(SYMBOL_ENEMY)
                    .setWishTarget(player)
                    .setIgnoreTarget(treasure)
                    .setColor(Color.RED);
            members.add(newEnemy);
            player.addIgnoreTarget(newEnemy);
        }
        // Участникам объяснили кто рулит игрой
        setGameMasterToMembers(members);
        // Ты будешь тут, ты тут, а ты вообще вот здесь
        for (Point member: members) {
            setMemberStartPosition(member); // дали роботу координаты
            putMemberOnField(member); // разместили робота на поле
        }
    }

    // Метод, который выводит графическое отображения поля на консоль
    private void drawField() {
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for( int j = 0; j < FIELD_WIDTH; j++) {
                System.out.print(field[i][j] + "\t");
            }
            System.out.println();
        }
        // Отступы для красоты консольного отображения
        System.out.println();
        System.out.println();
    }

    // Метод, который выдает участнику координаты на поле (не имеет какой-то привязки к целям)
    private void setMemberStartPosition (Point member) {
        while(true) {
            int memberY = random.nextInt(FIELD_HEIGHT);
            int memberX = random.nextInt(FIELD_WIDTH);
            if (field[memberY][memberX] == SYMBOL_EMPTY) {
                member.setY(memberY).setX(memberX);
                break;
            }
        }
    }

    // Метод, который размещает игрока на поле (его иконку\логотип)
    public void putMemberOnField(Point member) {
        int y = member.getY();
        int x = member.getX();
        field[y][x] = member.getSymbol();
    }

    // Метод, который всем участникам-роботам говорит кто правит игрой
    public void setGameMasterToMembers(ArrayList<Point> members) {
        for (Point member: members) {
            member.setGame(this);
        }
    }

    // метод, который сбрасывает отображение участника на его прошлом ходе
    public void emptyMemberLastTurnPlace(Point point) {
        int x = point.getX();
        int y = point.getY();
        field[y][x] = SYMBOL_EMPTY;
    }

    public char[][] getField() {
        return field;
    }

    public Player getPlayer() {
        return player;
    }

    public Enemy getEnemy() {
        return enemy;
    }

    public Treasure getTreasure() {
        return treasure;
    }

    public ArrayList<Point> getMembers() {
        return members;
    }

    public String getWinner() {
        return winner;
    }

    public void setWinner(String winner) {
        this.winner = winner;
    }

    public boolean getGameOverFlag() {
        return isGameOver;
    }

    public void setGameOver(boolean gameOver) {
        isGameOver = gameOver;
    }
}
