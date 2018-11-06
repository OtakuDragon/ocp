import java.io.*;
import java.sql.SQLException;

public class OCP {



    public static void main(String[] args) {
        /*
        Assertions

        java [-ea[:package|class] | -enableassertions[:package|class]
        java [-da[:package|class] | -disableassertions[:package|class]

        assert {booleanExpression} [: valueExpression];

        don't assert public method parameters
        don't assert with side effects
        don't catch AssertionError
        do assert private method parameters
        do assert private/public method logically unreacheable parts

        ----------------------------------------------------------------------------------------------------------
        Multi-catch

        try{
            //try content
        }catch(FirstThrowableType | SecondThrowableType | ... | NThrowableType e){
            // NONE of the exceptions in the list can be a subclass of another causes compilation error
            e = something; // WON'T COMPILE e is final in a multi-catch, not in a normal catch
            throw e; //e is rethrowable as Exception on the method signature or all of the exceptions in the list
        }[finally{}]

        ----------------------------------------------------------------------------------------------------------
        Try-with-resources

        try(FirstAutoCloableType f = new FirstAutoCloseableType();
            SecondAutoCloableType s = new SecondAutoCloseableType();
            ...
            NAutoCloableType s = new NAutoCloseableType();){
        }[catch(){}]//Optional catch*
        [finally(){}]//Optional finnaly

        Closeable extends AutoCloseable
        Closeable's close() throws IOException
        AutoCloseable's close() throws Exception

        All the declarations inside the parenthesis must implement/extend AutoCloseable

        The order of execution is:
         1 try declarations //if an exception happens here it goes to 3 closing the resources opened before de exception if any
         2 try body //if an exceptions happens here it stops the execution of the block and preecedes to 3
         3 method close() of all the AutoCloseables declared in the reverse order of declaration
         4 catch body
         5 finally body

        The catch clause must catch or the method signature must declare all the checked exceptions thrown by the
        declared resources close() methods.

        Only one exception goes to the catch block, the priority is for exceptions thrown in the try body if any
        other exception happened while calling the close() methods they go in to a Throwable[] acessible on the
        catch block calling e.getSupressed(), if the only exception came from a close() method it goes straight to
        the catch block.

        */

        try(B b = new B();A a = new A()){
            throw new IOException("1");
        } catch (IOException  e) {
            System.out.println(e.getSuppressed());
        }
    }

    public static boolean method() throws IOException, SQLException{throw new IOException();}

    private static class A implements AutoCloseable {

        public A() throws IOException {

        }

        @Override
        public void close() throws IOException {
            throw new IOException("c");
        }
    }

    private static class B implements AutoCloseable {

        public B() {
        }

        @Override
        public void close() {
            System.out.println("closed b ");
        }
    }

}
