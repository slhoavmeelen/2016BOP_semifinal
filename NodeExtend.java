import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.*;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

class TooManyException extends Exception {
    public TooManyException() {
        super();
    }
}

public class NodeExtend{

    public static final String[] attrString={"AA.AuId,C.CId,F.FId,J.JId","RId","Id","Id","AA.AuId,AA.AfId","Id","RId,AA.AuId,C.CId,F.FId,J.JId","Id,RId,AA.AuId,C.CId,F.FId,J.JId","Id,AA.AuId,C.CId,F.FId,J.JId"};
    public static final String uriString="https://oxfordhk.azure-api.net/academic/v1.0/evaluate";
    public static final String subs1="subscription-key",subs2="f7cc29509a8443c5b3a5e56b0e38b5a6";
    public static PoolingHttpClientConnectionManager HCCM = null;

    public synchronized static CloseableHttpClient getHttpClient(){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(HCCM).build();
        return httpClient;
    }

    public static void main(String[] args) {
        MAGnode.init();
        ArrayList<MAGnode> mytest=new ArrayList<>(),anstest0,anstest1,anstest2;
        MAGnode mynode=new MAGnode(0,2292217923L);
        mytest.add(mynode);

        long timeStamp1 = System.currentTimeMillis();
        int cnt=0;

        while(cnt<=100) {
            System.out.println(cnt);
            try {
                anstest0 = NodeSpread(0, mytest);
                anstest0 = TwoNodeSpread(anstest0,mynode);
                anstest1 = NodeSpread(2, mytest);
                anstest2 = NodeSpread(3, mytest);
                for(int i=0;i<anstest2.size();i++) {
                    anstest1.add(anstest2.get(i));
                }
                mytest = anstest1;
            } catch (Exception e) {
                System.out.println(e);
            }

        }
        long timeStamp2 = System.currentTimeMillis();
        System.out.println(timeStamp2-timeStamp1);
    }

    public static ArrayList<MAGnode> TwoNodeSpread(ArrayList<MAGnode> toSpread, MAGnode endPoint) {
        ExecutorService myExecutor = Executors.newFixedThreadPool(myservlet.xiancheng);
        ConcurrentLinkedQueue<MAGnode> ans=new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<MAGnode> retrylist=new ConcurrentLinkedQueue<>();
        ArrayList<MAGnode> ansarray=new ArrayList<>();

        //FirstTry
        for(int i=0;i<toSpread.size();i++) {
            myExecutor.execute(new SendHTTPRequest(toSpread.get(i),endPoint.id,ans,retrylist));
        }
        myExecutor.shutdown();
        try {
            myExecutor.awaitTermination(20, TimeUnit.SECONDS);
        }catch(Exception e){}
        if(!myExecutor.isShutdown()) {
            System.out.println("shutdownnow");
            myExecutor.shutdownNow();
        }


        //Retry
        myExecutor=Executors.newFixedThreadPool(myservlet.xiancheng);
        while(!retrylist.isEmpty()) {
            System.out.println("RETRYING");
            MAGnode temp=retrylist.poll();
            for(int offset=0;offset<=255000;offset+=5000) {
                myExecutor.execute(new SendHTTPRequest(offset,temp,endPoint.id,ans,retrylist));
            }
        }
        myExecutor.shutdown();
        try {
            myExecutor.awaitTermination(20, TimeUnit.SECONDS);
        }catch(Exception e){}
        if(!myExecutor.isShutdown()) {
            System.out.println("shutdownnow");
            myExecutor.shutdownNow();
        }


        while(!ans.isEmpty()) {
            ansarray.add(ans.poll());
        }

  /*      System.out.println("TwoNodeSpread : endpoint= "+endPoint.id);
        for(int i=0;i<toSpread.size();i++)
            System.out.print(toSpread.get(i).id+" ");
        System.out.println(" To: ");
        for(int i=0;i<ansarray.size();i++)
            System.out.print(ansarray.get(i).id+" ");*/

        return ansarray;
    }

    public static ArrayList<MAGnode> NodeSpread(int runningState,ArrayList<MAGnode> toSpread) {
        ExecutorService myExecutor = Executors.newFixedThreadPool(myservlet.xiancheng);
        ConcurrentLinkedQueue<MAGnode> ans=new ConcurrentLinkedQueue<>();
        ConcurrentLinkedQueue<MAGnode> retrylist=new ConcurrentLinkedQueue<>();
        ArrayList<MAGnode> ansarray=new ArrayList<>();

        //FirstTry
        for(int i=0;i<toSpread.size();i++) {
            if(runningState==2) {
                myExecutor.execute(new SendHTTPRequest(0, 2, toSpread.get(i), ans, retrylist));
                myExecutor.execute(new SendHTTPRequest(5000, 2, toSpread.get(i), ans, retrylist));
            }
                else
                myExecutor.execute(new SendHTTPRequest(runningState,toSpread.get(i),ans,retrylist));
        }
        myExecutor.shutdown();
        try {
            myExecutor.awaitTermination(20, TimeUnit.SECONDS);
        }catch(Exception e){}
        if(!myExecutor.isShutdown()) {
            System.out.println("shutdownnow");
            myExecutor.shutdownNow();
        }



        myExecutor=Executors.newFixedThreadPool(myservlet.xiancheng);
        if(runningState==2 && ans.size()==10000)
            for(int offset=10000;offset<=255000;offset+=5000)
                myExecutor.execute(new SendHTTPRequest(offset,2,toSpread.get(0),ans,retrylist));

        //Retry
        while(!retrylist.isEmpty()) {
            System.out.println("RETRYING");
            myExecutor.execute(new SendHTTPRequest(-2,runningState,retrylist.poll(),ans,retrylist));
        }

        myExecutor.shutdown();
        try {
            myExecutor.awaitTermination(20, TimeUnit.SECONDS);
        }catch(Exception e){}
        if(!myExecutor.isShutdown()) {
            System.out.println("shutdownnow");
            myExecutor.shutdownNow();
        }

        while(!ans.isEmpty()) {
            ansarray.add(ans.poll());
        }

        /*System.out.println("OneNodeSpread Running state= " +runningState+" From: ");
        for(int i=0;i<toSpread.size();i++)
            System.out.print(toSpread.get(i).id+" ");
        System.out.println(" To: ");
        for(int i=0;i<ansarray.size();i++)
            System.out.print(ansarray.get(i).id+" ");*/

        return ansarray;
    }


    public static class SendHTTPRequest implements Runnable{

        public MAGnode startNode;
        public MAGnode idNode;
        public int runningState;
        public long specialRunningId;
        public int offset;
        public ConcurrentLinkedQueue<MAGnode> ans;
        public ConcurrentLinkedQueue<MAGnode> retrylist;
        public char ch;
        public BufferedReader in;
        public StringBuffer buf;
        public int charAtIndex=-1;
        StringBuffer exprBuilder;


        SendHTTPRequest(int arg1,MAGnode arg2,ConcurrentLinkedQueue<MAGnode> ansarg,ConcurrentLinkedQueue<MAGnode> retarg) {
            offset=-1;
            runningState=arg1;
            startNode=arg2;
            ans=ansarg;
            retrylist=retarg;
        }
        SendHTTPRequest(int argoffset,int arg1,MAGnode arg2,ConcurrentLinkedQueue<MAGnode> ansarg,ConcurrentLinkedQueue<MAGnode> retarg) {
            offset=argoffset;
            runningState=arg1;
            startNode=arg2;
            ans=ansarg;
            retrylist=retarg;
        }
        SendHTTPRequest(MAGnode arg2,long arg3,ConcurrentLinkedQueue<MAGnode> ansarg,ConcurrentLinkedQueue<MAGnode> retarg) {
            offset=-1;
            runningState=5;
            startNode=arg2;
            specialRunningId=arg3;
            ans=ansarg;
            retrylist=retarg;
        }
        SendHTTPRequest(int argoffset,MAGnode arg2,long arg3,ConcurrentLinkedQueue<MAGnode> ansarg,ConcurrentLinkedQueue<MAGnode> retarg) {
            offset=argoffset;
            runningState=5;
            startNode=arg2;
            specialRunningId=arg3;
            ans=ansarg;
            retrylist=retarg;
        }

        public char nextChar() throws IOException{

            ch=buf.charAt(++charAtIndex);
            if(ch>='0' && ch<='9') return ch;
            if(ch=='n') {
                ch=buf.charAt(++charAtIndex);
                if(ch=='d') {
                    ch=buf.charAt(++charAtIndex);
                    if(ch>='0' && ch<='9') return ch;
                    while(ch!=' ' && ch!='\n' && ch!='\r' && ch!=':' && ch!='\"' && ch!=',' && ch!='[' && ch!=']' && ch!='{' && ch!='}')
                        ch=buf.charAt(++charAtIndex);
                    while(ch==' ' || ch=='\n' || ch=='\r' || ch==':' || ch=='\"' || ch==',')
                        ch=buf.charAt(++charAtIndex);
                    ch=buf.charAt(++charAtIndex);
                    if(ch>='0' && ch<='9') return ch;
                    while(ch!=' ' && ch!='\n' && ch!='\r' && ch!=':' && ch!='\"' && ch!=',' && ch!='[' && ch!=']' && ch!='{' && ch!='}')
                        ch=buf.charAt(++charAtIndex);
                    while(ch==' ' || ch=='\n' || ch=='\r' || ch==':' || ch=='\"' || ch==',')
                        ch=buf.charAt(++charAtIndex);
                    return ch;
                }
            }
            while(ch!=' ' && ch!='\n' && ch!='\r' && ch!=':' && ch!='\"' && ch!=',' && ch!='[' && ch!=']' && ch!='{' && ch!='}')
                ch=buf.charAt(++charAtIndex);
            while(ch==' ' || ch=='\n' || ch=='\r' || ch==':' || ch=='\"' || ch==',')
                ch=buf.charAt(++charAtIndex);
            return ch;
        }
        public long getLong() throws IOException{
            long tmp=0;
            while(ch>='0' && ch<='9') {
                tmp = tmp * 10 + (long) ch - (long) '0';
                ch=buf.charAt(++charAtIndex);
            }
            charAtIndex--;
            return tmp;
        }

        public void getlogprob() throws  IOException {
            while(ch=='-' || ch=='.' || (ch>='0' && ch<='9') ) {
                ch = buf.charAt(++charAtIndex);
            }
            charAtIndex--;
        }
        public void run() {

            MAGnode realStartNode=startNode;
            long timeStamp1 = System.currentTimeMillis();
            //long timeStampBeforeNetwork = 0, timeStampAfterNetwork = 0,t1=0;
            //long timeStamp3=0;
            if(timeStamp1-Work.timeStamp>=60000) {
                System.out.println("Timeout!");
                return;
            }

            /*if (runningState == 5) {
                System.out.println("5In!!");
            }*/

            exprBuilder = new StringBuffer(80);
            if (runningState == 5)
                exprBuilder.append("And(");
            if (startNode.typeOfAttribute > 1)
                exprBuilder.append("Composite(");
            if (runningState == 2)
                exprBuilder.append("RId");
            else
                exprBuilder.append(MAGnode.numberToAttribute[startNode.typeOfAttribute]);
            exprBuilder.append('=');
            exprBuilder.append(startNode.id);
            if (startNode.typeOfAttribute > 1)
                exprBuilder.append(')');
            if (runningState == 5) {
                exprBuilder.append(",RId=");
                exprBuilder.append(specialRunningId);
                exprBuilder.append(")");
            }

            CloseableHttpClient httpclient = getHttpClient();
            //long timehttpclient=System.currentTimeMillis()-timeStamp1;
            //System.out.println("httpclientcreation cost "+timehttpclient+"ms");
            try {

                URIBuilder builder = new URIBuilder(uriString);
                builder.setParameter("expr", exprBuilder.toString());
                builder.setParameter("model", "latest");
                builder.setParameter("attributes", attrString[runningState]);
                if (offset < 0) {
                    builder.setParameter("count", "300000");
                    builder.setParameter("offset", "0");
                } else {
                    builder.setParameter("count", "5000");
                    builder.setParameter("offset", String.valueOf(offset));
                }
                builder.setParameter(subs1, subs2);
                URI uri = builder.build();
                //System.out.println("expr=" + exprBuilder.toString() + "attr=" + attrString[runningState]);

                //timeStampBeforeNetwork = System.currentTimeMillis();

                HttpGet request = new HttpGet(uri);
                CloseableHttpResponse response = httpclient.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream instream = entity.getContent();

                //timeStampAfterNetwork = System.currentTimeMillis();
                //long netcost=timeStampAfterNetwork-timeStampBeforeNetwork;
                //System.out.println("network cost"+netcost+"ms");
                in = new BufferedReader(new InputStreamReader(instream));
                buf=new StringBuffer();
                String str;
                while((str=in.readLine())!=null && str.length()!=0)
                    buf.append(str);
                //System.out.println(buf.toString());

                //t1=System.currentTimeMillis();
                //long ts=t1-timeStampAfterNetwork;
                //System.out.println("tostring "+ts+"ms ");
                nextChar();
                while (nextChar()!='}') {
                    char tmpch1 = buf.charAt(charAtIndex+1);
                    nextChar();
                    if (tmpch1 == 'r') {
                        System.out.println("error returned");
                        if (offset == -1) retrylist.offer(startNode);
                        throw new TooManyException();
                    } else if (tmpch1 == 'b') {
                        System.out.println("aborted!");
                        if (offset == -1) retrylist.offer(startNode);
                        throw new TooManyException();
                    } else if(tmpch1=='n'){
                        while (nextChar()!=']') {
                            while (nextChar() != '}') {
                                char etc1 = ch, etc2 =buf.charAt(charAtIndex+1);
                                nextChar();
                                if (etc1 == 'I' && etc2 == 'd') {
                                    if (runningState >= 7) {
                                        idNode = new MAGnode(0, getLong(), realStartNode);
                                        startNode = idNode;
                                    } else {
                                        MAGnode newNode = new MAGnode(0, getLong(), startNode);
                                        ans.offer(newNode);
                                    }
                                } else if(etc1=='l' && etc2=='o') {
                                    getlogprob();
                                }
                                else if (etc1 == 'R') {
                                    while (nextChar() != ']') {
                                        MAGnode newNode = new MAGnode(0, getLong(), startNode);
                                        ans.offer(newNode);
                                    }
                                } else if (etc1 == 'F') {
                                    while (nextChar() != ']')
                                        while (nextChar() != '}') {
                                            nextChar();
                                            MAGnode newNode = new MAGnode(3, getLong(), startNode);
                                            ans.offer(newNode);
                                        }
                                } else if (etc1 == 'C') {
                                    while (nextChar() != '}') {
                                        nextChar();
                                        MAGnode newNode = new MAGnode(2, getLong(), startNode);
                                        ans.offer(newNode);
                                    }
                                } else if (etc1 == 'J') {
                                    while (nextChar() != '}') {
                                        nextChar();
                                        MAGnode newNode = new MAGnode(4, getLong(), startNode);
                                        ans.offer(newNode);
                                    }
                                }
                                else if (etc1 == 'A') {
                                    if (runningState != 4) {
                                        while (nextChar() != ']')
                                            while (nextChar() != '}') {
                                                nextChar();
                                                MAGnode newNode = new MAGnode(5, getLong(), startNode);
                                                ans.offer(newNode);
                                            }
                                    }
                                    else {
                                        while (nextChar() != ']') {
                                            long AuId = 0, AfId = 0;
                                            while (nextChar() != '}') {
                                                char atc = buf.charAt(charAtIndex+1);
                                                nextChar();
                                                if (atc == 'u') AuId = getLong();
                                                else AfId = getLong();
                                            }
                                            if (AuId != startNode.id || AfId == 0) continue;
                                            MAGnode newNode = new MAGnode(6, AfId, startNode);
                                            ans.offer(newNode);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                in.close();
                instream.close();
                //EntityUtils.consume(response.getEntity());
                //response.close();
                //HCCM.closeExpiredConnections();
                //request.abort();
                //httpclient.close();
            } catch (Exception e) {
                System.out.println("error!" + e.getMessage());
            }
            //System.out.println("Find! "+ans.size());

           // long timeStamp2 = System.currentTimeMillis();
           // long timeCost = timeStamp2 - timeStamp1;
          //  long timeCostNetwork = timeStampAfterNetwork - timeStampBeforeNetwork;
           // long timeForJson= timeStamp2-t1;
           // System.out.println("timeforJson"+timeForJson+"ms ");
           // System.out.println("Find! "+ans.size() + "it costs " + timeCost + "ms with server costing " + timeCostNetwork + "ms");
        }
    }
}
