/*
 * Original GA - SImple
 */
package integerfactorization;

/**
 * @date September 30, 2018
 * @author Emilia
 */

import java.util.*;
import java.util.Random;
import java.math.BigInteger;

public class GA {
    
    //variables to set before running
    private final int POP_SIZE = 2000; //the amount of chromosomes in a population
    private final int TOUR_NUM = 3;
    public int CROSS_RATE;
    public int MUT_RATE;
    
    //variables that will be initialized in the constructor
    private final int chromLength; //how long each chromosome is
    private final Random rand; //random number generator
    public int seed;
    private BigInteger N;
    public List<Chromosome> pop;
    private double convergedRate;
    
    //things to keep track of in the population
    private Chromosome fittestChrom;
    private BigInteger averageEval;
    
  public GA(BigInteger n, int cross, int mut) {
    N = n;
    CROSS_RATE = cross;
    MUT_RATE = mut;
    
    int length = N.bitLength();
    
    if (length % 2 == 0) {
        chromLength = length/2;
    } else {
        chromLength = (length + 1)/2;
    }

    rand = new Random();     //This block of code generates a
    seed = rand.nextInt();   //random number generator and sets the
    rand.setSeed(seed);      //through another random number
    
    pop = new ArrayList<>(); //create a list to hold the population
    
  } //constructor
  
  /** This function gives back how many bits it takes to represent the number n
        @param n  the number we want the but length of
        @return long  the number of 0's and 1's it takes to represent n       **/
//    private int getBitLength(long n) {
//        return Long.toBinaryString(n).length();
//    }//getBitLength
  
  /*This method initializes the population with POP_SIZE random generated chromosomes
   * */
  public void initialPOP() {
    for (int i = 0; i < POP_SIZE; i++) {
      Chromosome chrom = new Chromosome(chromLength, rand);
      chrom.randTour();
      chrom.evaluationFunction(N);
      pop.add(chrom);
    }

//      System.out.println("---------initial pop------------");
    findFittest();   //find the newst best Chromosome
    findAverage();   //Calculate the new Average Distance
//      System.out.println("---------initial pop------------");
  }//initialPOP()
  
  /*This method evolves the population by applying tournament selection, crossover,
   * and mutation
   * */
  public void evolvePopulation() {
    for(int t = 0; t < POP_SIZE; t++) {    //repeats the selection strategy the same amount of times as the POP_SIZE
      tournament();                        
    }
    
    for(int t = 0; t < POP_SIZE; t++) {    //Since the tournament selesction is adding the selected Chromosomes
      pop.remove(0);                       //into the same population, we have to delete the old population which
    }                                      //reside in the beggining of the list
    
    int index = 0;
    while(index < POP_SIZE) {                                   //this while loop grabs the 2 next consecutive
      if(rand.nextInt(100)+1 <= CROSS_RATE) {                 //Chromosomes in the population and uses the CROSS_RATE 
          crossover(pop.get(index), pop.get(index+1));
      }
      index = index + 2;
    }
    
    for(Chromosome chrom: pop) {                //Every Chromosome is tested using MUT_RATE to see if it should
      if(rand.nextInt(100)+1 <= MUT_RATE) {     //go through mutation
        chrom.mutation();
      }
    }
    
    for(Chromosome chrom: pop) {         //Each chromosome's evaluation function is calculated
      chrom.evaluationFunction(N);
    }
    
    elitism();   //replace the worst Chromosome in the population with the best Chromosome
                 //in the previous population 
    
    findFittest();   //find the newst best Chromosome
    findAverage();   //Calculate the new Average Distance        
    
//    System.out.println("----------------------------- next gen");
  }//evolvePopulation
  
  /*This method creates a list to hold all the Chromosomes selected for the tournament selection, the fittest Chromosome
   * is determined and is added to the population
   * */
  public void tournament() {
    
    ArrayList<Chromosome> tour = new ArrayList<Chromosome>();
    
    for (int t = 0; t < TOUR_NUM; t++) {
      Chromosome one = pop.get(rand.nextInt(POP_SIZE));
      tour.add(one);
    }
    
    Chromosome best = tour.get(0);
    
    for (Chromosome tours: tour) {
      if(tours.getEval().compareTo(best.getEval()) < 0) {
        best = tours;
      }
    }
    
    pop.add(best);
    
  } //tournament()
  
  /*This method finds the worst Chromosome in the population and replaces it with the best
   * Chromosome in the previous population
   * */
  public void elitism() {    
    int worstIndex = 0;
    
    for(int i = 0; i < POP_SIZE; i++) {
      if(pop.get(worstIndex).getEval().compareTo(pop.get(i).getEval()) < 0) {
        worstIndex = i;
      }
    }
    
    pop.remove(worstIndex);
    pop.add(fittestChrom);
  } //elitism()
  
  /*This method applies order crossover to the two Chromosomes selected, creating two Child Chromosomes that are then 
   * added to the population while the parents are removed from the population
   * @param p1  the first parent
   * @param p2  the second parent
   * */
  public void crossover(Chromosome p1, Chromosome p2) {  //ordered crossover
    int loc1, loc2;
    Chromosome child1 = new Chromosome(chromLength, rand);
    Chromosome child2 = new Chromosome(chromLength, rand);
    
    loc1 = rand.nextInt(chromLength);
    loc2 = rand.nextInt(chromLength);
    
    if(loc1 > loc2) {       //make sure location 2 comes after
      int temp = loc1;      //location 1
      loc1 = loc2;
      loc2 = temp;
    }
    
    //copy the cut set to the corresponding children
    for(int l = loc1; l <= loc2; l++) {
        child1.set(l, p1.get(l));
        child2.set(l, p2.get(l));
    }
    
    //fill in the rest of the cities from the opposite parent for child1
    for(int f = 0; f < loc1; f++) {
        child1.set(f, p2.get(f));
        child2.set(f, p1.get(f));
    }
    
    //fill in the rest of the cities from the opposite parent for child2
    for(int s = loc2+1; s < chromLength; s++) {
        child1.set(s, p2.get(s));
        child2.set(s, p1.get(s));
    }
    
    pop.remove(p1);
    pop.remove(p2);
    pop.add(child1);
    pop.add(child2);
  }//crossover()
  
  /*This method calculates the average distance in the population
   * */
  public void findAverage() {
    BigInteger sum = new BigInteger("0");
    for (Chromosome chrom: pop) {
      sum = sum.add(chrom.getEval());
    }
    averageEval = sum.divide(new BigInteger(POP_SIZE+""));
//    System.out.println("Average Eval: " + averageEval);
  } //findAverage()
  
  public BigInteger getAverageEval() {
      return averageEval;
  }//getAverageEval()
  
  /*This method iterates through the population to find the chromosome with
   * the shortest distance
   * */
  private void findFittest() {
      
//    fittestChrom = new Chromosome(chromLength, rand);
    fittestChrom = new Chromosome(pop.get(0));
//    fittestChrom.copy(pop.get(0));
//    fittestChrom.setPrime(pop.get(0).getPrime());
        
    for(int i = 0; i < POP_SIZE; i++) {
      if(fittestChrom.getEval().compareTo(pop.get(i).getEval()) > 0) {
          fittestChrom = new Chromosome(pop.get(i));
//        fittestChrom.copy(pop.get(i));
//        fittestChrom.setPrime(pop.get(i).getPrime());
      }
    }
//    System.out.println("fittest chrom in this population: ");
//    print(fittestChrom);
//    System.out.println("prime: " + fittestChrom.getPrime());
  } //findFittest()
  
  public Chromosome getFittestChrom() {
      return fittestChrom;
  }//getFittestChrom
  
  public BigInteger getfittestPrime() {
      return fittestChrom.getPrime();
  }//getFittestPrime
  
//  private void print(Chromosome chrom) {
//      int[] sol = chrom.getChromosome();
//      for (int i = 0; i < sol.length; i++) {
//          System.out.print(sol[i]);
//      }
//      System.out.print(" = " + chrom.getEval());
//      System.out.println("");
//  }//print
  
//  public void printGeneration() {
//      System.out.println("-------------------start Generation ------------------");
//      for(Chromosome chrom: pop) {
//          print(chrom);
//      }
//      System.out.println("-------------------end Generation ------------------");
//  }//printGeneration
  
    public boolean converged() {
      double same = 0.0;
      
      for(Chromosome c: pop) {
          if (c.getPrime() == fittestChrom.getPrime()) same++;
      }
      
      convergedRate = same/POP_SIZE;
      
      if (convergedRate > 0.99) {
//          System.out.println("Converge: " + convergedRate);
          return true;
      }
//      System.out.println("Converge: " + convergedRate);
      return false;
  } //converged
  
  public double getConvergedRate() {
      return convergedRate;
  }//getCOnvergedRate
  
} //GA