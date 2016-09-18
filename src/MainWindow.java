import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * окно для отображения игры. в метод по рисованию будет просто передаваться текущее состояние поля.
 * т.е. предполагается, что логика игры может выполнятся где угодно и передает что нужно в окно для отрисовки
 * окно будет создаваться по размерам игрового массива
 *
 * @author  Artur Antonov
 * @version 3.0.180916
 */
public class MainWindow extends JFrame {

    private int width;
    private int height;
    private Canvas canvas;
    RobotGame robotGame;

    // в конструктор передается размерность поля ( пока что - размерность массива 10х10)
    //TODO исправить комент по ходу дела
    public MainWindow(int width, int height) {
        this.width = width;
        this.height = height;

        setBounds(500, 300, 516, 588);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        canvas = new Canvas();
        add(canvas, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setPreferredSize(new Dimension(1, 50));
        add(buttonPanel, BorderLayout.SOUTH);

        JButton btn1 = new JButton("RESTART");
        btn1.setPreferredSize(new Dimension(120, 40));
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robotGame.round = 1;
                robotGame.setGameOver(true);
                robotGame.isRestarted = true;
            }
        });
        buttonPanel.add(btn1);

        JButton btn2 = new JButton("NEXT ROUND");
        btn2.setPreferredSize(new Dimension(120, 40));
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // полагаю, что кнопка не должна содержать в себе логику.
                // кнопка должна запускать метод из класса игры
                //TODO перенести логику в соответствующий метод класса игры
                if (robotGame.winner_state == 0) {
                    robotGame.round += 1;
                    robotGame.setGameOver(true);
                    robotGame.isRestarted = true;
                } else {
                    JOptionPane.showMessageDialog(MainWindow.this, "Player isn't winner. Press RESTART button to restart game");
                }
            }
        });
        buttonPanel.add(btn2);

        JButton btn3 = new JButton("next round cheat");
        //btn3.setPreferredSize(new Dimension(120, 40));
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                robotGame.round += 1;
                robotGame.setGameOver(true);
                robotGame.isRestarted = true;

            }
        });
        buttonPanel.add(btn3);

        setResizable(true);
        setVisible(true);
    }

    // Метод для получения канваса для последующего управления им
    public Canvas getCanvas() {
        return canvas;
    }

    // Метод для задания менеджера игры


    public void setGame(RobotGame robotGame) {
        this.robotGame = robotGame;
    }

    // Панель для отрисовки поля игры
    class Canvas extends JPanel {

        int canvasHeight;
        int canvasWidth;

        public Canvas() {

            setBackground(Color.WHITE);
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);

            canvasHeight = getHeight();
            canvasWidth = getWidth();
            int fieldSize = MainWindow.this.width;
            char[][] field = robotGame.getField();
            g.setColor(Color.LIGHT_GRAY);

            //когда поле нацело не делится на нужное количество строк, тогда появялются большие (маленькие) лишние ячейки
            // в этом случае надо отцентрировать. задать первоначальное значение x y не ноль, а число,
            // компенсирующее разницу в делении ширины окна на количество строк (столбцов)
            // TODO задать компенсирующее число для x y
            // TODO учесть вариант, когда поле может быть прямоугольным
            for (int i = 0; i < field.length; i++) {
                // Вертикальные линии
                g.drawLine(canvasWidth / fieldSize * (i + 1), 0, canvasWidth / fieldSize * (i + 1), getHeight());
                // Горизонтальные линии
                g.drawLine(0, canvasHeight / fieldSize * (i + 1), getWidth(), canvasHeight / fieldSize * (i + 1));
            }

            // Как оказалось - окошко занимает сверху 38  и по бокам еще 16. Основное окошко. можно вычислить нужный размер канвы
            // у меня тут канва 500 + 50 на нижнюю панель + 38 на основное окошко приложения. и по бокам + 16
//            System.out.println("canvas height = " + getHeight());
//            System.out.println("window height = " + MainWindow.this.getHeight());
//            System.out.println("canvas width = " + getWidth());
//            System.out.println("canvas width = " + MainWindow.this.getWidth());


            for (Point member : robotGame.getMembers()) {
                g.setColor(member.getColor());
                int cellWidth = canvasWidth / fieldSize;
                int cellHeight = canvasHeight / fieldSize;
                // в клетке делаем смещение
                g.fillOval(member.getX() * cellWidth  + (cellWidth / 4), member.getY() * cellHeight + (cellHeight / 4), canvasWidth / fieldSize / 2, canvasHeight / fieldSize / 2);
            }

            if (robotGame.winner_state != robotGame.NOT_OVER_STATE) {
                int state = robotGame.winner_state;
                g.setColor(new Color(20, 20, 20, 150));
                g.fillRect(0, 0, 500, 500);
                g.setFont(new Font("Arial", Font.BOLD, 28));
                g.setColor(new Color(230, 230, 230));
                // Добавляем несколько новых надписей.
                switch (state) {
                    case 0:
                        g.drawString("Победил игрок", 5, 110);
                        break;
                    case 1:
                        g.drawString("Победил компьютер", 5, 110);
                        break;
                    default:
                        g.drawString("Внутренняя ошибка", 5, 110);
                        break;
                }
            }
        }
    }
}
