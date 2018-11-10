import java.io.IOException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        ----------------------------------------------------------------------------------------------------------
        Dates, Numbers, Currencies e Locales

        Date date = new Date();//Agora
        Date date = new Date(0l);//01/01/1970

        long millis = date.getTime();//Millisegundos desde 01/01/1970

        Locale locale = new Locale("linguagem", "pais");
        Locale locale = new Locale("pt", "BR");//Português Brasil
        Locale locale = new Locale("pt");//Português
        Locale locale = Locale.getDefault();//Locale padrão da JVM
        Locale.setDefault(new Locale("en", "US"));//Muda o Locale padrão da JVM
        locale.getDisplayCountry();
        locale.getDisplayLanguage();

        Calendar calendar = Calendar.getInstance();//No Locale padrão
        Calendar calendar = Calendar.getInstance(locale);//No Locale passado

        Date date = calendar.getTime();
        calendar.setTime(date.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, 1);// +1 Dia mudando os outros campos
        calendar.add(Calendar.DAY_OF_MONTH, -1);// -1 Dia mudando os outros campos
        calendar.roll(Calendar.DAY_OF_MONTH, 1);// +1 Dia sem mudar os outros campos
        calendar.roll(Calendar.DAY_OF_MONTH, -1);// -1 Dia sem mudar os outros campos

        DateFormat dateFormat = DateFormat.getInstance();
        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        Date date = new Date();
        String dateString = dateFormat.format(date);
        try {
            Date parsedDate = dateFormat.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        NumberFormat numberFormat = NumberFormat.getInstance();
        NumberFormat numberFormat = NumberFormat.getNumberInstance();
        NumberFormat numberFormat = NumberFormat.getNumberInstance(locale);
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(locale);

        numberFormat.getMaximumFractionDigits();
        numberFormat.setParseIntegerOnly(boolean);

        double number = 1000;
        String formattedNumber = numberFormat.format(number);
        try {
            Number parsedNumber = numberFormat.parse(formattedNumber);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ----------------------------------------------------------------------------------------------------------
        Parsing, Tokenizing and Formatting

        Regex Metacharacters

        \d (digit, 0-9)
        \D (not digit, 0-9)
        \s (whitespace, \t, \n, \f, \r)
        \S (not whitespace, \t, \n, \f, \r)
        \w (word character, a-z, A-Z, _)
        \W (not word character, a-z, A-Z, _)
        \b (word boundary, antes ou depois de um word character, mesmo no inicio(0) e fim(length + 2) da String)
        \B (not word boundary)
        . Qualquer charactere
        [abc] - a ou b ou c
        [a-c] - a ou b ou c
        [a-cA-C] - aA ou bB ou cC
        + - Um ou mais
        * - Zero ou mais
        ? - Zero ou um
        (regex)quantifier - aplica o quantifier no regex dentro dos parenteses.

        Pattern pattern = Pattern.compile("\\b");
        Matcher matcher = pattern.matcher("value");
        boolean anyMatches = matcher.matches();
        while(matcher.find()) {
            System.out.print(matcher.start()+","+matcher.group());
        }

        Tokenizing

        String[] tokens = "someString".split("tokenRegex");

        Scanner scanner = new Scanner(someInputStream);
        scanner.useDelimiter("regex");//Default delimiter espaço em branco
        scanner.useDelimiter(pattern);
        boolean hasNextToken = scanner.hasNext();
        String token = scanner.next();
        boolean hasNextToken = scanner.hasNextInt();
        Integer token = scanner.nextInt();
        ... todos os tipos wrapper menos Character(char)

         StringTokenizer stringTokenizer = new StringTokenizer("someString", "delimiter");
        if(stringTokenizer.hasMoreTokens()){
            String token = stringTokenizer.nextToken();
        }

        Formatting

        %[index][flags][width][.precision]conversionChar

        Conversion Chars

        b - boolean
        c - char
        d - integer
        f - float/double
        s - String

        Flags

        - - Left justify
        + - Sinal do número + ou -
        0 - 0 padding
        , - Formatar número com pontos e virgulas de acordo com o locale
        ( - Coloca numero negativo entre parenteses

         System.out.format("%+0,(40.3f", -123456.78910d);

        Resource bundles

        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundleName", locale);
        resourceBundle.getString("key");

        Prioridade de busca(sendo que se encontrou java passa a procurar somente java e o mesmo properties)

        bundleName_pt_BR.java
        bundleName_pt.java
        bundleName.java

        bundleName_pt_BR.properties
        bundleName_pt.properties
        bundleName.properties





        */
        ResourceBundle resourceBundle = ResourceBundle.getBundle("bundleName", locale);
        resourceBundle.getString("key");





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
