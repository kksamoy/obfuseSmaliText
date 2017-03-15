import org.apache.commons.lang3.StringEscapeUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by qtfreet on 2017/3/13.
 */
public class DecodeDasho {

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

    public static String copyValueOf(String str, int i) {
        int i2 = 0;
        try {
            char[] toCharArray = str.toCharArray();
            int length = toCharArray.length;
            while (i2 != length) {
                int i3 = i2 + 1;
                toCharArray[i2] = (char) ((i & 95) ^ toCharArray[i2]);
                i++;
                i2 = i3;
            }
            return String.valueOf(toCharArray, 0, length).intern();
        } catch (Exception e) {
            return null;
        }
    }


    public static String copyValueOf2(String arg5, int arg6) {
        String v0_2;
        int v0 = 0;
        try {
            char[] v2 = arg5.toCharArray();
            int v3 = v2.length;
            while (v0 != v3) {
                int v4 = arg6 & 95 ^ v2[v0];
                ++arg6;
                v2[v0] = ((char) v4);
                ++v0;
            }

            v0_2 = String.valueOf(v2, 0, v3).intern();
        } catch (Exception v0_1) {
            v0_2 = null;
        }

        return v0_2;
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
                    tmp = StringEscapeUtils.unescapeJava(tmp);
                    String register = m.group(1);
                    if (tmp.equals("")) {
                        sb.append(str + "\n");
                        continue;
                    }
                    String blank1 = br.readLine();
                    String keyNum = br.readLine();
                    String blank2 = br.readLine();
                    String decodeSign = br.readLine();
                    String blank3 = br.readLine();
                    String movSign = br.readLine();
                    if (decodeSign.contains("LJVMInfo;->copyValueOf(Ljava/lang/String;I)Ljava/lang/String;")) {
                        //匹配是否有字符串加密特征
                        Matcher m2 = Pattern.compile("const/16 ([vp]\\d{1,2}), 0x(\\w+)").matcher(keyNum);
                        if (m2.find()) {
                            System.out.println(path);
                            String num = m2.group(2);
//                            System.out.println(num);

                            int s = Integer.parseInt(num, 16);
                            System.out.println(tmp);
                            //    System.out.println(s);
                            String dec = copyValueOf(tmp, s);
                            System.out.println(dec);
                            System.out.println(register);
                            if (movSign.equals("    move-result-object " + register)) {
                                String sign = "    const-string " + register + ", " + "\"" + dec + "\"";
                                //   System.out.println(sign);
                                sb.append(sign + "\n\n");
                            }
                        }
//                        String dec = OooOOoo0oo(tmp);
//                        dec = StringEscapeUtils.escapeJava(dec);//转义字符串，防止意外情况
//                        String sign = "    const-string " + register + ", " + "\"" + dec + "\""; //替换为解密后的字符串
//                        sb.append(sign + "\n\n\n\n\n");//每readline一行就补足一行，防止意外情况
                    } else {
                        if (blank1 == null) {
                            blank1 = "";
                        }
                        if (keyNum == null) {
                            keyNum = "";
                        }
                        if (blank2 == null) {
                            blank2 = "";
                        }
                        if (decodeSign == null) {
                            decodeSign = "";
                        }
                        if (blank3 == null) {
                            blank3 = "";

                        }
                        if (movSign == null) {
                            movSign = "";
                        }
                        //因为readline在为null说明文件已经读完，但之前没有进行判断，所以这里进行判断，为空替换为""


                        sb.append(str + "\n" + blank1 + "\n" + keyNum + "\n" + blank2 + "\n" + decodeSign + "\n" + blank3 + "\n" + movSign + "\n");
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
                getFiles(file.getPath());
            } else {
                if (file.getName().endsWith(".smali")) {
                    filelist.add(file.getPath());
                }
            }
        }
    }
}
