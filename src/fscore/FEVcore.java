/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fscore;
import static java.lang.System.arraycopy;


/**
 *
 * @author Andrej Borovski
 */
public class FEVcore {
    static void printBytes(byte[] bytes)
    {
        for (int b : bytes) {
            System.out.print(b);
            System.out.print(" ");
        }
        System.out.println();
    }
    static boolean arcmp(byte[] b1, byte[] b2)
    {
        boolean r = true;
        for (int i =0; i < b1.length; i++)
            if (b1[i] != b2[i])
                return false;
        return true;
    }
    static void test()
    {
        byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};
        for (int i = 0; i < 12008; i++)
            bytes = PermutationTables.chainPermute(bytes);
        byte[] b1 = new byte[bytes.length];
        arraycopy(bytes, 0, b1, 0, b1.length);
        int count = 0;
        /*while(true) {
            bytes = PermutationTables.chainPermute(bytes);
            if (arcmp(bytes, b1))
                break;
            try {
                sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(FEVcore.class.getName()).log(Level.SEVERE, null, ex);
            }
            count++; 
            System.out.println(count);
            //printBytes(bytes);
            //bytes[0]++;
        }*/
        
    }
    /**
     * @param args the command line arguments
     */
   /* public static void main(String[] args) {
        // TODO code application logic here
        //test();
        Storage storage = new Storage();
        storage.createFile("fastval.test", "апропос");
        Writer w = storage.getWriter();
        storage.writeText("Hello", "Hello crypto world Подключение к водопроводной системе осуществляется следующим образом.\n" +
"\n" +
"Для выполнения этой операции используют гибкие шланги с диаметром ¾ дюйма. Шланг при необходимости делают длиннее, уплотняя места соединения частей при помощи манжет (резиновых прокладок).\n" +
"\n" +
"Рассмотрим, как подключить стиральную машину самостоятельно, используя отдельный вентиль:\n" +
"\n" +
"    В определенном месте водопровода нарезают резьбу и, используя соответствующие крепежные элементы, устанавливают вентиль. После проведения каждой стирки подачу воды полностью перекрывают.\n" +
"    Рекомендуется устанавливать защитную сетку-фильтр для предотвращения попадания механических частичек в барабан машины. Фильтр подвергают периодической очистке.Подключение к водопроводной системе осуществляется следующим образом.\n" +
"\n" +
"Для выполнения этой операции используют гибкие шланги с диаметром ¾ дюйма. Шланг при необходимости делают длиннее, уплотняя места соединения частей при помощи манжет (резиновых прокладок).\n" +
"\n" +
"Рассмотрим, как подключить стиральную машину самостоятельно, используя отдельный вентиль:\n" +
"\n" +
"    В определенном месте водопровода нарезают резьбу и, используя соответствующие крепежные элементы, устанавливают вентиль. После проведения каждой стирки подачу воды полностью перекрывают.\n" +
"    Рекомендуется устанавливать защитную сетку-фильтр для предотвращения попадания механических частичек в барабан машины. Фильтр подвергают периодической очистке.");
        Record rc = new Record();
        rc.url = "bnfds";
        rc.login = "asdf";
        rc.password = "letmein";
        rc.note = "";
        //w.writeRecord(rc, "MyRec");
        storage.writeRecord("MyRec", "www.yandex.ru", "log", "pas", "fake record");
        w.writeText("I am a small text", "Small Text");
        FileRecord fr = new FileRecord();
        fr.name = "l";
        fr.note = "llkk";
        w.writeFileRecord(fr, "My file", "bfx.aml.xml");
        w.writeText("Both Rabin and Williams have an advantage over RSA in that they are provably as secure as\n" +
"factoring. However, they are completely insecure against a chosen-ciphertext attack. If you are going\n" +
"to use these schemes in instances where an attacker can mount this attack (for example, as a digital\n" +
"signature algorithm where an attacker can choose messages to be signed), be sure to use a one-way\n" +
"hash function before signing. Rabin suggested another way of defeating this attack: Append a\n" +
"different random string to each message before hashing and signing. Unfortunately, once you add a\n" +
"one-way hash function to the system it is no longer provably as secure as factoring [628], although\n" +
"adding hashing cannot weaken the system in any practical sense.", "PCrypto");
        rc = new Record();
        rc.url = "www.yandex.ru";
        rc.login = ";lkjkjkhhgh";
        rc.password = "iuy876tfg";
        rc.note = "In 1978 Robert McEliece developed a public-key cryptosystem based on algebraic coding theory\n" +
"[1041]. The algorithm makes use of the existence of a class of error-correcting codes, known as Goppa\n" +
"codes. His idea was to construct a Goppa code and disguise it as a general linear code. There is a fast\n" +
"algorithm for decoding Goppa codes, but the general problem of finding a code word of a given\n" +
"weight in a linear binary code is NP-complete. A good description of this algorithm can be found in\n" +
"[1233]; see also [1562]. Following is just a quick summary.";
        w.writeRecord(rc, "rec3");
        storage.openFileReader("fastval.test", "апропос");
        Reader r = storage.getReader();
        //RecordInfo ri = r.readRecordInfo();
        //System.out.println(ri.modified);
        //System.out.println(r.readText(ri));
        BaseRecord br = storage.readRecord();
        if ("Text".equals(br.type))        
            System.out.println(((TextRecord)br).Text);
        RecordInfo ri1 = r.readRecordInfo();
        Record rc1 = r.readRecord();
        System.out.println(rc1.password);
        System.out.println(rc1.note);
        RecordInfo ri = r.readRecordInfo();
        System.out.println(r.readText(ri));
        ri = r.readRecordInfo();
        r.readFileRecord();
        r.extractFile("b.mlx", ri);
        r.close();
        storage.deleteRecord(ri1);
        storage.changePassword("апропос", "letmein");
        storage.openFileReader("fastval.test", "letmein");
        r = storage.getReader();
        BaseRecord br1 = storage.readRecord();
        if ("Text".equals(br1.type))        
            System.out.println(((TextRecord)br1).Text);
        ri = r.readRecordInfo();
        System.out.println(r.readText(ri));
        ri = r.readRecordInfo();
        r.readFileRecord();
        r.extractFile("b.mlx", ri);
        r.close();
        System.out.println(PasswordGenerator.generate(12));
    }*/
    
}
