# 2D Solar System Simulation
A of the solar system in two dimensions, approximating all orbits as circular. The source code consists of 7 main components.

1. `VecMath.java` contains some vector math such as dot product, cross product etc.

2. `Physics.java` contains some physics functions and constants, such as newton's law of gravity, gravitational potential and unit conversion functions

3. `Constallation.java` contains some useful functions and classes to deal with planet objects.

4. `Simulator.java` Contains functions for solving a differential equation numerically. There are options for different approaches such as velocity-verlet, euler etc.

5. `pprinter.java` Contains functions that print the simulation data to .txt files, which can in turn be read by python.

6. `Main.java` Sets up the initial conditions and runs the simulation, resulting in printed data.

7. `plotData.py` plots the data in python. 


[Link to my results](https://imgur.com/a/ms1dYXV)




