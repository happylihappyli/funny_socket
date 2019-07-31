package funnyai;

import com.funnyai.Time.Old.S_Time;
import com.funnyai.io.Old.C_Property_File;
import com.funnyai.net.Old.S_Net;
import com.funnyai.string.Old.S_Strings;
import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.listener.ConnectListener;
import com.funnyai.common.AI_Var2;
import com.funnyai.common.S_Debug;
import com.funnyai.common.S_Save;
import java.io.FileNotFoundException;
import static java.lang.System.out;
import java.util.Collection;
import java.util.TreeMap;

public class JavaMain {

    public static TreeMap pTreap=new TreeMap();
    public static String XPath="";
    
    public static void main(String[] args) throws InterruptedException {
        
        String strFile=args[0];
        try {
            C_Property_File pFile=new C_Property_File(strFile);

            String strIP = pFile.Read("ip");//这个是外网IP
            int iPort = S_Strings.getIntFromStr(pFile.Read("port"),9999);

            AI_Var2.Site=pFile.Read("site");
            AI_Var2.local_ip=S_Net.get_local_ip();//这个可能是内网IP
            JavaMain.XPath = "Server_Chat/LastTime/"+strIP;

            Configuration config = new Configuration();
            config.setHostname(strIP);//"localhost");
            config.setPort(iPort);

            final SocketIOServer server = new SocketIOServer(config);

            addConnectListener(server);
            
            server.addEventListener("chat_event", ChatObject.class,
                    (SocketIOClient client,
                    ChatObject data, AckRequest ackRequest) -> {
                out.println("from:"+data.getFrom());
                out.println("message:"+data.getMessage());
                out.println("to:"+data.getTo());
                try{
                    out.println("msg_id:"+data.getMsg());
                    out.println("user:"+data.getUser());
                }catch(Exception ex){
                    
                }
                Send_Msg(server,client.getSessionId().toString(), data,"chat_event");
            });
            
            server.addEventListener("sys_event", ChatObject.class, (SocketIOClient client, ChatObject data, AckRequest ackRequest) -> {
                out.println("from:"+data.getFrom());
                out.println("message:"+data.getMessage());
                out.println("to:"+data.getTo());
                try{
                    out.println("msg_id:"+data.getMsg());
                    out.println("user:"+data.getUser());
                }catch(Exception ex){
                    
                }
                switch (data.getTo()){
                    case "*_session":
                    case ":_session":
                        String UID=client.getSessionId().toString();
                        String Name=data.getFrom();
                        if (UID.equals(data.getMessage())){
                            pTreap.containsKey(Name);
                            if (pTreap.get(Name)!=null){
                                pTreap.remove(Name);
                            }
                            pTreap.put(Name, new C_User(data.getFrom(),UID));
                        }
                        break;
                    default:
                        Send_Msg(server,client.getSessionId().toString(),data,"sys_event");
                        break;
                }
            });
            
            server.start();

            while(true){
                Update_DB_Time();
                
                Collection<SocketIOClient> pCollect=server.getAllClients();
                
                Object[] clients = pCollect.toArray();
                for(int i = 0; i < clients.length; i++){
                    SocketIOClient pClient=(SocketIOClient) clients[i];
                    
                    ChatObject pData=new ChatObject();
                    pData.setFrom("system");
                    pData.setTo("30s:session");
                    String Session_ID = pClient.getSessionId().toString();
                    pData.setMessage(Session_ID);
                    pClient.sendEvent("sys_event", pData);
                }
                S_Debug.Write_DebugLog("chat","system 30s");
                Thread.sleep(1000*30);
            }

        }catch(FileNotFoundException | InterruptedException ex){

        }
    }
    
    
    public static void addConnectListener(SocketIOServer server) {
        server.addConnectListener(new ConnectListener() {

            public void onConnect(SocketIOClient client) {
                //logger.info(client.getRemoteAddress() + " web客户端接入");
                out.println(client.getRemoteAddress() + " web客户端接入");
                
                ChatObject pData=new ChatObject();
                pData.setFrom("system");
                pData.setTo("30s:session");
                String Session_ID = client.getSessionId().toString();
                pData.setMessage(Session_ID);
                client.sendEvent("sys_event", pData);
            }
        });
    }

    public static void Send_Msg(
            SocketIOServer server,
            String sender_session_id,
            ChatObject data,
            String strEvent){
        
        
        String strTo=data.getTo();
        if (strTo.equals("*")){
            out.println("send to *");
            //server.getBroadcastOperations().sendEvent(strEvent, data);
            
            out.println("not found,send to all except me");

            Collection<SocketIOClient> pCollect=server.getAllClients();
            Object[] clients = pCollect.toArray();
            for(int i = 0; i < clients.length; i++){
                SocketIOClient pClient=(SocketIOClient) clients[i];
                String Session_ID = pClient.getSessionId().toString();
                if (!Session_ID.equals(sender_session_id)){
                    pClient.sendEvent(strEvent, data);
                }
            }
        }else{
            if (pTreap.containsKey(strTo)){
                out.println("found,send to "+strTo);
                C_User pUser=(C_User) pTreap.get(strTo);
                Collection<SocketIOClient> pCollect=server.getAllClients();

                Object[] clients = pCollect.toArray();
                for(int i = 0; i < clients.length; i++){
                    SocketIOClient pClient=(SocketIOClient) clients[i];
                    String Session_ID = pClient.getSessionId().toString();
                    if (pUser.Session_ID.equals(Session_ID)){
                        pClient.sendEvent(strEvent, data);
                    }
                }
            }else{
                out.println("not found,send to all except me");
                
                Collection<SocketIOClient> pCollect=server.getAllClients();
                Object[] clients = pCollect.toArray();
                for(int i = 0; i < clients.length; i++){
                    SocketIOClient pClient=(SocketIOClient) clients[i];
                    String Session_ID = pClient.getSessionId().toString();
                    if (!Session_ID.equals(sender_session_id)){
                        pClient.sendEvent(strEvent, data);
                    }
                }
                //server.getBroadcastOperations().sendEvent(strEvent, data);
            }
        }
    }
    
    
    public static void Update_DB_Time(){
        //funnywatch 用，要测试一下
        S_Save.Save_Value(JavaMain.XPath,S_Time.now_YMD_Hms());
        
    }
}
