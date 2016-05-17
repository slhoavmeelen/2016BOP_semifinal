import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import java.util.concurrent.ConcurrentHashMap;

public class MAGnode {
    public static ConcurrentHashMap<String,Integer> attributeToNumber;
    public static final String[] numberToAttribute={"Id","RId","C.CId","F.FId","J.JId","AA.AuId","AA.AfId"};

    public int typeOfAttribute;
    public long id;
    public MAGnode father;
    MAGnode(int arg1,long arg2) {
        typeOfAttribute=arg1;
        id=arg2;
        father=null;
    }
    MAGnode(int arg1,long arg2,MAGnode arg3) {
        typeOfAttribute=arg1;
        id=arg2;
        father=arg3;
    }

    public static void init() {
        attributeToNumber = new ConcurrentHashMap<>();
        attributeToNumber.put("Id",0);
        attributeToNumber.put("RId",1);
        attributeToNumber.put("C.CId",2);
        attributeToNumber.put("F.FId",3);
        attributeToNumber.put("J.JId",4);
        attributeToNumber.put("AA.AuId",5);
        attributeToNumber.put("AA.AfId",6);
        NodeExtend.HCCM = new PoolingHttpClientConnectionManager();
    }
}
