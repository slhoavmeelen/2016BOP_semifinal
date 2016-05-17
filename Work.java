import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.Arrays;

/**
 * Created by dyh12 on 2016/5/7.
 */

public class Work {
    public static long start;
    public static long end;
    public static int stype;
    public static int etype;
    public static ArrayList<MAGnode> array00;
    public static ArrayList<MAGnode> array01;
    public static ArrayList<MAGnode> array10;
    public static ArrayList<MAGnode> array11;
    public static ArrayList<MAGnode> array20;
    public static ArrayList<MAGnode> array21;
    public static ArrayList<MAGnode> array30;
    public static ArrayList<MAGnode> array31;
    public static ArrayList<MAGnode> array32;
    public static ArrayList<MAGnode> array0;
    public static ArrayList<MAGnode> array1;
    public static ArrayList<MAGnode> array2;
    public static ArrayList<MAGnode> array3;
    public static ArrayList<String> answer;
    public static long timeStamp=0;
    public static ArrayList<String> check(long l, long r) throws InterruptedException {
        array00 = new ArrayList<>();
        array01 = new ArrayList<>();
        array10 = new ArrayList<>();
        array11 = new ArrayList<>();
        array20 = new ArrayList<>();
        array21 = new ArrayList<>();
        array30 = new ArrayList<>();
        array31 = new ArrayList<>();
        array32 = new ArrayList<>();
        answer = new ArrayList<>();
        /*flag=0;*/
        timeStamp=System.currentTimeMillis();

        start = l;
        end = r;
        ExecutorService myExecutor = Executors.newFixedThreadPool(9);
        ExtendsRunnable[] run = new ExtendsRunnable[9];
        run[0] = new ExtendsRunnable(0, start, 0, 0);
        run[1] = new ExtendsRunnable(1, start, 0, 1);
        run[2] = new ExtendsRunnable(7, start, 5, 10);
        run[3] = new ExtendsRunnable(4, start, 5, 11);
        run[4] = new ExtendsRunnable(0, end, 0, 20);
        run[5] = new ExtendsRunnable(2, end, 0, 21);
        run[6] = new ExtendsRunnable(3, end, 5, 30);
        run[7] = new ExtendsRunnable(4, end, 5, 31);
        run[8] = new ExtendsRunnable(8, end, 5, 32);
        for (int i = 0; i < 9; i++){
            myExecutor.execute(run[i]);
        }
        myExecutor.shutdown();
        myExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        array0 = new ArrayList<>();array0.addAll(array00);array0.addAll(array01);
        array1 = new ArrayList<>();array1.addAll(array11);
        array2 = new ArrayList<>();array2.addAll(array20);array2.addAll(array21);
        array3 = new ArrayList<>();array3.addAll(array30);array3.addAll(array31);

        if (array1.size() == 0 && array10.size() == 0 && array3.size() == 0 && array32.size() == 0) method0();
        else if ((array1.size() != 0 || array10.size() != 0) && array3.size() == 0 && array32.size() == 0) method1();
        else if (array1.size() == 0 && array10.size() == 0 && (array3.size() != 0 || array32.size() != 0)) method2();
        else method3();
        deal();
        return answer;
    }

    public static void deal(){
        String[] deal = new String[answer.size()];
        for (int i = 0; i < answer.size(); i++){
            deal[i] = answer.get(i);
        }
        Arrays.sort(deal);
        answer = new ArrayList<>();
        answer.add(deal[0]);
        for (int i = 1; i < deal.length; i++){
            if (!(deal[i].equals(answer.get(answer.size()-1)))){
                answer.add(deal[i]);
            }
        }
    }

    public static void method3(){
        stype = 5;
        etype = 5;
        HashMap<String , MAGnode> hashMap = new HashMap<>();
        for (int i = 0; i < array3.size(); i++){
            MAGnode mag = array3.get(i);
            hashMap.put(mag.id+" "+mag.typeOfAttribute,mag.father);
        }
        CompareArray(hashMap, array10, 2);
        for (int i = 0; i < array10.size(); i++){
            MAGnode mag = array10.get(i);
            if (mag.typeOfAttribute == 5 && mag.id == end){
                answer.add("[" + start + "," + mag.father.id + "," + end + "]");
            }
        }
        CompareArray(hashMap, array1, 0);
    }

    public static void method2() throws InterruptedException{
        stype = 0;
        etype = 5;
        HashMap<String , MAGnode> hashMap = new HashMap<>();
        ArrayList<MAGnode> arrayListID = new ArrayList<>();
        ArrayList<MAGnode> arrayListAU = new ArrayList<>();
        ArrayList<MAGnode> secondID = new ArrayList<>();
        ArrayList<MAGnode> secondAF = new ArrayList<>();
        for (int i = 0; i < array3.size(); i++){
            MAGnode mag = array3.get(i);
            hashMap.put(mag.id+" "+mag.typeOfAttribute,mag.father);
            if (mag.typeOfAttribute == 0 && mag.id == start){
                answer.add("[" + start + "," + end + "]");
            }
        }
        for (int i = 0; i < array0.size(); i++){
            MAGnode mag = array0.get(i);
            if (mag.typeOfAttribute == 0) arrayListID.add(mag);
            if (mag.typeOfAttribute == 5) arrayListAU.add(mag);
        }
        CompareArray(hashMap, array0, 0);

        ExecutorService myExecutor = Executors.newFixedThreadPool(2);
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                secondID.addAll(NodeExtend.NodeSpread(1, arrayListID));
            }
        });
        myExecutor.execute(new Runnable() {
            @Override
            public void run() {
                secondAF.addAll(NodeExtend.NodeSpread(4, arrayListAU));
            }
        });
        myExecutor.shutdown();
        myExecutor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        CompareArray(hashMap, secondID, 1);
        CompareArray(hashMap, secondAF, 1);
        hashMap = new HashMap<>();
        for (int i = 0; i < array0.size(); i++){
            MAGnode mag = array0.get(i);
            if (mag.typeOfAttribute != 0){
                hashMap.put(mag.id+" "+mag.typeOfAttribute,mag.father);
            }
        }
        CompareArray(hashMap, array32, 3);
    }

    public static void method1(){
        stype = 5;
        etype = 0;
        HashMap<String , MAGnode> hashMap = new HashMap<>();
        ArrayList<MAGnode> secondAF = new ArrayList<>();
        ArrayList<MAGnode> arrayListAU = new ArrayList<>();
        for (int i = 0; i < array2.size(); i++){
            MAGnode mag = array2.get(i);
            if (mag.typeOfAttribute == 5) arrayListAU.add(mag);
            if (mag.typeOfAttribute == 5 && mag.id == start){
                answer.add("[" + start + "," + end + "]");
            }
        }
        secondAF.addAll(NodeExtend.NodeSpread(4, arrayListAU));
        for (int i = 0; i < array1.size(); i++){
            MAGnode mag = array1.get(i);
            hashMap.put(mag.id+" "+mag.typeOfAttribute,mag.father);
        }
        CompareArray(hashMap, secondAF, 1);
        hashMap = new HashMap<>();
        for (int i = 0; i < array2.size(); i++){
            MAGnode mag = array2.get(i);
            hashMap.put(mag.id+" "+mag.typeOfAttribute,mag.father);
        }
        CompareArray(hashMap, array10, 2);
        for (int i = 0; i < array10.size(); i++){
            MAGnode mag = array10.get(i);
            if (mag.typeOfAttribute == 0 && mag.id == end){
                answer.add("[" + start + "," + mag.father.id + "," + end + "]");
            }
        }
    }

    public static void method0() {
        stype = 0;
        etype = 0;
        HashMap<String, MAGnode> hashMap = new HashMap<>();
        ArrayList<MAGnode> secondIA = new ArrayList<>();
        ArrayList<MAGnode> arrayListID = new ArrayList<>();
        ArrayList<MAGnode> arrayListAC = new ArrayList<>();
        for (int i = 0; i < array2.size(); i++) {
            MAGnode mag = array2.get(i);
            hashMap.put(mag.id + " " + mag.typeOfAttribute, mag.father);
        }
        CompareArray(hashMap, array0, 0);
        for (int i = 0; i < array0.size(); i++) {
            MAGnode mag = array0.get(i);
            if (mag.typeOfAttribute == 0) arrayListID.add(mag);
            if (mag.typeOfAttribute != 0) arrayListAC.add(mag);
            if (mag.typeOfAttribute == 0 && mag.id == end) {
                answer.add("[" + start + "," + end + "]");
            }
        }
        secondIA.addAll(NodeExtend.NodeSpread(6, arrayListID));
        CompareArray(hashMap, secondIA, 1);
        print2(NodeExtend.TwoNodeSpread(arrayListAC, new MAGnode(0, end)));
    }

    public static void CompareArray(HashMap<String , MAGnode> hashMap, ArrayList<MAGnode> array, int k){
        for (int i = 0; i < array.size(); i++) {
            if (hashMap.containsKey(array.get(i).id + " " + array.get(i).typeOfAttribute)) {
                print3(array.get(i),k);
            }
        }
    }


    public static void print2(ArrayList<MAGnode> arrayList){
        for (int i = 0; i < arrayList.size(); i++){
            String str = "[" + start + "," + arrayList.get(i).father.id + "," + arrayList.get(i).id + "," + end + "]";
            answer.add(str);
        }
    }

    public static void print3(MAGnode mag, int k){
        String str = "";
        if (mag.father.id == start && mag.father.typeOfAttribute == stype && k == 0){
            str =  "[" + start + "," + mag.id + "," + end + "]";
        }
        else if (mag.father.id == end && mag.father.typeOfAttribute == etype && k == 0){
            str =  "[" + start + "," + mag.id + "," + end + "]";
        }
        else if (mag.father.father.id == start && mag.father.father.typeOfAttribute == stype && k == 1){
            str = "[" + start + "," + mag.father.id + "," + mag.id + "," + end + "]";
        }
        else if (mag.father.father.id == end && mag.father.father.typeOfAttribute == etype && k == 1){
            str = "[" + start + "," + mag.id + "," + mag.father.id + "," + end + "]";
        }
        else if (k == 2){
            str = "[" + start + "," + mag.father.id + "," + mag.id + "," + end + "]";
        }
        else if (k == 3){
            str = "[" + start + "," + mag.id + "," + mag.father.id + "," + end + "]";
        }
        if (!(str.equals(""))) answer.add(str);
    }
}
