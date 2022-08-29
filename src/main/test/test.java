import com.imooc.miaosha.domain.MiaoshaUser;
import com.imooc.miaosha.redis.MiaoshaKey;
import org.apache.commons.lang3.time.FastDateFormat;

import javax.imageio.ImageIO;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;


public class test {
    public static void main(String[] args) throws FileNotFoundException {

//        // 获取一个时间对象
//        Calendar cal = Calendar.getInstance();
//
//        // getInstance指定格式获得FastDateFormat对象
//        FastDateFormat fastDateFormat = FastDateFormat.getInstance("yyyyMMddHHmm");
//        String minute = fastDateFormat.format(cal);
//        System.out.println(minute);
//
//        String key = String.join(":", FAIL_COUNTER, username, minute);
//
//        // 获取这个key, 登录失败的次数，注意key可以保持一分钟不变， 所以下面有登录失败次数+1的逻辑
//        Integer count = (Integer) redisTemplate.opsForValue().get(key);
//        redisTemplate.opsForValue().increment(key);  // 登录错误次数+1

        String a = "65";
        int b = Integer.parseInt(a);
        System.out.println(b);

        File[] hiddenFiles = new File(".").listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return file.isHidden;
            }
        })

    }
}
