/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funnyai;

import io.socket.client.IO;
import io.socket.client.Socket;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import static java.lang.System.out;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONObject;
/**
 *
 * @author happyli
 */
public class JavaMain {
    
    public static void main(String[] args) throws URISyntaxException, IOException{
        Socket socket = IO.socket("http://robot3.funnyai.com:9998");//robot3.funnyai.com
        Frame f=new Frame("Main");  
        f.setSize(500,400);  
        f.setLocation(300,200);  
        f.setLayout(new BorderLayout());  
        
        
        TextArea textarea=new TextArea("",8,50,TextArea.SCROLLBARS_VERTICAL_ONLY); 
        f.add(textarea,BorderLayout.NORTH);  
        
        TextField text1=new TextField(); 
        f.add(text1,BorderLayout.CENTER);  
        
        
        TextField text2=new TextField(); 
        f.add(text2,BorderLayout.SOUTH);
        text2.setText("your name");
        
        Button button_send=new Button("Send");  
        button_send.addActionListener(new ActionListener() {  
               
             @Override  
             public void actionPerformed(ActionEvent e) {  

                    JSONObject obj = new JSONObject();
                    obj.put("from", text2.getText());
                    obj.put("to", "*");
                    String test_str=StringEscapeUtils.escapeHtml(text1.getText());
                    obj.put("message",test_str);
                    obj.put("user","0");
                    obj.put("msg","0");
                    socket.emit("chatevent", obj);
                    text1.setText("");
             }  
         }); 

        f.add(button_send,BorderLayout.EAST);  

        f.addWindowListener(new MyWin());  

        f.setVisible(true);  
        System.out.println("Hello world!");
        
        
 
        socket.on(Socket.EVENT_CONNECT, (Object... args1) -> {
            JSONObject obj = new JSONObject();
            obj.put("from", "server");
            obj.put("to", "*");
            String test_str=StringEscapeUtils.escapeHtml("FunnySocket启动了");
            obj.put("message",test_str);
            obj.put("user","0");
            obj.put("msg","0");
            socket.emit("chatevent", obj);
        });
        socket.on("chatevent", (Object... args1) -> {
            for(int i=0;i<args1.length;i++){
                JSONObject obj = (JSONObject) args1[i];
                String strMsg=obj.getString("message");
                String strFrom=obj.getString("from");
                String strTo=obj.getString("to");
                
                strMsg=StringEscapeUtils.unescapeHtml(strMsg);
                out.println(obj.toString());
                if (strFrom.equals("system")==false){
                    textarea.append(strFrom+"->"+strTo+":\n");
                    textarea.append(strMsg+"\n");
                    if (f.isActive()==false){
                        RemindWindow remindWin = new RemindWindow();
                        remindWin.Show(strFrom+"->"+strTo,strMsg);
                    }
                }
            }
        });
        socket.on(Socket.EVENT_DISCONNECT, (Object... args1) -> {
            socket.connect();
        });
        
        socket.connect();
        while(true){
            try {
                Thread.sleep(10*1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(JavaMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
