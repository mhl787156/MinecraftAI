package edu.uwa.aidan.robot.world;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class ShapeHelper {
	public static Line2D getLine(Line2D line, double pixelRatio) {
		return new Line2D.Double(getPoint(line.getP1(), pixelRatio), getPoint(
				line.getP2(), pixelRatio));
	}

	public static Point2D getPoint(Point2D point, double pixelRatio) {
		return new Point2D.Double(point.getX() * pixelRatio, point.getY()
				* pixelRatio);
	}

	public static Ellipse2D getEllipse(Ellipse2D shape, double pixelRatio) {
		double x = shape.getX() * pixelRatio;
		double y = shape.getY() * pixelRatio;

		double w = shape.getWidth() * pixelRatio;
		double h = shape.getHeight() * pixelRatio;

		return new Ellipse2D.Double(x, y, w, h);
	}

	public static Arc2D convertArc(Arc2D shape, double pixelRatio) {
		double x = shape.getX() * pixelRatio;
		double y = shape.getY() * pixelRatio;

		double w = shape.getWidth() * pixelRatio;
		double h = shape.getHeight() * pixelRatio;

		return new Arc2D.Double(x, y, w, h, shape.getAngleStart(), shape
				.getAngleExtent(), Arc2D.PIE);
	}

	public static Shape getRectangle(Rectangle2D rect, double pixelRatio) {
		double x = rect.getX() * pixelRatio;
		double y = rect.getY() * pixelRatio;

		double w = rect.getWidth() * pixelRatio;
		double h = rect.getHeight() * pixelRatio;

		return new Rectangle2D.Double(x, y, w, h);
	}
}
