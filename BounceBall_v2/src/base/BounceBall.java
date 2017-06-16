package base;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class BounceBall extends JPanel {
	//球的位置以及速度
	int x, y, dx, dy;
	//RGB方式产生随机色彩
	int red = 0, green = 0, blue = 0;
	//游戏区域
	int width = 0, height = 0;
//球以及挡板的大小
	int ballSize = 50;
	int barSize = 200;

	int barX, barY = (600 - 16 - 24) - 20;
	
	//游戏速度
	static final int SPEED=10;
	static int maxScore = 0;
	static int score = 0;

	static JFrame f = new JFrame("Bounce Ball Game");
	JButton btn = new JButton("Start");
	//使用timer作为游戏循环
	Timer t = new Timer(3, new TimerHandler());
	JLabel max = new JLabel("Highest:  " + maxScore);
	JLabel sco = new JLabel("Score:     " + score);
	JLabel dev = new JLabel("Developed by YeongHyeon");

	BounceBall() {
		setLayout(null);
		init();

		Container contentPane = f.getContentPane();
		contentPane.setLayout(null);

		btn.setBackground(Color.WHITE);
		btn.setFont(new Font("", 1, 15));
		btn.setSize(150, 30);
		btn.setLocation(800 - 30 - 150, 20);
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JButton b = (JButton) e.getSource();
				if (b.getText().equals("Start")) {
					t.start();
					b.setText("Pause");
					f.requestFocus();
				} else {
					t.stop();
					b.setText("Start");
				}
			}
		});
		f.addKeyListener(new MyKeyListener());

		max.setFont(new Font("", 0, 15));
		max.setSize(150, 30);
		max.setLocation(800 - 30 - 150, 60);
		sco.setFont(new Font("", 0, 15));
		sco.setSize(150, 30);
		sco.setLocation(800 - 30 - 150, 90);

		dev.setFont(new Font("", 0, 10));
		dev.setSize(150, 30);
		dev.setLocation(800 - 30 - 150, 600 - 100);

		add(btn);
		add(max);
		add(sco);
		add(dev);
	}

	public void init() {
		ballSize = 50;
		x = (int) (Math.random() * 800 - 16 - ballSize);
		y = 10;
		dx = 1;
		dy = 1;
		barSize = 2000;
		barX = (800 - 16) / 2 - barSize / 2;
	}

	public void randomColor() {
		red = (int) (Math.random() * 256);
		green = (int) (Math.random() * 256);
		blue = (int) (Math.random() * 256);
	}
/**
 * 调试球与板子的接触信息
 * @return
 */
	public boolean touch() {
		int center = x + ballSize / 2;
		if (center >= barX && center <= barX + barSize) {
			System.out.printf("Ball:%4d(%3d)\t%4d -%4d(%3d)\n", center, ballSize, barX, barX + 100, barSize);
			return true;
		} else {
			return false;
		}
	}
/**
 * 随着积分越多球变得越来越小
 */
	public void sizing() {
		if (score >= 5000) {
			int size = 0;
			while (true) {
				size = (int) (Math.random() * 100);
				if (size >= 10) {
					break;
				}
			}
			ballSize = size;
			barSize -= 10;
		} else if (score >= 3000) {
			ballSize += 10;
			barSize -= 10;
		} else if (score >= 2000) {
			ballSize += 10;
			barSize -= 10;
		} else if (score >= 1500) {
			ballSize += 10;
			barSize -= 10;
		} else if (score >= 1000) {
			ballSize += 5;
			barSize -= 5;
		} else if (score >= 500) {
			ballSize += 5;
			barSize -= 5;
		} else if (score >= 300) {
			ballSize += 5;
			barSize -= 5;
		}
	}
/**
 * timer的时间循环实现
 * @author lenovo
 *
 */
	class TimerHandler implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			boolean notTouched = true;

			width = f.getWidth() - (ballSize + 16);
			height = f.getHeight() - (ballSize + 16 + 24);

			//球的位移实现
			if (dx == 1) {
				x += SPEED;
				if (x >= width) {
					dx = -1;
					randomColor();
				}
			} else {
				x -= SPEED;
				if (x <= 0) {
					dx = 1;
					randomColor();
				}
			}
			if (dy == 1) {
				y += SPEED;
				if (y >= height - 20 && touch()) {
					notTouched = false;
					score += ballSize;
					sco.setText("Score:     " + score);
					dy = -1;
					randomColor();
				}
			} else {
				y -= SPEED;
				if (y <= 0) {
					dy = 1;
					randomColor();
					sizing();
				}
			}

			if (y >= height - 20 && notTouched) {
				init();
				t.stop();
				btn.setText("Start");
				maxScore = Math.max(score, maxScore);
				max.setText("Highest:  " + maxScore);
			}

			repaint();
		}
	}
/**
 * 内部类实现按键监控
 * @author lenovo
 *
 */
	class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_LEFT) {
				if (barX >= 10) {
					barX -= 10;
				}
			} else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
				if (barX <= (800 - 20 - barSize)) {
					barX += 10;
				}
			}
			//每次按键后重绘
			repaint();
		}
	}
/**
 * 绘制游戏组件
 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(red, green, blue));
		//画球
		g.fillOval(x, y, ballSize, ballSize);
		g.setColor(Color.BLACK);
		//画挡板
		g.fillRect(barX, barY, barSize, 20);
	}
/**
 * 游戏启动
 * @param args
 */
	public static void main(String[] args) {
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setContentPane(new BounceBall());
		f.setSize(800, 600);
		f.setVisible(true);
	}
}