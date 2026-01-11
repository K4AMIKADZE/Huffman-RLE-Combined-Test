public class Node implements Comparable<Node> {

    char ch; //  raides kurios bus paimamos is duodamo failo 
    int freq; // suskaiciuotas raidzius kiekis unikaliu raidziu

    Node left; // medzio kaire puse
    Node right;  // medzio desine puse

    // konstruktorius  naudojamas kad buti pridėta raidė ir jos pasikartojimas šis konstruktorius bus naudojimas HashMap pagrinde
    Node(char ch, int freq){
        this.ch = ch;
        this.freq = freq;
    }


    // konstuktorius kuris bus naudojimas huffman medzio sudarymui
    Node(Node left, Node right){
        this.left = left;
        this.right = right;
        this.freq = left.freq +  right.freq;   // kai Node bus iskviestas bus padaroma freq is visu left ir right kintamuju
    }

    public int compareTo(Node other) {
        return this.freq - other.freq;
    }

    boolean isleaf(){
        return left == null && right == null;
    }


    @Override
    public String toString() {
        return "Node [ch=" + ch + ", freq=" + freq + ", left=" + left + ", right=" + right + "]";
    }

    

}
