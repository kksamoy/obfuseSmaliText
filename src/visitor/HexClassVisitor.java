package visitor;

import com.OooOO0OO;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * We visit the class {@link Xor} to modify the value of the default key.
 *
 * @author Megatron King
 * @since 2017/3/7 18:43
 */

public class HexClassVisitor extends ClassVisitor {

    private static final String CLASS_FIELD_KEY_NAME = "DEFAULT_KEY";
    private static final String INJECT_METHOD_NAME1 = "encode";
    private static final String INJECT_METHOD_NAME2 = "OooOOoo0oo";
    private static final String INJECT_METHOD_DESC = "(Ljava/lang/String;)Ljava/lang/String;";

    private String mKey;

    public HexClassVisitor(String key, ClassWriter cw) {
        super(Opcodes.ASM5, cw);
        this.mKey = key;
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        if (CLASS_FIELD_KEY_NAME.equals(name)) {
            value = mKey;
        }
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        if ((INJECT_METHOD_NAME1.equals(name) || INJECT_METHOD_NAME2.equals(name)) && INJECT_METHOD_DESC.equals(desc)) {
            mv = new MethodVisitor(Opcodes.ASM5, mv) {

                @Override
                public void visitLdcInsn(Object cst) {
                    if (OooOO0OO.DEFAULT_KEY.equals(cst)) {
                        cst = mKey;
                    }
                    super.visitLdcInsn(cst);
                }
            };
        }
        return mv;
    }
}
