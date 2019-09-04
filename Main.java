import java.lang.Math;

public class Main{
    public static void main(String[] args){

        // plotAngles(1E-4, 1.03950, 1.0405, 100);
        // Simulator.simulate(3, solarSystem(mass, startY, startAngle), 0.001, 1.2, 1.5).save("Data");
        // double angle = gradientNearMiss(mass, startAngle, steps);
        // Simulator.simulate(3, solarSystem(mass, angle), 0.001, 1.2, 1.5).save("Data");
        // saveTemps();

        // SimData data = Simulator.simulate(3, solarSystem(1E0, Math.PI/3), 1E-4, 0.856,0.862);
        // data.save("DataE0");
        // SimData data = Simulator.simulate(3, solarSystem(1E-2, 1.03828), 1E-4, 0,2.0);
        // data.save("DataE-2");
        // SimData data = Simulator.simulate(3, solarSystem(1E-4, 1.03998), 1E-4, 0,2.0);
        // data.save("DataE-4");
        // SimData data = Simulator.simulate(3, solarSystem(1E-6, 1.03998), 1E-4, 0,2.0);
        // data.save("DataE-6");
        // SimData data = Simulator.simulate(3, solarSystem(1E-8, 1.03999), 1E-4, 0,2.0);
        // data.save("DataE-8");
        // SimData data = Simulator.simulate(3, solarSystem(1E-10, 1.039986), 1E-4, 0,2.0);
        // data.save("DataE-10");
        // SimData data = Simulator.simulate(3, solarSystem(1E-13, 1.039987), 1E-4, 0,2.0);
        // data.save("DataE-12");
        
        
        SimData data = Simulator.simulate(3, moonSystem(), 1E-4, 0, 1.0);
        // SimData data = Simulator.simulate(3, defaultSystem(), 1E-4, 0, 2.0);
        data.save("Data");

        // saveTemps();
        // saveEnergies();
        // plotAngles(1E-10, 0.1, 1, 20);

        // print(data.getMinDist(3,9));

        
        // Simulator.simulate(3, solarSystem(1E-10, -4.987758169973063, 1.037095556211891), 0.001, 1.2, 1.5).save("Data");
        // print(simulateAsteroid(mass, posAng[0], posAng[1], true));
    }

    //mass [solar masses], startY [Astronomical units], velAngle [radians]
    //Basic solar system with an asteroid placed at a distance 5AU from the sun, centripetal velocity and initial angle velAngle
    public static Constallation solarSystem(double mass, double velAngle){
        
        String[] names  = {"Sun",  "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Asteroid"};
        // Coordinates and masses in SI units.
        double[] masses = {1.99E30, 3.31E24,  4.87E24, 5.97E24, 6.39E23, 1.90E27,  5.68E26,  8.68E25,   1.02E26,       0    };
        double[] posx  =  {0,       5.79E10,  1.08E11, 1.50E11, 2.28E11, 7.79E11,  1.43E12,  2.87E12,   4.50E12,       0    };
        double[] posy  =  {0,           0,        0,        0,      0,       0,         0,         0,      0,          0    };
        
        int size = names.length;
        //Scale masses.
        masses = Physics.scaleSIMasses(masses);
    
        //Scale distances
        posx = Physics.scaleSIDists(posx);
        posy = Physics.scaleSIDists(posy);

        double[][] vels = new double[size][2];
        double[][] poss = new double[size][2];

        // Set sun velocity
        vels[0] = new double[2];
        // Set planet velocities (and positions)
        for (int i = 1; i<size-1; i++){
            vels[i][0] = 0;
            vels[i][1] = Physics.centrVel(masses[0], posx[i]);
            poss[i][0] = posx[i];
            poss[i][1] = posy[i];
        }
        // Set Asteroid velocity, mass and postion
        double startY = -5;
        masses[size - 1] = mass;
        poss[size-1][0] = 0;
        poss[size-1][1] = startY;
        vels[size-1] = VecMath.getVector(Physics.centrVel(masses[0], Math.abs(startY)), velAngle);

        Constallation solarSystem = new Constallation(names, masses, poss, vels);
        // print(solarSystem);
        return solarSystem;
    }

    public static Constallation defaultSystem(){
        String[] names  = {"Sun",  "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune"};
        // Coordinates and masses in SI units.
        double[] masses = {1.99E30, 3.31E24,  4.87E24, 5.97E24, 6.39E23, 1.90E27,  5.68E26,  8.68E25,   1.02E26 };
        double[] pos  =  {0,       5.79E10,  1.08E11, 1.50E11, 2.28E11, 7.79E11,  1.43E12,  2.87E12,   4.50E12 };
        
        int size = names.length;
        //Scale masses.
        masses = Physics.scaleSIMasses(masses);
        //Scale distances
        pos = Physics.scaleSIDists(pos);

        double[][] vels = new double[size][2];
        double[][] poss = new double[size][2];

        // Set sun velocity
        vels[0] = new double[2];
        // Set planet velocities (and positions)
        for (int i = 1; i<size; i++){
            vels[i][0] = 0;
            vels[i][1] = Physics.centrVel(masses[0], pos[i]);
            poss[i][0] = pos[i];
            poss[i][1] = 0;
        }
        Constallation solarSystem = new Constallation(names, masses, poss, vels);
        // print(solarSystem);
        return solarSystem;

    }

    public static Constallation moonSystem(){
        String[] names  = {"Sun",  "Mercury", "Venus", "Earth", "Mars", "Jupiter", "Saturn", "Uranus", "Neptune", "Moon"};
        // Coordinates and masses in SI units.
        double[] masses = {1.99E30, 3.31E24,  4.87E24, 5.97E24, 6.39E23, 1.90E27,  5.68E26,  8.68E25,   1.02E26,  7.3E22};
        double[] pos  =  {0,       5.79E10,  1.08E11, 1.50E11, 2.28E11, 7.79E11,  1.43E12,  2.87E12,   4.50E12,    1.50E11 + 3.8E8};
        
        int size = names.length;
        //Scale masses.
        masses = Physics.scaleSIMasses(masses);
        //Scale distances
        pos = Physics.scaleSIDists(pos);

        double[][] vels = new double[size][2];
        double[][] poss = new double[size][2];

        // Set sun velocity
        vels[0] = new double[2];
        // Set planet velocities (and positions)
        for (int i = 1; i<size; i++){
            vels[i][0] = 0;
            vels[i][1] = Physics.centrVel(masses[0], pos[i]);
            poss[i][0] = pos[i];
            poss[i][1] = 0;
        }


        vels[9] = VecMath.add(vels[9], VecMath.getVector(Physics.centrVel(masses[3], 3.8E8/1.50E11), Math.PI/2));
        Constallation solarSystem = new Constallation(names, masses, poss, vels);
        // print(solarSystem);
        return solarSystem;

    }

    public static void saveTemps(){
        double[] masses = VecMath.logSpace(1E-11, 1E-7,100);
        double[] mintemps = new double[100];
        double[] maxtemps = new double[100];
        for (int i = 0; i<100; i++){
            SimData data = Simulator.simulate(3, solarSystem(masses[i], 1.03998), 1E-4, 2.0, 4.0);
            print(i);
            double[][] pos1 = VecMath.transpose(data.posFrames[3]);
            double[][] pos2 = VecMath.transpose(data.posFrames[0]);
            double[] dists = new double[data.frames];
            for (int j = 0; j < data.frames; j++){
                dists[j] = VecMath.dist(pos1[j],pos2[j]);
            }
            mintemps[i] = 288/Math.sqrt(VecMath.min(dists));
            maxtemps[i] = 288/Math.sqrt(VecMath.max(dists));
        }
        String[] lbls = {"Masses", "Mintemps", "Maxtemps"};
        String[] data = {pprinter.array2string(masses), pprinter.array2string(maxtemps), pprinter.array2string(mintemps)};
        pprinter.printData(data, lbls, "TempData3");
    }

    private static double getMinDist(double mass, double angle, double dt){
        int option = 3;
        double duration = 2;
        double skipDuration = 0;
        Constallation system = solarSystem(mass, angle);
        SimData data = Simulator.simulate(option, system, dt, skipDuration, duration);
        return data.getMinDist(3, data.size-1);
    }

    private static void plotAngles(double mass, double a0, double a1, int steps){
        double dt = 1E-4;
        double da = (a1-a0)/(double)steps;
        String[] labels = {"Angles", "Distances"};
        double[] angles = new double[steps];
        double[] distances = new double[steps];
        for (int i = 0; i<steps; i++){
            angles[i] = a0 + da*(double)i;
            distances[i] = getMinDist(mass, angles[i], dt);
        }
        String[] data = {pprinter.array2string(angles), pprinter.array2string(distances)};
        pprinter.printData(data, labels, "angleTest");

    }
    
    private static void saveEnergies(){
        double dt = 1E-3;
        int[] opts  = {1,2,3};
        int frames = 5000;

        double[][] energies = new double[opts.length][frames];
        SimData data = null;
        for (int i = 0; i<opts.length; i++){
            data = Simulator.simulate(opts[i],solarSystem(1,1.2),dt,frames);
            energies[i] = VecMath.add(data.kinEnFrames, data.potEnFrames);
        }
        double[] time = data.timeArray;
        String[] lbls = {"Time", "TotEns"};
        String[] out = {pprinter.array2string(time), pprinter.array2string(energies)};
        pprinter.printData(out, lbls, "IntegratorData");
    }

    private static void plotAccuracy(int points){
        double dtMax = 0.01;
        double dtMin = 1E-5;
        double[] dts =  VecMath.logSpace(dtMax, dtMin, points);
        double[] temps = new double[points];
        for (int i = 0; i<points; i++){
            SimData data = Simulator.simulate(3, solarSystem(1E-10, 1.039987), dts[i], 0.0, 2.0);
            double x = data.posFrames[3][0][data.size-1] - data.posFrames[0][0][data.size-1];
            double y = data.posFrames[3][1][data.size-1] - data.posFrames[0][1][data.size-1];
            temps[i] = 288*(1/Math.sqrt(x*x + y*y)-1);
            print("Done with dt = " + String.valueOf(dts[i]));
        }
        String[] labels = {"Time Steps", "Temperature"};
        String[] data = {pprinter.array2string(dts), pprinter.array2string(temps)};
        pprinter.printData(data, labels, "TemperatureData2");
    }

    public static void print(Object object){
        System.out.println(object);
    }
}
