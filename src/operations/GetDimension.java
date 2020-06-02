package operations;

import important.Important;
import java.awt.*;

public class GetDimension {

    public GraphicsEnvironment graphicsEnvironment;
    public static Rectangle window;
    public static Point center;

    public GetDimension() {
        graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment();
        window = graphicsEnvironment.getMaximumWindowBounds();
        center = graphicsEnvironment.getCenterPoint();
    }

    public GraphicsEnvironment getGraphicsEnvironment() {
        return graphicsEnvironment;
    }

    public static Rectangle getWindow() {
        return window;
    }

    public static Point getCenter() {
        return center;
    }

    public void setWindow(Rectangle window) {
        this.window = window;
    }

    public void setCenter(Point center) {
        GetDimension.center = center;
    }

    public static double getDynamicWidth(double dWidth) {
        double x = 1920 / dWidth;

        return Important.getWidth() / x;
    }

    public static double getDynamicHeight(double dHeight) {
        double y = 1080 / dHeight;

        return Important.getHeight() / y;
    }
}
