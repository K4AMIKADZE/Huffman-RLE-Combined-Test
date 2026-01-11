public class RleMethod {

    char ch;
    int freq;


    public RleMethod(char ch, int freq) {
        this.ch = ch;
        this.freq = freq;
    }


    public char getCh() {
        return ch;
    }


    public int getFreq() {
        return freq;
    }


    @Override
    public String toString() {
        return "RleMethod [ch=" + ch + ", freq=" + freq + "]";
    }

}
