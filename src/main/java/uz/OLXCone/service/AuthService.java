package uz.OLXCone.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.OLXCone.component.MyMapper;
import uz.OLXCone.controller.AuthController;
import uz.OLXCone.model.User;
import uz.OLXCone.model.Verify;
import uz.OLXCone.payload.ApiResult;
import uz.OLXCone.payload.in.SignInDTO;
import uz.OLXCone.payload.in.SignUpDTO;
import uz.OLXCone.repository.UserRepository;
import uz.OLXCone.repository.VerifyRepository;
import uz.OLXCone.security.JwtProvider;
import uz.OLXCone.utils.Checks;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;

import static uz.OLXCone.utils.AppConstants.BEARER;
import static uz.OLXCone.utils.AppConstants.DOMAIN;

@Service
@EnableScheduling
@RequiredArgsConstructor
public class AuthService {
    private final MyMapper mapper;

    private final VerifyRepository verifyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    private String sender;

    public ApiResult<?> signUp(SignUpDTO signUpDTO) {
        if (userRepository.existsByEmail(signUpDTO.getEmail()))
            return ApiResult.noObject(
                    "email busy", false);
        User user = mapper.signUpDTOToUser(signUpDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Integer code = codeGenerate();
        Verify verify = new Verify();
        verify.setCode(code);
        verify.setUser(user);
        sendSMS(code, user.getEmail());
        userRepository.save(user);
        verifyRepository.save(verify);
        return ApiResult.noObject(
                "enter verification code",
                true);
    }

    public ApiResult<?> signIn(SignInDTO signInDTO) {
        User user = userRepository.
                findByEmail(signInDTO.getEmail())
                .orElse(null);
        if (user != null) {
            if (user.getIsBlocked())
                return ApiResult.noObject(
                        "you are blocked",
                        false);
            if (passwordEncoder.matches(
                    signInDTO.getPassword(),
                    user.getPassword())
            ) {
                String jwt = jwtProvider.generateJWT(user.getEmail());
                return ApiResult.noMessage(
                        true,
                        BEARER + jwt);
            }
        }
        return ApiResult.noObject(
                "email or password wrong"
                , false);
    }

    public ApiResult<?> verification(Integer code) {
        if (Checks.isValidCode(code)) {
            Verify verify = verifyRepository
                    .findByCode(code);
            if (verify != null) {
                if (verify.getTime().after(thirtyMinuteAgo())) {
                    User user = verify.getUser();
                    user.setIsBlocked(false);
                    userRepository.save(user);
                    verifyRepository.delete(verify);
                    return ApiResult.noObject(
                            "verified your email",
                            true);
                } else {
                    User user = verify.getUser();
                    verifyRepository.delete(verify);
                    userRepository.delete(user);
                    return ApiResult.noObject(
                            "this code expired " +
                                    "try age sign up",
                            false);
                }
            }
        }
        return ApiResult.noObject(
                "code not exist",
                false);
    }

    private Date thirtyMinuteAgo() {
        Date date = new Date();
        long time = date.getTime();
        time = time - 30 * 60 * 1000;
        date.setTime(time);
        return date;
    }

    private void sendSMS(Integer code, String email) {
        String htmlContent = "<div style=\"text-align: center;align-items: center\">\n" +
                "<h4 style=\"font-size:24px;font-weight:bold;line-height:1.2;\n" +
                "margin:0 0 32px 0;color:#002f34;\">OLX hisobingiz deyarli tayyor </h4>\n" +
                "<p>OLX hisobingizni faollashtirish uchun uni yaratishni boshlagan sahifaga\n" +
                "<br> ushbu tasdiq kodini nusxalab qoʻying:</p><h1 style=\"\n" +
                "display:inline-block;background:#e9fcfb;padding:18px;border-radius:4px;\n" +
                "font-weight:bold;letter-spacing:8px;width: 20%;\">" + code + "</h1>\n" +
                "<p>Quyidagi havolani bosish orqali hisobingizni faollashtirishingiz mumkin:</p>\n" +
                "<a href=\"" + DOMAIN + AuthController.PATH + "/verification/" + code + "\"\n" +
                "style=\"color:#ffffff;display:inline-block;background-color:#002f34;\n" +
                "padding:15px 18px;font-weight:bold;border-radius:4px;width:15%\">\n" +
                "Profilni faollashtirish </a><br><p>Agar bu email bilan OLX\n" +
                "hisobini yarata olmasangiz, bizga xabar bering.</p><br></div>";
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom(new InternetAddress(sender));
            message.setRecipients(MimeMessage.RecipientType.TO, email);
            message.setSubject("OLX Clone hisobingizni faollashtiring");
            message.setContent(htmlContent, "text/html; charset=utf-8");
        } catch (MessagingException e) {
            throw new RuntimeException("email sending exception", e);
        }
        javaMailSender.send(message);
    }

    private Integer codeGenerate() {
        Integer code;
        do {
            code = (int) (Math.random() * 1000000);
        } while (verifyRepository.
                existsByCode(code));
        return code;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void deleteNeedlessVerify() {
        verifyRepository.deleteAllByTimeBefore(
                Timestamp.valueOf(LocalDateTime.now()
                        .minusHours(1)));

    }
}