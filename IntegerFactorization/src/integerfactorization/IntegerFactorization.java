/*
 * Original GA -Simple
 */
package integerfactorization;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @date September 30, 2018
 * @author Emilia
 */
public class IntegerFactorization {

//    public long N; // the integer we want to factor 518896453363L
//    public int POP_SIZE = 100;
    private BufferedWriter out; // the file to write the output to
    private BufferedWriter summary; // the summary file to write the summary to
//    private final String PATH = "C:\\Users\\Emilia\\Documents\\University\\Year 5\\COSC 4F90";
    private int minGen = Integer.MAX_VALUE;
    private int maxGen = 0;
    private int avgGen = 0;

    public IntegerFactorization() {
        try {
            //get the input file
            Scanner in = new Scanner(new File("runs.txt"));
            
            try {
                //create the file to write to
                summary = new BufferedWriter(new FileWriter("summary.txt"));
            } catch (IOException ex) {
                Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            while (in.hasNext()) {
                
                BigInteger nLong = new BigInteger(in.next());
                int inCross = in.nextInt();
                int inMut = in.nextInt();
                
                int foundTimes = 0;
                minGen = Integer.MAX_VALUE;
                maxGen = 0;
                avgGen = 0;
                
                for (int i = 0; i < 30; i++) {
                    
                    long startTime = System.nanoTime();
                
                    //create a new GA
                    GA g = new GA(nLong, inCross, inMut);
                
//                    try {
//                        //create the file to write to
//                        out = new BufferedWriter(new FileWriter(nLong + "_" + g.CROSS_RATE + "_" + g.MUT_RATE + "_" + g.seed +".txt"));
//                        //print the GA information
//                        out.write("Seed used:\t" + g.seed +"\n");
//                        out.write("N:\t" + nLong + "\n");
//                        out.write("Crossover rate:\t" + g.CROSS_RATE +"\n");
//                        out.write("Mutation Rate:\t" + g.MUT_RATE +"\n");
//                        out.write("\n");
//                    } catch (IOException ex) {
//                        Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    
                    int generations = 0;
                    
                    //generate an initial population
                    g.initialPOP();
                    
                    //write stats to the output file about the initial population
//                    try {
//                        out.write("Generation #\t" + generations +"\n");
//                        out.write("Fittest Chrom Eval:\t" + g.getFittestChrom().getEval() +"\n");
//                        out.write("Fittest Chrom Prime:\t" + g.getfittestPrime() +"\n");
//                        out.write("Population Average Eval:\t" + g.getAverageEval() +"\n");
//                        out.write("Convergence:\t" + g.getConvergedRate() +"\n");
//                        out.write("\n");
//                    } catch (IOException ex) {
//                        Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                    //        g.printGeneration();
                    
                    if (g.getFittestChrom().getEval().equals(new BigInteger("0"))) {
                        foundTimes++;
                        avgGen = avgGen + generations;
                        if (generations < minGen) minGen = generations;
                        if (generations > maxGen) maxGen = generations;
                    }

                    while(!g.converged() && !g.getFittestChrom().getEval().equals(new BigInteger("0")) && generations < 2000) {

                        generations++;
//                        System.out.println("Generation: " + generations);
                        g.evolvePopulation();
                        //          g.printGeneration();

//                        try {
//                            out.write("Generation #\t" + generations +"\n");
//                            out.write("Fittest Chrom Eval:\t" + g.getFittestChrom().getEval() +"\n");
//                            out.write("Fittest Chrom Prime:\t" + g.getfittestPrime() +"\n");
//                            out.write("Population Average Eval:\t" + g.getAverageEval() +"\n");
//                            out.write("Convergence:\t" + g.getConvergedRate() +"\n");
//                            out.write("\n");
//                        } catch (IOException ex) {
//                            Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
//                        }
                        
                        if (g.getFittestChrom().getEval().equals(new BigInteger("0"))) {
                            foundTimes++;
                            avgGen = avgGen + generations;
                            if (generations < minGen) minGen = generations;
                            if (generations > maxGen) maxGen = generations;
                        }
                        
                    }
                    long endTime = System.nanoTime();
                    long microseconds = (endTime - startTime) / 1000;
                    
                    //        g.printGeneration();

//                    try {
//                        //how long this GA took
//                        out.write("Total time (in microseconds):\t" + microseconds);
//                        out.close();
//                    } catch (IOException ex) {
//                        Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
//                    }
                }
                  
                try {
                    if (foundTimes != 0) avgGen = avgGen/foundTimes;
                    //write the summary for this rate combo
                    summary.write(nLong + "\t" + inCross + "\t" + inMut + "\t" + foundTimes + "/30\t" + minGen + "\t" + maxGen + "\t" + avgGen + "\n");
                } catch (IOException ex) {
                    Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            try {
                //close summary output file
                summary.close();
            } catch (IOException ex) {
                Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //close inputfilestream connection
            in.close();
            
//            System.out.println("Original GA - Simple");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(IntegerFactorization.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//constructor

    public static void main(String[] args) { IntegerFactorization i = new IntegerFactorization(); }

}//IntegerFactorization
