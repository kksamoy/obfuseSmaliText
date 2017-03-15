package visitor;

import org.objectweb.asm.*;


public class TestClassVisitor extends ClassVisitor {


    public TestClassVisitor(String key, ClassWriter cw) {
        super(Opcodes.ASM5, cw);

    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {

        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {

        return super.visitAnnotation(desc, visible);
    }

    @Override
    public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
        System.out.println("----------------------field-----------------------------");
        System.out.println("access=  " + access + "\nname=  " + name + "\ndesc=  " + desc + "\nsignature= " + signature + "\nvalue= " + value);
        System.out.println("----------------------field-----------------------------");
        return super.visitField(access, name, desc, signature, value);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        System.out.println("----------------------method-----------------------------");
        System.out.println("access=  " + access + "\nname=  " + name + "\ndesc=  " + desc + "\nsignature= " + signature);
        System.out.println("----------------------method-----------------------------");
        return mv;
    }

    @Override
    public void visitEnd() {

        super.visitEnd();
    }
}
