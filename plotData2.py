import matplotlib.pyplot as plt
import numpy as np


def readData(filename):
    print("Reading...")
    file = open(filename + ".txt")
    text = file.read()
    file.close()
    print("Evaluating...")
    data = eval(text)
    print("Done!")
    return data

def plotAngles():
    data = readData("angleTest")
    plt.plot(data["Angles"], data["Distances"])
    plt.show()

def plotTemps(save):
    data = readData("TempData3")
    mass = data["Masses"]
    maxTs = data["Maxtemps"]
    minTs = data["Mintemps"]
    plt.xscale("log")
    plt.plot(mass, [m*2.2E7 for m in mass], label = "Fitted function", color = "black", lw = .6)
    plt.plot(mass, [m*(-.9E6) for m in mass], color = "black", lw = .6)
    plt.scatter(mass, [T-maxTs[0] for T in maxTs], label = r"$\Delta T_{max}$", s = 6)
    plt.scatter(mass, [T-minTs[0] for T in minTs], label = r"$\Delta T_{min}$", s = 6)
    
  
    plt.title("Earth temperature")
    plt.xlabel("Asteroid mass [MS]")
    plt.ylabel("Final temperature  [K]")
    plt.legend()
    plt.tight_layout()
    if save:
        plt.savefig("temps.png", dpi = 200)
    plt.show()

plotTemps(True)
def plotEns():
    data = readData("IntegratorData")
    time = data["Time"]
    Es1 = data["TotEns"][0]
    Es2 = data["TotEns"][1]
    Es3 = data["TotEns"][2]

    plt.plot(time, Es1, label = "Euler")
    plt.plot(time, Es2, label = "Euler-Cromer")
    plt.plot(time, Es3, label = "Verlet")
    plt.legend()

    plt.xlim(.8, 1)
    plt.ylim(-8, 0)

    plt.title("Integrator comparison")
    plt.xlabel("Time [years]")
    plt.ylabel(r"Energy [$ M_s au^2 yrs^{-2}$]")
    plt.tight_layout()

    plt.savefig("integrators.png", dpi = 200)
    plt.show()