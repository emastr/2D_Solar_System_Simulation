// import java.lang.Math;

//A constallation that contains bodies (point masses)
public class Constallation{
    
    public final int size;
    public final Body[] bodies;

    //Create constallation from scaled units.
    public Constallation(String[] names, double[] masses, double[][] poss, double[][] vels){
        size = names.length;
        bodies = new Body[size];
        for (int i = 0; i<size; i++){
            bodies[i] = new Body(names[i], masses[i], poss[i], vels[i]);
        }
        this.updateForces();
    }

    //Calculate attractive force from body2 on body1.
    public static double[] calcForce(Body body1, Body body2){
        //Vector from body 1 to body 2
        return Physics.force(body1.MASS, body2.MASS, body1.pos, body2.pos);
    }

    //Update the forces on all bodies
    //Automatically calculates acceleration.
    public void updateForces(){
        for (Body body: bodies){
            VecMath.empty(body.force);
        }
        double[] tempForce = new double[2];
        for (int i = 0; i<size; i++){
            for (int j = i+1; j<size; j++){
                tempForce = calcForce(bodies[i], bodies[j]);
                bodies[i].setForce(VecMath.add(bodies[i].force, tempForce));
                bodies[j].setForce(VecMath.subtract(bodies[j].force, tempForce));
            }
        }
    }

    //Calculate total potential energy
    public double calcPotEn(){
        double pot = 0;
        for (int i = 0; i<size; i++){
            for (int j = i+1; j<size; j++){
                pot += Physics.potEnergy(bodies[i].MASS, bodies[j].MASS, bodies[i].pos, bodies[j].pos);
            }
        }
        return pot;
    }

    //Calculate total kinetic energy
    public double calcKinEn(){
        double kin = 0;
        for(Body body: bodies){
            kin += body.getKinEn();
        }
        return kin;
    }

    //Calculate total Energy of system
    public double energyTotal(){
        return calcKinEn() + calcPotEn();
    }

    //Calculate total momentum of system
    public double[] momentumTotal(){
        double[] momentum = new double[2];
        for (Body body: bodies){
            momentum = VecMath.add(momentum, body.getMomentum());
        }
        return momentum;
    }

    public void moveAll(double[][] deltas){
        for (int i = 0; i<size; i++){
            bodies[i].move(deltas[i]);
        }
    }

    public void accelAll(double[][] deltas){
        for (int i = 0; i<size; i++){
            bodies[i].accelerate(deltas[i]);
        }
    }

    //Print planets in a list.
    public String toString(){
        String string = "";
        for (Body body: bodies){
            string += body.toString() + "\n";
        }
        string += "Energy ratio: " + String.valueOf(calcKinEn()/calcPotEn());
        return string;
    }
}
//This is a planet with mass, position, velocity and acceleration
class Body{
    final String name;
    final double MASS;
    double[] pos;
    double[] vel;
    double[] acc;
    double[] force;
    

    public Body(String name, double mass, double[] pos, double[] vel, double[] acc){
        this.name = name;
        this.MASS = mass;
        this.pos = pos;
        this.vel = vel;
        this.acc = acc;
        this.force = VecMath.getScaled(MASS, acc);
    }

    public Body(String name, double mass, double[] pos, double[] vel){
        this(name, mass, pos, vel, new double[2]);
    }

    //Get current kinetic energy
    public double getKinEn(){
        return Physics.kinEnergy(MASS, vel);
    }

    //Get potential from other body
    public double getPotEn(Body body){
        return Physics.potential(body.MASS, body.pos, pos);
    }

    //Get current momentum
    public double[] getMomentum(){
        return Physics.momentum(MASS, vel);
    }

    //Set the position
    public void setPos(double[] pos){
        this.pos = pos;
    }

    //Move along some vector
    public void move(double[] delta){
        this.pos = VecMath.add(this.pos, delta);
    }

    //Set the velocity
    public void setVel(double[] vel){
        this.vel = vel;
    }

    //Increase velocity with some vector
    public void accelerate(double[] delta){
        this.vel = VecMath.add(this.vel, delta);
    }

    //Set the acceleration 
    public void setAcc(double[] acc){
        this.acc = acc;
        this.force = VecMath.getScaled(MASS, acc);
    }

    //Set the force
    public void setForce(double[] force){
        this.force = force;
        acc = VecMath.getScaled(1/MASS, force);
    }

    //Print body.
    public String toString(){
        String[] Spos = new String[2];
        String[] Svel = new String[2];
        for (int i = 0; i<2; i++){
            Spos[i] = Physics.sciNotation(pos[i]);
            Svel[i] = Physics.sciNotation(vel[i]);
        }
        // return "Body " + name + ", " + String.valueOf(MASS) + " earth masses";
        return "Body " + name + ", Mass: " + Physics.sciNotation(MASS) + ", Position: " + pprinter.printStringArray(Spos) + ", Velocity: " + pprinter.printStringArray(Svel);
    }
}
