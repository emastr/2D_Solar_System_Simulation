from matplotlib.animation import FuncAnimation
import matplotlib.animation as animation
import matplotlib.pyplot as plt
from  matplotlib.gridspec import GridSpec
import numpy as np
import random as rnd
import ast

Writer = animation.writers['ffmpeg']
writer = Writer(fps=40, metadata=dict(artist='Me'), bitrate=1800)


def readData(filename):
    print("Reading...")
    file = open(filename + ".txt")
    text = file.read()
    file.close()
    print("Evaluating...")
    data = ast.literal_eval(text)
    print("Done!")
    return data

def prepPlot(title, xlabel, ylabel):
    fig = plt.figure()
    plt.xlabel(xlabel)
    plt.ylabel(ylabel)
    return fig

def plotEnergies(time, Epots, Ekins, plot):
    plt.figure()
    plt.title("Energy over time")
    plt.xlabel("Time [yr]")
    plt.ylabel("Energy [MS AU^2/yr]")
    plt.plot(time, kinEns, label = "Total Kinetic Energy")
    plt.plot(time, potEns, label = "Total Potential Energy")
    plt.plot(time, [p + k for p,k in zip(potEns, kinEns)], label = "Total Energy")
    plt.legend()
    plt.tight_layout()
    if plot:
        plt.savefig("Energies.png", dpi = 200, transparent=True)
    plt.show()

def plotEnergyRatio(time, Epots, Ekins):
    #(mV^2)/(Gm/r) = R^3/T^2/G
    plt.figure()
    plt.title("Energy ratio over time")
    plt.xlabel("Time [year]")
    plt.ylabel("Energy ratio [1]")
    plt.plot(time, [k/p for p,k in zip(potEns, kinEns)], label = "Energy ratio")
    plt.legend()
    plt.show()

def plotMomentum(time, momentum):
    plt.figure()
    plt.tight_layout()
    plt.title("Momentum over time")
    plt.xlabel("Time [yr]")
    plt.ylabel("Momentum [MS AU / yr]")
    plt.plot(time, momentum[0], label = "Total momentum in X direction")
    plt.plot(time, momentum[1], label = "Total momentum in Y direction")
    plt.legend()
    plt.savefig("Momentum.png", dpi = 200)
    plt.show()

def getPeriods(time, poss):
    nbodies = len(poss)
    periods = []
    for i in range(nbodies):
        periods.append(getPeriod(time, poss[i]))
    return periods

def getPeriod(time, pos):
    nsteps = len(time)
    xlast = pos[0][0]
    ylast = pos[1][0]
    nOrbits = 0
    for x,y,t in zip(pos[0],pos[1], time):
        if (ylast*y<0)and(x>0):
            nOrbits += 1
        xlast = x
        ylast = y
    nOrbits += (np.arctan2(y,x)/np.pi/2)%1
    return time[-1]/nOrbits

def getPeriodList(time, pos):
    periods = []
    nsteps = len(time)
    tlast = 0
    xlast = pos[0][0]
    ylast = pos[1][0]
    for x,y,t in zip(pos[0],pos[1], time):
        if (ylast*y<0)and(x>0):
            periods.append(t-tlast)
            tlast = t
        xlast = x
        ylast = y
    return periods

def plotSpeed(indices, time, vels):
    vs = [[(vx*vx+vy*vy)**0.5 for vx, vy in zip(vels[i][0],vels[i][1])] for i in indices]
    plt.figure()
    plt.title("Speed over time")
    plt.xlabel("Time [years]")
    plt.ylabel("Speed [AU/year]")
    [plt.plot(time, vs[i], label = names[i]) for i in indices]
    plt.legend()
    plt.show()

def plotAngularSpeed(name, time, vel, pos):
    v = [((vx*vx+vy*vy)/(px*px+py*py))**0.5 for vx, vy, px, py in zip(vel[0],vel[1], pos[0], pos[1])]
    plt.figure()
    plt.title("Angular velocity over time")
    plt.xlabel("Time [years]")
    plt.ylabel("Angular velocity [rad/year]")
    plt.plot(time, v, label = name + " speed")
    plt.legend()
    plt.show()

def getTemp(index, time, poss, names, r0, T0):
    pos = poss[index]
    sunPos = poss[0]
    temps = []
    posx = [p - ps for p,ps in zip(pos[0], sunPos[0])]
    posy = [p - ps for p,ps in zip(pos[1], sunPos[1])]
    rs = [(px*px+py*py)**0.5 for px,py in zip(posx, posy)]
    temps = [T0*(r0/r)**0.5 - 273 for r in rs]
    return temps

def plotTemp(index, time, poss, names, r0, T0):
    #Intensity - T^4
    #Intensity - r^2
    #T - sqrt(r)
    #T1 = ksqrt(r1)
    #T2 = ksqrt(r2)
    #T1/sqrt(r1)*sqrt(r2) = T2
    temps = getTemp(index,time,poss, names, r0, T0)
    plt.figure()
    plt.title("Temperature over time")
    plt.xlabel("Time [years]")
    plt.ylabel("Temperature [C]")
    plt.plot(time, temps, label =  names[index] + " temperature")
    plt.legend()
    plt.show()

def getAngles(vec):
    return [np.arctan2(vy, vx)%(2*np.pi) for vx,vy in zip(vec[0],vec[1])]

def getDistances(pos1, pos2):
    distx = [p1-p2 for p1,p2 in zip(pos1[0], pos2[0])]
    disty = [p1-p2 for p1,p2 in zip(pos1[1], pos2[1])]

    return [(dx*dx + dy*dy)**0.5 for dx,dy in zip(distx, disty)]

def plotAngle(time, vec):
    prepPlot("Velocity angle asteroid", "Time [years]", "Angle [radians]")
    angles = getAngles(vec)
    plt.plot(time, angles)
    plt.tight_layout()
    plt.show()

def getRandVec(size, lim):
    return [(1-2*rnd.random())*lim for r in range(size)]

## READ DATA________________________

data = readData("Data")

names = data["Names"]
masses = data["Masses"]
time = data["Time"]
poss = data["Positions"]
vels = data["Velocities"]
accs = data["Accelerations"]
moms = data["Total Momentum"]
kinEns = data["Kinetic energy"]
potEns = data["Potential energy"]

nframes = len(time)
nbodies = len(poss)

## PLOT DATA______________________________

# PLOT SPECS_____________
#Origo point can be any body name ("Sun", "Earth", etc") or "Default"
def getOrigo(origoName):
    if (origoName == "Default"):
        origo = [[0]*nframes]*2
    else:
        for i in range(nbodies):
            if names[i] == origoName:
                origo = poss[i]
    return origo

def plotFrame(j):
     # Radius and color of each body.
    radii = [0.2, 0.05, 0.05, 0.1, 0.1, 0.2, 0.15, 0.15, 0.15, 0.05]
    radii = [r for r in radii]
    colors = [(1,1,0.5), (0.7,0.8,0.8), (1,0.9,0.5), (0.4,0.5,1),(1,0.6,0), (1,0.8,0.6), (0.98,1,0.8), 'lightblue', 'cyan', 'white']

    #Create figure.
    fig =  plt.figure(dpi = 200)
    # fig.suptitle("Animation")

    ax = fig.gca()
    plt.title("Animation")
    ax.set_facecolor('black')
    ax.set_xlim(-1, 35)
    ax.set_ylim(-13, 13)
    plt.autoscale(False)

    for i in range(nbodies):
        pos = (poss[i][0][j], poss[i][1][j])
        body = plt.Circle(pos, radii[i], color = colors[i])
        ax.add_artist(body)
        # text = plt.text(pos[0],pos[1]+0.1,  names[i], horizontalalignment='left', verticalalignment='bottom', color = 'white', fontsize = 3, rotation = 45)
        # ax.add_artist(text)
    plt.show()

def showAnimation(savefig, filename, origoName, lim, yearsPerSecond, data1, data2, name1, name2, relativeCurves = False):
    global poss, time, nbodies, nframes
    showNames = True
    showOrbits = True
    #Time step in years
    dt = time[1]
    #Steps per year
    spy = 1/dt
    #Desired frames per second
    fps = 40
    #Needed steps per frame
    spf = round(yearsPerSecond*spy/fps)
    #Total runtime in frames
    frames = round(len(time)/spf)

    origo = getOrigo(origoName)

    # Radius and color of each body.
    radii = [0.2, 0.05, 0.05, 0.1, 0.1, 0.2, 0.15, 0.15, 0.15, 0.05]
    radii = [r*(lim/15) for r in radii]
    colors = [(1,1,0.5), (0.7,0.8,0.8), (1,0.9,0.5), (0.4,0.5,1),(1,0.6,0), (1,0.8,0.6), (0.98,1,0.8), 'lightblue', 'cyan', 'white']

    #Create figure.
    fig =  plt.figure(dpi = 200)
    # fig.suptitle("Animation")
    gs = GridSpec(2,3)

    ax = plt.subplot(gs[:,:-1])
    plt.title("Animation")
    ax.set_facecolor('black')
    ax.set_xlim(origo[0][0]-lim, origo[0][0] + lim)
    ax.set_ylim(origo[1][0]-lim, origo[1][0] + lim)
    plt.autoscale(False)
    # Bodies as artists
    bodies = []
    # Plot orbits
    orbits = []
    # text boxes
    texts = []
    # plt.scatter(getRandVec(100, lim), getRandVec(100, lim), color = 'white', s = .001, marker = '*')
    
    if relativeCurves:
        poss = [[[poss[i][j][k] - origo[j][k] for k in range(nframes)]for j in range(2)] for i in range(nbodies)]
    
    for i in range(nbodies):
        pos = (poss[i][0][0], poss[i][1][0])
        body = plt.Circle(pos, radii[i], color = colors[i])
        ax.add_artist(body)
        bodies.append(body)

        if showOrbits:
            orbit, = plt.plot(pos[0], pos[1], color = colors[i], lw = .3)
            # orbit, = plt.plot(possRel[i][0][0], possRel[i][1][0], color = colors[i], lw = .3)
            orbits.append(orbit)

        if showNames:
            text = plt.text(pos[0],pos[1], names[i], color = 'white', fontsize = 5)
            ax.add_artist(text)
            texts.append(text)

    deltaT = time[-1]

    sideAx1 = plt.subplot(gs[0,2])
    #Set limits
    sideAx1.set_xlim(-deltaT, deltaT)
    marg = (max(data1) - min(data1))*0.1
    sideAx1.set_ylim(min(data1)-marg, max(data1)+marg)
    plt.autoscale(False)
    plt.title(name1)
    timePlot1, = plt.plot(time, data1)
    sideAx1.add_artist(timePlot1)

    sideAx2 = plt.subplot(gs[1,2])
    sideAx2.set_xlim(-deltaT, deltaT)
    marg = (max(data2) - min(data2))*0.1
    sideAx2.set_ylim(min(data2)-marg, max(data2)+marg)
    plt.title(name2)
    timePlot2, = plt.plot(time, data2)
    sideAx2.add_artist(timePlot2)


    plt.tight_layout()
    def animate(frame):
        j = frame*spf
        
        if relativeCurves:
            ax.set_xlim(-lim, lim)
            ax.set_ylim(-lim, lim)
        else:
            ax.set_xlim(origo[0][j]-lim, origo[0][j] + lim)
            ax.set_ylim(origo[1][j]-lim, origo[1][j] + lim)
        for i in range(nbodies):
            pos = (poss[i][0][j], poss[i][1][j])
            bodies[i].center = pos
            if showOrbits:
                k = max(0, j-5000)
                k=0
                orbits[i].set_data(poss[i][0][k:j], poss[i][1][k:j])
                # orbits[i].set_data(possRel[i][0][0:j], possRel[i][1][0:j])
            if showNames:
                texts[i].set_position(pos)
        objects = []
        for body, text, orbit in zip(bodies, texts, orbits):
            objects.append(body)
            objects.append(text)
            objects.append(orbit)
        timePlot1.set_data(time[:j], data1[:j])
        timePlot2.set_data(time[:j], data2[:j])
        sideAx1.set_xlim(time[j]-deltaT, time[j]+deltaT/10)
        sideAx2.set_xlim(time[j]-deltaT, time[j]+deltaT/10)

        objects.append(timePlot1)
        objects.append(timePlot2)

        return objects

    print("Animating...")
    anim = FuncAnimation(fig, animate, frames = frames, blit = True, interval = 1/fps*1000, repeat = True)
    if savefig:
        print("saving animation...")
        anim.save(filename+ '.mp4', writer = writer)
        print("Done!")
    plt.tight_layout()
    plt.show()
    
def simEarthTemp():
    global time, poss
    year = 31556926
    T0 = 288
    r0 = 1
    sigma = 5.67E-8
    C = 4186
    rEarth = 6.371E6
    mass = 5.97E24/1E4
    pos = poss[3]
    sunPos = poss[0]
    coeff = sigma*np.pi*rEarth**2/C/mass*year
    
    temps = []
    T = T0
    diffPos = [[p-ps for p,ps in zip(pxy,psxy)]for pxy,psxy in zip(pos, sunPos)]
    rs = [(x**2 + y**2)**0.5 for x,y in zip(diffPos[0], diffPos[1])]
    dt = time[1]
    for i in range(nframes):
        T += coeff*(T0**4*(r0/rs[i])**2 - T**4)*dt
        temps.append(T)
    plotTemp("Earth", time, pos, sunPos, r0, T0)
    plt.plot(time, temps)
    plt.show()

def plotTemps(save):
    data = readData("TempData3")
    mass = data["Masses"]
    maxTs = data["Maxtemps"]
    minTs = data["Mintemps"]
    plt.xscale("log")
    plt.scatter(mass, [T-288 for T in maxTs], label = r"$\Delta T_{max} [K]$", s = 4)
    plt.scatter(mass, [T-288 for T in minTs], label = r"$\Delta T_{min}$", s = 4)
    plt.plot(mass, [np.exp(m) for m in mass], label = "Fitted data", color = "black")
    plt.plot(mass, [-np.exp(m) for m in mass], color = "black")
  
    plt.title("Earth temperature")
    plt.xlabel("Asteroid mass [MS]")
    plt.ylabel("Final temperature  [K]")
    plt.legend()
    plt.tight_layout()
    if save:
        plt.savefig("temps.png", dpi = 200)
    plt.show()

def plotAccuracy():
    data = readData("TemperatureData2")
    # plt.xscale("log")
    # plt.yscale("log")
    plt.scatter(data["Time Steps"], [d for d in data["Temperature"]], label = "Earth surface temperature", s = 4)

    # fit = [-0.7722 - c*np.exp(a*d) for d in data["Time Steps"]]
    # plt.plot(data["Time Steps"], fit, label = "Fit")
    # plt.plot(data["Time Steps"][:-1], thing, label = "Fit")
    plt.title("Difference in earth's temperature")
    plt.xlabel("Time step [years]")
    plt.ylabel("Final temperature difference [K]")
    plt.legend()
    plt.tight_layout()
    plt.savefig("FinalTemps.png", dpi = 200, transparent = True)
    plt.show()


# plotAngle(time, vels[9])
# plotSpeed([0,1,2,3,4,5,6,7,8], time, vels)
# print(getPeriodList(time, poss[3]))
# plotEnergyRatio(time, kinEns, potEns)

# plotEnergies(time, kinEns, potEns, False)
# plotMomentum(time, moms)


eTots = [p+k for p,k in zip(potEns, kinEns)]
# data1 = kinEns
# data1 = getDistances(poss[3], poss[9])
# angles = getAngles(vels[9])
temps = getTemp(3, time, poss, names, 1, 288)
dists = getDistances(poss[3], poss[9])
accSun = accs[3]
acc = [np.sqrt(ax*ax + ay*ay) for ax, ay in zip(accSun[0],accSun[1])]
# plt.plot(time, data2)
# plt.show()
# plotFrame(0)


showAnimation(False, "anim1", "Earth", 1, 0.2, acc, dists, "Earth acceleration", "Distance to moon", relativeCurves=False)
# showAnimation(False, "anim1", "Sun", 1, 1, dists, angles, "Distance to Earth", "Velocity angle")


# data = readData("angleTest")
# plt.plot(data["Angles"], data["Distances"])
# plt.show()

# plotTemps(False)
