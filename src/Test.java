import java.io.ByteArrayOutputStream;

/**
 * Created by qtfreet00 on 2017/3/14.
 */
public class Test {
    public static void main(String[] args) {
        String s=qtfreet00.encode("你好啊");
        System.out.println(s);

        System.out.println(OooOOoo0oo("C586AD91D9DDC192CB8AB2B3"));
    }
    public static final String DEFAULT_KEY = "qtfreet";
    private static final String hexString = "0123456789ABCDEF";

    public static String OooOOoo0oo(String str) {
        int i;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(str.length() / 2);
        for (i = 0; i < str.length(); i += 2) {
            baos.write((hexString.indexOf(str.charAt(i)) << 4) | hexString.indexOf(str.charAt(i + 1)));
        }
        byte[] b = baos.toByteArray();
        int len = b.length;
        int keyLen = DEFAULT_KEY.length();
        for (i = 0; i < len; i++) {
            b[i] = (byte) (b[i] ^ DEFAULT_KEY.charAt(i % keyLen));
        }
        return new String(b);
    }


}
