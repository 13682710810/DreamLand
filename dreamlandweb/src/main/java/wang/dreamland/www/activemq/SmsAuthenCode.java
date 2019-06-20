package wang.dreamland.www.activemq;

import org.springframework.stereotype.Component;

import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

@Component
public class SmsAuthenCode implements MessageListener {
    //消息监听类实现消息监听接口 MessageListener 重写 onMessage 方法
    public void onMessage(Message message){
        MapMessage mapMessage = (MapMessage)message;

        try{
            //从 MapMessage 中取出手机验证码和手机号，调用发送短信的方法将短信发送给用户
            SendMessage.sendMessages(mapMessage.getString("code"), mapMessage.getString("telephone"));
            System.out.println("-----发送消息成功..." + mapMessage.getString("code"));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
