package com.example.helloworld;

public class VectorUtils {
    public static float calculateDistance(Point p1, Point p2){
        Point diff = new Point(p2.getX() - p1.getX(), p2.getY() - p1.getY());

        if ((Float.compare(0f, diff.getX())== 0) && (Float.compare(0f, diff.getY()) == 0))
            return 0f;
        else {
            return (float) Math.sqrt(diff.getX() * diff.getX() + diff.getY() * diff.getY());
        }
    }

    public static Point getUnitVector(Point p){
        float length = (float) Math.sqrt(p.getX() * p.getX() + p.getY() * p.getY());
        Point unitVector;

        if (!(Float.compare(length, 0f) == 0)){
            unitVector = new Point(p.getX() / length, p.getY() / length);
        } else {
            unitVector = new Point(0f, 0f);
        }

        return unitVector;
    }
}
