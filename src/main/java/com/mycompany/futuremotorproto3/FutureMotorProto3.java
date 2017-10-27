/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.futuremotorproto3;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.la4j.Vector;
import org.la4j.vector.dense.BasicVector;
import org.la4j.Matrix;
import org.la4j.matrix.dense.Basic2DMatrix;
import org.la4j.matrix.DenseMatrix;

/**
 *
 * @author gergohajzer
 */
public class FutureMotorProto3 {
    
    private static List<Node> graph;        //graph, cointaning the nodes
    private static int iterations = 300;     //pieces of simulations
    

    public FutureMotorProto3(){
        this.graph = new ArrayList<>();
        readFiles("City");

        Vector V = createVector();
        System.out.println(V);
        Matrix M = createMatrix();
        System.out.println(M);
        iterations(V,M);
    }
    
    
    //return the population of Debrecen
    public int sumNumber(){    
        int sum = 0;
        for(Node n : graph){
            sum += n.getNumber();
        }
        return sum;
    }
    
    //run the iterations
    public void iterations(Vector myVec, Matrix myMatrix){
        for (int i = 0; i <= iterations; i++) {
            System.out.println(i + ". iteration");
            myVec = myVec.multiply(myMatrix);
            System.out.println(myVec);
            System.out.printf("%9.8f ",myVec.sum());
            System.out.println();
        }
    }
    
    //create the vector
    public Vector createVector(){
        int size = graph.size();
        double sum = sumNumber();
        Vector myVec = new BasicVector(size);
        for (int i = 0; i < size; i++) {
            myVec.set(i,graph.get(i).getNumber()/sum);
        }
        
        return myVec;
    }
    //create the matrix
    public Matrix createMatrix(){
        int size = graph.size();
        Matrix myMatrix = new Basic2DMatrix(size,size);
        for (int i = 0; i < size; i++) {
            double linksCount = graph.get(i).getLinks().size();
            for (int j = 0; j < linksCount; j++) {
                String actual = "City/Debrecen" + graph.get(i).getLink(j);
                for (int k = 0; k < size; k++) {
                    if (actual.equals(graph.get(k).getName())) {
                        myMatrix.set(i, k, 1.0/linksCount);
                    }
                }
            }
        }
        return myMatrix;
    }
    
    
    //read the files recursively
    public void readFiles(String sDir){
        File[] Files = new File(sDir).listFiles();
        String name;
        int number;
        List<String> links = new ArrayList<>();
        for(File file: Files){
          if(file.isFile()){
              try{
                  java.util.Scanner in = new java.util.Scanner(new java.io.FileReader(file.getAbsoluteFile()));
                  Node node = new Node(file.getPath());
                  node.setNumber(in.nextInt());
                  
                  while(in.hasNext()){
                      node.setLink(in.next());
                  }
                  graph.add(node);
              }catch(IOException ex){
                  ex.printStackTrace();
              }
          } else {
          }
          if(file.isDirectory()){
            readFiles(file.getAbsolutePath());
          }
        }
    }
    
    public static void main(String[] args) {
        FutureMotorProto3 proba = new FutureMotorProto3(); 
    }
}
