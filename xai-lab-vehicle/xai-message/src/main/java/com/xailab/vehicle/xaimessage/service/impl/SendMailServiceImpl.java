package com.xailab.vehicle.xaimessage.service.impl;

import com.xailab.vehicle.xaimessage.config.EmailConfig;
import com.xailab.vehicle.xaimessage.entity.constant.EmailConstant;
import com.xailab.vehicle.xaimessage.entity.vo.ToEmail;
import com.xailab.vehicle.xaimessage.service.SendMailService;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.internet.MimeMessage;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;

import jakarta.mail.internet.MimeMultipart;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Service("SendMailService")
@RequiredArgsConstructor
public class SendMailServiceImpl implements SendMailService {

    private final EmailConfig emailConfig;

    @Resource
    private JavaMailSender mailSender;

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public Boolean sendCode(String email, String code) throws MessagingException {

        // 一、创建参数配置, 用于连接邮件服务器的参数配置
        Properties props = new Properties();

        props.setProperty("mail.smtp.host", emailConfig.getHost()); // 设置发送邮件的邮件服务器的属性（这里使用网易的smtp服务器）

//        props.setProperty("mail.smtp.port","465");

        props.put("mail.smtp.host", emailConfig.getHost());

        props.put("mail.smtp.auth", "true"); // 需要经过授权，也就是用户名和密码的校验，这样才能通过验证（一定要有这一条）

        // 二、根据配置创建会话对象, 用于和邮件服务器交互
        Session session = Session.getDefaultInstance(props);

        session.setDebug(true); // true 在控制台（console)上看到发送邮件的过程

        // 三、 创建一封复杂邮件（文本+附件）


        // 3.1. 创建邮件对象
        MimeMessage message = new MimeMessage(session); // 加载发件人地址

        // 3.2. From: 发件人
        message.setFrom(new InternetAddress(emailConfig.getFrom()));

        // 3.3. To: 收件人（可以增加多个收件人）
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(email)); // 加载收件人地址

        // 3.4. To: 收件人（可以增加多个抄送）
//            message.addRecipient(Message.RecipientType.CC, new InternetAddress(to)); // 加载抄件人地址

        // 3.5. Subject: 邮件主题
        message.setSubject(EmailConstant.emailTitleCode +code); // 加载标题

        // 3.6. 邮件内容
        MimeMultipart multipart = new MimeMultipart(); // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件

        MimeBodyPart contentPart = new MimeBodyPart(); // 设置邮件的文本内容

        String text = EmailConstant.textCode;

        text = text.replace("BMWAccount",email);

        text = text.replace("BMWcode",code);

        text = text.replace("BMWData",sdf.format(new Date()));

        contentPart.setContent(text, "text/html;charset=utf-8");

        multipart.addBodyPart(contentPart);

        // 3.7. 邮件附件
//            String attPath = "D:\\data\\ftpUpload\\2020\\12\\10\\盖章指令文件.zip";
//            MimeBodyPart attachment = new MimeBodyPart();
//            DataHandler dh = new DataHandler(new FileDataSource(attPath)); // 读取本地文件
//            attachment.setDataHandler(dh); // 将附件数据添加到“节点”
//            attachment.setFileName(MimeUtility.encodeText(dh.getName())); // 设置附件的文件名（需要编码）
//            multipart.addBodyPart(attachment);

        multipart.setSubType("mixed"); // 混合关系

        // 3.8. 设置整个邮件的关系（将最终的混合“节点”作为邮件的内容添加到邮件对象）
        message.setContent(multipart);

        // 3.9. 设置发件时间
        message.setSentDate(new Date());

        // 3.10. 保存上面的所有设置
        message.saveChanges(); // 保存变化

        // 四、 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport("smtp");

        // 五、 使用 邮箱账号 和 授权码 连接邮件服务器
        transport.connect(emailConfig.getHost(), emailConfig.getUser(), emailConfig.getPassword());

        // 六、 发送邮件,
        transport.sendMessage(message, message.getAllRecipients());

        // 七、 关闭连接
        transport.close();

        return true;

    }

    @Override
    public Boolean newSendCode(String email, String password){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            ToEmail toEmail = new ToEmail();
            // 添加 toEmail
            toEmail.setTos(new String[]{email});
            toEmail.setSubject(EmailConstant.emailTitleCode);
            String text = EmailConstant.textCode;

            text = text.replace("BeeEvalAccount",email);

            text = text.replace("BeeEvalPassword",password);

            text = text.replace("BeeEvalData",sdf.format(new Date()));
            toEmail.setContent(text);

            //谁发的
            minehelper.setFrom(emailConfig.getFrom());
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件标题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容
            minehelper.setText(toEmail.getContent(), true);

            // 假设您有一个文件路径 TODO 这里改成 统一下面的图片路径
            // File file = new File("src/main/resources/logo.a7650d93.png");
//            Resource file = new ClassPathResource("logo.a7650d93.png");
            //FileSystemResource file = new FileSystemResource(new ClassPathResource("./logo.a7650d93.png").getFile());
            //FileSystemResource file = new FileSystemResource( new ClassPathResource("logo.a7650d93.png", FontUtil.class.getClassLoader()).getFile());
            // FileSystemResource resource = new FileSystemResource(file);
            // 添加内联资源
//            minehelper.addInline("logo.a7650d93.png", file);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean newSendEnCode(String email, String password){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            ToEmail toEmail = new ToEmail();
            // 添加 toEmail
            toEmail.setTos(new String[]{email});
            toEmail.setSubject(EmailConstant.emailTitleCodeEn);
            String text = EmailConstant.textCodeEn;

            text = text.replace("BeeEvalAccount",email);

            text = text.replace("BeeEvalPassword",password);

            text = text.replace("BeeEvalData",sdf.format(new Date()));
            toEmail.setContent(text);

            //谁发的
            minehelper.setFrom(emailConfig.getFrom());
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件标题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容
            minehelper.setText(toEmail.getContent(), true);

            // 假设您有一个文件路径 TODO 这里改成 统一下面的图片路径
            // File file = new File("src/main/resources/logo.a7650d93.png");
//            Resource file = new ClassPathResource("logo.a7650d93.png");
            //FileSystemResource file = new FileSystemResource(new ClassPathResource("./logo.a7650d93.png").getFile());
            //FileSystemResource file = new FileSystemResource( new ClassPathResource("logo.a7650d93.png", FontUtil.class.getClassLoader()).getFile());
            // FileSystemResource resource = new FileSystemResource(file);
            // 添加内联资源
//            minehelper.addInline("logo.a7650d93.png", file);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean newSendJpCode(String email, String password){
        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            ToEmail toEmail = new ToEmail();
            // 添加 toEmail
            toEmail.setTos(new String[]{email});
            toEmail.setSubject(EmailConstant.emailTitleCodeEn);
            String text = EmailConstant.textCodeJp;

            text = text.replace("BeeEvalAccount",email);

            text = text.replace("BeeEvalPassword",password);

            text = text.replace("BeeEvalData",sdf.format(new Date()));
            toEmail.setContent(text);

            //谁发的
            minehelper.setFrom(emailConfig.getFrom());
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件标题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容
            minehelper.setText(toEmail.getContent(), true);

            // 假设您有一个文件路径 TODO 这里改成 统一下面的图片路径
            // File file = new File("src/main/resources/logo.a7650d93.png");
//            Resource file = new ClassPathResource("logo.a7650d93.png");
            //FileSystemResource file = new FileSystemResource(new ClassPathResource("./logo.a7650d93.png").getFile());
            //FileSystemResource file = new FileSystemResource( new ClassPathResource("logo.a7650d93.png", FontUtil.class.getClassLoader()).getFile());
            // FileSystemResource resource = new FileSystemResource(file);
            // 添加内联资源
//            minehelper.addInline("logo.a7650d93.png", file);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean sendAccountAudit(String reason, String email,Integer aon) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            ToEmail toEmail = new ToEmail();
            // 添加 toEmail
            toEmail.setTos(new String[]{email});
            toEmail.setSubject(EmailConstant.emailTitleAudit);
            String text ="";
            if (aon==0) {
                text= EmailConstant.textAuditA;
            }else if (aon==1){
                text= EmailConstant.textAuditN;

                text = text.replace("BmwReason",reason);
            }

            text = text.replace("BMWAccount",email);

            text = text.replace("BMWData",sdf.format(new Date()));
            toEmail.setContent(text);

            //谁发的
            minehelper.setFrom(emailConfig.getFrom());
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件标题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容
            minehelper.setText(toEmail.getContent(), true);

            // 假设您有一个文件路径 TODO 这里改成 统一下面的图片路径
            // File file = new File("src/main/resources/logo.a7650d93.png");
//            Resource file = new ClassPathResource("logo.a7650d93.png");
            //FileSystemResource file = new FileSystemResource(new ClassPathResource("./logo.a7650d93.png").getFile());
            //FileSystemResource file = new FileSystemResource( new ClassPathResource("logo.a7650d93.png", FontUtil.class.getClassLoader()).getFile());
            // FileSystemResource resource = new FileSystemResource(file);
            // 添加内联资源
//            minehelper.addInline("logo.a7650d93.png", file);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }


        return true;

    }

    @Override
    public Boolean sendZhengShi(String email, String password) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            ToEmail toEmail = new ToEmail();
            // 添加 toEmail
            toEmail.setTos(new String[]{email});
            toEmail.setSubject(EmailConstant.emailZhengShiTitleCodeEn);
            String text = EmailConstant.textCodeZhengshi;

            text = text.replace("BeeEvalAccount",email);

            text = text.replace("BeeEvalPassword",password);

            text = text.replace("BeeEvalData",sdf.format(new Date()));
            toEmail.setContent(text);

            //谁发的
            minehelper.setFrom(emailConfig.getFrom());
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件标题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容
            minehelper.setText(toEmail.getContent(), true);

            // 假设您有一个文件路径 TODO 这里改成 统一下面的图片路径
            // File file = new File("src/main/resources/logo.a7650d93.png");
//            Resource file = new ClassPathResource("logo.a7650d93.png");
            //FileSystemResource file = new FileSystemResource(new ClassPathResource("./logo.a7650d93.png").getFile());
            //FileSystemResource file = new FileSystemResource( new ClassPathResource("logo.a7650d93.png", FontUtil.class.getClassLoader()).getFile());
            // FileSystemResource resource = new FileSystemResource(file);
            // 添加内联资源
//            minehelper.addInline("logo.a7650d93.png", file);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public Boolean sendZhengShiTest(String email, String password) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper minehelper = new MimeMessageHelper(message, true);
            ToEmail toEmail = new ToEmail();
            // 添加 toEmail
            toEmail.setTos(new String[]{email});
            toEmail.setSubject(EmailConstant.emailZhengShiTitleCodeEn);
            String text = EmailConstant.textCodeZhengshiTest;

            text = text.replace("BeeEvalAccount",email);

            text = text.replace("BeeEvalPassword",password);

            text = text.replace("BeeEvalData",sdf.format(new Date()));
            toEmail.setContent(text);

            //谁发的
            minehelper.setFrom(emailConfig.getFrom());
            //谁要接收
            minehelper.setTo(toEmail.getTos());
            //邮件标题
            minehelper.setSubject(toEmail.getSubject());
            //邮件内容
            minehelper.setText(toEmail.getContent(), true);

            // 假设您有一个文件路径 TODO 这里改成 统一下面的图片路径
            // File file = new File("src/main/resources/logo.a7650d93.png");
//            Resource file = new ClassPathResource("logo.a7650d93.png");
            //FileSystemResource file = new FileSystemResource(new ClassPathResource("./logo.a7650d93.png").getFile());
            //FileSystemResource file = new FileSystemResource( new ClassPathResource("logo.a7650d93.png", FontUtil.class.getClassLoader()).getFile());
            // FileSystemResource resource = new FileSystemResource(file);
            // 添加内联资源
//            minehelper.addInline("logo.a7650d93.png", file);

            mailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            e.printStackTrace();
        }

        return true;
    }

}
