package betvictor.text.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name="text")
public class Text {

    @Id
    @GeneratedValue
    @JsonIgnore
    private Integer id;

    @Column(name = "freq_word")
    @JsonProperty(value = "freq_word")
    private String mostFrequentWord;

    @Column(name = "avg_paragraph_size")
    @JsonProperty(value = "avg_paragraph_size")
    private String averageParagraphSize;

    @Column(name = "avg_paragraph_processing_time")
    @JsonProperty(value = "avg_paragraph_processing_time")
    private String averageParagraphProcessingTime;

    @Column(name = "total_processing_time")
    @JsonProperty(value = "total_processing_time")
    private String totalProcessingTime;

    public Text() {
    }

    public Text(String mostFrequentWord, String averageParagraphSize, String averageParagraphProcessingTime, String totalProcessingTime) {
        this.mostFrequentWord = mostFrequentWord;
        this.averageParagraphSize = averageParagraphSize;
        this.averageParagraphProcessingTime = averageParagraphProcessingTime;
        this.totalProcessingTime = totalProcessingTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMostFrequentWord() {
        return mostFrequentWord;
    }

    public void setMostFrequentWord(String mostFrequentWord) {
        this.mostFrequentWord = mostFrequentWord;
    }

    public String getAverageParagraphSize() {
        return averageParagraphSize;
    }

    public void setAverageParagraphSize(String averageParagraphSize) {
        this.averageParagraphSize = averageParagraphSize;
    }

    public String getAverageParagraphProcessingTime() {
        return averageParagraphProcessingTime;
    }

    public void setAverageParagraphProcessingTime(String averageParagraphProcessingTime) {
        this.averageParagraphProcessingTime = averageParagraphProcessingTime;
    }

    public String getTotalProcessingTime() {
        return totalProcessingTime;
    }

    public void setTotalProcessingTime(String totalProcessingTime) {
        this.totalProcessingTime = totalProcessingTime;
    }
}
