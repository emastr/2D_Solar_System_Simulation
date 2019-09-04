import java.lang.Math;

//Vector math
public class VecMath{
    public static void empty(double[] vector){
        for (int i = 0; i<vector.length;i++){
            vector[i] = 0;
        }
    }

    public static double norm(double[] vector){
        return Math.sqrt(dot(vector, vector));
    }

    //Does not return new object!
    public static double[] scale(double scalar, double[] vector){
        for (int i = 0; i<vector.length; i++){
            vector[i] *= scalar;
        }
        return vector;
    }

    public static double[] getScaled(double scalar, double[] vector){
        double[] scaled = scale(scalar, vector.clone());
        return scaled;
    }

    public static double[] add(double[] vector1, double[] vector2){
        int length = vector1.length;
        if (length!=vector2.length){
            throw new IllegalArgumentException("Arrays must have the same size");
        }
        double[] vectorSum = new double[length];
        for (int i = 0; i<length; i++){
            vectorSum[i] = vector1[i] + vector2[i];
        }
        return vectorSum;
    }

    public static double[] subtract(double[] vector1, double[] vector2){
        double[] negVector2 = getScaled(-1.0, vector2);
        return add(vector1, negVector2);
    }

    public static double dot(double[] vector1, double[] vector2){
        int length = vector1.length;
        if (length!=vector2.length){
            throw new IllegalArgumentException("Arrays must have the same size");
        }
        double dotProduct = 0;
        for (int i = 0; i<length; i++){
            dotProduct += vector1[i]*vector2[i];
        }
        return dotProduct;
    }

    public static double[] cross(double[] vector1, double[] vector2){ 
        if ((vector1.length!=3) || (vector2.length!=3)){
            throw new IllegalArgumentException("Arrays must have size 3");
        }
        double[] crossProduct = new double[3];
        crossProduct[0] = vector1[1]*vector2[2] - vector1[2]*vector2[1];
        crossProduct[1] = vector1[2]*vector2[0] - vector1[0]*vector2[2];
        crossProduct[2] = vector1[0]*vector2[1] - vector1[1]*vector2[0];
        return crossProduct;
    }

    public static double dist(double[] vector1, double[] vector2){
        return norm(subtract(vector1, vector2));
    }

    //Rotate vector
    public static double[] rotateClockwise(double[] vector, double radians){
        vector[0] = Math.cos(radians)*vector[0] - Math.sin(radians)*vector[1];
        vector[1] = Math.sin(radians)*vector[0] + Math.cos(radians)*vector[1];
        return vector;
    }

    //Create a vector with magnitude and angle from positive x axis
    public static double[] getVector(double magnitude, double angle){
        double[] vector = {magnitude*Math.cos(angle), magnitude*Math.sin(angle)};
        return vector;
    }

    public static double[][] transpose(double[][] vec){
        int I = vec.length;
        int J = vec[0].length;
        double[][] vecT = new double[J][I];
        for (int i = 0; i <I; i++){
            for (int j = 0; j < J; j++){
                vecT[j][i] = vec[i][j];
            }
        }
        return vecT;
    }

    public static double shortestDistance(double[][] r1, double[][] r2){
        //r1 and r2 of the type r[time][coord] or [vector1, vector2, ...]
        int size = r1.length;
        int index = 0;
        double[] dists = new double[size];
        for (int i = 0; i<size; i++){
            dists[i] = dist(r1[i],r2[i]);
        }
        return min(dists);
    }

    public static double min(double[] vec){
        return -max(getScaled(-1,vec));
    }

    public static double max(double[] vec){
        double val = vec[0];
        for (int i = 1; i<vec.length; i++){
            double newVal = vec[i];
            if (val < newVal){
                val = newVal;
            }
        }
        return val;
    }

    public static int shortestDistanceIndex(double[][] r1, double[][] r2){
        //r1 and r2 of the type r[time][coord] or [vector1, vector2, ...]
        int size = r1.length;
        int index = 0;
        double dist = dist(r1[0],r2[0]);
        for (int i = 1; i<size; i++){
            double newDist = dist(r1[i], r2[i]);
            if (dist > newDist){
                dist = newDist;
                index = i;
            }
        }
        return index;
    }

    public static double[] logSpace(double min, double max, int points){
        double[] dts = new double[points];
        double lgMax = Math.log10(max);
        double lgMin = Math.log10(min);
        for (int i = 0; i<points; i++){
            // lgmin ........  lgMax
            dts[i] = Math.pow(10,lgMin + (lgMax-lgMin)*i/(points));
        }
        return dts;
    }
}