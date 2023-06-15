package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class UserDaoImp implements UserDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    public void add(User user) {
        sessionFactory.getCurrentSession().save(user);
    }


    @Override
    @SuppressWarnings("unchecked")
    public List<User> listUsers() {
        TypedQuery<User> query = sessionFactory.getCurrentSession().createQuery("from User");
        return query.getResultList();
    }
    private static final String hql = """
            SELECT c FROM Car c WHERE c.model = :model AND series = :series
            """;

    @Override
    public User searchUserByCar(String model, int series) {
        var query = sessionFactory.getCurrentSession().createQuery(hql, Car.class);
        query.setParameter("model", model);
        query.setParameter("series",series);
        Optional<Car> car = query.uniqueResultOptional();
        User resultUser = null;
        if (car.isPresent()){
            Car resultCar = car.get();
            List<User> list = this.listUsers();
            for (User u : list) {
                if (u.getCar().equals(resultCar)) {
                    resultUser = u;
                }
            }
        } else {
            System.err.println("CAR NOT FOUND");
        }
        return resultUser;
    }

}
