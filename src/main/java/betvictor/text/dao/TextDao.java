package betvictor.text.dao;

import betvictor.text.entity.Text;

import java.util.List;

public interface TextDao {

    void save(Text text);

    List<Text> findLast(int number);

}
