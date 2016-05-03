package betvictor.text.pojo;

public class Word implements Comparable<Word> {

    private String word;

    private int count;

    public Word(String word, int count) {
        this.word = word;
        this.count = count;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public void incrementCount() {
        this.count++;
    }

    public void sumCount(int sum) {
        this.count += sum;
    }

    @Override
    public int hashCode() {
        return word.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Word && word.equals(((Word) obj).word);
    }

    @Override
    public int compareTo(Word wordObj) {
        if (wordObj.count - this.count == 0) {
            return this.word.compareTo(wordObj.word);
        }

        return wordObj.count - this.count;
    }
}
