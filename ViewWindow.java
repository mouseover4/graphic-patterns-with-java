import javax.swing.*;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.*;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import uwcse.graphics.*;
import java.awt.Point;

/**
 * Construct a window to display the graphics elements received from
 * GraphicsElements. 
 */

public class ViewWindow extends JPanel implements ActionListener {

	/** Width of the inner panel of this ViewWindow */
	public static final int WINDOW_WIDTH = 400;

	/** Height of the inner panel of this ViewWindow */
	public static final int WINDOW_HEIGHT = 300;

	/** What is currently painted */
	public static final int PIE = 0;

	public static final int STRIPES = 1;

	public static final int SNOW_FLAKE = 2;

	private int which;

	// The frame this ViewWindow is in
	private JFrame frame;

	// Other elements in the window
	// Radio buttons (with their title)
	private String[] titles;

	private JRadioButton[] radioButtons;

	// The button to rotate the colors on a GraphicsElements
	private JButton rotateColors;

	// The list of the graphics elements to display
	private ArrayList graphicsList;

	// The object that generates the graphics elements to display
	private GraphicsElements graphicsElements = new GraphicsElements();

	// The color of the snow flake
	private Color snowFlakeColor;

	/** Construct a ViewWindow */
	public ViewWindow() {
		// Use a windows look and feel (if available)
		try {
			UIManager.LookAndFeelInfo[] lfinfo = UIManager
					.getInstalledLookAndFeels();
			UIManager.setLookAndFeel(lfinfo[3].getClassName());
		} catch (Exception e) {/* ignore any problem */
		}

		// The components making up the window
		// Radio buttons
		this.titles = new String[] { "Pie", "Stripes", "Snow flake" };
		this.radioButtons = new JRadioButton[this.titles.length];
		// Only one radio button can be selected at a time
		ButtonGroup buttonGroup = new ButtonGroup();
		for (int i = 0; i < this.radioButtons.length; i++) {
			this.radioButtons[i] = new JRadioButton(this.titles[i]);
			buttonGroup.add(this.radioButtons[i]);
			this.radioButtons[i].addActionListener(this);
		}

		// Button to rotate the colors
		this.rotateColors = new JButton("Change colors");
		this.rotateColors.addActionListener(this);

		// Place the components in this WindowView
		JPanel contentPane = new JPanel();
		contentPane.setLayout(new BorderLayout());
		// at the bottom (SOUTH)
		JPanel southPanel = new JPanel(new GridLayout(2, 1));
		JPanel southPanelFirstRow = new JPanel();
		for (int i = 0; i < this.radioButtons.length; i++)
			southPanelFirstRow.add(this.radioButtons[i]);
		southPanel.add(southPanelFirstRow);
		JPanel southPanelSecondRow = new JPanel();
		southPanelSecondRow.add(this.rotateColors);
		southPanel.add(southPanelSecondRow);
		contentPane.add(southPanel, BorderLayout.SOUTH);
		// Background color of this WindowView
		this.setBackground(Color.white);
		contentPane.add(this, BorderLayout.CENTER);
		// Put everything in a frame
		this.frame = new JFrame("Doing graphics with loops");
		// With JBuilder, replace DISPOSE_ON_CLOSE with EXIT_ON_CLOSE
		this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.frame.setContentPane(contentPane);
		// Show it
		this.frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
		this.frame.setLocation(100, 100);
		this.frame.setVisible(true);
		// Resize it with the actual size
		Insets insets = this.frame.getInsets();
		int width = WINDOW_WIDTH + insets.left + insets.right;
		int height = WINDOW_HEIGHT + insets.top + insets.bottom
				+ (int) (southPanel.getPreferredSize().getHeight());
		this.frame.setSize(width, height);
		this.frame.setResizable(false);
		this.frame.setVisible(true);
	}

	/** Handle the button clicks */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.radioButtons[0]) {
			// Pie
			this.graphicsList = this.graphicsElements.createAPie();
			this.which = ViewWindow.PIE;
		} else if (e.getSource() == this.radioButtons[1]) {
			// Stripes
			this.graphicsList = this.graphicsElements.createStripes();
			this.which = ViewWindow.STRIPES;
		} else if (e.getSource() == this.radioButtons[2]) {
			// Create a snow flake
			this.graphicsList = this.graphicsElements.createASnowFlake();
			this.which = ViewWindow.SNOW_FLAKE;
		} else if (e.getSource() == this.rotateColors) {
			// Don't do anything if there is no display
			if (this.graphicsList == null)
				return;

			// Change the colors of the display
			switch (this.which) {
			case ViewWindow.PIE:
				this.graphicsList = this.graphicsElements
						.rotateColorsInPie(this.graphicsList);
				break;
			case ViewWindow.STRIPES:
				this.graphicsList = this.graphicsElements
						.flipColorsInStripes(this.graphicsList);
				break;
			case ViewWindow.SNOW_FLAKE:
				this.snowFlakeColor = this.graphicsElements
						.changeColorOfSnowFlake();
				break;
			}
		} else
			// unknown source
			return;

		// display the new drawing
		this.repaint();
	}

	/** Display this WindowView */
	public void paintComponent(Graphics gfx) {
		super.paintComponent(gfx);
		// If there is nothing to display, stop here
		if (this.graphicsList == null)
			return;

		// Use some graphics2D features (smooth edges)
		Graphics2D g = (Graphics2D) gfx;
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		// Display the different graphics elements
		if (this.which == ViewWindow.PIE) {
			// Pie
			Iterator it = this.graphicsList.iterator();
			while (it.hasNext()) {
				Arc arc = (Arc) (it.next());
				g.setColor(arc.getColor());
				// The only way I can access startAngle and arcAngle!
				StringTokenizer st = new StringTokenizer(arc.toString(), "=,");
				int startAngle = 0;
				int arcAngle = 0;

				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					if (s.equals(" startAngle"))
						startAngle = Integer.parseInt(st.nextToken());
					if (s.equals(" arcAngle"))
						arcAngle = Integer.parseInt(st.nextToken());
				}

				g.fillArc(arc.getX(), arc.getY(), arc.getWidth(), arc
						.getHeight(), startAngle, arcAngle);
			}
		} else if (this.which == ViewWindow.STRIPES) {
			// Stripes
			Iterator it = this.graphicsList.iterator();
			while (it.hasNext()) {
				Triangle t = (Triangle) (it.next());
				g.setColor(t.getColor());
				// The only way I can access the coordinates of the triangle!
				// Expensive.
				StringTokenizer st = new StringTokenizer(t.toString(), "=, )(");
				int[] xpt = new int[3];
				int[] ypt = new int[3];
				int index = 0;

				while (st.hasMoreTokens()) {
					String s = st.nextToken();
					if (s.startsWith("x")) {
						xpt[index] = (int) Double.parseDouble(st.nextToken());
					}
					if (s.startsWith("y")) {
						ypt[index++] = (int) Double.parseDouble(st.nextToken());
					}
				}
				g.fillPolygon(xpt, ypt, xpt.length);
			}
		} else if (this.which == ViewWindow.SNOW_FLAKE) {
			if (graphicsList.size() == 0)
				return;
			// Koch's snow flake
			if (this.snowFlakeColor != null)
				g.setColor(snowFlakeColor);
			else
				g.setColor(Color.black);
			Point p = (Point) graphicsList.get(0);
			for (int i = 0; i < graphicsList.size(); i++) {
				Point q = (Point) graphicsList.get(i);
				g.drawLine(p.x, p.y, q.x, q.y);
				p = q;
			}
			// Connect the last point with the first point
			Point q = (Point) graphicsList.get(0);
			g.drawLine(p.x, p.y, q.x, q.y);
		}
	}

	/** Start the application */
	public static void main(String[] args) {
		ViewWindow viewWindow = new ViewWindow();
	}
}
