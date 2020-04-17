import org.ejml.simple.SimpleMatrix;

public class Calculator {
    public static Point[] map3Dto2d(Point3d[] cube, int screenWidth, int screenHeight, int cameraViewAngle) {
        Point[] points2d = new Point[cube.length];
        int i = 0;
        int focalLength = (int) ((screenWidth / 2) / Math.tan(Math.toRadians((double) cameraViewAngle / 2)));

        for (Point3d p : cube) {
            double x = p.x * focalLength / p.z;
            double y = p.y * focalLength / p.z;

            x += screenWidth / 2;
            y -= screenHeight / 2;
            y = -y;
            points2d[i++] = new Point(x, y);
        }
        return points2d;
    }

    public static Point3d[] translateVertices(Point3d[] cube, int xTrans, int yTrans, int zTrans) {
        int i = 0;
        for (Point3d p : cube) {
            Point3d translatedVertex = translateVertex(p, xTrans, yTrans, zTrans);
            cube[i++] = translatedVertex;
        }
        return cube;
    }

    public static Point3d translateVertex(Point3d vertex, int xTrans, int yTrans, int zTrans) {
        SimpleMatrix secMatrix = new SimpleMatrix(
                new double[][]{
                        new double[]{1, 0, 0, 0},
                        new double[]{0, 1, 0, 0},
                        new double[]{0, 0, 1, 0},
                        new double[]{xTrans, yTrans, zTrans, 1},
                }
        );
        SimpleMatrix firstMatrix = new SimpleMatrix(
                new double[][]{
                        new double[]{vertex.x, vertex.y, vertex.z, 1}
                }
        );
        SimpleMatrix result = firstMatrix.mult(secMatrix);
        return new Point3d(result.get(0), result.get(1), result.get(2));
    }

    public static Point3d[] rotateVertices(Point3d[] vertices, int angle, char axis) {
        int i = 0;
        for (Point3d p : vertices) {
            Point3d translatedVertex = rotateVertex(p, angle, axis);
            vertices[i++] = translatedVertex;
        }
        return vertices;
    }

    public static Point3d rotateVertex(Point3d vertex, int angle, char axis) {
        double radAngle = Math.toRadians(angle);
        SimpleMatrix secMatrix = SimpleMatrix.diag(1, 1, 1, 1);
        SimpleMatrix rot = new SimpleMatrix(
                new double[][]{
                        new double[]{Math.cos(radAngle), -Math.sin(radAngle)},
                        new double[]{Math.sin(radAngle), Math.cos(radAngle)}
                }
        );
        if (axis == 'x') {
            secMatrix.insertIntoThis(1, 1, rot);
        } else if (axis == 'y') {
            secMatrix.set(0, 0, Math.cos(radAngle));
            secMatrix.set(0, 2, Math.sin(radAngle));
            secMatrix.set(2, 0, -Math.sin(radAngle));
            secMatrix.set(2, 2, Math.cos(radAngle));
        } else {
            secMatrix.insertIntoThis(0, 0, rot);
        }
        SimpleMatrix firstMatrix = new SimpleMatrix(
                new double[][]{
                        new double[]{vertex.x, vertex.y, vertex.z, 1}
                }
        );
        SimpleMatrix result = firstMatrix.mult(secMatrix);
        return new Point3d(result.get(0), result.get(1), result.get(2));
    }

}
