import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class App {

    public static void main(String[] args) throws Exception {

        String text = Files.readString(Path.of("src\\text.txt"));
        int orginalusdydis = text.length() * 8;

        Runtime runtime = Runtime.getRuntime();

        //huffman medzio logika
        Map<Character, Integer> map = new HashMap<>(); // unikaliu raidziu masyvas
        PriorityQueue<Node> pq = new PriorityQueue<>();  PriorityQueue<Node> TempPQ = new PriorityQueue<>(); // "MASYVAS" kuris sudelioja visa informacija didejimo tvarka automatiskai
        Map<Character, String> HuffCodes = new HashMap<>();  // huffman kodai pagal raidziu pozicija medyje

        long totalMemoryBytes = runtime.totalMemory();
        
        System.out.println("----------------------------------------------------");
        System.out.println("Unikaliu raidziu informacija");
        System.out.println("----------------------------------------------------");
        GautiUnikaliuRaidziuKiekis(map, text); // metodas kuris grazina unikalias raides su ju kiekiu
        System.out.println(map); 
        System.out.println("----------------------------------------------------");
        System.out.println("Informacijos surusiavimas su priorityqueue");
        System.out.println("----------------------------------------------------");
        SurusiuotiUnikaliasRaides(map, pq, TempPQ); // metodas kuris surusiuoja "map" duota informacija didejimo tvarka
        System.out.println(TempPQ);
        System.out.println("----------------------------------------------------");
        System.out.println("Medzio Huffman sukurimas");
        System.out.println("----------------------------------------------------");
        long HuffmanMedzioStart = System.nanoTime();
        Node root = SukurtiHuffmanMedi(pq); // Huffman sukurimas
        long HuffmanMedzioEnd = System.nanoTime();
        System.out.println(root);
        System.out.println("----------------------------------------------------");
        System.out.println("Huffman kodai");
        System.out.println("----------------------------------------------------");
        BuildCodesHuffman(root, "", HuffCodes);
        System.out.println(HuffCodes);
        System.out.println("----------------------------------------------------");
        System.out.println("Uzkoduota informacija");
        System.out.println("----------------------------------------------------");
        long HuffmanCompressionStart = System.nanoTime();
        String encoded = EncodeHuffman(text, HuffCodes);
        long HuffmanCompressionEnd = System.nanoTime();
        System.out.println(encoded);
        System.out.println("----------------------------------------------------");
        System.out.println("Atkoduota informacija");
        System.out.println("----------------------------------------------------");
        long HuffmanDecompresionStart = System.nanoTime();
        String decoded = DecodingHuffman(encoded, root);
        long HuffmanDecompresionEnd = System.nanoTime();
        System.out.println(decoded);
        System.out.println("----------------------------------------------------");
        long freeMemoryBytes = runtime.freeMemory();

        long usedMemoryBytes = totalMemoryBytes - freeMemoryBytes;

        double HuffmanTreeCreationTime = (HuffmanMedzioEnd - HuffmanMedzioStart) / 1e6;
        double HuffmanCompresionTime = (HuffmanCompressionEnd - HuffmanCompressionStart) / 1e6;
        double HuffmanDecompresionTime = (HuffmanDecompresionEnd - HuffmanDecompresionStart) / 1e6;
        double WholeHuffmanTime = HuffmanTreeCreationTime + HuffmanCompresionTime + HuffmanDecompresionTime;

        System.out.println("Huffman medzio kurimo laikas " + HuffmanTreeCreationTime + " ms");
        System.out.println("Huffman kvompresijos kurimo laikas " + HuffmanCompresionTime + " ms");
        System.out.println("Huffman dekompresijos kurimo laikas " + HuffmanDecompresionTime + " ms");
        System.out.println("Huffman laikas " + WholeHuffmanTime + " ms");

        // gauti kopresuoto failo dydi
        int suspaustaHuffmanbits = Huffmancomressesfilesize(map, HuffCodes);

        //huffman medzio logika

        // RLY logika
        long totalMemoryBytes2 = runtime.totalMemory();
        List<RleMethod> rle = new ArrayList<>();
        System.out.println("----------------------------------------------------");
        System.out.println("RLE kodavimas");
        System.out.println("----------------------------------------------------");
        long RLEKODAVIMOSTART = System.nanoTime();
        CountAllLetterCount(text, rle);
        long RLEKODAVIMOEND = System.nanoTime();
        System.out.println(rle);
        System.out.println("----------------------------------------------------");
        System.out.println("RLE Iskodavimas");
        System.out.println("----------------------------------------------------");
        long RLEDEKODAVIMOSTART = System.nanoTime();
        String RLEdecoded = DecodeRle(rle);
        long RLEDEKODAVIMOEND = System.nanoTime();
        System.out.println(RLEdecoded);
        System.out.println("----------------------------------------------------");
        long freeMemoryBytes2 = runtime.freeMemory();

        long usedMemoryBytes2 = totalMemoryBytes2 - freeMemoryBytes2;

        double RLEcompression = (RLEKODAVIMOEND - RLEKODAVIMOSTART) / 1e6;
        double RLEdecompression = (RLEDEKODAVIMOEND - RLEDEKODAVIMOSTART) / 1e6;
        double totalRLETime = RLEcompression + RLEdecompression;

        System.out.println("RLE kompresijos laikas: " + RLEcompression + " ms");
        System.out.println("RLE deckompresijos laikas: " + RLEdecompression + " ms");
        System.out.println("RLE Algoritmo laika " + totalRLETime + " ms");
        //RLY logika

        // RLY bitu dydis
        int RLYbits = (rle.size() *2) *8;


        // kombinuotas algoritmas RLE HUFFMAN
        long totalMemoryBytes3 = runtime.totalMemory();
        List<RleMethod> rle2 = new ArrayList<>();
        List<RleMethod> getINFOfromHUFFMAN = new ArrayList<>();
        
        
        
        System.out.println("----------------------------------------------------");
        System.out.println("RLE kodavimas");
        System.out.println("----------------------------------------------------");
        long RLEKODAVIMOSTART2 = System.nanoTime();
        CountAllLetterCount(text, rle2);
        long RLEKODAVIMOEND2 = System.nanoTime();
        
        StringBuilder sb = new StringBuilder();
        for(RleMethod s : rle2){
            sb.append("" + s.getCh() + s.getFreq());
        }
        String RLEencoded = sb.toString();


        double RLEcompression2 = (RLEKODAVIMOEND2 - RLEKODAVIMOSTART2) / 1e6;
        
        double totalRLETime2 = RLEcompression2;

        System.out.println("RLE kompresijos laikas:  2  " + RLEcompression2 + " ms");
        System.out.println("RLE Algoritmo laika  2  " + totalRLETime2 + " ms");


        Map<Character, Integer> map2 = new HashMap<>(); // unikaliu raidziu masyvas
        PriorityQueue<Node> pq2 = new PriorityQueue<>();  PriorityQueue<Node> TempPQ2 = new PriorityQueue<>(); // "MASYVAS" kuris sudelioja visa informacija didejimo tvarka automatiskai
        Map<Character, String> HuffCodes2 = new HashMap<>();  // huffman kodai pagal raidziu pozicija medyje
    

        System.out.println("----------------------------------------------------");
        System.out.println("Unikaliu raidziu informacija");
        System.out.println("----------------------------------------------------");
        GautiUnikaliuRaidziuKiekis(map2, RLEencoded); // metodas kuris grazina unikalias raides su ju kiekiu
        System.out.println(map2); 
        System.out.println("----------------------------------------------------");
        System.out.println("Informacijos surusiavimas su priorityqueue");
        System.out.println("----------------------------------------------------");
        SurusiuotiUnikaliasRaides(map2, pq2, TempPQ2); // metodas kuris surusiuoja "map" duota informacija didejimo tvarka
        System.out.println(TempPQ2);
        System.out.println("----------------------------------------------------");
        System.out.println("Medzio Huffman sukurimas");
        System.out.println("----------------------------------------------------");
        long HuffmanMedzioStart2 = System.nanoTime();
        Node root2 = SukurtiHuffmanMedi(pq2); // Huffman sukurimas
        long HuffmanMedzioEnd2 = System.nanoTime();
        System.out.println(root2);
        System.out.println("----------------------------------------------------");
        System.out.println("Huffman kodai");
        System.out.println("----------------------------------------------------");
        BuildCodesHuffman(root2, "", HuffCodes2);
        System.out.println(HuffCodes2);
        System.out.println("----------------------------------------------------");
        System.out.println("Uzkoduota huffman informacija kombinuotas");
        System.out.println("----------------------------------------------------");
        long HuffmanCompressionStart2 = System.nanoTime();
        String encoded2 = EncodeHuffman(RLEencoded, HuffCodes2);
        long HuffmanCompressionEnd2 = System.nanoTime();
        System.out.println(encoded2);
        System.out.println("----------------------------------------------------");
        System.out.println("Atkoduota huffman informacija");
        System.out.println("----------------------------------------------------");
        long HuffmanDecompresionStart2 = System.nanoTime();
        String decoded2 = DecodingHuffman(encoded2, root2);
        long HuffmanDecompresionEnd2 = System.nanoTime();
        System.out.println(decoded2);
        System.out.println("----------------------------------------------------");

        System.out.println("RLE kobinuoto informacija informacija");
        System.out.println("----------------------------------------------------");
        RLESTRINGTOARRAY(decoded2,getINFOfromHUFFMAN);
        System.out.println(getINFOfromHUFFMAN);
        System.out.println("----------------------------------------------------");


        System.out.println("RLE atkoduota informacija ");
        System.out.println("----------------------------------------------------");
        long RlecombinedDEcodestart = System.nanoTime();
        String RLEHUFFMANDECODED = DecodeRle(getINFOfromHUFFMAN);
        long RlecombinedDEcodeend = System.nanoTime();
        System.out.println(RLEHUFFMANDECODED);
        System.out.println("----------------------------------------------------");

        long freeMemoryBytes3 = runtime.freeMemory();

        long usedMemoryBytes3 = totalMemoryBytes3 - freeMemoryBytes3;

        double HuffmanTreeCreationTime2 = (HuffmanMedzioEnd2 - HuffmanMedzioStart2) / 1e6;
        double HuffmanCompresionTime2 = (HuffmanCompressionEnd2 - HuffmanCompressionStart2) / 1e6;
        double HuffmanDecompresionTime2 = (HuffmanDecompresionEnd2 - HuffmanDecompresionStart2) / 1e6;
        double RLEHUFFMANDECOMRESSION = (RlecombinedDEcodeend - RlecombinedDEcodestart) / 1e6;
        double WholeHuffmanTime2 = HuffmanTreeCreationTime2 + HuffmanCompresionTime2 + HuffmanDecompresionTime2 + RLEHUFFMANDECOMRESSION;

        System.out.println("Huffman kombinuotas medzio kurimo laikas " + HuffmanTreeCreationTime2 + " ms");
        System.out.println("Huffman kombinuotas kvompresijos kurimo laikas " + HuffmanCompresionTime2 + " ms");
        System.out.println("Huffman kombinuotas dekompresijos kurimo laikas " + HuffmanDecompresionTime2 + " ms");
        System.out.println("Huffman rle kombinuotas dekompresijos laikas " + RLEHUFFMANDECOMRESSION + " ms");
        System.out.println("Huffman kombinuotas laikas " + WholeHuffmanTime2 + " ms");

         // gauti kopresuoto failo dydi
        int kombinuotassuspaustofailodydis = Huffmancomressesfilesize(map2, HuffCodes2);



        // kombinuotas algoritmas RLE HUFFMAN

        System.out.println();
        System.out.println();
        System.out.println();
        //System.out.println("Orginalus tekstas " + text);
        System.out.println("Orginalus teksto dydis bitais: " + orginalusdydis );
        System.out.println("------------------------------------------Huffman kompresija dekompresija-------------------------------------------------");
        System.out.println("Huffman medzio kurimo laikas " + HuffmanTreeCreationTime + " ms");
        System.out.println("Huffman kvompresijos kurimo laikas " + HuffmanCompresionTime + " ms");
        System.out.println("Huffman dekompresijos kurimo laikas " + HuffmanDecompresionTime + " ms");
        System.out.println("Huffman kompresijos failo dydis bitais " + suspaustaHuffmanbits + " bit");
        System.out.println("Huffman suspaudimo santykis " + (double)orginalusdydis / suspaustaHuffmanbits + " : 1");
        System.out.println("Sunaudota RAM Huffman " + usedMemoryBytes/ (1024 * 1024) + " mb");
        System.out.println("Huffman laikas " + WholeHuffmanTime + " ms");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");

        System.out.println();
        System.out.println("------------------------------------------RLE kompresija dekompresija-----------------------------------------------------");
        System.out.println("RLE kompresijos laikas: " + RLEcompression + " ms");
        System.out.println("RLE deckompresijos laikas: " + RLEdecompression + " ms");
        System.out.println("RLE kompresijos failo dydis bitais " + RLYbits + " bit");
        System.out.println("RLE kompresijos santykis " + (double)orginalusdydis / RLYbits + " : 1");
        System.out.println("Sunaudota RAM Huffman " + usedMemoryBytes2/ (1024 * 1024) + " mb");
        System.out.println("RLE Algoritmo laika " + totalRLETime + " ms");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");

        System.out.println();
        System.out.println("----------------------------------------RLE + HUFFMAN ALGORITMAS---------------------------------------------------------");
        System.out.println("Huffman kombinuotas medzio kurimo laikas " + HuffmanTreeCreationTime2 + " ms");
        System.out.println("Huffman kombinuotas kvompresijos kurimo laikas " + HuffmanCompresionTime2 + " ms");
        System.out.println("Huffman kombinuotas dekompresijos kurimo laikas " + HuffmanDecompresionTime2 + " ms");
        System.out.println("Huffman rle kombinuotas dekompresijos laikas " + RLEHUFFMANDECOMRESSION + " ms");
        System.out.println("Huffman ir RLE kompresijos dydis bitais " + kombinuotassuspaustofailodydis + " bit" );
        System.out.println("RLE kompresijos santykis " + (double)orginalusdydis / kombinuotassuspaustofailodydis + " : 1");
        System.out.println("Sunaudota RAM Huffman " + usedMemoryBytes3 / (1024 * 1024) + " mb");
        System.out.println("Huffman kombinuotas laikas " + WholeHuffmanTime2 + " ms");
        System.out.println("-------------------------------------------------------------------------------------------------------------------------");

       
        
    }

    public static void GautiUnikaliuRaidziuKiekis(Map<Character, Integer> map, String text){

        for(char letters : text.toCharArray()){
            map.put(letters, map.getOrDefault(letters, 0)+1);
        }

    }

    public static void SurusiuotiUnikaliasRaides(Map<Character, Integer> map, PriorityQueue<Node> pq, PriorityQueue<Node> TempPQ){

        for(Map.Entry<Character, Integer> letters : map.entrySet()){
            pq.add(new Node(letters.getKey(), letters.getValue())); // sis masyva reikalingas
        
            TempPQ.add(new Node(letters.getKey(), letters.getValue())); // sis masyvas nereikalingas bet yra naudojamas tam kad galima butu gauti statistikas
        }

    }

    public static Node SukurtiHuffmanMedi(PriorityQueue<Node> pq){

        while (pq.size() > 1) {
            Node left = pq.poll(); // gauti pirma mažiausiai naudojama raide is priorityque
            Node right = pq.poll(); // gauti antra mažiausiai naudojama raide is priorityque
            Node parent = new Node(left, right); // prideti visa informacija is left ir right i parent tokiu budu sukuriant medi
            pq.add(parent); // po visos informacijos pasalinimo su while is pq masyvo mes padarom medi parent ir idedam ji atgal i pq masyva
        }
        
        
        return pq.poll();   // po visko mes atimam informacija is pq ir idedam i Node root;
    }

    public static void BuildCodesHuffman(Node root, String codes, Map<Character, String> map){
        
        if(root == null) return;

        if(root.isleaf()){
            
            map.put(root.ch, codes);
            return;
        }

        BuildCodesHuffman(root.left, codes + "0", map);
        BuildCodesHuffman(root.right, codes + "1", map);

    }

    public static String EncodeHuffman(String text, Map<Character, String> codes){

        StringBuilder sb = new StringBuilder(); // padarom string builder tam kad galetume koduoti informacija

        for(char letter : text.toCharArray()){ // sukam cikla kad gautume kieviena text raide
            sb.append(codes.get(letter)); // pridedam prie stringbuilder skaicius naudodami map codes.get(letter) jeigu yra tokia pati raide i sb yra idedama skaicius pvz 10
        }

        return sb.toString(); // grazinamas string kad galima lengviau naudotis
    }

    public static String DecodingHuffman(String encoded, Node root){

        StringBuilder sb = new StringBuilder();  // skirtas atkodavimui ir teksto idejumui

        Node tree = root;  // gaunam medi kuri sudarem anksciau

        for(char bits : encoded.toCharArray()){
            if(bits == '0'){
                tree = tree.left;
            }
            else{
                tree = tree.right;  
            }
            if(tree.isleaf()){
                sb.append(tree.ch);
                tree = root;
            }
        }


        return sb.toString();
    }

    public static void CountAllLetterCount(String text, List<RleMethod> rle){

        char[] ch = text.toCharArray();

        int count = 1;
        char current = ch[0];


        for(int i = 1; i < ch.length; i++){
            
            if(ch[i] == current){
                count++;
            }
            else{
                rle.add(new RleMethod(current, count));
                count = 1;
                current = ch[i];
            }
            
        }
        rle.add(new RleMethod(current, count));
        

    }

    public static String DecodeRle(List<RleMethod> list){

        StringBuilder sb = new StringBuilder();

        for(RleMethod s : list){
            char letter = s.getCh();
            int times = s.getFreq();

            for(int i = 0; i < times; i++){
                sb.append(letter);
            }

        }

        return sb.toString();

    }

    public static void RLESTRINGTOARRAY(String text, List<RleMethod> RLE){
      
            // char[] letters = text.toCharArray();
            
            // for(int i = 0; i < letters.length;i += 2){
                
            //     char count = letters[i+1];
            //     RLE.add(new RleMethod(letters[i], Integer.parseInt(String.valueOf(count))));

            // }



                  char[] letters = text.toCharArray();

            char ch = letters[0];
            String freq = "";

            for(int i = 1; i < letters.length; i++){

                try {
                    Integer.parseInt(String.valueOf(letters[i]));
                    freq += String.valueOf(letters[i]);

                    
                } catch (NumberFormatException e) {

                    if(!freq.isEmpty()){
                    RLE.add(new RleMethod(ch, Integer.parseInt(freq)));
                    System.out.println(RLE);
                    }
                    ch = letters[i];
                    freq = "";
                    
                    
                }
                
            }
            if(!freq.isEmpty()){
                RLE.add(new RleMethod(ch, Integer.parseInt(freq)));
            }
            


    }

    public static int Huffmancomressesfilesize(Map<Character, Integer> map, Map<Character, String> codes){

        int bitcount = 0;

        for(Map.Entry<Character, Integer> s : map.entrySet()){
            char ch = s.getKey();
            int times = s.getValue();

            String codelenght = codes.get(ch);

            bitcount += times * codelenght.length();
        }


        return bitcount;
        
    }

  
    
}
