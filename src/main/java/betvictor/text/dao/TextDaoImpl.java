package betvictor.text.dao;

import betvictor.text.entity.Text;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public class TextDaoImpl implements TextDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<Text> findLast(int number) {
        return (List<Text>) sessionFactory.getCurrentSession()
                .createCriteria(Text.class)
                .addOrder(Order.desc("id"))
                .setMaxResults(number)
                .list();
    }

    @Override
    public void save(Text text) {
        sessionFactory.getCurrentSession().persist(text);
    }
}
