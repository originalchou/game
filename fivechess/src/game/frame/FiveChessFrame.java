package game.frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class FiveChessFrame extends JFrame implements MouseListener, Runnable {
	
	int width = Toolkit.getDefaultToolkit().getScreenSize().width;
	int height = Toolkit.getDefaultToolkit().getScreenSize().height;
	BufferedImage bg= null;
	int x = 0;
	int y = 0;
	int [][] allchess = new int [19][19];
	boolean isblack = true;
	boolean canplay = true;
	String message = "black";
	int lastx = 0;
	int lasty = 0;
	int maxtime = 0;
	Thread th = new Thread(this);
	int blacktime = 0;
	int whitetime = 0;
	int blackleft = blacktime;
	int whiteleft = whitetime;
	String blackmessage = "null";
	String whitemessage = "null";
	boolean canregre = false;
	
	public FiveChessFrame() {
		this.setVisible(true);
		this.setTitle("Five Chess");
		this.setSize(500, 500); 
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation((width-500)/2, (height-500)/2);
		this.addMouseListener(this);
		th.start();
		th.suspend();
		String imagePath = "" ;
		try {
			imagePath = System.getProperty("user.dir")+"/background2.jpg" ;
			bg = ImageIO.read(new File(imagePath.replaceAll("\\\\", "/")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.repaint();
	}
	
	public void paint(Graphics g) {
		BufferedImage bi = new BufferedImage(500, 500,
				BufferedImage.TYPE_INT_RGB);
		Graphics g2 = bi.createGraphics();
		g2.setColor(Color.BLACK);
		g2.drawImage(bg, 1, 20, this);
		g2.setFont(new Font("����",Font.BOLD,25));
		g2.drawString("current side :" + message, 100, 60);
		g2.setFont(new Font("����",0,15));
		g2.drawString("black time:" + blackmessage, 30, 470);
		g2.drawString("white time:" + whitemessage, 260, 470);
		
		for (int i = 0; i < 19; i++) {
			g2.drawLine(10, 70 + 20 * i, 370 , 70 + 20 * i);
			g2.drawLine(10 + 20 * i, 70 , 10 + 20 * i, 430);
		}
		
		g2.fillOval(68,128,5,5);
		g2.fillOval(308,128,5,5);
		g2.fillOval(308,368,5,5);
		g2.fillOval(68,368,5,5);
		g2.fillOval(308,248,5,5);
		g2.fillOval(188,128,5,5);
		g2.fillOval(68,248,5,5);
		g2.fillOval(188,368,5,5);
		g2.fillOval(188,248,5,5);
		
		for (int i = 0; i < 19; i++) {
			for (int j = 0 ; j < 19 ; j++) {
				int tempx = i * 20 + 10;
				int tempy = j * 20 + 70;
				if (allchess[i][j] == 1) {
					
					g2.fillOval(tempx - 7 , tempy - 7, 14, 14);
				}
				if (allchess[i][j] == 2) {
					g2.setColor(Color.WHITE);
					g2.fillOval(tempx - 7 , tempy - 7, 14, 14);
					g2.setColor(Color.BLACK);
					g2.drawOval(tempx - 7 , tempy - 7, 14, 14);
				}
			}
		}
		g.drawImage(bi, 0, 0, this);
	}
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		if (canplay == true) {
		x = arg0.getX();
		y = arg0.getY();
		if (x >= 10 && x <= 370 && y >= 70 && y <= 430) {
			x = (x - 10) / 20;
			y = (y - 70) / 20;
			lastx = x;
			lasty = y;
			if (allchess[x][y] == 0) {
				if (isblack == true) {
					whiteleft = whitetime;
					allchess[x][y] = 1;
					isblack = false;
					canregre = true;
					message = "�ֵ��׷�";
					} else {
						blackleft = blacktime;
						allchess[x][y] = 2;
						isblack = true;
						canregre = true;
						message = "white";
						}
				boolean winflag = this.checkwin();
				if (winflag == true) {
					JOptionPane.showMessageDialog(this,"��Ϸ����,"+( allchess[x][y] == 1 ? "�ڷ�ʤ" : "�׷�ʤ"));
					canplay = false;
				}
			} else {
				JOptionPane.showMessageDialog(this, "��λ����������,����������");
			}
			this.repaint();
			}
		}
		// ��� ��ʼ��Ϸ ��ť
				if (arg0.getX() >= 400 && arg0.getX() <= 470 && arg0.getY() >= 70
						&& arg0.getY() <= 100) {
					int result = JOptionPane.showConfirmDialog(this, "�Ƿ����¿�ʼ��Ϸ?");
					if (result == 0) {
						
						for (int i = 0; i < 19; i++) {
							for (int j = 0; j < 19; j++) {
								allchess[i][j] = 0;
							}
						}
						canregre = false;
						message = "�ڷ�����";
						isblack = true;
						blacktime = maxtime;
						whitetime = maxtime;
						if (maxtime > 0) {
							blackmessage = maxtime / 3600 + ":"
									+ (maxtime / 60 - maxtime / 3600 * 60) + ":"
									+ (maxtime - maxtime / 60 * 60);
							whitemessage = maxtime / 3600 + ":"
									+ (maxtime / 60 - maxtime / 3600 * 60) + ":"
									+ (maxtime - maxtime / 60 * 60);
							th.resume();
						} else {
							blackmessage = "������";
							whitemessage = "������";
						}
						this.canplay = true; 
						this.repaint();

					}
				}
				// ��� ��Ϸ���� ��ť
				if (arg0.getX() >= 400 && arg0.getX() <= 470 && arg0.getY() >= 120
						&& arg0.getY() <= 150) {
					String input = JOptionPane
							.showInputDialog("��������Ϸ�����ʱ��(��λ:����),�������0,��ʾû��ʱ������:");
					try {
						maxtime = Integer.parseInt(input) * 60;
						if (maxtime < 0) {
							JOptionPane.showMessageDialog(this, "��������ȷ��Ϣ,���������븺��!");
						}
						if (maxtime == 0) {
							int result = JOptionPane.showConfirmDialog(this,
									"�������,�Ƿ����¿�ʼ��Ϸ?");
							if (result == 0) {
								for (int i = 0; i < 19; i++) {
									for (int j = 0; j < 19; j++) {
										allchess[i][j] = 0;
									}
								}
								canregre = false;
								message = "�ڷ�����";
								isblack = true;
								blacktime = maxtime;
								whitetime = maxtime;
								blackmessage = "������";
								whitemessage = "������";
								this.canplay = true; 
								this.repaint();
							}
						}
						if (maxtime > 0) {
							int result = JOptionPane.showConfirmDialog(this,
									"�������,�Ƿ����¿�ʼ��Ϸ?");
							if (result == 0) {
								for (int i = 0; i < 19; i++) {
									for (int j = 0; j < 19; j++) {
										allchess[i][j] = 0;
									}
								}
								canregre = false;
								message = "�ڷ�����";
								isblack = true;
								blacktime = maxtime;
								whitetime = maxtime;
								blackmessage = maxtime / 3600 + ":"
										+ (maxtime / 60 - maxtime / 3600 * 60) + ":"
										+ (maxtime - maxtime / 60 * 60);
								whitemessage = maxtime / 3600 + ":"
										+ (maxtime / 60 - maxtime / 3600 * 60) + ":"
										+ (maxtime - maxtime / 60 * 60);
								th.resume();
								this.canplay = true; 
								this.repaint();
							}
						}
					} catch (NumberFormatException e1) {
						// TODO Auto-generated catch block
						JOptionPane.showMessageDialog(this, "����ȷ������Ϣ!");
					}
				}
		
		//  ��Ϸ˵�� 
		if (arg0.getX() >= 400 && arg0.getX() <= 470 && arg0.getY() >= 170
				&& arg0.getY() <= 200) {
			JOptionPane.showMessageDialog(this,
					"���һ����������Ϸ���򣬺ڰ�˫���������壬��ĳһ����������ʱ����Ϸ������");
		}
		// ���� ��ť
		if (arg0.getX() >= 400 && arg0.getX() <= 470 && arg0.getY() >= 270
				&& arg0.getY() <= 300) {
			int result = JOptionPane.showConfirmDialog(this, "�Ƿ�ȷ������?");
			if (result == 0) {
				if (isblack) {
					JOptionPane.showMessageDialog(this, "�ڷ��Ѿ�����,��Ϸ����!");
				} else {
					JOptionPane.showMessageDialog(this, "�׷��Ѿ�����,��Ϸ����!");
				}
				canplay = false;
			}
		}
		// ����
		if (arg0.getX() >= 400 && arg0.getX() <= 470 && arg0.getY() >= 320
				&& arg0.getY() <= 350) {
			if (canregre == true) { 
			if (canplay == true) {
				th.suspend();
			int result2 = JOptionPane.showConfirmDialog(this, "�Ƿ�ȷ�ϻ���?");
			if (result2 == 0) {
				if (isblack) {
					blacktime = blackleft;
					allchess[lastx][lasty] = 0;
					message = "�ֵ��׷�";
					isblack = false;
					this.repaint();
					th.resume();
					canregre = false;
				} else {
					whitetime = whiteleft;
					allchess[lastx][lasty] = 0;
					message = "�ֵ��ڷ�";
					isblack = true;
					this.repaint();
					th.resume();
					canregre = false;
					}
				}
			} else {
				JOptionPane.showMessageDialog(this, "�������¿�ʼ��Ϸ");
			}
			} else {
				JOptionPane.showMessageDialog(this, "ֻ�ܻ�һ����");
			}
		}
		// �˳� 
		if (arg0.getX() >= 400 && arg0.getX() <= 470 && arg0.getY() >= 370
				&& arg0.getY() <= 400) {
			JOptionPane.showMessageDialog(this, "��Ϸ����");
			System.exit(0);
		}
	}


	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub

	}
	
	private boolean checkwin() {
		boolean flag = false;
		int count = 1;
		int color = allchess[x][y];
		//�᷽���ж�
		int i = x;
		while (i != 18 && color == allchess[i+1][y]) {
			i++;
			count++;
		}
		i = x;
		while (i != 0 && color == allchess[i-1][y]) {
			i--;
			count++;
		}
		if (count >= 5) {
			flag = true;
		}
		//����
		int count2 = 1;
		int j = y;
		while (j != 18 && color == allchess[x][j+1]) {
			j++;
			count2++;
		}
		j = y;
		while (j != 0 && color == allchess[x][j-1]) {
			j--;
			count2++;
		}
		if (count2 >= 5) {
			flag = true;
		}
		//��������
		int count3 = 1;
		i = x;
		j = y;
		while (i != 18 && y != 0 && color == allchess[i+1][j-1]) {
			i++;
			j--;
			count3++;
		}
		i = x;
		j = y;
		while (i != 0 && y != 18 && color == allchess[i-1][j+1]) {
			i--;
			j++;
			count3++;
		}
		if (count3 >= 5) {
			flag = true;
		}
		//��������
		int count4 = 1;
		i = x;
		j = y;
		while (i != 0 && y != 0 && color == allchess[i-1][j-1]) {
			i--;
			j++;
			count4++;
		}
		i = x;
		j = y;
		while (i != 18 && y != 18 && color == allchess[i+1][j+1]) {
			i++;
			j++;
			count4++;
		}
		if (count4 >= 5) {
			flag = true;
		}
		return flag;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		if (maxtime > 0) {
			while (true) {
				if (isblack) {
					blacktime--;
					if (blacktime == 0) {
						JOptionPane.showMessageDialog(this, "�ڷ���ʱ,��Ϸ����!");
					}
				} else {
					whitetime--;
					if (whitetime == 0) {
						JOptionPane.showMessageDialog(this, "�׷���ʱ,��Ϸ����!");
					}
				}
				blackmessage = blacktime / 3600 + ":"
						+ (blacktime / 60 - blacktime / 3600 * 60) + ":"
						+ (blacktime - blacktime / 60 * 60);
				whitemessage = whitetime / 3600 + ":"
						+ (whitetime / 60 - whitetime / 3600 * 60) + ":"
						+ (whitetime - whitetime / 60 * 60);
				this.repaint();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}


