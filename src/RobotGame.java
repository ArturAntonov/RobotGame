import java.util.Random;

/**
 * Treasure Adventure
 * Robot Game
 * Этот класс также выполняет роль менеджера игры
 *
 * @author  Artur Antonov
 * @version 31.08.2016
 */
public class RobotGame {

    // Константы
    private final int FIELD_HEIGHT = 10;
    private final int FIELD_WIDTH = 10;
    private final char SYMBOL_EMPTY = '.';
    private final char SYMBOL_ENEMY = 'X';
    private final char SYMBOL_TREASURE = '!';
    private final char SYMBOL_PLAYER = 'o';
    private final long TURN_DELAY = 500;

    // Координаты ходов
    private int[] lastPlayerTurnCoord = new int[2];
    private int[] lastEnemyTurnCoord = new int[2];
    private int[] treasureCoord = new int[2];

    Random random = new Random();

    private char[][] field = new char[FIELD_HEIGHT][FIELD_WIDTH];

    public static void main(String[] args) {
        new RobotGame().go();
    }

    private void go() {
        //инициализация игрового поля призом, врагом, игроком
        initField();
        // Начальная отрисовка игрового поля
        drawField();

        while (true) {
            playerTurn();
            reInitField();
            drawField();
            if (isWinPlayer()) {
                System.out.println("Congratulation!!!");
                System.out.println("Player WIN!!! Player have treasure");
                break;
            }

            // Для просмотра и тестирования ходы автоматизированы. Добавлена задержка между ходами
            try {
                Thread.sleep(TURN_DELAY);
            } catch (InterruptedException ex) {
                ex.getStackTrace();
            }

            computerTurn();
            reInitField();
            drawField();
            if (isLoosePlayer()) {
                System.out.println("GAME OVER");
                System.out.println("Player loose. Enemy WIN!");
                break;
            }
        }

    }

    /**
     * Метод для обновления поля с актуальными координатами
     */
    private void reInitField() {
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for( int j = 0; j < FIELD_WIDTH; j++) {
                field[i][j] = SYMBOL_EMPTY;
            }
        }

        //Добавление координат игрока, противника и приза
        field[treasureCoord[0]][treasureCoord[1]] = SYMBOL_TREASURE;
        field[lastPlayerTurnCoord[0]][lastPlayerTurnCoord[1]]= SYMBOL_PLAYER;
        field[lastEnemyTurnCoord[0]][lastEnemyTurnCoord[1]] = SYMBOL_ENEMY;
    }

    /**
     * Ход игрока. Пока что автоматизирован. Логика такая же как и у компьютера.
     * Проверка на совпадение с врагом.
     */
    private void playerTurn() {
        while (true) {
            int playerTurnY = random.nextInt(FIELD_HEIGHT);
            int playerTurnX = random.nextInt(FIELD_WIDTH);
            if (playerTurnY != lastEnemyTurnCoord[0] && playerTurnX != lastEnemyTurnCoord[1]) {
                // Размещение хода на поле
                field[playerTurnY][playerTurnX] = SYMBOL_PLAYER;
                // Сохранение последнего хода
                lastPlayerTurnCoord[0] = playerTurnY;
                lastPlayerTurnCoord[1] = playerTurnX;
                break;
            }
        }
    }

    /**
     * Метод для хода компьютера.
     * Ход будет выполняться на рандомные координаты поля, отличные от координат сокровища
     */
    private void computerTurn() {
        while (true) {
            int enemyTurnY = random.nextInt(FIELD_HEIGHT);
            int enemyTurnX = random.nextInt(FIELD_WIDTH);
            if (enemyTurnY != treasureCoord[0] && enemyTurnX != treasureCoord[1]) {
                // Размещение хода на поле
                field[enemyTurnY][enemyTurnX] = SYMBOL_ENEMY;
                // Сохранение последнего хода
                lastEnemyTurnCoord[0] = enemyTurnY;
                lastEnemyTurnCoord[1] = enemyTurnX;
                break;
            }
        }

    }

    // Метод для проверки проигрыша игрока. Координаты врага совпадают с координатами игрока
    private boolean isLoosePlayer() {
        int playerY = lastPlayerTurnCoord[0];
        int playerX = lastPlayerTurnCoord[1];
        return (playerY == lastEnemyTurnCoord[0] && playerX == lastEnemyTurnCoord[1]);
    }

    // Метод для проверки выигрыша игрока. Координаты игрока совпали с координатами сокровища
    private boolean isWinPlayer() {
        int playerY = lastPlayerTurnCoord[0];
        int playerX = lastPlayerTurnCoord[1];
        return (playerY == treasureCoord[0] && playerX == treasureCoord[1]);
    }

    private void initField() {
        // Добавление пустых ячеек в матрицу
        for (int i = 0; i < FIELD_HEIGHT; i++) {
            for( int j = 0; j < FIELD_WIDTH; j++) {
                field[i][j] = SYMBOL_EMPTY;
            }
        }

        //TODO Учесть вариант, когда координаты могут сразу совпасть. дописать проверку
        // Добавление в случайную позицию врага
        int enemyY = random.nextInt(10);
        int enemyX = random.nextInt(10);
        field[enemyY][enemyX] = SYMBOL_ENEMY;
        // Добавление в случайную позицию игрока
        int playerY = random.nextInt(10);
        int playerX = random.nextInt(10);
        field[playerY][playerX] = SYMBOL_PLAYER;
        // Добавление приза
        int treasureY = random.nextInt(10);
        int treasureX = random.nextInt(10);
        // Запоминаем координаты приза на будущее
        treasureCoord[0] = treasureY;
        treasureCoord[1] = treasureX;
        field[treasureX][treasureY] = SYMBOL_TREASURE;
    }

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
}
