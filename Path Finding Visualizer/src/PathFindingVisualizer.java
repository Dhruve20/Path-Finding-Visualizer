import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;


public class PathFindingVisualizer {

	JFrame frame;
	int width = 850;
	int height = 700;
	int cells = 15;
	int s = 600;
	int csize = s/cells;
	Border border= BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	Node[][] map;
	int startx = -1;
	int starty = -1;
	int finishx = -1;
	int finishy = -1;
	Map canvas;
	int checks = 0;
	int length = 0;
	int tool=0;
	boolean flag = false;
	JLabel Title = new JLabel("Algorithm :  Dijkstra ");
	JLabel toolLabel = new JLabel("Tools");
	JLabel l = new JLabel("Length   : "+length);
	JLabel ch = new JLabel("Checks  : "+checks);
	JLabel note = new JLabel("<html>Note: Movement is in only <br>up,down,right and left<br> directions</html>");
	JPanel panel = new JPanel();
	JButton searchButton=new JButton("Search");
	JButton clearButton=new JButton("Clear");
	JComboBox toolBx = new JComboBox(new String [] {"Start","Finish","Wall","Erase"});

	public static void main(String[] args) {
		new PathFindingVisualizer();
	
	}
	PathFindingVisualizer() {	
		init();
	}
	public void init() {
		clearMap();
		frame = new JFrame();
		frame.setSize(width,height);
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setTitle("Path Finding Visualizer");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(null);
		canvas = new Map();
		canvas.setBounds(230, 30, s+1, s+1);
		frame.getContentPane().add(canvas);
		panel.setBorder(BorderFactory.createTitledBorder(border, "Path Finding Visualizer"));
		panel.setBackground(Color.LIGHT_GRAY);
		JPanel subPanel = new JPanel();
		subPanel.setBorder(border);
		subPanel.add(Title);
		panel.add(subPanel);
		panel.add(searchButton);
		subPanel.setBounds(10, 20, 180, 40);
		subPanel.add(Title);
		Title.setBounds(10,10,150,20);
		
		panel.setLayout(null);
		panel.setBounds(10,30,210,600);
		frame.getContentPane().add(panel);
		panel.add(searchButton);
		searchButton.setBounds(50, 80, 100, 40);
		panel.add(clearButton);
		clearButton.setBounds(50, 130, 100, 40);
		panel.add(toolLabel);
		toolLabel.setBounds(30, 210, 100, 20);
		toolBx.setBounds(30, 240, 100, 30);
		panel.add(toolBx);
		panel.revalidate();
		panel.repaint();
		JPanel subPane2 = new JPanel();
		subPane2.setBorder(border);
		subPane2.add(note);
		
		subPane2.setBounds(10, 350, 190, 70);
		note.setBounds(50,390,150,50);
		panel.add(subPane2);
		
		l.setBounds(30, 480, 180, 30);
		panel.add(l);
		ch.setBounds(30,520, 180, 30);
		panel.add(ch);
		toolBx.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				tool = toolBx.getSelectedIndex();
			}
		});
		clearButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				clearMap();
				canvas.repaint();
				
			}
			
		});
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				length = 0;
				checks = 0;
				if((startx > -1 && starty > -1) && (finishx > -1 && finishy > -1)) {
					flag = true;
					Dijkstra();
				}
					
			}
			});
		
	}
	public void Dijkstra() {
		ArrayList<Node> priority = new ArrayList<Node>();	
		priority.add(map[startx][starty]);	
		while(flag) {
			if(priority.size() < 1) {	
				flag = false;
				break;
			}
			int hops = priority.get(0).getHops()+1;	
			ArrayList<Node> explored = exploreNeighbors(priority.get(0), hops);
			if(explored.size() > 0) {
				priority.remove(0);	
				priority.addAll(explored);	
				l.setText("Length   : "+length);
				ch.setText("Checks  : "+checks);
				canvas.repaint();
			
			} else {	
				priority.remove(0);
			}
				
		}
	}

	public ArrayList<Node> exploreNeighbors(Node current, int hops) {	
		ArrayList<Node> explored = new ArrayList<Node>();	
		for(int a = -1; a <= 1; a++) {
			for(int b = -1; b <= 1; b++) {
				if(Math.abs(a)!=Math.abs(b)) {
					int xbound = current.getX()+a;
					int ybound = current.getY()+b;
				
				if((xbound > -1 && xbound < cells) && (ybound > -1 && ybound < cells)) {
					Node neighbor = map[xbound][ybound];
					if((neighbor.getHops()==-1 || neighbor.getHops() > hops) && neighbor.getcType()!=2) {	
						explore(neighbor, current.getX(), current.getY(), hops);
						explored.add(neighbor);	
					}
				}
			}
			}
		}
		return explored;
	}
	
	public void explore(Node current, int lastx, int lasty, int hops) {	
		if(current.getcType()!=0 && current.getcType() != 1)
			current.setcType(4);
		current.setLNode(lastx, lasty);	
		current.setHops(hops);	
		checks++;
		if(current.getcType() == 1) {	
			backtrack(current.getLx(), current.getLy(),hops);
		}
	}
	public void backtrack(int lx, int ly, int hops) {	
		length = hops;
		while(hops > 1) {	
			Node current = map[lx][ly];
			current.setcType(5);
			lx = current.getLx();
			ly = current.getLy();
			hops--;
		}
		flag = false;
	}
	
	public void clearMap() {	
		finishx = -1;	
		finishy = -1;
		startx = -1;
		starty = -1;
		map = new Node[cells][cells];
		for(int i = 0; i < cells; i++) {
			for(int j = 0; j < cells; j++) {
				map[i][j] = new Node(3,i,j);	
			}
		}
		length = 0;
		checks = 0;
		l.setText("Length   : "+length);
		ch.setText("Checks  : "+checks);
	}

	
	class Map extends JPanel implements MouseListener, MouseMotionListener{

		Map() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			boolean cStart = false;
			boolean cFinish = false;
			for(int i = 0; i < cells; i++) {
				for(int j=0;j<cells;j++) {
					switch(map[i][j].getcType()) {
					case 0:
						g.setColor(Color.MAGENTA);
						cStart =true;
						break;
					case 1:
						g.setColor(Color.RED);
						cFinish = true;
						break;
					case 2:
						g.setColor(Color.DARK_GRAY);
						break;
					case 3:
						g.setColor(Color.WHITE);
						break;
					case 4:
						g.setColor(Color.YELLOW);
						break;
					case 5:
						g.setColor(Color.GREEN);
						break;
				}
					g.fillRect(i*csize,j*csize,csize,csize);
					g.setColor(Color.BLACK);
					g.drawRect(i*csize,j*csize,csize,csize);
					if(cStart) {
						cStart =false;
						g.drawString(String.valueOf('S'), i*csize+15,j*csize + csize/2+5);
					}
					if(cFinish) {
						cFinish =false;
						g.drawString(String.valueOf('F'), i*csize+15,j*csize + csize/2+5);
					}
				}
			}
			
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {
			// TODO Auto-generated method stub
			int x = e.getX()/csize;
			int y = e.getY()/csize;
			Node current = map[x][y];
			if((tool == 2 || tool == 3) && (current.getcType() != 0 && current.getcType() != 1)) {
				current.setcType(tool);
			}
			canvas.repaint();
				
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub
				
			try {
				int i = e.getX()/csize;	
				int j = e.getY()/csize;
				Node current = map[i][j];
				switch(tool ) {
					case 0: {	
						if(current.getcType()!=2) {
							if(startx > -1 && starty > -1) {	
								map[startx][starty].setcType(3);
								map[startx][starty].setHops(-1);
							}
							current.setHops(0);
							startx = i;	
							starty = j;
							current.setcType(0);	
						}
						break;
					}
					case 1: {
						if(current.getcType()!=2) {
							if(finishx > -1 && finishy > -1)
								map[finishx][finishy].setcType(3);
							finishx = i;	
							finishy = j;
							current.setcType(1);	
						}
						break;
					}
					default:
						if(current.getcType() != 0 && current.getcType() != 1)
							current.setcType(tool);
						break;
				}
				canvas.repaint();
			} catch(Exception E) {}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	class Node{
		int cType;
		int x;
		int y;
		int hops;
		int lx;
		int ly;
		double dist;
		Node(int cType,int x,int y){
			this.cType = cType;
			this.x = x;
			this.y = y;
			hops = -1;
		}
		public int getcType() {
			return cType;
		}
		public void setcType(int cType) {
			this.cType = cType;
		}
		public int getX() {
			return x;
		}
	
		public int getY() {
			return y;
		}
		
		public int getHops() {
			return hops;
		}
		public void setHops(int hops) {
			this.hops = hops;
		}
		public int getLx() {
			return lx;
		}
	
		public int getLy() {
			return ly;
		}
		public void setLNode(int x, int y) {
			this.lx = x;
			this.ly=y;
		}
	
	}
	
}
