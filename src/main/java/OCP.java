import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class OCP {


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

        %[index$][flags][width][.precision]conversionChar

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
       -----------------------------------------------------------------------------------
        IO/NIO

        File

        Todos os métodos abaixo retornam boolean com o resultado
        e o createNewFile joga IOException.

        renameTo também funciona como move e não existe método para mover.

        File dir = new File("dir1/dir2");
        File file = new File("dir1/dir2","file.txt");

        dir.mkdirs();

        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        file.delete();
        file.renameTo(new File("dir1/dir2","file2.txt"));
        file.renameTo(new File("dir1","file.txt"));//Move para o dir1 o mesmo arquivo
        file.exists();
        file.isFile();
        file.isDirectory();
        String[] dirs = dir.list();//tipo ls em um diretório

        FileReader

        FileReader fileReader = new FileReader(file);//Ou com a string "dir1/dir2/file.txt" construtor joga FileNotFoundException
        //lê char a char e retorna -1 no fim do arquivo
        int read = fileReader.read(); //Joga IOException
        //Lê os chars para um char array.
        char[] conteudo = new char[256];
        fileReader.read(conteudo);//Joga IOException
        fileReader.close();//é AutoCloseable, Joga IOException

        FileWriter

        //Todos os métodos de FileWriter e o construtor abaixo jogam IOException
        FileWriter fileWriter = new FileWriter(file, Boolean.TRUE);//Segundo parametro "append" opcional, também funciona com a String do file

        fileWriter.append("aaa");
        fileWriter.write(new char[]{'a', 'a', 'a'});
        fileWriter.flush();//Envia tudo que está cacheado em buffer para dentro do arquivo.
        fileWriter.close();//É AutoCloseable

        BufferedReader

        //Funciona como wrapper de FileReader para ler linhas inteiras do arquivo
        BufferedReader bufferedReader = new BufferedReader(fileReader);//Segundo parametro opcional tipo int que é o tamanho do buffer
        String line = bufferedReader.readLine();//joga IOException
        bufferedReader.close();//É AutoCloseable, joga IOException

        BufferedWriter

        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        //Todos os métodos jogam IOException
        bufferedWriter.write("text");
        bufferedWriter.append("text");
        bufferedWriter.newLine();
        bufferedWriter.flush();
        bufferedWriter.close();//é AutoCLoseable

        PrintWriter

        //O construtor joga FileNotFoundException, os métodos não jogam checked exception.
        PrintWriter printWriter = new PrintWriter(file);//Funciona com File, String(da File), OutputStream e Writer
        printWriter.printf("%d%s",1,"2");//ou format
        printWriter.println();
        printWriter.print("aaaa");
        printWriter.append("aaa");//mesmo que print
        printWriter.println("aaaa");
        printWriter.flush();
        printWriter.close();
        printWriter.checkError();//True se aconteceu alguma exceção

        Path/Paths/Files(NIO.2)

        Path dir = Paths.get("dir1/dir2");

        Path file = Paths.get("dir1/dir2","file.txt");
        file.toFile().toPath();
        file.normalize();//remove ..'s se possível
        file.relativize(other);//Constroi um caminho relativo entre file e other
        file.resolve(other);//Tenta fazer um join de other com file

        //Jogam IOException
        Files.createDirectories(dir);
        Files.createFile(file);
        Files.copy(pathSource, pathTarget);
        Files.move(pathSource, pathTarget);
        Files.delete(path);//Joga exceção se o arquivo ou diretório não existe
        Files.deleteIfExists(path);//Não joga exceção se o arquivo ou diretório não existe
        //Não joga IOException
        Files.exists(path);

        File Attributes

        Files.getLastModifiedTime(file);//Throws IOException
        Files.setLastModifiedTime(file, FileTime.fromMillis(1000L));//Throws IOException
        Files.isReadable(file);
        Files.isExecutable(file);
        Files.isExecutable(file);

        BasicFileAttributes basicFileAttributes = Files.readAttributes(file, BasicFileAttributes.class);//Throws IOException
        basicFileAttributes.creationTime();
        basicFileAttributes.isDirectory();
        basicFileAttributes.lastAccessTime();
        basicFileAttributes.lastModifiedTime();
        basicFileAttributes.size();
        BasicFileAttributeView basicFileAttributeView = Files.getFileAttributeView(file, BasicFileAttributeView.class);
        //lastModifiedTime, lastAcessTime, createTime
        basicFileAttributeView.setTimes(FileTime.fromMillis(1000L), FileTime.fromMillis(1000L), FileTime.fromMillis(1000L));//Throws IOException

        DosFileAttributes dosFileAttributes = Files.readAttributes(file, DosFileAttributes.class);//Throws IOException
        dosFileAttributes.isArchive();
        dosFileAttributes.isHidden();
        dosFileAttributes.isReadOnly();
        dosFileAttributes.isSystem();

        DosFileAttributeView dosFileAttributeView = Files.getFileAttributeView(file, DosFileAttributeView.class);

        dosFileAttributeView.setArchive(boolean);//Throws IOException
        dosFileAttributeView.setHidden(boolean);//Throws IOException
        dosFileAttributeView.setReadOnly(boolean);//Throws IOException
        dosFileAttributeView.setSystem(boolean);//Throws IOException

        PosixFileAttributes posixFileAttributes = Files.readAttributes(file, PosixFileAttributes.class);//Throws IOException
        GroupPrincipal group = posixFileAttributes.group();
        UserPrincipal owner = posixFileAttributes.owner();
        Set<PosixFilePermission> permissions = posixFileAttributes.permissions();

        PosixFileAttributeView posixFileAttributeView = Files.getFileAttributeView(file, PosixFileAttributeView.class);

        posixFileAttributeView.setGroup(someGroupPrincipal);//Throws IOException
        posixFileAttributeView.setPermissions(PosixFilePermissions.fromString("rrrxxxxxx"));//Throws IOException
        posixFileAttributeView.setOwner(someUserPrincipal);//Throws IOException

        DirectoryStream

        DirectoryStream não é recursivo, se tiver uma pasta dentro do diretório ele só
        vai recuperar o Path da pasta, não dos conteúdos dela.

        DirectoryStream<Path> directoryStream = directoryStream = Files.newDirectoryStream(Paths.get(""));//Throws IOException, Path "" = pasta atual.
        for (Path directChild: directoryStream) {
            System.out.println(directChild);
        }

        FileVisitor

        Para arquivos e pasta em um mesmo nível não é possível garantir a ordem em que
        cada arquivo ou pasta será visitado.

        FileVisitor<Path> fileVisitor = new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Object dir, BasicFileAttributes attrs) throws IOException {
                return  FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Object file, BasicFileAttributes attrs) throws IOException {
                return  FileVisitResult.SKIP_SIBLINGS;
            }

            @Override
            public FileVisitResult visitFileFailed(Object file, IOException exc) throws IOException {
                return FileVisitResult.SKIP_SUBTREE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Object dir, IOException exc) throws IOException {
                return FileVisitResult.TERMINATE;
            }
        };

        Files.walkFileTree(file, fileVisitor);//Throws IOException

        PatchMatcher

        PathMatcher pathMatcher = FileSystems.getDefault().getPathMatcher(gobPattern);
        boolean matchResult = pathMatcher.matches(file);

        Gob
        *.java Matches a path that represents a file name ending in .java
        *.* Matches file names containing a dot
        *.{java,class} Matches file names ending with .java or .class
        foo.? Matches file names starting with foo. and a single character extension
        ** match qualquer coisa com file boundary

        WatchService

        WatchService não é recursivo ele não pega os eventos dentro de pastas dentro da pasta.

        WatchService watchService = FileSystems.getDefault().newWatchService();//Throws IOException
        WatchKey watchKey = Paths.get("").register(watchService, StandardWatchEventKinds.ENTRY_CREATE);//Throws IOException

        Path dir = Paths.get("dir1/dir2");
        Files.createDirectories(dir);
        Path file = Paths.get("dir1/dir2","file.txt");
        Files.createFile(file);
        for (WatchEvent<?> watchEvent: watchKey.pollEvents()) {
            System.out.println(watchEvent.context());//O Path criado nesse caso
            System.out.println(watchEvent.count());//1
            System.out.println(watchEvent.kind());//StandardWatchEventKinds.ENTRY_CREATE
        }

        Generics

        List<Shape> shape = new ArrayList<>();

        Pode escrever e ler Shape ou suas subclasses(IS A Shape)

        List<? extends Shape> extendsShape = new ArrayList<>();

        Não permite a escrita de nenhum tipo, e pode ler o tipo Shape

        List<? super Shape> superShape = new ArrayList<>();

        Pode ler o tipo Object e pode escrever Shape ou suas subclasses(IS A Shape)

        List<Shape> IS A List<? extends Shape>
        List<Shape> IS A List<? super Shape>

        //Proibido
        //Em uma referencia de wildcard não é permitido inserir ou remover.
        shape = extendsShape;
        extendsShape.addAll(shape);
        extendsShape.addAll(extendsShape);
        extendsShape.remove(1);//unsupported operation

        //Aceitavel
        extendsShape = shape;
        superShape = shape;
        shape.addAll(extendsShape);
        superShape.addAll(extendsShape);
        superShape.addAll(shape);

        */

    static class ShapeParent{}

     static class Shape extends ShapeParent{}

     static class ShapeSon extends Shape{}




    public static void main(String[] args) throws IOException, SQLException {
//        Path p1 = Paths.get("c:\\personal\\.\\photos\\..\\readme.txt");
//        Path p2 = Paths.get("c:\\personal\\index.html");
        Path p1 = Paths.get("/personal/./photos/../readme.txt");
        Path p2 = Paths.get("/personal/index.html");
        Path p3 = p1.relativize(p2);
        System.out.println(p3);
    }





}

