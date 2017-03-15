import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qtfreet on 2017/2/24.
 */
public class DecodeNeteaseString {

    private static List<String> filelist = new ArrayList();

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("请输入已反编译apk的路径：");
        Scanner scanner = new Scanner(System.in);
        String path = scanner.next();


        getFiles(path);
        if (filelist == null) {
            System.out.println("未发现smali文件");
            return;
        }
        int size = filelist.size();
        for (int i = 0; i < size; i++) {
            String signalFile = filelist.get(i);
            int j = i + 1;
            int k = size - i;
            //      System.out.println("当前是第 " + j + "个文件，还剩" + k + "个");
            FileTofindString(signalFile);
        }
        System.out.println("任务完成");
        System.out.println("直接重打包apk即可");
    }


    private static final String key = "Encrypt";

    //网易云音乐的字符串混淆方法
    public static String decode(String str) {
        if (str == null) {
            return null;
        }
        try {

            byte[] b = Base64.getDecoder().decode(str);
            int lenStr = b.length;
            int lenKey = key.length();
            int i = 0;
            int j = 0;
            while (i < lenStr) {
                if (j >= lenKey) {
                    j = 0;
                }
                b[i] = (byte) (b[i] ^ key.charAt(j));
                i++;
                j++;
            }
            return new String(b);
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * 匹配文件
     *
     * @param path 每个文件对应路径
     */
    private static void FileTofindString(String path) {
        StringBuilder sb = new StringBuilder();
        try {
            InputStreamReader read = new InputStreamReader(new FileInputStream(path), "UTF-8");
            BufferedReader br = new BufferedReader(read);
            String str = "";

            while ((str = br.readLine()) != null) {
                //利用正则去匹配方法中定义的字符串
                Matcher m = Pattern.compile("const-string ([vp]\\d{1,2}), \"(.*)\"").matcher(str);
                if (m.find()) {
                    String tmp = m.group(2);
                    String register = m.group(1);
                    if (tmp.equals("")) {
                        sb.append(str + "\n");
                        continue;
                    }
                    String blank1 = br.readLine();
                    String decodeSign = br.readLine();
                    String blank2 = br.readLine();
                    String movSign = br.readLine();
                    if (decodeSign.contains(", La/auu/a;->c(Ljava/lang/String;)Ljava/lang/String;") && movSign.contains("move-result-object")) {
                        //匹配是否有字符串加密特征
                        String dec = decode(tmp);
                        dec = StringEscapeUtils.escapeJava(dec);//转义字符串，防止意外情况
                        String sign = "    const-string " + register + ", " + "\"" + dec + "\""; //替换为解密后的字符串
                        sb.append(sign + "\n\n\n\n\n");//每readline一行就补足一行，防止意外情况
                    } else {
                        if (blank1 == null) {
                            blank1 = "";
                        }
                        if (decodeSign == null) {
                            decodeSign = "";
                        }
                        if (blank2 == null) {
                            blank2 = "";
                        }
                        if (movSign == null) {
                            movSign = "";
                        }
                        //因为readline在为null说明文件已经读完，但之前没有进行判断，所以这里进行判断，为空替换为""
                        System.out.println(movSign);

                        sb.append(str + "\n" + blank1 + "\n" + decodeSign + "\n" + blank2 + "\n" + movSign + "\n");
                    }
                } else {
                    sb.append(str + "\n");
                }
            }
            br.close();
            read.close();
            //覆盖掉源文件
            FileOutputStream fos = new FileOutputStream(new File(path));
            fos.write(sb.toString().getBytes("UTF-8"));
            fos.flush();
            fos.close();

        } catch (Exception e) {
        }
    }


    private static boolean pass = false;

    /**
     * 遍历所有文件，添加到list中
     *
     * @param filePath 文件路径
     */
    private static void getFiles(String filePath) {
        File[] files = new File(filePath).listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {

            if (file.isDirectory()) {
                if (!pass) {
                    if (file.getName().equals("android")) { //不去处理系统的android资源库
                        pass = true;
                        continue;
                    }
                }
                getFiles(file.getPath());
            } else {
                if (file.getName().endsWith(".smali")) {
                    filelist.add(file.getPath());
                }
            }
        }
    }
}

