public class Simulator{
    
    static SimData simulate(int option, Constallation constallation, double dt, int skipSteps, int steps){
        if (steps<skipSteps){
            throw new IllegalArgumentException("Cannot skip more steps than the total amount.");
        }
        SimData data = new SimData(steps-skipSteps, constallation);
        
        // System.out.println(constallation);
        for (int i = 0; i<steps; i++){
            // System.out.println(i);
            step(option, constallation, dt);
            if (i>=skipSteps){
                data.addFrame(constallation, dt);
            }
        }
        return data;
    }

    static SimData simulate(int option, Constallation constallation, double dt, int steps){
        return simulate(option, constallation, dt, 0, steps);
    }

    //Simulate for some duration
    static SimData simulate(int option, Constallation constallation, double dt, double skipDuration, double duration){
        int skipSteps = (int)Math.round(skipDuration/dt);
        int steps = (int)Math.round(duration/dt);
        return simulate(option, constallation, dt, skipSteps, steps);
    }

    static SimData simulate(int option, Constallation constallation, double dt, double duration){
        return simulate(option, constallation, dt, 0, duration);
    }

    static Constallation step(int option, Constallation constallation, double dt){
        switch (option){
            case 1: eulerStep(constallation, dt);
                    break;
            case 2: eulerCromerStep(constallation, dt);
                    break;
            case 3: verletStep(constallation, dt);
                    break;
            case 4: someStep(constallation, dt);
                    break;
            case 5: rungeKuttaStep(constallation, dt);
                    break;
            default:eulerStep(constallation, dt);
        }
        constallation.updateForces();
        return constallation;
    }

    static Constallation eulerStep(Constallation constallation, double dt){
        //Calculatie acceleration
        // constallation.updateForces();
        int size = constallation.size;

        double[][] deltaVs = new double[size][2];
        double[][] deltaRs = new double[size][2];
        for (int i = 0; i<size; i++){
            double[] adt = VecMath.getScaled(dt, constallation.bodies[i].acc);
            deltaVs[i] = adt;
            double[] vdt = VecMath.getScaled(dt, constallation.bodies[i].vel);
            deltaRs[i] = vdt; 
            
        }
        constallation.accelAll(deltaVs);
        constallation.moveAll(deltaRs);
        // System.out.println(constallation);
        return constallation;
    }

    static Constallation eulerCromerStep(Constallation constallation, double dt){
        // constallation.updateForces();
        //Calculatie acceleration
        int size = constallation.size;

        double[][] deltaVs = new double[size][2];
        double[][] deltaRs = new double[size][2];
        for (int i = 0; i<size; i++){
            double[] adt = VecMath.getScaled(dt, constallation.bodies[i].acc);
            deltaVs[i] = adt;
        }
        constallation.accelAll(deltaVs);
        for (int i = 0; i<size; i++){
            double[] vdt = VecMath.getScaled(dt, constallation.bodies[i].vel);
            deltaRs[i] = vdt;
        }
        constallation.moveAll(deltaRs);
        // System.out.println(constallation);
        return constallation;
    }
    
    static Constallation verletStep(Constallation constallation, double dt){
        //Calculatie acceleration
        // constallation.updateForces();
        int size = constallation.size;
        double[][] deltaVs = new double[size][2];
        double[][] deltaRs = new double[size][2];
        for (int i = 0; i<size; i++){
            deltaVs[i] = VecMath.getScaled(dt/2, constallation.bodies[i].acc);
            double[] vdt = VecMath.getScaled(dt, constallation.bodies[i].vel);
            deltaRs[i] = VecMath.add(vdt, VecMath.getScaled(dt, deltaVs[i]));
            
        }
        constallation.moveAll(deltaRs);
        constallation.updateForces();
        for (int i = 0; i<size; i++){
            double[] adtNew = VecMath.getScaled(dt/2, constallation.bodies[i].acc);
            deltaVs[i] = VecMath.add(deltaVs[i], adtNew);
        }
        constallation.accelAll(deltaVs);
        // System.out.println(constallation);
        return constallation;
    }

    static Constallation someStep(Constallation constallation, double dt){
        //Calculatie acceleration
        // constallation.updateForces();
        int size = constallation.size;

        double[][] deltaVs = new double[size][2];
        double[][] deltaRs = new double[size][2];
        for (int i = 0; i<size; i++){
            double[] adt = VecMath.getScaled(dt, constallation.bodies[i].acc);
            deltaVs[i] = adt;
            double[] vdt = VecMath.getScaled(dt, constallation.bodies[i].vel);
            deltaRs[i] = VecMath.add(vdt, VecMath.getScaled(dt/2, adt)); 
            
        }
        constallation.accelAll(deltaVs);
        constallation.moveAll(deltaRs);
        // System.out.println(constallation);
        return constallation;
    }

    static Constallation rungeKuttaStep(Constallation constallation, double dt){
        
        return constallation;
    }

}

class SimData{
    //Array of positions for every body over some frames.
    public double[][][] posFrames;
    public double[][][] velFrames;
    public double[][][] accFrames;
    //Array of kinetic energy over some frames
    public double[] timeArray;
    public double[][] totalMomentum;
    public double[] kinEnFrames;
    public double[] potEnFrames;
    //Array of names for every body
    public String[] bodyNames;
    //Array of masses for every body
    public double[] bodyMasses;

    public int size;
    public int index;
    public int frames;
    
    public SimData(int frames, Constallation cons){
        this.frames = frames;
        this.size = cons.size;
        // vector[time][planet][coordinate]
        posFrames = new double[size][2][frames];
        velFrames = new double[size][2][frames];
        accFrames = new double[size][2][frames];

        timeArray = new double[frames];
        totalMomentum = new double[2][frames];
        kinEnFrames = new double[frames];
        potEnFrames = new double[frames];

        bodyNames = new String[size];
        bodyMasses = new double[size];

        for (int i = 0; i<size; i++){
            bodyNames[i] = cons.bodies[i].name;
            bodyMasses[i] = cons.bodies[i].MASS;
        }
        index = 0;
    }

    public void addFrame(Constallation cons, double dt){
        if (index < frames){
            for (int i = 0; i<size; i++){
                posFrames[i][0][index] = cons.bodies[i].pos[0];
                posFrames[i][1][index] = cons.bodies[i].pos[1];
                //Set velocity
                velFrames[i][0][index] = cons.bodies[i].vel[0];
                velFrames[i][1][index] = cons.bodies[i].vel[1];
                //Set acceleration
                accFrames[i][0][index] = cons.bodies[i].acc[0];
                accFrames[i][1][index] = cons.bodies[i].acc[1];
            }
            timeArray[index] = dt*(double)index;
             // Total momentum
            double[] momentum = cons.momentumTotal();
            totalMomentum[0][index] = momentum[0];
            totalMomentum[1][index] = momentum[1];
            // Total kinetic and potential energy
            kinEnFrames[index] = cons.calcKinEn();
            potEnFrames[index] = cons.calcPotEn();
            }
        else{
            throw new IllegalArgumentException("Can not add new frame.");
        }
        index += 1;
    }

    public double getMinDist(int index1, int index2){
        double[][] pos1 = VecMath.transpose(posFrames[index1]);
        double[][] pos2 = VecMath.transpose(posFrames[index2]);        
        return VecMath.shortestDistance(pos1, pos2);
    }

    public double getMinVelDiff(int index1, int index2){
        double[][] vel1 = VecMath.transpose(velFrames[index1]);
        double[][] vel2 = VecMath.transpose(velFrames[index2]);        
        return VecMath.shortestDistance(vel1, vel2);

    }


    public void save(String fileName){
        String[] labels = {"Names", "Masses", "Time", "Positions", "Velocities", "Accelerations", "Total Momentum", "Kinetic energy", "Potential energy"};
        String[] data = new String[9];
        data[0] = pprinter.array2string(bodyNames);
        data[1] = pprinter.array2string(bodyMasses);
        data[2] = pprinter.array2string(timeArray);
        data[3] = pprinter.array2string(posFrames);
        data[4] = pprinter.array2string(velFrames);
        data[5] = pprinter.array2string(accFrames);
        data[6] = pprinter.array2string(totalMomentum);
        data[7] = pprinter.array2string(kinEnFrames);
        data[8] = pprinter.array2string(potEnFrames);

        pprinter.printData(data, labels, fileName);
        System.out.println("Saved " +String.valueOf(frames) + " frames.");
    }

}