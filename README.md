# P2-Simulation
The program is used to simulate the spread of the flu, over a small population (a max of 2000 people).  
The program was created as part of a study project, during the second semester of computer science at AAU.  
The study belongs to group dat310a.  

## Guide to use of the program
When you first open the program, you will be presented with a menu, containing several boxes which represents values used in the simulation.  
Once values have been entered hit apply and press start to begin the simulation. You will need to hit apply in order use your values.  
If the simulation has been started you are able to continue with what was previously the start button. If you wish to restart hit apply again.  
The boxes are separated into two sections.
### SIR-Options.
These values represent start values in terms of the amount of people to be simulated.  
Susceptible: The start value for the amount of people who have yet to contract the flu and therefore are susceptible.  
Infected: The start value for the amount of people who are currently infected with the flu and able to transmit it.  

### Spread-modifiers
These values represent which percentage of susceptibles take certain precautions.  
Vaccinated: The percentage of susceptibles who are vaccinated against the flu and therefore immune to it.  
Handsanitizer: The percentage of susceptibles who use handsanitizers, which will reduce their chances of contracting the flu.  
Cover mouth while coughing: The percentage of people (susceptibles & infected) who cover their mouth while coughing, reducing the chance they transmit the flu.  
Stays home after symptoms: The percentage of people who will stay home after symptoms start showing (as they become infected).

## The simulation
Once the simulation has been started, the window is split into multiple parts.  
In the lower left corner of the window, is the core of the simulation. The picture of a village with housings (dark squares), workplaces (blue squares) and roads (grey). People (circles) move between housings and work by using the road.  
People have a chance to contract the flu from a infected person if they are nearby.  

In the lower right corner is a textfield with information about every person in the simulation, such as their health status and age.  
Just above the textfield is overall statistics about the current simulation located, these are the amount of susceptibles, infected, recovered, dead and whether there is a chance of the flu becoming an epidemic. 
- Susceptibles marked as light blue circles, are at risk of being infected
- Infected marked as red circles, risk infecting susceptibles
- Recovered marked as yellow circles, are immune to being infected
- Dead are marked with a red cross


## Credits
Pictures shown during the simulation are drawn by the project group (dat310a).
The group consists of seven people including:
- Daniél Garrido
- Esben Jensen
- Jonas Pedersen
- Marcus Rynkeby Jørgensen
- Peter Madsen
- Simon Mathiasen
- Stefan ́ı Dali Mir-Mackiewicz

Other graphical elements such as buttons and textboxes are part of JavaFX.
