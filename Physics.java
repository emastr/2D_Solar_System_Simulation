import java.lang.Math;

public class Physics{
    // private static double G = 6.67408E-11;

    // Using Earth mass units, earth distance, earth years.
    // G enhet R^3/MT^2 -> G' = MU*T^2/AU^2 G

    // CONSTANTS ______________________________

    //Earth mass unit
    public static double ME = 5.97219E24;
    //Sun mass unit
    public static double MS = 1.99E30;
    //Mass unit
    public static double MU = MS;
    //Earth year
    public static double T = 31556926;
    //Astronomical Unit (distance from earth to sun)
    public static double AU = 1.495978E11;
    //G constant in transformed units.
    // public static double G = 6.67408E-11;
    public static double G = T*T*MU/AU/AU/AU*6.67408E-11;


    //Softness constant to avoid singularity
    private static double e = 0;
    //FUNCTIONS________________________________

    //Potential from m in point r
    public static double potential(double m, double[] r0, double[] r){
        return potential(m, VecMath.dist(r0,r));
    }

    //Potential distance r from a point mass m
    public static double potential(double m, double r){
        return -G*m/(r+e);
    }

    //Force on m1 from m2
    public static double[] force(double m1, double m2, double[] r1, double[] r2){
        double[] rVector = VecMath.subtract(r2, r1);
        return force(m1,m2,rVector);
    }

    //Force on m1 from m2 if r2 is zero
    public static double[] force(double m1, double m2, double[] rVector){
        double r = VecMath.norm(rVector)+e;
        return VecMath.scale(G*m1*m2/r/r/r, rVector);
    }

    //Potential energy
    public static double potEnergy(double m1, double m2, double[] r1, double[] r2){
        return potEnergy(m1,m2,VecMath.dist(r1,r2));
    }

    //Potential energy
    public static double potEnergy(double m1, double m2, double r){
        return -G*m1*m2/(r+e);
    }

    //Kinetic energy
    public static double kinEnergy(double m, double[] vel){
        double v = VecMath.norm(vel);
        return m*v*v/2;
    }

    //Momentum
    public static double[] momentum(double m, double[] vel){
        return VecMath.getScaled(m, vel);
    }

    //Velocity from radius, mass
    public static double centrVel(double m, double[] r){
        return Math.sqrt(G*m/(VecMath.norm(r)+e));
    }
    
    //Centripetal velocity from radius, mass
    public static double centrVel(double m, double r){
        return Math.sqrt(G*m/(r+e));
    }

    //Centripetal velocity from radii, masses
    public static double[] centrVels(double[] ms, double[] rs){
        int size = ms.length;
        double[] vels = new double[size];
        for (int i=0; i<size; i++){
            vels[i] = centrVel(ms[i], rs[i]);
        }
        return vels;
    }

    //Escape velocity from mass m at point r1 with respect to r2.
    public static double escapeVel(double m, double[] r1, double[] r2){
        return escapeVel(m, VecMath.dist(r1,r2));
    }

    //Escape velocity from mass m at distance r.
    public static double escapeVel(double m, double r){
        return Math.sqrt(2*G*m/r);
    }

    public static double[] scaleSIMasses(double[] masses){
        return VecMath.getScaled(1/MU, masses);
    }

    public static double[] scaleSIDists(double[] dists){
        return VecMath.getScaled(1/AU, dists);
    }

    public static String sciNotation(double value){
        double pow = Math.floor(Math.log10(Math.abs(value)));
        double sci = (double)Math.round(value/Math.pow(10.0,pow)*100)/100;
        if (pow < -100){
            return "0";
        }
        return String.valueOf(sci) + "E" + String.valueOf((int)pow);
    }
}