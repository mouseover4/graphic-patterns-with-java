import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import uwcse.graphics.Arc;
import uwcse.graphics.Triangle;
import uwcse.io.Input;

//import acm.graphics.GObject;
//import acm.graphics.GOval;
//import acm.graphics.GRect;

/**
 * A class to create and manipulate graphics elements stored in an ArrayList
 * @author Helen Lawrence
 */

public class GraphicsElements {

	/** Maximum number of wedges in a pie */
	public static final int MAXIMUM_NUMBER_OF_PIE_WEDGES = 100;

	/** Maximum number of stripes of one color in the pattern of stripes */
	public static final int MAXIMUM_NUMBER_OF_STRIPES = 15;

	/** Maximum number of divisions in a Koch snow flake */
	public static final int MAXIMUM_NUMBER_OF_DIVISIONS = 5;

	/** Instance variables for the pie wedges */
	int userNum; // Number of wedges entered by the user
	Arc wedge;
	Arc firstWedge;
	int x = 50;
	int y = 0;
	int start_angle = 0;
	int arc_angle = 0;
	int diameter = ViewWindow.WINDOW_HEIGHT;
	int remainder = 0;
	private Color color;
	private ArrayList<Color> colorList;

	/** Instance variables for the stripes */
	int NRows;
	int NColumns;
	int triangle_height;
	int tx = 0;// starting x coordinate for the triangle
	int ty = 0;// starting y coordinate for the triangle

	/** Instance variables for the snowflake */
	int order;
	int x1 = 70;
	int y1 = 240;
	int x2 = 260 + x1;
	int y2 = 240;
	int x3;
	int y3;

	// The window is 400 pixels wide and 300 pixels high

	/**
	 * Creates a pie using filled arcs. The color of each arc is
	 * random. 
	 */
	public ArrayList<Arc> createAPie() {
		Input in = new Input();

		do {
			userNum = in.readIntDialog("Enter a number " + "between 1 and "
					+ MAXIMUM_NUMBER_OF_PIE_WEDGES + " ");
			if (userNum < 1 || userNum > MAXIMUM_NUMBER_OF_PIE_WEDGES) {
				JOptionPane.showMessageDialog(null,
						"Please enter a vaild number", "Input error",
						JOptionPane.WARNING_MESSAGE);
			}
		} while (userNum < 1 || userNum > MAXIMUM_NUMBER_OF_PIE_WEDGES);

		ArrayList<Arc> graphicsList = new ArrayList<Arc>();
		colorList = new ArrayList<Color>();

		// Set the angle for the arc
		arc_angle = 360 / userNum; // angle in degrees of each pie wedge
		remainder = 360 % userNum;

		for (int i = 0; i < userNum; i++) {
			if (i < (userNum - remainder))// number of wedges to draw at the
			// usual angle (360/userNum)
			{
				setColor();
				wedge = new Arc(x, y, diameter, diameter, start_angle,
						arc_angle, color, true);
				graphicsList.add(wedge);
				colorList.add(color);
				start_angle += arc_angle;
			} else // distribute the extra degrees over the rest of the wedges
			{
				setColor();
				wedge = new Arc(x, y, diameter, diameter, start_angle,
						arc_angle + 1, color, true);
				graphicsList.add(wedge);
				colorList.add(color);
				start_angle += arc_angle + 1;
			}
		}

		return graphicsList;
	}

	private void setColor() {
		int r = (int) (Math.random() * 256);
		int g = (int) (Math.random() * 256);
		int b = (int) (Math.random() * 256);
		color = new Color(r, g, b);
	}

	/**
	 * Creates a pattern of diagonal stripes.
	 */
	public ArrayList<Triangle> createStripes() {
		Input in = new Input();

		do {
			NRows = in.readIntDialog("Enter a number " + "between 1 and "
					+ MAXIMUM_NUMBER_OF_STRIPES + " ");
			if (NRows < 1 || NRows > MAXIMUM_NUMBER_OF_STRIPES) {
				JOptionPane.showMessageDialog(null,
						"Please enter a vaild number", "Input error",
						JOptionPane.WARNING_MESSAGE);
			}
		} while (NRows < 1 || NRows > MAXIMUM_NUMBER_OF_STRIPES);

		NColumns = NRows; // display will produce an equal number of rows and
		// columns
		triangle_height = ViewWindow.WINDOW_HEIGHT / NRows; // set height of
		// triangle

		ArrayList<Triangle> graphicsList = new ArrayList<Triangle>();

		for (int i = 0; i < NRows; i++) {
			for (int j = 0; j < NColumns; j++)
				if ((i + j) % 2 == 0)// if the row number + the column number is
				// even(0, 2, 4, etc)
				{
					tx = j * triangle_height + 50;
					ty = i * triangle_height;
					Triangle t1 = new Triangle(tx, ty, tx,
							ty + triangle_height, tx + triangle_height, ty
									+ triangle_height, Color.BLUE, true);
					Triangle t2 = new Triangle(tx, ty, tx + triangle_height,
							ty, tx + triangle_height, ty + triangle_height,
							Color.YELLOW, true);
					graphicsList.add(t1);
					graphicsList.add(t2);
				} else // reverse the colors
				{
					tx = j * triangle_height + 50;
					ty = i * triangle_height;
					Triangle t1 = new Triangle(tx, ty, tx,
							ty + triangle_height, tx + triangle_height, ty
									+ triangle_height, Color.YELLOW, true);
					Triangle t2 = new Triangle(tx, ty, tx + triangle_height,
							ty, tx + triangle_height, ty + triangle_height,
							Color.BLUE, true);
					graphicsList.add(t1);
					graphicsList.add(t2);
				}
		}
		return graphicsList;
	}

	/**
	 * Creates a Koch snow flake using the class java.awt.Point. 
	 */
	public ArrayList<Point> createASnowFlake() {
		Input in = new Input();

		do {
			order = in.readIntDialog("Enter a number " + "between 0 and "
					+ MAXIMUM_NUMBER_OF_DIVISIONS + " ");
			if (order < 0 || order > MAXIMUM_NUMBER_OF_DIVISIONS) {
				JOptionPane.showMessageDialog(null,
						"Please enter a vaild number", "Input error",
						JOptionPane.WARNING_MESSAGE);
			}
		} while (order < 0 || order > MAXIMUM_NUMBER_OF_DIVISIONS);

		ArrayList<Point> graphicsList = new ArrayList<Point>();
		// Create initial points of the triangle
		Point p1 = new Point(100, 20);
		Point p2 = new Point(320, 135);
		Point p3 = new Point(100, 250);

		// Add first three points to array list
		graphicsList.add(p1);
		graphicsList.add(p2);
		graphicsList.add(p3);

		// iterate according to order number given by the user
		while (order > 0) {
			int lastIndex = graphicsList.size() - 1;

			for (int i = 0; i <= lastIndex; i += 4) {
				// if (i != lastIndex) {
				Point p = graphicsList.get(i);
				Point q = graphicsList.get((i + 1) % graphicsList.size());

				Point a = new Point((int) (p.x + (q.x - p.x) / 3.0),
						(int) (p.y + (q.y - p.y) / 3.0));
				Point c = new Point((int) (p.x + 2 * (q.x - p.x) / 3.0),
						(int) (p.y + 2 * (q.y - p.y) / 3.0));
				Point b = new Point();
				b.x = (int) (a.x + (c.x - a.x) * Math.cos(Math.PI / 3.0) + (c.y - a.y)
						* Math.sin(Math.PI / 3.0));
				b.y = (int) (a.y - (c.x - a.x) * Math.sin(Math.PI / 3.0) + (c.y - a.y)
						* Math.cos(Math.PI / 3.0));

				graphicsList.add(i + 1, a);
				graphicsList.add(i + 2, b);
				graphicsList.add(i + 3, c);

				lastIndex += 3;
			}
			order--;
		}// end while

		return graphicsList;
	}

	/**
	 * Rotates the colors in the pie, in a clockwise direction. Precondition:
	 * graphicsList describes a pie. Returns the updated ArrayList
	 */
	public ArrayList<Arc> rotateColorsInPie(ArrayList<Arc> list) {
		// Add your code here
		int omega = list.size() - 1; // the number of last item in the array

		for (int i = omega; i >= 0; i--) {
			if (i == omega) {
				Arc a = (Arc) list.get(i);
				a.setColor(colorList.get(0));
			} else {
				Arc a = (Arc) list.get(i);
				Color c = (Color) (colorList.get(i + 1));
				a.setColor(c);
			}
		}

		int i = 0;
		for (Arc a : list) {
			colorList.set(i, a.getColor());
			i++;
		}

		return list;
	}

	/**
	 * Flips the 2 colors of the pattern of stripes Precondition: graphicsList
	 * describes a pattern of stripes. Returns the updated ArrayList
	 */
	public ArrayList<Triangle> flipColorsInStripes(
			ArrayList<Triangle> graphicsList) {
		// Add your code here
		for (int i = 0; i < graphicsList.size(); i++) {
			Triangle t = (Triangle) graphicsList.get(i);
			if (t.getColor() == Color.YELLOW) {
				t.setColor(Color.BLUE);
			} else if (t.getColor() == Color.BLUE) {
				t.setColor(Color.YELLOW);
			}
		}

		return graphicsList;
	}

	/**
	 * Returns the new color of the snow flake using
	 * the Random class (in java.util) to select the new random color. 
	 */
	public Color changeColorOfSnowFlake() {
		setColor();
		return color;
	}
}
