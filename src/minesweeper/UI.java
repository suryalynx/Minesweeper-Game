package minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class UI extends JFrame {
    private JButton[][] buttons;
    private int rows;
    private int cols;
    private JLabel minesLabel;
    private int mines;
    private JLabel timePassedLabel;
    private Thread timer;
    private int timePassed;
    private boolean stopTimer;
    private final String FRAME_TITLE = "Minesweeper";
    private int FRAME_WIDTH = 520;
    private int FRAME_HEIGHT = 550;
    private int FRAME_LOC_X = 430;
    private int FRAME_LOC_Y = 50;
    private Icon redMine;
    private Icon mine;
    private Icon flag;
    private Icon tile;
    private JMenuBar menuBar;
    private JMenu gameMenu;
    private JMenuItem newGame;
    private JMenuItem statistics;
    private JMenuItem exit;

    public UI(int r, int c, int m) {
        this.rows = r;
        this.cols = c;
        buttons = new JButton[rows][cols];
        setSize(FRAME_WIDTH, FRAME_HEIGHT);
        setTitle(FRAME_TITLE);
        setLocation(FRAME_LOC_X, FRAME_LOC_Y);
        JPanel gameBoard;
        JPanel tmPanel;
        gameBoard = new JPanel();
        gameBoard.setLayout(new GridLayout(rows, cols, 0, 0));
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                buttons[x][y] = new JButton("");
                buttons[x][y].setName(Integer.toString(x) + "," + Integer.toString(y));
                buttons[x][y].setFont(new Font("Serif", Font.BOLD, 24));
                buttons[x][y].setBorder(BorderFactory.createLineBorder(Color.black, 1, true));
                gameBoard.add(buttons[x][y]);
            }
        }
        JPanel timePassedPanel = new JPanel();
        timePassedPanel.setLayout(new BorderLayout(10, 0));
        this.timePassedLabel = new JLabel("  0  ", SwingConstants.CENTER);
        timePassedLabel.setFont(new Font("Serif", Font.BOLD, 20));
        Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
        timePassedLabel.setBorder(loweredetched);
        timePassedLabel.setBackground(new Color(110, 110, 255));
        timePassedLabel.setForeground(Color.white);
        timePassedLabel.setOpaque(true);
        JLabel iT = new JLabel("", SwingConstants.CENTER);
        iT.setIcon(new ImageIcon(getClass().getResource("/resources/clock.png")));
        timePassedPanel.add(iT, BorderLayout.WEST);
        timePassedPanel.add(timePassedLabel, BorderLayout.CENTER);
        timePassedPanel.setOpaque(false);
        this.timePassed = 0;
        this.stopTimer = true;
        JPanel minesPanel = new JPanel();
        minesPanel.setLayout(new BorderLayout(10, 0));
        this.minesLabel = new JLabel("  0  ", SwingConstants.CENTER);
        minesLabel.setFont(new Font("Serif", Font.BOLD, 20));
        minesLabel.setBorder(loweredetched);
        minesLabel.setBackground(new Color(110, 110, 255));
        minesLabel.setForeground(Color.white);
        minesLabel.setOpaque(true);
        setMines(m);
        JLabel mT = new JLabel("", SwingConstants.CENTER);
        mT.setIcon(new ImageIcon(getClass().getResource("/resources/mine.png")));
        minesPanel.add(minesLabel, BorderLayout.WEST);
        minesPanel.add(mT, BorderLayout.CENTER);
        minesPanel.setOpaque(false);
        tmPanel = new JPanel();
        tmPanel.setLayout(new BorderLayout(0, 20));
        tmPanel.add(timePassedPanel, BorderLayout.WEST);
        tmPanel.add(minesPanel, BorderLayout.EAST);
        tmPanel.setOpaque(false);
        menuBar = new JMenuBar();
        gameMenu = new JMenu("Game");
        newGame = new JMenuItem("   New Game");
        statistics = new JMenuItem("   Statistics");
        exit = new JMenuItem("   Exit");
        newGame.setName("New Game");
        statistics.setName("Statistics");
        exit.setName("Exit");
        gameMenu.add(newGame);
        gameMenu.add(statistics);
        gameMenu.add(exit);
        menuBar.add(gameMenu);
        JPanel p = new JPanel();
        p.setLayout(new BorderLayout(0, 10));
        p.add(gameBoard, BorderLayout.CENTER);
        p.add(tmPanel, BorderLayout.SOUTH);
        p.setBorder(BorderFactory.createEmptyBorder(60, 60, 14, 60));
        p.setOpaque(false);
        setLayout(new BorderLayout());
        JLabel background = new JLabel(new ImageIcon(getClass().getResource("/resources/2.jpg")));
        add(background);
        background.setLayout(new BorderLayout(0, 0));
        background.add(menuBar, BorderLayout.NORTH);
        background.add(p, BorderLayout.CENTER);
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/resources/mine.png")));
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void startTimer() {
        stopTimer = false;
        timer = new Thread() {
            @Override
            public void run() {
                while (!stopTimer) {
                    timePassed++;
                    SwingUtilities.invokeLater(() -> {
                        timePassedLabel.setText("  " + timePassed + "  ");
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                    }
                }
            }
        };
        timer.start();
    }

    public void interruptTimer() {
        stopTimer = true;
        try {
            if (timer != null)
                timer.join();
        } catch (InterruptedException ex) {
        }
    }

    public void resetTimer() {
        timePassed = 0;
        timePassedLabel.setText("  " + timePassed + "  ");
    }

    public void setTimePassed(int t) {
        timePassed = t;
        timePassedLabel.setText("  " + timePassed + "  ");
    }

    public void initGame() {
        hideAll();
        enableAll();
    }

    public void enableAll() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].setEnabled(true);
            }
        }
    }

    public void disableAll() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].setEnabled(false);
            }
        }
    }

    public void hideAll() {
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].setText("");
                buttons[x][y].setBackground(new Color(0, 103, 200));
                buttons[x][y].setIcon(tile);
            }
        }
    }

    public void setButtonListeners(Game game) {
        addWindowListener(game);
        for (int x = 0; x < cols; x++) {
            for (int y = 0; y < rows; y++) {
                buttons[x][y].addMouseListener(game);
            }
        }
        newGame.addActionListener(game);
        statistics.addActionListener(game);
        exit.addActionListener(game);
        newGame.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        exit.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        statistics.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
    }

    public JButton[][] getButtons() {
        return buttons;
    }

    public int getTimePassed() {
        return timePassed;
    }

    public static void setLook(String look) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (look.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
        }
    }

    public void setMines(int m) {
        mines = m;
        minesLabel.setText("  " + Integer.toString(m) + "  ");
    }

    public void incMines() {
        mines++;
        setMines(mines);
    }

    public void decMines() {
        mines--;
        setMines(mines);
    }

    public int getMines() {
        return mines;
    }

    private static Icon resizeIcon(ImageIcon icon, int resizedWidth, int resizedHeight) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(resizedWidth, resizedHeight, java.awt.Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }

    public void setIcons() {
        int bOffset = buttons[0][1].getInsets().left;
        int bWidth = buttons[0][1].getWidth();
        int bHeight = buttons[0][1].getHeight();
        ImageIcon d;
        d = new ImageIcon(getClass().getResource("/resources/redmine.png"));
        redMine = resizeIcon(d, bWidth - bOffset, bHeight - bOffset);
        d = new ImageIcon(getClass().getResource("/resources/mine.png"));
        mine = resizeIcon(d, bWidth - bOffset, bHeight - bOffset);
        d = new ImageIcon(getClass().getResource("/resources/flag.png"));
        flag = resizeIcon(d, bWidth - bOffset, bHeight - bOffset);
        d = new ImageIcon(getClass().getResource("/resources/tile.png"));
        tile = resizeIcon(d, bWidth - bOffset, bHeight - bOffset);
    }

    public Icon getIconMine() {
        return mine;
    }

    public Icon getIconRedMine() {
        return redMine;
    }

    public Icon getIconFlag() {
        return flag;
    }

    public Icon getIconTile() {
        return tile;
    }

    public void setTextColor(JButton b) {
        if (b.getText().equals("1"))
            b.setForeground(Color.blue);
        else if (b.getText().equals("2"))
            b.setForeground(new Color(76, 153, 0));
        else if (b.getText().equals("3"))
            b.setForeground(Color.red);
        else if (b.getText().equals("4"))
            b.setForeground(new Color(153, 0, 0));
        else if (b.getText().equals("5"))
            b.setForeground(new Color(153, 0, 153));
        else if (b.getText().equals("6"))
            b.setForeground(new Color(96, 96, 96));
        else if (b.getText().equals("7"))
            b.setForeground(new Color(0, 0, 102));
        else if (b.getText().equals("8"))
            b.setForeground(new Color(153, 0, 76));
    }
}
