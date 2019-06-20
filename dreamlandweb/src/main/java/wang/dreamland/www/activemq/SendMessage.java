package wang.dreamland.www.activemq;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;

public class SendMessage {
    private static String accessKeyId = "LTAIszdpa21re22p";
    private static String accessKeySecret = "zhILMrSrDdUUCa92hkjplhENzUI4Ez";
    private static String setSignName = "梦境网";
    private static String dayutemplateCode = "SMS_163525720";

    public static void sendMessages(String code,String phone) {
        //设置超时时间-可自行调整
        System.setProperty("sun.net.client.defaultConnectionTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");
        //初始化ascClient需要的几个参数
        final String product = "Dysmsapi";  //短信API产品名称
        final String domain = "dysmsapi.aliyuncs.com";    //短信API产品域名
        //初始化ascClient,暂时不支持多region
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        try {
            DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        } catch (ClientException e) {
            e.printStackTrace();
        }
        IAcsClient acsClient = new DefaultAcsClient(profile);
        SendSmsRequest request = new SendSmsRequest();
        //待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为20个手机号码,批量调用相对于单条调用及时性稍有延迟,
        // 验证码类型的短信推荐使用单条调用的方式
        request.setPhoneNumbers(phone);
        request.setSignName(setSignName); //短信签名
        request.setTemplateCode(dayutemplateCode);  //短信模板
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,
        // 此处的值为request.setTemplateParam("{\"name\":\"Tom\", \"code\":\"123\"}");
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,
        // 否则会导致JSON在服务端解析失败
        request.setTemplateParam("{\"code\":\"" + code + "\"}");
        request.setOutId("yourOutId"); //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        SendSmsResponse sendSmsResponse = null;
        try {
            sendSmsResponse = acsClient.getAcsResponse(request);    //请求失败这里会抛ClientException异常
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        if (sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
            System.out.println("success");
        }
    }
}
