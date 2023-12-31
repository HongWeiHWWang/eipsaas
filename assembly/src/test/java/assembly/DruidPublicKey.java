package assembly;

import com.alibaba.druid.filter.config.ConfigTools;

public class DruidPublicKey {
    public static void main(String[] ar123gs) throws Exception {
        String p = ConfigTools.encrypt("mySql@im.root");
       System.out.println("p="+p);
    }
}
