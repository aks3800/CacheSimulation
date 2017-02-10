package cachesimulation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 * @author Akshat Sharma
 */
public class CacheSimulation {
    String location[] = new String[1000002];
    //String fileName ="E:/PECS/try.trace";
    String fileName ="E:/PECS/spice.trace";
    int len=0;
    ArrayList tag = new ArrayList();
    int lineNumStart, lineNumOffset;
    int tagNumStart, tagNumOffset;
    int hit,miss; 
    Double hitRate, missRate;
    ArrayList<Lines> setCache = new ArrayList<Lines>();
    int setTagNumStart, setTagNumOffset;
    int setSetNumStart, setSetNumOffset;
    int setHit, setMiss;
    Double setHitRate, setMissRate;
    
    
    public CacheSimulation(){
        hit=0;
        miss=0;
        lineNumStart=8;
        lineNumOffset=20;
        tagNumStart=0;
        tagNumOffset=8;
        for(int i=0;i<1048575;i++){
            tag.add("empty");
        }
        setHit = 0;
        setMiss = 0;
        
        setTagNumStart = 0;
        setTagNumOffset = 10;
        setSetNumStart = 10;
        setSetNumOffset = 18;
        for(int i=0;i<262143;i++){
            Lines l = new Lines();
            l.insert("empty");
            setCache.add(l);
        }
        /*
        setTagNumStart = 0;
        setTagNumOffset = 3;
        setSetNumStart = 3;
        setSetNumOffset = 2;
        for(int i=0;i<4;i++){
            Lines l = new Lines();
            l.insert("empty");
            setCache.add(l);
        }
        */
    }
    
    public int boolToInt(String word){
        int value=0;
        int lenghtOfWord = word.length();
        int l=lenghtOfWord-1;
        for(int i=0;i<lenghtOfWord;i++){
            value += Integer.parseInt(String.valueOf(word.charAt(l-i)))*Math.pow(2, i);
            //System.out.println("i="+i+" and char="+word.charAt(l)+" and value="+value);
        }
        return value;
    }
    
    public void readContentOfFile()throws IOException{
        try {
         File file = new File(fileName);
         Scanner scanner = new Scanner(file);
         while (scanner.hasNextLine()) {
             String str = scanner.nextLine();
//             if(str.charAt(0) == '1'){
//                 continue;
//             }
             str = str.substring(2);
             if(str.length() == 6){
                 str = "00" + str;
             }
             String bitLoc = convertToBits(str);
             location[len++] = bitLoc; 
             
             
             //location[len++] = str;
         }
         scanner.close();
       } catch (FileNotFoundException e) {
         e.printStackTrace();
       }
        for(int i=0;i<len;i++){
            System.out.println(location[i]);
        }
        //System.out.println("tag is " + location[0].substring(tagNumStart, tagNumStart+tagNumOffset));
        //System.out.println("line number is " + location[0].substring(lineNumStart, lineNumStart+lineNumOffset));
    }
    
    public String getBinaryFromHex(Character ch){
        String binary="";
        switch(ch){
            case '0':
                binary = "0000";break;
            case '1':
                binary = "0001";break;
            case '2':
                binary = "0010";break;
            case '3':
                binary = "0011";break;
            case '4':
                binary = "0100";break;
            case '5':
                binary = "0101";break;
            case '6':
                binary = "0110";break;
            case '7':
                binary = "0111";break;
            case '8':
                binary = "1000";break;
            case '9':
                binary = "1001";break;
            case 'a':
                binary = "1010";break;
            case 'b':
                binary = "1011";break;
            case 'c':
                binary = "1100";break;
            case 'd':
                binary = "1101";break;
            case 'e':
                binary = "1110";break;                
            case 'f':
                binary = "1111";break;
            default :
                System.out.println("");
        }
        return binary;
    }
    
    public String convertToBits(String word){
        String loc="";
        for(int i=0;i<word.length();i++){
            loc+=getBinaryFromHex(word.charAt(i));
        }
        return loc;
    }
    
    public void directMapping(){
        for(int i=0;i<len;i++){
            String line = location[i].substring(lineNumStart, lineNumStart+lineNumOffset);
            String tagOfLoc = location[i].substring(tagNumStart, tagNumStart+tagNumOffset);
            int lineNum = boolToInt(line);
            System.out.println("line = "+line+" tagOfLoc is "+tagOfLoc);
            System.out.println("tag = "+tag.get(lineNum));
            if(tag.get(lineNum) == "empty"){
                System.out.println("Block initialized to cache line number "+lineNum);
                tag.set(lineNum, tagOfLoc);
                miss++;
                System.out.println("Current tag = "+tag.get(lineNum));
            }
            else{
                if(tag.get(lineNum).equals(tagOfLoc)){
                    System.out.println("Hit tag = "+tag.get(lineNum));
                    hit++;
                    System.out.println("It's a hit ! Hit Count = "+ hit);
                }
                else{
                    System.out.println("Miss tag = "+tag.get(lineNum));
                    tag.set(lineNum, tagOfLoc);
                    miss++;
                    System.out.println("It's a miss. Miss Count = "+miss);
                }
            }
        }
        System.out.println("For Direct Mapping");
        System.out.println("Hits are "+hit);
        System.out.println("Misses are "+ miss);
        hitRate = (double)((double)hit/(double)(hit+miss)); 
        System.out.println("Hit Rate = " + hitRate);
        missRate = 1 - hitRate ;
        System.out.println("Miss Rate = " + missRate);
    }
    
    public void printStructure(){
        for(int i=0;i<setCache.size();i++){
            System.out.println("SetNumber : " + i);
            Lines l = setCache.get(i);
            l.printList();
        }
    }
    
    
    public void setAssociativeMapping(){
        for(int i=0;i<len;i++){
            System.out.println("\n" + (i+1) + " iteration\n");
            String set = location[i].substring(setSetNumStart, setSetNumStart+setSetNumOffset);
            String tagOfLoc = location[i].substring(setTagNumStart, setTagNumStart+setTagNumOffset);
            int setNum = boolToInt(set);
            System.out.println("\nset = "+set+" tagOfLoc is "+tagOfLoc);
            
            if(setCache.get(setNum).first.setTag == "empty"){
                System.out.println("Block initialized to Set Number "+setNum);
                Lines temp = new Lines();
                temp.insert(tagOfLoc);
                setMiss++;
                setCache.set(setNum, temp);
                System.out.println("Tags in current set :");
                temp.printList();
                //printStructure();
            }
            else{
                Lines line = setCache.get(setNum);
                Line temp = line.first;
                while(temp!=null){
                    if(temp.setTag.equals(tagOfLoc)){
                        setHit++;
                        line.moveToFirst(temp.setTag);
                        System.out.println("Hit Cache updated at set num "+setNum);
                        setCache.set(setNum,line);
                        System.out.println("It's a hit ! Hit Count = " + setHit);  
                        line.printList();
                        //printStructure();
                        break;
                    }
                    temp = temp.next;
                }
                if(temp == null){
                    setMiss++;
                    line.insert(tagOfLoc);
                    System.out.println("Miss Cache updated at set num "+setNum);
                    setCache.set(setNum, line);
                    System.out.println("It's a miss. Miss Count = " + setMiss);
                    line.printList();
                    //printStructure();
                }
            }
        }
        System.out.println("For 4 Way Set Associative Mapping");
        System.out.println("\nHits are " + setHit);
        System.out.println("Misses are " + setMiss);
        setHitRate = (double) ((double)setHitRate/(double)(setHit + setMiss));
        System.out.println("Hit Rate = " + setHitRate);
        setMissRate = 1 - setHitRate;
        System.out.println("Miss Rate = " + setMiss);
    }
    
    
    public static void main(String[] args) throws IOException {
        CacheSimulation obj = new CacheSimulation();
        obj.readContentOfFile();
        obj.directMapping();
        //System.out.println(obj.boolToInt("111111111111111111"));
        obj.setAssociativeMapping();
    }
    
}


class Line{
    public Line next;
    public String setTag;
    public Line(String sT){
        setTag = sT;
    }
    public void printLine(){
        System.out.print("(" + setTag + ")");
    }
}

class Lines{
    public Line first;
    public Lines(){
        first = null;
    }
    public boolean isEmpty(){
        return first == null;
    }
    public void insert(String sT){
        Line temp = new Line(sT);
        temp.next = null;
        if(isEmpty()){
            first = temp;
        }
        else{
            if(count() == 4){
                delete();
            }
            temp.next = first;
            first = temp;
        }
    }
    public void delete(){
        Line temp = first;
        while(temp.next.next!=null){
            temp=temp.next;
        }
        temp.next = null;
    }
    public void moveToFirst(String test){
        Line temp = first;
        if(count() != 1){
            while (temp!=null){
                if(temp.next!=null && temp.next.setTag.equals(test)){
                    Line temp1 = temp.next;
                    temp.next = temp.next.next;
                    temp1.next = first;
                    first = temp1;
                    break;
                }
                temp = temp.next;
            }
        }
        
    }
    public int count(){
        Line temp = first;
        int c = 0;
        while(temp!=null){
            c++;
            temp = temp.next;
        }
        return c;
    }
    public void printList(){
        Line temp = first;
        //System.out.println("List :");
        while(temp!=null){
            temp.printLine();
            temp=temp.next;
        }
        System.out.println("");
    }
}
/*

When a page is accessed, there can be 2 cases:
1. Page is present in the cache - If the page is already present in the cache, we move the page to the start of the list.
2. Page is not present in the cache - If the page is not present in the cache, we add the page to the list. 
How to add a page to the list:
   a. If the cache is not full, add the new page to the start of the list.
   b. If the cache is full, remove the last node of the linked list and move the new page to the start of the list.




*/