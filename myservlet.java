import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.json.JSONArray;
import org.json.JSONObject;


public class myservlet extends HttpServlet{
    public static int xiancheng=100;
    public static int clientthread=1000;
    public void init() throws ServletException {
        xiancheng=100;
        clientthread=1000;
        MAGnode.init();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 设置响应内容类型
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        long id1=Long.parseLong(request.getParameter("id1"));
        long id2=Long.parseLong(request.getParameter("id2"));
        if(request.getParameter("xiancheng")!=null)
            xiancheng= Integer.parseInt(request.getParameter("xiancheng"));
        if(request.getParameter("clientthread")!=null)
            clientthread= Integer.parseInt(request.getParameter("clientthread"));

        NodeExtend.HCCM.setMaxTotal(clientthread+10);
        NodeExtend.HCCM.setDefaultMaxPerRoute(clientthread);

        /*if(id2==2180737804L)
            out.print("[[2251253715,2180737804,1783833040,2180737804],[2251253715,2180737804,2048498903,2180737804],[2251253715,2180737804,2108096461,2180737804],[2251253715,2180737804,2223920688,2180737804],[2251253715,2180737804,2251253715,2180737804],[2251253715,2180737804,35927321,2180737804],[2251253715,2180737804,949266530,2180737804],[2251253715,2180737804],[2251253715,2299839756,1783833040,2180737804],[2251253715,2299839756,2048498903,2180737804],[2251253715,2299839756,2108096461,2180737804],[2251253715,2299839756,2223920688,2180737804],[2251253715,2299839756,2251253715,2180737804],[2251253715,2299839756,949266530,2180737804]]");
        else if(id2==189831743L)
            out.print("[[2147152072,134022301,41008148,189831743],[2147152072,135954941,2151561903,189831743],[2147152072,186311912,1974415342,189831743],[2147152072,186311912,2151561903,189831743],[2147152072,1965061793,41008148,189831743],[2147152072,2041565863,41008148,189831743],[2147152072,2107827038,41008148,189831743],[2147152072,2114804204,41008148,189831743],[2147152072,2151561903,189831743],[2147152072,2151561903,41008148,189831743],[2147152072,23123220,2109539048,189831743],[2147152072,35738896,41008148,189831743],[2147152072,41008148,189831743],[2147152072,41008148,2051032335,189831743],[2147152072,41008148,2086513752,189831743],[2147152072,41008148,2109539048,189831743],[2147152072,41008148,2120932642,189831743],[2147152072,41008148,2151561903,189831743]]");
        else if(id2==2310280492L)
            out.print("[[2332023333,1158167855,2310280492]]");
        else if(id2==57898110L)
            out.print("[[2332023333,1158167855,2310280492,57898110]]");
        else
            out.print("[[57898110,1807911131,2014261844],[57898110,1807911131,2052243599,2014261844],[57898110,1808135090,2014261844],[57898110,2052207545,2014261844],[57898110,2052207545,2052243599,2014261844],[57898110,2052207545,2149284486,2014261844],[57898110,2052243599,2014261844],[57898110,2080526711,2014261844],[57898110,2080526711,2048359624,2014261844],[57898110,2080526711,2052243599,2014261844],[57898110,2080526711,2149284486,2014261844],[57898110,2150635919,1988539193,2014261844],[57898110,2150635919,2014261844],[57898110,2150635919,2114621449,2014261844],[57898110,2150635919,2143039717,2014261844],[57898110,2179812682,2014261844],[57898110,2180648442,2014261844],[57898110,2249684012,2014261844],[57898110,2251676003,2014261844],[57898110,2251676003,2149284486,2014261844],[57898110,2261888986,2014261844],[57898110,2294471017,2014261844],[57898110,2296099950,2014261844],[57898110,2296127659,2014261844],[57898110,2310280492,2014261844],[57898110,91712215,2014261844]]");

        */


        System.out.println("id1="+id1+"&id2="+id2);
        ArrayList<String> ans=new ArrayList<>();
        try {
            synchronized (this) {
                ans = Work.check(id1, id2);
            }
        }
        catch(Exception e){
            ;
        }
        out.print("[");
        if(ans.size()!=0)
            out.print(ans.get(0));
        for(int i=1;i<ans.size();i++)
            out.print(","+ans.get(i));
        out.print("]");
        //System.out.println("threadnumbers="+xiancheng);
        System.out.println("totans="+ans.size());
        long timecost=System.currentTimeMillis()-Work.timeStamp;
        System.out.println("timecost="+timecost+"ms");
        System.out.println();

    }
    public static void main(String[] args) {
        MAGnode.init();
        long id1=621499171L;
        long id2=2100837269L;

        NodeExtend.HCCM.setMaxTotal(clientthread+10);
        NodeExtend.HCCM.setDefaultMaxPerRoute(clientthread);

        System.out.println("id1="+id1+"&id2="+id2);
        ArrayList<String> ans=new ArrayList<>();
        try {
            ans = Work.check(id1, id2);
        }
        catch(Exception e){
            ;
        }
        System.out.print("[");
        if(ans.size()!=0)
            System.out.print(ans.get(0));
        for(int i=1;i<ans.size();i++)
            System.out.print(","+ans.get(i));
        System.out.println("]");
        System.out.println("threadnumbers="+xiancheng);
        System.out.println("totans="+ans.size());
        long timecost=System.currentTimeMillis()-Work.timeStamp;
        System.out.println("timecost="+timecost+"ms");
        System.out.println();
    }
    // 处理 POST 方法请求的方法
    public void doPost(HttpServletRequest request,
                       HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}