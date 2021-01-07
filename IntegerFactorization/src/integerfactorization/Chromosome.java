/*
 * Simple
 */
package integerfactorization;

/**
 * @date September 30, 2018
 * @author Emilia
 */

import java.util.Random;
import java.math.BigInteger;

public class Chromosome {
    
    private int[] chrom;  //the array that holds the order of the cities in question
    private int length;
    private Random rand;
    private BigInteger eval;
    private BigInteger decChrom;
  
  public Chromosome(int l, Random r) {
    
      length = l;
      chrom = new int[length];
      eval = new BigInteger(BigInteger.valueOf(Long.MAX_VALUE).toString());
      rand = r;
    
  } //constructor
  
  public Chromosome(Chromosome c) {
      length = c.length;
      chrom = new int[length];
      for(int i= 0; i < length; i ++) {
          chrom[i] = c.chrom[i];
      }
      decChrom = c.getPrime();
      eval = c.getEval();
      rand = c.rand;
  } //copy
  
  public int[] getChromosome() {
      return chrom;
  }//getChromosome
  
  /*This method sets the ith element in the Chromosome to c
   * @param i  the position in the chrom array
   * @param c  the value the element will be set to
   * */
  public void set(int i, int c) {
    chrom[i] = c;
  } //set()
  
  public void copy(Chromosome c) {
      for(int i= 0; i < length; i ++) {
          chrom[i] = c.get(i);
      }
      eval = c.getEval();
  }//copy
  
  public int getSize() {
      return chrom.length;
  }//getSize
  
  /*This function returns the value of the ith element in the chrom array
   * @param i  the element of the chrom array
   * @return int  the value of the ith element
   * */
  public int get(int i) {
    return chrom[i];
  } //get()
  
  public BigInteger getEval() {
      return eval;
  }//getEval
  
  public void randTour() {
      for (int i=0; i < length; i++) {
            if (i == 0) {
                chrom[i] = 1;
            } else {
                chrom[i] = rand.nextInt(2);
            }
        }
  }//randTour
  
  public void mutation() {
      int randAllele = rand.nextInt(length-1) + 1;
      
      if (chrom[randAllele] == 0) {
          chrom[randAllele] = 1;
      } else {
          chrom[randAllele] = 0;
      }
  }//mutation
  
  public void evaluationFunction(BigInteger n) {
      String strChrom = "";
      for(int i = 0; i < chrom.length; i++) {
          strChrom = strChrom + chrom[i];
      }
      decChrom = new BigInteger(Long.parseLong(strChrom, 2)+"");
      
      if (!decChrom.equals(new BigInteger("0")) && !decChrom.equals(new BigInteger(n+"")) && !decChrom.equals(new BigInteger("1"))) {
        eval = n.remainder(decChrom);
      } else {
        eval = new BigInteger(BigInteger.valueOf(Long.MAX_VALUE).toString());
      }
  }//evaluationFunction
  
  public BigInteger getPrime() {
      return decChrom;
  }//getPrim()
  
} //Chromosome