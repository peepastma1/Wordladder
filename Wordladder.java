//6313133 Punyawat Jaroensiripong
//6313139 Ruttiyaporn Kongtrakul
//6313174 Natthabodi Bochol
import java.util.*;
import java.io.*;
import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;

class Solution{
    ArrayList<String> Word_AL;
    SimpleWeightedGraph<String,DefaultWeightedEdge> G = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    public Solution(ArrayList<String> W_AL){
        Word_AL = new ArrayList<>(W_AL);
    }

    public void creategraph(){
        String main_word,bucket;
        int weight;
        Set<String> key_set;
        HashMap<String,ArrayList<String>> HM = new HashMap<>();
        ArrayList<String> temp_AL;
        for(int i =0;i<Word_AL.size();i++){
            main_word = Word_AL.get(i);
            for(int j=0;j<Word_AL.get(i).length();j++){
                bucket = main_word.substring(0,j)+ "_" + main_word.substring(j+1,main_word.length());
                if(HM.containsKey(bucket)){
                    HM.get(bucket).add(main_word);
                    
                }
                else if(!HM.containsKey(bucket)){
                    temp_AL = new ArrayList<>();
                    temp_AL.add(main_word);
                    HM.put(bucket,temp_AL);
                    
                }
            }
        }
        key_set = HM.keySet();
        for(String s:key_set){
            for(int i = 0;i<HM.get(s).size();i++){
                for(int j = i+1;j<HM.get(s).size();j++){
                    if(HM.get(s).get(i) != HM.get(s).get(j) ){
                        weight = find_distance(HM.get(s).get(i),HM.get(s).get(j));
                        Graphs.addEdgeWithVertices(G, HM.get(s).get(i), HM.get(s).get(j),weight);
                    }
                }
            }
        }

    }

    public int find_distance(String k1,String k2){
        char[] list_k1 = k1.toCharArray();
        char[] list_k2 = k2.toCharArray();
        int finddistance = 0;
        for(int i =0;i<k1.length();i++){
            if(list_k1[i] != list_k2[i]){
                finddistance = distance(list_k1[i], list_k2[i]);
            }
        }
        return finddistance;
    }

    public void findshortestpath(String k1, String k2){
        ShortestPathAlgorithm<String, DefaultWeightedEdge> shortestpath = null;
        List<String> shortest_List;
        List<Double> shortest_weight_list = new ArrayList<Double>();
        try{
            shortestpath = new DijkstraShortestPath<>(G);
            GraphPath<String, DefaultWeightedEdge> path = shortestpath.getPath(k1,k2);
            shortest_List = path.getVertexList();
            
            for(DefaultWeightedEdge e : path.getEdgeList()){
                shortest_weight_list.add(G.getEdgeWeight(e));
            }
            
            System.out.println("\n====================");

            for(int i = 0;i<shortest_List.size();i++){
                if(shortest_List.get(i).equals(k1)){
                    System.out.printf("%s\n",shortest_List.get(i));
                }
                else System.out.printf("%s (+%.0f)\n",shortest_List.get(i),shortest_weight_list.get(i-1));
            }
            System.out.printf("Total cost: %.0f \n", path.getWeight());
            System.out.println("====================\n");

        }
        catch (Exception e) {
            System.out.printf(">>Cannot Transform %s into %s \n",k1,k2);
        }
    }

    public int distance(char x ,char y){
        int dis1,dis2,shortest_dis;

        dis1 = x-y;
        dis2 = y-x;

        if(dis1 < 0){
            dis1 += 26;
        }
        if(dis2 < 0){
            dis2 += 26;
        }
        
        if(dis1<dis2){
            shortest_dis = dis1;
        }
        else shortest_dis = dis2;

        return shortest_dis;
    }

    public void printGraph(){
        Set<DefaultWeightedEdge> allEdges;
        allEdges = G.edgeSet();
            for(DefaultWeightedEdge e: allEdges){
                System.out.println(e.toString());
            }
    }

    
}

class Search{
    ArrayList<String> Word_AL;
    PriorityQueue<String> PQ = new PriorityQueue<>();
    public Search(ArrayList<String> W_AL){
        Word_AL = new ArrayList<>(W_AL);
    }

    public void searchword(String keyword){
        
        for(int i=0;i<Word_AL.size();i++){
            if(Word_AL.get(i).substring(0,keyword.length()).contains(keyword)){
                PQ.add(Word_AL.get(i));
            }
        }
        if(PQ.isEmpty()){
            System.out.println("Cannot find words start with : " + keyword);
        }
        while(!PQ.isEmpty()){
            System.out.println("- "+PQ.poll());
        }
        System.out.println("===========================");
    }

}

public final class Wordladder {
    public static void main(String[] args) {
        ArrayList<String> Word_AL = new ArrayList<String>();
        boolean quit = false; 
        Solution sol;
        Search sch;
        String word, word1, word2, select;
        boolean find = true;
        int word_len;
        while(find){
            try{
            Scanner input = new Scanner(System.in);
            String fileName, conti;
            System.out.println("Welcome to WordLadder Program");
            System.out.println("=============================");
            System.out.println("Enter file name: ");
            fileName = input.nextLine();
            System.out.println("Reading file . . .");
            
            Scanner file = new Scanner(new File(fileName));
            find = false;
            while(file.hasNext()){
                String line = file.nextLine();
                Word_AL.add(line.trim());
                
            }
            word_len = Word_AL.get(0).length();
            sol = new Solution(Word_AL);
            sch = new Search(Word_AL);
            sol.creategraph();
            
            while(!quit){
                System.out.println("Please Select mode(S, W, Q) when S = Search, W = Wordladder, Q = Quit :  ");
                select = input.nextLine();
                select = select.toLowerCase();
                switch(select){
                    case "s":
                        do{
                            System.out.println("===========================");
                            System.out.println("Enter your word to search : ");
                            word = input.nextLine();
                            word = word.toLowerCase();
                            if(word.length()>word_len){System.out.printf(">>Please Enter word less than or equal to %d-letters\n",word_len);}
                        }while(word.length()>word_len);
                        
                        sch.searchword(word);
                        
                        do{
                                System.out.println("Continue (y/n) = ");
                                conti = input.nextLine();
                                conti = conti.toLowerCase();
                                if(conti.compareToIgnoreCase("y")==0) quit=false;
                                else if(conti.compareToIgnoreCase("n")==0){
                                    System.out.println("=== Thank you for playing ===");
                                    input.close();
                                    quit=true;
                                }
                        }while(conti.compareToIgnoreCase("y")!=0&&conti.compareToIgnoreCase("n")!=0);
                        
                        break;

                    case "w":
                        do{
                            System.out.println("=======================================");
                            System.out.printf("Enter first word to search (%d-letters): \n",word_len);
                            word1 = input.nextLine();
                            word1 = word1.toLowerCase();
                            System.out.printf("Enter second word to search (%d-letters): \n",word_len);
                            word2 = input.nextLine();
                            word2 = word2.toLowerCase();
                            if(word1.length()!=word_len||word2.length()!=word_len){System.out.printf(">>Please Enter words equal to %d-letters\n",word_len);}
                        }while(word1.length()!=word_len||word2.length()!=word_len);
                        sol.findshortestpath(word1,word2);
                        
                        do{
                                System.out.println("Continue (y/n) = ");
                                conti = input.nextLine();
                                conti = conti.toLowerCase();
                                if(conti.compareToIgnoreCase("y")==0) quit=false;
                                else if(conti.compareToIgnoreCase("n")==0){
                                    System.out.println("=== Thank you for playing ===");
                                    input.close();
                                    quit=true;
                                }
                        }while(conti.compareToIgnoreCase("y")!=0&&conti.compareToIgnoreCase("n")!=0);
                        
                        break;

                    case "q":
                        System.out.println("=== Thank you for playing ===");
                        quit = true;
                        input.close();
                        break;
                    default:
                        System.out.println(">>Please Enter S, W or Q, try again...");
                        break;
                }

            }
            
        }catch (FileNotFoundException e){
                System.out.printf(">>Cannot find this file\n");
            }
        }

    }
}

    