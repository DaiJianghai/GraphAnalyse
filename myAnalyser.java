package work;


import it.unimi.dsi.bits.LongArrayBitVector;
import it.unimi.dsi.fastutil.ints.IntArrayFIFOQueue;
import it.unimi.dsi.fastutil.ints.IntArrays;
import it.unimi.dsi.fastutil.ints.IntIterator;
import it.unimi.dsi.logging.ProgressLogger;
import it.unimi.dsi.webgraph.*;
import it.unimi.dsi.webgraph.algo.*;
import it.unimi.dsi.webgraph.jung.JungAdapter;

import java.io.*;
import java.util.*;

public class myAnalyser {
    static public void main(String arg[]) throws Exception {

          /**HELLO WORLD */
//        System.out.printf("Vertices: %d\n", num_v);
//        System.out.printf("Edges: %d\n", graph.numArcs());
//        System.out.printf("outdegrees: %d\n", graph.outdegrees());
//        System.out.printf("basename: %s\n", graph.basename());
//        System.out.printf("Edges: %d\n", graph.);
//        System.out.printf("Edges: %d\n", graph.numArcs());
//        BVGraph bvGraph = BVGraph.loadOffline(arg[0]);
//        System.out.println(bvGraph.numArcs());
//        System.out.println(bvGraph.numNodes());
//        System.out.println(bvGraph.maxRefCount());
//        System.out.println(bvGraph.windowSize());
//        System.out.println(bvGraph.getClass());
//
//
//        System.out.printf("EdgesOf-8: %d\n", graph.outdegree(8));
//
////        bvGraph.writeOffsets();
////        System.out.println(bvGraph.);
//
//
//
        /**OUTPUT FILE METHOD*/

//        ImmutableGraph graph = ImmutableGraph.load(arg[0]);
//        BufferedWriter bw = new BufferedWriter(new FileWriter(arg[0] + ".txt"));
//        int num_v = graph.numNodes();
//        int num_e = 0;
//        for (int v = 0; v < num_v; ++v) {
//            LazyIntIterator successors = graph.successors(v);
//            for (int i = 0; i < graph.outdegree(v); ++i) {
//                int w = successors.nextInt();
//                bw.write(Integer.toString(v));
//                bw.write("\t");
//                bw.write(Integer.toString(w));
//                bw.write("\n");
//                ++num_e;
//            }
//        }
//
//        bw.flush();
//        System.out.printf("Output Edges: %d\n", num_e);




        /**
         * DATA ANALYSE
         * */
        long nodes = 0 ;
        long arcs = 0 ;
        double bitsLink = 0;
        double bitsLinkTranspose = 0;
        double  averageDegree = 0;
        long maximumIndegree = 0;
        long maximumOutdegree = 0;
        long danglingNodes = 0;
        double buckets = 0;
        long largestComponent = 0;
        double spid = 0;
        double averageDistance = 0;
        double reachablePairs = 0;
        double medianDistance = 0;
        double harmonicDiameter = 0;

        /** load graph*/
        ProgressLogger progressLogger = new ProgressLogger();
//        ImmutableGraph graph = ImmutableGraph.loadOffline(arg[0],progressLogger);
        BVGraph graph = BVGraph.load(arg[0], 1, progressLogger);
        System.out.println("GRAPH load SUCCESS");

        /** compute nodes number*/
        nodes = graph.numNodes();
        System.out.println("GRAPH nodes:"+nodes);
////
        /** compute arc number*/
        arcs = graph.numArcs();
        System.out.println("GRAPH arcs:"+arcs);

        /** compute averageDegree*/
        averageDegree = (double)arcs/nodes;
        System.out.println("GRAPH averageDegree:"+String.format("%.3f",averageDegree));
////
         /** compute maximumOutdegree*/
        for(int v = 0; v < nodes; v++){
            long temp = graph.outdegree(v);
            if(temp>maximumOutdegree)
                maximumOutdegree = temp;
        }
        System.out.println("GRAPH maximumOutdegree:"+maximumOutdegree);
////
        /** compute maximumIndegree*/
        ImmutableGraph transpose = Transform.transpose(graph);
        JungAdapter jungAdapter = new JungAdapter(graph,transpose);
        for(int v = 0; v < nodes; v++){
            long temp = jungAdapter.inDegree(v);
            if(temp>maximumIndegree)
                maximumIndegree = temp;
        }
        System.out.println("GRAPH maximumIndegree:"+maximumIndegree);
////
        /** compute danglingNodes number*/
        for(int v = 0; v<nodes; v++){
            if(graph.outdegree(v)==0)
                danglingNodes++;
        }
        double danglingNodesRate =(double)danglingNodes/nodes;
        System.out.println("GRAPH danglingNodesRate:"+String.format("%.4f",danglingNodesRate));
//
////        /**compute largestComponent number    (it is wrong ,the following is right)*/
////        ConnectedComponents connectedComponents = ConnectedComponents.compute(graph,0,progressLogger);
////        int sizearray[] = connectedComponents.computeSizes();
////        System.out.println("OK");
////        Arrays.sort(sizearray);
////        System.out.println("GRAPH componetsNumberMAX:"+sizearray[sizearray.length-1]);
//
////        ImmutableGraph largestComponent1 = connectedComponents.getLargestComponent(graph, 0, progressLogger);
////        System.out.println("OK");
////        System.out.println(largestComponent1.numNodes());
////        System.out.println("OK");
//
//
        /** compute spid (much time)**/
//      ParallelBreadthFirstVisit parallelBreadthFirstVisit = new ParallelBreadthFirstVisit(graph,0,false,progressLogger);
        double[] computereslut = NeighbourhoodFunction.compute(graph);
        spid = NeighbourhoodFunction.spid(computereslut);
        System.out.println("GRAPH spid:"+String.format("%.2f",spid));
//////        System.out.println("OK");
////
////
        /** compute averageDistance (much time because base of over result)*/
        averageDistance = NeighbourhoodFunction.averageDistance(computereslut);
        System.out.println("GRAPH averageDistance:"+String.format("%.2f",averageDistance));
////
////
        /** compute medianDistance (much time because base of over result)*/
        medianDistance = NeighbourhoodFunction.medianDistance(computereslut);
        System.out.println("GRAPH medianDistance:"+medianDistance);
////
////
        /** compute harmonicDiameter (much time because base of over result)*/
        harmonicDiameter = NeighbourhoodFunction.harmonicDiameter(computereslut);
        System.out.println("GRAPH harmonicDiameter:"+String.format("%.2f",harmonicDiameter));
//
        /** compute reachable pairs (need time)*/
        GeometricCentralities geometricCentralities = new GeometricCentralities(graph,0,progressLogger);
        geometricCentralities.compute();
        long[] temp = geometricCentralities.reachable;
        int reachableNodes = 0;
        for(int k = 0; k<temp.length; k++){
            if(temp[k]==10247){                             //具体的图具体对待
                reachableNodes++;
            }
        }
        double reachableRate = (double)reachableNodes/nodes;
        System.out.println("GRAPH reachable pairs:"+String.format("%.2f",reachableRate));
//
//
        /**
         * compute StronglyConnectedComponents besides buckets
         * */

        /** compute largest Component*/
        StronglyConnectedComponents stronglyConnectedComponents = StronglyConnectedComponents.compute(graph,true,progressLogger);
//        System.out.println(stronglyConnectedComponents.numberOfComponents);
        int[] size = stronglyConnectedComponents.computeSizes();
        int max=0;
        for(int i = 0; i<size.length;i++){
            if(size[i]>max)
                max = size[i];
        }
        System.out.println("GRAPH largest Component:"+max);


//        System.out.println("OK");

        /** compute buckets*/
        LongArrayBitVector longArrayBitVector = stronglyConnectedComponents.buckets;
        long numberOfSetTrue = longArrayBitVector.count();
        double bucketsRate = (double)numberOfSetTrue/nodes;
        System.out.println("GRAPH bucketsRate:"+String.format("%.4f",bucketsRate));
////        System.out.println("hello");

        /** compute bits/link */



    }
}
