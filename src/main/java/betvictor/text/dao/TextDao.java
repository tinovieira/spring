package betvictor.text.dao;

import betvictor.text.entity.Text;

import java.util.List;

public interface TextDao {

    public void save(Text text);

    public List<Text> findLast(int number);

}
