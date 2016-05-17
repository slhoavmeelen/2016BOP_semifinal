import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by dyh12 on 2016/5/7.
 */
public class ExtendsRunnable implements Runnable{

    public int array;
    public long start;
    public int type;
    public int method;
    ExtendsRunnable(int a, long s, int t, int m){
        array = a;
        start = s;
        type = t;
        method = m;
    }
    @Override
    public void run() {
        ArrayList<MAGnode> myArray = new ArrayList<>();
        ArrayList<MAGnode> arrayList = new ArrayList<>();
        MAGnode mag = new MAGnode(type,start);
        arrayList.add(mag);
        myArray.addAll(NodeExtend.NodeSpread(array, arrayList));
        if (method == 0) Work.array00.addAll(myArray);
        if (method == 1) Work.array01.addAll(myArray);
        if (method == 10) Work.array10.addAll(myArray);
        if (method == 11) Work.array11.addAll(myArray);
        if (method == 20) Work.array20.addAll(myArray);
        if (method == 21) Work.array21.addAll(myArray);
        if (method == 30) Work.array30.addAll(myArray);
        if (method == 31) Work.array31.addAll(myArray);
        if (method == 32) Work.array32.addAll(myArray);
    }

}
